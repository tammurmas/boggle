package tamm.org.boggle.gui.dialog;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import tamm.org.boggle.board.WordList;
import tamm.org.boggle.server.ClientInfo;


/**
 * This class provides a view of one Boggle client's results.  This is used in
 * the {@link JGameResultsDialog} class to display all the clients' results from
 * the server.
 **/
public class JClientInfoView extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = -7230645858268937629L;
/** This is the client-information from the Boggle server. **/
  private ClientInfo info;


  /**
   * Construct a new client-info view that displays the specified information.
   **/
  public JClientInfoView(ClientInfo info) {
    // Store the client-info we are handed, as long as it isn't null.

    if (info == null)
      throw new NullPointerException();

    this.info = info;

    // Use a grid-bag layout to make the view resizeable.
    GridBagLayout gbl = new GridBagLayout();
    setLayout(gbl);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;

    JLabel lbl;

    // We always want extra horizontal space to be distributed
    // between the rows evenly.
    gbc.weightx = 1.0;

    // Basic info:  Username and their score

    gbc.weighty = 0.0;

    lbl = new JLabel("User:  " + info.getName());
    gbl.setConstraints(lbl, gbc);
    add(lbl);

    gbc.gridwidth = GridBagConstraints.REMAINDER;

    lbl = new JLabel("Score:  " + info.getScore());
    gbl.setConstraints(lbl, gbc);
    add(lbl);

    // Word list labels:

    gbc.gridwidth = GridBagConstraints.RELATIVE;

    lbl = new JLabel("Words submitted:");
    gbl.setConstraints(lbl, gbc);
    add(lbl);

    gbc.gridwidth = GridBagConstraints.REMAINDER;

    lbl = new JLabel("Words scored:");
    gbl.setConstraints(lbl, gbc);
    add(lbl);

    // Word lists themselves:

    gbc.weighty = 1.0;

    gbc.gridwidth = GridBagConstraints.RELATIVE;
    JList<String> originalWords = new JList<String>(getArrayFromWordList(info.getWords()));
    gbl.setConstraints(originalWords, gbc);
    add(new JScrollPane(originalWords));

    gbc.gridwidth = GridBagConstraints.REMAINDER;
    JList<String> filteredWords =
      new JList<String>(getArrayFromWordList(info.getFilteredWords()));
    gbl.setConstraints(filteredWords, gbc);
    add(new JScrollPane(filteredWords));
  }


  /** Returns the {@link ClientInfo} object that this view is displaying. **/
  public ClientInfo getClientInfo() {
    return info;
  }


  /** Returns the name of the client that the client-info view is for. **/
  public String getClientName() {
    return info.getName();
  }


  /** A simple helper to turn a word-list into a sorted array of strings. **/
  private static String[] getArrayFromWordList(WordList words) {
    ArrayList<String> wordArray = new ArrayList<String>();

    Iterator<String> iter = words.iterator();
    while (iter.hasNext())
      wordArray.add(iter.next());

    String[] array = wordArray.toArray(new String[0]);
    Arrays.sort(array);

    return array;
  }
}
