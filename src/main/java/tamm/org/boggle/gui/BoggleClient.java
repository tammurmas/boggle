package tamm.org.boggle.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tamm.org.boggle.board.BoggleBoard;
import tamm.org.boggle.board.WordList;
import tamm.org.boggle.gui.JBoggleButton.State;
import tamm.org.boggle.server.BoggleServer;
import tamm.org.boggle.server.GameResults;
import tamm.org.boggle.server.PlayerException;

public class BoggleClient {
	
	// the main frame containing parts of the BoggleBoard
	private JFrame mainFrame;
	// the panel containing start button and time
	private final JPanel startButtonPanel = new JPanel();
	// the panel containing boggle board buttons
	private JBoggleBoard boardPanel;
	// the panel containing control buttons for adding words and clearing selection
	private final JPanel controlPanel = new JPanel();
	// the panel for the list of words found by the user
	private final JPanel listPanel = new JPanel();
	
	// the wordlist instance
	private WordList wordList;
	// a control button for clearButtoning the last selection of letters
	private final JButton clearButton = new JButton("Clear Word");
	// a control button for submitButtonting the word
	private final JButton submitButton = new JButton("Add Word");
	// a control button for startButtoning a new round of boggle
	private final JButton startButton = new JButton("Start Game");
	// the timer instance
	private final JBoggleTimer timer = new JBoggleTimer();

	// list of words found displayed in the client
	private JList<String> listView = new JList<String>(new DefaultListModel<String>());
	// action handler that acts as a controller
	private final BoggleActionHandler bActionHandler = new BoggleActionHandler();
	
	private BoggleServer server;
	private String username;
	
	public BoggleClient(BoggleServer s, String u)
	{
		server = s;
		username = u;
	}
	
