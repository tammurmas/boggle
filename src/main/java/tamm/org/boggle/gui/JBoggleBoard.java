package tamm.org.boggle.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tamm.org.boggle.board.BoggleBoard;
import tamm.org.boggle.gui.JBoggleButton.State;

public class JBoggleBoard extends JPanel {

	private static final long serialVersionUID = 1L;
	//the main frame containing parts of the BoggleBoard
	private JFrame mainFrame;
	
	//array of buttons on the board
	private final List<JBoggleButton> buttons  = new ArrayList<JBoggleButton>();
	//array of selected buttons
	private List<JBoggleButton> selected = new ArrayList<JBoggleButton>();
	//the boggle board instance 
	private BoggleBoard boggleBoard;
	//label to show the game results
	private final JLabel resLabel = new JLabel("Results: ");
	
	/**
	 * Constructor
	 * @param size - the size of the board
	 */
	public JBoggleBoard(int size)
	{
		boggleBoard = new BoggleBoard(size);
	}
	
	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              JBoggleBoard boardGUI = new JBoggleBoard(BoggleBoard.DEFAULT_SIZE);
              boardGUI.createAndShowGUI();
            }
        });
    }
	
	/**
	 * Set up the board GUI
	 */
	private void createAndShowGUI()
	{
		mainFrame = new JFrame("Boggle Game");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainFrame.setSize(400, 400);
		Container pane = mainFrame.getContentPane();
		
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new GridLayout(boggleBoard.getSize(),boggleBoard.getSize()));
		addBoardButtons(butPanel);
		pane.add(butPanel, BorderLayout.NORTH);
		
		JPanel contPanel = new JPanel();
		contPanel.setLayout(new GridLayout(1,2));
		contPanel.setSize(400, 50);
		addControlButtons(contPanel);
		pane.add(contPanel, BorderLayout.SOUTH);
		
		JPanel resPanel = new JPanel();
		resPanel.setLayout(new GridLayout(1,1));
		
		resPanel.add(resLabel);
		pane.add(resPanel, BorderLayout.CENTER);
		
		mainFrame.setVisible(true);
	}
	
	/**
	 * Add buttons on the control panel
	 * @param contPanel
	 */
	private void addControlButtons(JPanel contPanel)
	{
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	for(JBoggleButton but: buttons)
        		{
            		but.setState(State.AVAILABLE);
        		}
            	selected.clear();
            }
        });
		
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	submitButtonAction();
            }
        });
		
		contPanel.add(clear);
		contPanel.add(submit);
	}
	
	/**
	 * Implementation for the submit button action after being pressed
	 */
	private void submitButtonAction() {
		for (JBoggleButton but : buttons) 
		{
			but.setState(State.AVAILABLE);
		}
		
		resLabel.setText(resLabel.getText()
				.concat((resLabel.getText().equals("Results: ") ? "" : "; ")
						+ getWord()));
		selected.clear();
	}
	
	/**
	 * Create word from the selected button values
	 * @return the resulting word
	 */
	private String getWord()
	{
		String result = "";
		for (JBoggleButton but: selected)
		{
			result = result.concat(but.getText().toLowerCase());
		}
		
		return result;
	}
	
	/**
	 * Adds buttons to the game board
	 */
	private void addBoardButtons(JPanel butPanel)
	{
		for(int i=0; i<boggleBoard.getSize(); i++)
		{
			for(int j=0; j<boggleBoard.getSize(); j++)
			{
				JBoggleButton button = new JBoggleButton(boggleBoard.getCell(i, j));
				
				button.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e)
		            {
		            	JBoggleButton button = (JBoggleButton) e.getSource();
		            	
		            	if(!button.getState().equals(State.UNAVAILABLE))
		            	{
		            		setButtonStates(button);
		            	}
		            }
		        });
				
				buttons.add(button);
				butPanel.add(button);
			}
		}
	}
	
	/**
	 * Changes buttons' states according to their selection
	 * @param button - the button that was pressed
	 */
	private void setButtonStates(JBoggleButton button) {
		switch (button.getState()) {
		case AVAILABLE:
			selectButton(button);
			break;
		case SELECTED:
			int index = 0;
			// find the button that was pressed
			for (int i = 0; i < selected.size(); i++) {
				if (selected.get(i).equals(button)) {
					index = i;
					break;
				}
			}

			// set the state for rest of the buttons to unavailable
			if (index != selected.size()) {
				for (int i = index + 1; i < selected.size(); i++) {
					selected.get(i).setState(State.UNAVAILABLE);
				}
			}

			// truncate the list and select the button
			selected = selected.subList(0, index);
			selectButton(button);
			break;
		default:
			break;
		}

	}
	
	/**
	 * Selects the given button
	 * @param button - button that was pressed
	 */
	private void selectButton(JBoggleButton button)
	{
		selected.add(button);
		button.setState(State.SELECTED);
		
		for(JBoggleButton but: buttons)
		{
			if(!but.getState().equals(State.SELECTED))
			{
				if(button.inVicinity(but))
				{
					but.setState(State.AVAILABLE);
				}
				else
				{
					but.setState(State.UNAVAILABLE);
				}
			}
		}
	}
}
