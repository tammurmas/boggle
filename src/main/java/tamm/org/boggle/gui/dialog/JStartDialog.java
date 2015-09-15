package tamm.org.boggle.gui.dialog;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This is a very simple modal dialog that the Boggle server can show when
 * waiting for a Boggle game to start. The dialog is a bit special in that it
 * actually prevents the user from closing it! This is done by calling
 * {@link javax.swing.JDialog#setDefaultCloseOperation()} with the
 * {@link javax.swing.WindowConstants#DO_NOTHING_ON_CLOSE} value. This means
 * that the dialog must be closed <em>programmatically</em>, by calling
 * <tt>setVisible(false)</tt> on the dialog, or by calling <tt>dispose()</tt> on
 * the dialog.
 **/
public class JStartDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5698113625036338081L;

	/**
	 * Construct a new "start game" dialog. The parent-frame is passed to the
	 * dialog constructor so that the dialog can be centered within the parent
	 * window.
	 **/
	public JStartDialog(JFrame parent) {
		super(parent, "Starting Game", true);

		if (parent == null)
			throw new NullPointerException();

		// Set up the appearance of the dialog. Also, disable any window-closing
		// operations so that we can make the user wait until the next game
		// actually starts.

		JOptionPane pane = new JOptionPane("Waiting for next game to start...",
				JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);

		setContentPane(pane);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		// Lay out the contents of the dialog.
		pack();

		// Center the dialog with respect to its parent, so we look nice...
		setLocationRelativeTo(parent);
	}
}
