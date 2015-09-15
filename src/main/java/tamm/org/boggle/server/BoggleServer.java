package tamm.org.boggle.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import tamm.org.boggle.board.BoggleBoard;
import tamm.org.boggle.board.WordList;

/**
 * This interface is implemented by Boggle servers to allow clients to play
 * rounds of the game.  The interaction is very simple, since Boggle is also
 * a pretty simple game.
 **/
public interface BoggleServer extends Remote {

  /**
   * This method is called by Boggle clients that want to particpate in the
   * next round of the Boggle game.  It returns when the Boggle server is
   * ready for a new game to start.
   *
   * @throws PlayerException if the specified client-name is already
   *         being used on the server.
   **/
  public BoggleBoard startGame(String clientName)
    throws PlayerException, RemoteException;

  /**
   * This method is called by Boggle clients when their time is up and
   * the clients are submitting their word-lists.  The results of the round
   * are returned in the {@link GameResults} object.
   *
   * @throws PlayerException if the specified client-name is not recognized by
   *         the server.
   **/
  public GameResults gameOver(String clientName, WordList myWords)
    throws PlayerException, RemoteException;
}
