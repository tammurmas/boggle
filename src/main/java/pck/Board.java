package pck;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Board  extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static class BoardHandler implements ActionListener {
		
		private static final BoardHandler bHandler = new BoardHandler();
		
		private BoardHandler(){}
		
		public static BoardHandler getInstance()
		{
			return bHandler;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("button"))
			{
				System.out.println("Button pressed");
			}
		}
	}
	
	public static void main(String[] args)
	{
		Board board = new Board();
		board.createAndShowGUI();
	}
	
	private void createAndShowGUI()
	{
		JFrame mainFrame = new JFrame("Frame");
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainFrame.setSize(400, 400);
		
		Container pane = mainFrame.getContentPane();
		
		JButton button = new JButton("Java Code Geeks - Java Examples");
		
		button.setActionCommand("button");
		
		button.addActionListener(new BoardHandler());
		
		pane.add(button);
		
		mainFrame.setVisible(true);
	}
	
}
