package tamm.org.boggle.client;

import tamm.org.boggle.board.BoggleBoard;
import tamm.org.boggle.gui.JBoggleBoard;

public class BoggleClient {

	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              JBoggleBoard boardGUI = new JBoggleBoard(BoggleBoard.DEFAULT_SIZE);
              boardGUI.createAndShowGUI();
            }
        });
    }
}