	public static void main(String[] args) {
		if(args.length == 1 || args.length == 2)
		{
			try
			{
				String host;
				if(args.length == 2)
				{
					host = args[1]; 
				}
				else
				{
					host = "localhost";
				}
				
				Registry registry = LocateRegistry.getRegistry(host);
				
				//this "final" thing is a bit weird...
				final BoggleServer server = (BoggleServer)registry.lookup("BoggleServer");
				final String username = args[0];
				
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		            	BoggleClient client = new BoggleClient(server, username);
		            	client.createAndShowGUI();
		            }
		        });
			}
			catch (RemoteException | NotBoundException e) {
				System.err.println("Server internal error!");
				e.printStackTrace();
				System.exit(1);
			}
		}
		else
		{
			throw new IllegalArgumentException("Provide your username (mandatory) and host name (optional)!");
		}
    }
	
	/**
	 * Add components to the client
	 */
	public void createAndShowGUI() {

		mainFrame = new JFrame("Boggle Game: "+username);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainFrame.setSize(400, 400);
		Container pane = mainFrame.getContentPane();

		addStartButton(startButtonPanel);
		pane.add(startButtonPanel, BorderLayout.NORTH);

		boardPanel = new JBoggleBoard(BoggleBoard.DEFAULT_SIZE);
		boardPanel.setBoard(new BoggleBoard());
		boardPanel.setLayout(new GridLayout(boardPanel.getBoardSize(), boardPanel.getBoardSize()));
		boardPanel.setActionListener(bActionHandler);
		
		pane.add(boardPanel, BorderLayout.CENTER);

		controlPanel.setLayout(new GridLayout(1, 2));
		controlPanel.setSize(400, 50);
		addControlboardButtons(controlPanel);
		pane.add(controlPanel, BorderLayout.SOUTH);

		listPanel.setLayout(new GridLayout(1, 1));
		listPanel.add(listView);
		pane.add(listPanel, BorderLayout.WEST);

		mainFrame.setVisible(true);

	}
	
	/**
	 * Add the startButton button to the startButton button panel
	 * 
	 * @param startButtonPanel
	 */
	private void addStartButton(JPanel startButtonPanel) {

		startButtonPanel.setLayout(new GridLayout(2, 1));

		startButton.setActionCommand("start_button_event");
		startButton.addActionListener(bActionHandler);

		startButtonPanel.add(startButton);
		startButtonPanel.add(timer);
	}
	
	/**
	 * Add boardButtons on the control panel
	 * 
	 * @param contPanel
	 */
	private void addControlboardButtons(JPanel contPanel) {
		clearButton.setActionCommand("clear_button_event");
		clearButton.addActionListener(bActionHandler);

		submitButton.setActionCommand("submit_button_event");
		submitButton.addActionListener(bActionHandler);

		clearButton.setEnabled(false);
		contPanel.add(clearButton);

		submitButton.setEnabled(false);
		contPanel.add(submitButton);
	}
	
	/**
	 * An inner controller class meant for handling all the BoggleBoard actions
	 * 
	 * @author Urmas
	 *
	 */
	private class BoggleActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "start_button_event":
				startButtonAction();
				break;
			case "game_over_event":
				timerAction();
				break;
			case "submit_button_event":
				submitButtonAction();
				break;
			case "clear_button_event":
				clearButtonAction();
				break;
			case "board_button_event":
				boardButtonAction(e);
				break;
			default:
				break;
			}
		}
		
		private void startButtonAction() {
			try {
				BoggleBoard sBoard = server.startGame(username);
				mainFrame.setTitle("Boggle Game: "+username);
				
				toggleControlboardButtons(false);

				// create new boggleboard layout
				boardPanel.removeAll();
				boardPanel.validate();

				// create new boggleboard and empty wordlist
				boardPanel.setBoard(sBoard);
				wordList = new WordList();

				boardPanel.clearSelValues();

				// clear out last game's results
				listPanel.removeAll();
				listPanel.validate();
				listView = new JList<String>(new DefaultListModel<String>());

				timer.addActionListener(this);
				timer.setTimeRemaining(1 * 30);
				timer.startTimer();
			} catch (RemoteException | PlayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * Listener for the timer object
		 */
		private void timerAction() {
			try {
				GameResults results = server.gameOver(username, wordList);
				mainFrame.setTitle("Boggle Game: "+username+"; FINAL SCORE: "+results.getClientResult(username).getScore());
				
				//clear listView's selection and select only the filtered words (present in the dictionary)
				listView.clearSelection();
				DefaultListModel<String> model = (DefaultListModel<String>) listView.getModel();
				WordList filteredWords = results.getClientResult(username).getFilteredWords();

				for (int i=0; i<model.size(); i++) {
					if(filteredWords.containsWord(model.getElementAt(i)))
					{
						listView.getSelectionModel().addSelectionInterval(i, i);
					}
				}
				
			} catch (RemoteException | PlayerException e) {
				e.printStackTrace();
			}
			toggleControlboardButtons(true);
		}

		/**
		 * Implementation for the submitButton button action after being pressed
		 */
		private void submitButtonAction() {
			for (JBoggleButton but : boardPanel.getBoardButtons()) {
				but.setState(State.AVAILABLE);
			}

			DefaultListModel<String> model = (DefaultListModel<String>) listView.getModel();

			int oldSize = wordList.size();
			wordList.addWord(boardPanel.getWord());
			int newSize = wordList.size();

			if (oldSize != newSize) {
				model.addElement(boardPanel.getWord());
			}

			// add the scrollbar
			if (newSize == 1) {
				listPanel.add(new JScrollPane(listView));
			}

			boardPanel.clearSelValues();

			submitButton.setEnabled(false);
			clearButton.setEnabled(false);
		}

		/**
		 * Implementation for the clearButton button action after being pressed
		 */
		private void clearButtonAction() {
			for (JBoggleButton but : boardPanel.getBoardButtons()) {
				but.setState(State.AVAILABLE);
			}

			boardPanel.clearSelValues();

			submitButton.setEnabled(false);
			clearButton.setEnabled(false);
		}

		/**
		 * Action for each of the board buttons
		 * 
		 * @param e
		 *            - the event that took place
		 */
		private void boardButtonAction(ActionEvent e) {
			JBoggleButton button = (JBoggleButton) e.getSource();

			if (button.getState().equals(State.AVAILABLE)) {
				boardPanel.selectButton(button);
				
				// enable control boardButtons if game is running
				if (!startButton.isEnabled()) {
					// add the word if the word's length is at least 3 characters
					if (boardPanel.getWord().length() >= 3) {
						submitButton.setEnabled(true);
					}

					clearButton.setEnabled(true);
				}
			}
		}
		
		/**
		 * Toggles the states of startButton, add and clearButton boardButtons
		 * 
		 * @param gameOver - the state of the game
		 *            
		 */
		private void toggleControlboardButtons(boolean gameOver) {
			if (gameOver == true) {
				startButton.setEnabled(true);
			} else {
				startButton.setEnabled(false);
			}
			
			submitButton.setEnabled(false);
			clearButton.setEnabled(false);
		}
	}
	
	
}
