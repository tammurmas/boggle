package tamm.org.boggle.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import tamm.org.boggle.board.BoggleBoard;
import tamm.org.boggle.board.WordList;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * This is a very simplistic implementation of a Boggle server that can handle
 * multiple requests from Boggle clients wanting to play a round of Boggle.
 * Clients interact with the Boggle server by making RMI calls to the
 * {@link BoggleServer} remote interface. Clients call
 * {@link BoggleServer#startGame} on the server, and are blocked until the
 * server receives enough requests to start a game. Then the Boggle board to use
 * is returned to the clients, and they begin playing.
 * <p>
 * Once clients have completed the elapsed time, they call back to the server
 * via the {@link BoggleServer#gameOver} method, reporting their word-lists.
 * Again, the clients are blocked until all playing clients have reported their
 * word-lists. Then, the server computes each client's score, and reports all
 * game results back to each client via the return-value of this method call.
 * <p>
 * This server is very simplistic; it cannot handle many basic scenarios that
 * one would want such a server to handle. If a client drops out mid-game, the
 * server has no idea. If a client cheats by taking too long, the server can't
 * detect it. If a client cheats by making up words not on the Boggle board, the
 * server can't detect it. Basically, this server would never be useful in a
 * real-world environment!
 **/
public class BoggleServerApp implements BoggleServer {

	/**
	 * The logger instance
	 */
	private static Logger logger = Logger.getLogger(BoggleServerApp.class); 
	/**
	 * This map of usernames to client-information contains the directory of all
	 * clients that are currently talking to the Boggle server. Clients are
	 * added to this map when they try to join a round via {@link #startGame},
	 * and they are removed from this map when they complete (or leave) a round
	 * via {@link #gameOver}. This map is also used to ensure that multiple
	 * users don't have the same username.
	 **/
	private HashMap<String, ClientInfo> clients;

	/**
	 * Clients are initially added to this collection when they call
	 * {@link #startGame}.
	 **/
	private ArrayList<ClientInfo> waiting;

	/**
	 * When clients complete a round of the game, their results are stored into
	 * this collection. Note that the <tt>ClientInfo</tt> objects will be the
	 * same as those stored in the {@link #clients} collection. This collection
	 * is mainly used as a way to verify that all players have indeed submitted
	 * their results.
	 **/
	private ArrayList<ClientInfo> playerResults;

	/** This is the Boggle board being used for the current round of play. **/
	private BoggleBoard boggleBoard;

	/**
	 * These are the results of the round of Boggle that the client just played.
	 **/
	private GameResults gameResults;
	
	/**
	 * This is the internal thread that runs the Boggle server. It is started by
	 * the <tt>BoggleServerApp</tt> constructor, and it encodes the flow of the
	 * Boggle game.
	 **/
	private class GameRunner implements Runnable {

		/**
		 * This constant specifies how long to wait after the last player joins
		 * the current round before going ahead and starting the round. If a
		 * client joins the round, and then another client joins before this
		 * timeout elapses, then the second client will also be part of the
		 * round. The round will start when there are enough clients, and when
		 * no other client joins before this timeout passes.
		 **/
		private static final long START_TIMEOUT = 5 * 1000;

		/**
		 * The minimum number of players we must have to play a round of Boggle.
		 **/
		private static final int MIN_PLAYERS = 2;

		/**
		 * This is the method run by the Boggle server thread. It follows a very
		 * simple sequence of operations, although the actual implementation
		 * details are a bit complex. When players join, they are added to the
		 * {@link #waiting} collection. After a certain minimum number of
		 * players have joined, the Boggle game can commence, at which point all
		 * players are moved into the <tt>players</tt> collection. Once players
		 * complete their round, they submit their results, which are stored
		 * into the {@link #playerResults} collection. Finally, the server
		 * computes the score for each player, then returns the overall game
		 * results back to each playing client.
		 **/
		public void run() {

			// Each iteration of this loop runs another round of the Boggle
			// game.
			while (true) {

				ArrayList<ClientInfo> players = new ArrayList<ClientInfo>();

				// Monitor the "waiting" list until we have a minimum number of
				// players
				// waiting to play a round of the game. (Note that we also give
				// players
				// 15 seconds from the last time a client joined, to join in on
				// the
				// round.)

				synchronized (waiting) {

					long lastJoin = System.currentTimeMillis();
					int lastCount = waiting.size();

					while (waiting.size() < MIN_PLAYERS
							|| (System.currentTimeMillis() - lastJoin < START_TIMEOUT)) {
						try {
							// Sleep for a bit, then wake up and see what is
							// going on.
							// (NOTE: We don't actually need any notifications
							// here to work
							// properly.)
							waiting.wait(500); // 0.5 seconds
						} catch (InterruptedException e) {
							// Ignore. Should log...
						}

						// If more players joined since we last awoke, update
						// our variables.
						if (waiting.size() > lastCount) {
							lastCount = waiting.size();
							lastJoin = System.currentTimeMillis();
						}
					}

					//System.out.println("=== NEW ROUND STARTING ===");
					logger.info("=== NEW ROUND STARTING ===");

					// Generate a new Boggle board for the current round. This
					// is shared
					// state; when we notify the waiting clients, they will
					// access the
					// updated value of this board.
					boggleBoard = new BoggleBoard();

					// Grab all waiting players and switch them over to playing
					// the
					// current round.

					players.addAll(waiting);
					waiting.clear();
					for (ClientInfo c : players)
						c.setPlaying();

					// Wake up all waiting threads. The ones who actually became
					// players
					// will see their state changed to PLAYING.
					waiting.notifyAll();
				}

				// Now, wait for all players to return their results.

				synchronized (playerResults) {

					// Clear out the results for this round.
					gameResults.clear();

					while (playerResults.size() < players.size()) {
						try {
							playerResults.wait();
						} catch (InterruptedException e) {
							// Ignore. Should log...
						}
					}

					// Handle all the player-result computation, generate
					// "game results",
					// then wake up all the waiting players.

					gameResults.computeResults(playerResults);
					playerResults.clear();
					playerResults.notifyAll();
				}
			}
		}
	}

	/**
	 * Constructs a new Boggle server instance, and spins up the internal thread
	 * used to govern the progress of each round of the game.
	 **/
	public BoggleServerApp() {
		// Collections used to coordinate what players are playing or waiting to
		// play.
		clients = new HashMap<String, ClientInfo>();
		waiting = new ArrayList<ClientInfo>();
		playerResults = new ArrayList<ClientInfo>();

		// This object gets reused across rounds.
		gameResults = new GameResults();

		// Start the thread that runs the Boggle server logic.
		Thread t = new Thread(new GameRunner());
		t.start();
	}

	/**
	 * This function is called by Boggle clients via RMI when they want to play
	 * a round of Boggle. Clients must register their own unique client-name
	 * that identifies them to the Boggle server.
	 * <p>
	 * This method blocks until the Boggle server decides that there are enough
	 * players for the current round. Then the method returns the
	 * <tt>BoggleBoard</tt> that clients should use for the round.
	 *
	 * @throws PlayerException
	 *             if the calling client specifies a username that is already
	 *             used.
	 **/
	public BoggleBoard startGame(String clientName) throws PlayerException {
		/*System.out.println("Client \"" + clientName
				+ "\" wants to start a game.");*/
		logger.info("Client \"" + clientName
				+ "\" wants to start a game.");

		ClientInfo myInfo = new ClientInfo(clientName);

		synchronized (clients) {
			if (clients.containsKey(clientName))
				throw new PlayerException(clientName + " already used!");

			clients.put(clientName, myInfo);
		}

		// Register in the "waiting" list, then wait until the server marks our
		// token as "playing."
		synchronized (waiting) {
			waiting.add(myInfo);
			// waiting.notifyAll(); // THIS IS REALLY GROSS!!!

			while (myInfo.isWaiting()) {
				try {
					waiting.wait();
				} catch (InterruptedException e) {
					// Ignore. Should log...
				}
			}
		}

		// Once we get here, we are ready to start playing.
		// Return the BoggleBoard for this round's players to use.

		/*System.out.println("Client \"" + clientName
				+ "\" is playing this round.");*/
		logger.info("Client \"" + clientName
				+ "\" is playing this round.");
		return boggleBoard;
	}

	/**
	 * This function is called by Boggle clients via RMI when they have
	 * completed the current round of Boggle. Clients must submit the same
	 * username that they specified initially, and they must also submit their
	 * word-list to the server.
	 * <p>
	 * This method blocks until <em>all</em> playing clients have submitted
	 * their word-lists to the Boggle server. The server then computes the
	 * overall game results, and returns these results back to each client.
	 *
	 * @throws PlayerException
	 *             if the calling client specifies a username that is already
	 *             used.
	 **/
	public GameResults gameOver(String clientName, WordList myWords)
			throws PlayerException {

		// Make sure the client is actually registered.

		ClientInfo myInfo = null;
		synchronized (clients) {
			myInfo = clients.get(clientName);
			if (myInfo == null)
				throw new PlayerException(clientName + " is unrecognized!");
		}

		/*System.out.println("Client \"" + clientName
				+ "\" has submitted a word-list of " + myWords.size()
				+ " words.");*/
		logger.info("Client \"" + clientName
				+ "\" has submitted a word-list of " + myWords.size()
				+ " words.");

		myInfo.setWords(myWords);

		synchronized (playerResults) {
			playerResults.add(myInfo);
			playerResults.notifyAll();

			// Wait for all players to report in, and for the results to be
			// processed.
			// This is signalled by the server clearing the player-results list.
			while (!playerResults.isEmpty()) {
				try {
					playerResults.wait();
				} catch (InterruptedException e) {
					// Ignore. Should log...
				}
			}
		}

		// Finally, remove this client's info from the clients list.
		synchronized (clients) {
			clients.remove(clientName);
		}

		return gameResults;
	}

	/**
	 * This method creates an instance of the Boggle server application, and
	 * then exposes it via an RMI registry that it starts.
	 * @throws RemoteException 
	 **/
	public static void main(String[] args) {
		try {
			BasicConfigurator.configure();
			
			Registry registry = LocateRegistry.createRegistry(1099);
			
			BoggleServerApp server = new BoggleServerApp();
			
			BoggleServer serverStub = (BoggleServer)UnicastRemoteObject.exportObject(server, 0);
			
			registry.rebind("BoggleServer", serverStub);
			
			//System.out.println("Server ready and running!");
			logger.info("Server ready and running!");
		} catch (RemoteException e) {
			//System.err.println("Server internal error!");
			logger.error("Server internal error!");
			e.printStackTrace();
		}
		
		
	}
}
