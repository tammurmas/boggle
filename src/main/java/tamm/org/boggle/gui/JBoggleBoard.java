package tamm.org.boggle.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tamm.org.boggle.board.BoggleBoard;
import tamm.org.boggle.board.WordList;
import tamm.org.boggle.gui.JBoggleButton.State;

public class JBoggleBoard extends JPanel {

	private static final long serialVersionUID = 1L;
	// the main frame containing parts of the BoggleBoard
	private final JFrame mainFrame = new JFrame("Boggle Game");
	// the panel containing start button and time
	private final JPanel startButtonPanel = new JPanel();
	// the panel containing boggle board buttons
	private final JPanel butPanel = new JPanel();
	// the panel containing control buttons for adding words and clearing
	// selection
	private final JPanel contPanel = new JPanel();
	// the panel for the list of words found by the user
	private final JPanel listPanel = new JPanel();

	// array of boardButtons on the board
	private final List<JBoggleButton> boardButtons = new ArrayList<JBoggleButton>();
	// array of selected values from the board
	private List<JBoggleButton> selValues = new ArrayList<JBoggleButton>();
	// the boggle board instance
	private BoggleBoard boggleBoard;
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

	// private final String[] data = {"one", "two", "three", "four"};
	private JList<String> listView = new JList<String>(new DefaultListModel<String>());

	private final BoggleActionHandler bActionHandler = new BoggleActionHandler();

	/**
	 * Constructor
	 * 
	 * @param size
	 *            - the size of the board
	 */
	public JBoggleBoard(int size) {
		boggleBoard = new BoggleBoard(size);
		wordList = new WordList();
	}

	/**
	 * Set up the board GUI
	 */
	public void createAndShowGUI() {

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainFrame.setSize(400, 400);
		Container pane = mainFrame.getContentPane();

		addStartButton(startButtonPanel);
		pane.add(startButtonPanel, BorderLayout.NORTH);

		butPanel.setLayout(new GridLayout(boggleBoard.getSize(), boggleBoard.getSize()));
		addBoardboardButtons(butPanel);
		pane.add(butPanel, BorderLayout.CENTER);

		contPanel.setLayout(new GridLayout(1, 2));
		contPanel.setSize(400, 50);
		addControlboardButtons(contPanel);
		pane.add(contPanel, BorderLayout.SOUTH);

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
	 * Toggles the states of startButton, add and clearButton boardButtons
	 * 
	 * @param gameOver
	 *            - the state of the game
	 */
	private void toggleControlboardButtons(boolean gameOver) {
		if (gameOver == true) {
			startButton.setEnabled(true);
		} else {
			startButton.setEnabled(false);
		}

		// create new boggleboard and empty wordlist
		boggleBoard = new BoggleBoard(boggleBoard.getSize());
		wordList = new WordList();

		selValues.clear();
		submitButton.setEnabled(false);
		clearButton.setEnabled(false);
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
	 * Create word from the selValues button values
	 * 
	 * @return the resulting word
	 */
	private String getWord() {
		String result = "";
		for (JBoggleButton but : selValues) {
			result = result.concat(but.getText().toLowerCase());
		}

		return result;
	}

	/**
	 * Adds boardButtons to the game board
	 */
	private void addBoardboardButtons(JPanel butPanel) {

		for (int i = 0; i < boggleBoard.getSize(); i++) {
			for (int j = 0; j < boggleBoard.getSize(); j++) {
				JBoggleButton button = new JBoggleButton(boggleBoard.getCell(i,j));

				button.setActionCommand("board_button_event");
				button.addActionListener(bActionHandler);

				boardButtons.add(button);
				butPanel.add(button);
			}
		}
	}

	/**
	 * Selects the given button
	 * 
	 * @param button - button that was pressed
	 *            
	 */
	private void selectButton(JBoggleButton button) {
		selValues.add(button);
		button.setState(State.SELECTED);

		for (JBoggleButton but : boardButtons) {
			if (!but.getState().equals(State.SELECTED)) {
				if (button.inVicinity(but)) {
					but.setState(State.AVAILABLE);
				} else {
					but.setState(State.UNAVAILABLE);
				}
			}
		}

		// enable control boardButtons if game is running
		if (!startButton.isEnabled()) {
			// add the word if the word's length is at least 3 characters
			if (getWord().length() >= 3) {
				submitButton.setEnabled(true);
			}

			clearButton.setEnabled(true);
		}
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

		/**
		 * Start button's action listener
		 */
		private void startButtonAction() {
			toggleControlboardButtons(false);

			// create new boggleboard layout
			butPanel.removeAll();
			butPanel.validate();
			addBoardboardButtons(butPanel);

			// clear out last game's results
			listPanel.removeAll();
			listPanel.validate();
			listView = new JList<String>(new DefaultListModel<String>());

			timer.addActionListener(this);
			timer.setTimeRemaining(1 * 15);
			timer.startTimer();
		}

		/**
		 * Listener for the timer object
		 */
		private void timerAction() {
			toggleControlboardButtons(true);
		}

		/**
		 * Implementation for the submitButton button action after being pressed
		 */
		private void submitButtonAction() {
			for (JBoggleButton but : boardButtons) {
				but.setState(State.AVAILABLE);
			}

			DefaultListModel<String> model = (DefaultListModel<String>) listView.getModel();

			int oldSize = wordList.size();
			wordList.addWord(getWord());
			int newSize = wordList.size();

			if (oldSize != newSize) {
				model.addElement(getWord());
			}

			// add the scrollbar
			if (newSize == 1) {
				listPanel.add(new JScrollPane(listView));
			}

			selValues.clear();

			submitButton.setEnabled(false);
			clearButton.setEnabled(false);
		}

		/**
		 * Implementation for the clearButton button action after being pressed
		 */
		private void clearButtonAction() {
			for (JBoggleButton but : boardButtons) {
				but.setState(State.AVAILABLE);
			}

			selValues.clear();

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
				selectButton(button);
			}
		}
	}
}
