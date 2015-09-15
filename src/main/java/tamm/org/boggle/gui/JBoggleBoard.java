package tamm.org.boggle.gui;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import tamm.org.boggle.board.BoggleBoard;
import tamm.org.boggle.gui.JBoggleButton.State;

/**
 * This class creates a JPanel containing a number of game buttons
 * @author Urmas
 *
 */
public class JBoggleBoard extends JPanel {

	private static final long serialVersionUID = 1L;

	// array of boardButtons on the board
	private final List<JBoggleButton> boardButtons = new ArrayList<JBoggleButton>();
	// array of selected values from the board
	private List<JBoggleButton> selValues = new ArrayList<JBoggleButton>();
	//boggle board size
	private int boardSize;
	// the boggle board instance
	private BoggleBoard boggleBoard;
	// action handler for catching action events from board buttons
	private ActionListener bActionHandler;

	/**
	 * Constructor
	 * 
	 * @param size
	 *            - the size of the board
	 */
	public JBoggleBoard(int size) {
		boardSize = size;
	}
	
	/**
	 * Sets action listener for the JBoggleBoard instance
	 */
	public void setActionListener(ActionListener listener)
	{
		bActionHandler = listener;
	}
	
	/**
	 * Create word from the selValues button values
	 * 
	 * @return the resulting word
	 */
	public String getWord() {
		String result = "";
		for (JBoggleButton but : selValues) {
			result = result.concat(but.getText().toLowerCase());
		}

		return result;
	}
	
	/**
	 * Return the size of the board
	 * @return int - size of the board
	 */
	public int getBoardSize()
	{
		return boardSize;
	}
	
	public void clearSelValues()
	{
		selValues.clear();
	}
	
	public List<JBoggleButton> getBoardButtons()
	{
		return boardButtons;
	}
	
	/**
	 * Adds boardButtons to the game board
	 */
	public void setBoard(BoggleBoard b) {
		
		if(b.getSize() != getBoardSize())
		{
			throw new IllegalArgumentException("Size mismatch!");
		}
		else
		{
			boggleBoard = b;
		}
		
		for (int i = 0; i < boggleBoard.getSize(); i++) {
			for (int j = 0; j < boggleBoard.getSize(); j++) {
				JBoggleButton button = new JBoggleButton(boggleBoard.getCell(i,j));

				button.setActionCommand("board_button_event");
				button.addActionListener(bActionHandler);

				boardButtons.add(button);
				add(button);
			}
		}
	}
	
	public BoggleBoard getBoard()
	{
		return boggleBoard;
	}
	
	/**
	 * Selects the given button
	 * 
	 * @param button - button that was pressed
	 *            
	 */
	public void selectButton(JBoggleButton button) {
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
	}
	
	/*public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              JFrame f = new JFrame("BoggleTest");
              f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

              JBoggleBoard boardGUI = new JBoggleBoard(BoggleBoard.DEFAULT_SIZE);
              boardGUI.setLayout(new GridLayout(boardGUI.getBoardSize(), boardGUI.getBoardSize()));
              boardGUI.setActionListener(boardGUI.new BoggleActionHandler());
              f.add(boardGUI);

              f.pack();
              f.setVisible(true);
              f.setSize(300, 300);

              boardGUI.setBoard(new BoggleBoard());
            }
        });
    }
	
	private class BoggleActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "board_button_event":
				boardButtonAction(e);
				break;
			default:
				break;
			}
		}
		
		*//**
		 * Action for each of the board buttons
		 * 
		 * @param e
		 *            - the event that took place
		 *//*
		private void boardButtonAction(ActionEvent e) {
			JBoggleButton button = (JBoggleButton) e.getSource();

			if (button.getState().equals(State.AVAILABLE)) {
				selectButton(button);
			}
		}
	}*/

}
