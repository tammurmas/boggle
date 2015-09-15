package tamm.org.boggle.gui.dialog;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import tamm.org.boggle.server.ClientInfo;
import tamm.org.boggle.server.GameResults;

/**
 * This class provides a simple dialog for displaying the game results from the
 * Boggle server. It uses the {@link JClientInfoView} class to display each
 * client's results from the round.
 **/
public class JGameResultsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2079951340449630132L;

	/**
	 * Construct a new game-results dialog to display the specified results. The
	 * parent frame is specified so that the dialog can be centered within the
	 * parent window. Finally, the dialog can be made to be modal or modeless by
	 * using the <tt>modal</tt> argument.
	 **/
	public JGameResultsDialog(GameResults results, JFrame parent, boolean modal) {
		super(parent, "Boggle Game Results", modal);

		Container content = getContentPane();
		content.setLayout(new BorderLayout());

		// A helpful label.

		content.add(new JLabel("The results for this round of Boggle are:"),
				BorderLayout.NORTH);

		// Now a tabbed pane with one tab per each client that participated in
		// this
		// round.

		JTabbedPane tabResults = new JTabbedPane();

		for (String clientName : results.getClientNames()) {
			ClientInfo clientInfo = results.getClientResult(clientName);

			tabResults.addTab(clientName, new JClientInfoView(clientInfo));
		}

		content.add(tabResults, BorderLayout.CENTER);

		// Finally, a button to close everything up.

		JButton btn = new JButton("Close");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		content.add(btn, BorderLayout.SOUTH);

		// Lay out the contents of the dialog.
		pack();

		// Center the dialog with respect to its parent, so we look nice...
		setLocationRelativeTo(parent);
	}
}
