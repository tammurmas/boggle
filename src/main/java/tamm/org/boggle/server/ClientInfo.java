package tamm.org.boggle.server;

import java.io.Serializable;

import tamm.org.boggle.board.WordList;

/**
 * This object represents the information for a single client that wants to play
 * a round of Boggle.  Each time a client calls
 * {@link BoggleServerApp#startGame}, a new client-information object is created
 * to track that client.
 * <p>
 * This class is also used to return the game-results to all clients when a
 * round of Boggle is completed.
 **/
public class ClientInfo implements Serializable{

  /**
	 * 
	 */
	private static final long serialVersionUID = -1705675367217831288L;


/**
   * This enum specifies the states that clients can be in while they are
   * playing a round of Boggle.
   **/
  public enum State {
    WAITING,
    PLAYING,
    FINISHED
  }


  /**
   * The name of the client that they submitted, when the client called
   * {@link BoggleServer#startGame}.
   **/
  private String name;


  /** The current state of the client, as maintained by the Boggle server. **/
  private State state;


  /**
   * The list of words that the client reported at the end of the current round
   * of play.
   **/
  private WordList words;


  /**
   * The list of words that this client actually gets a score for; i.e. the
   * words that only this client found, that are also in the dictionary.
   **/
  private WordList filteredWords;


  /**
   * The score for this client, computed from the {@link #filteredWords} set of
   * words, which only contains the valid words that only this client found.
   **/
  private int score;


  /**
   * Constructs a new client-information object for the specified client name.
   * All other values are initialized to reasonable defaults.
   **/
  public ClientInfo(String clientName) {
    if (clientName == null)
      throw new NullPointerException();

    setName(clientName);
    state = State.WAITING;
    words = null;
    filteredWords = null;
    score = 0;
  }


  /** Returns the client's current state. **/
  public State getState() {
    return state;
  }


  /** Returns true if the client's current state is WAITING. **/
  public boolean isWaiting() {
    return (state == State.WAITING);
  }


  /** This helper method sets the client's state to the specified value. **/
  public void setState(State newState) {
    if (newState == null)
      throw new NullPointerException();

    state = newState;
  }


  public void setPlaying() {
    setState(State.PLAYING);
  }


  public void setWaiting() {
    setState(State.WAITING);
  }


  /**
   * This method allows the caller to store the list of words that the client
   * submitted to the server.  The client is also moved to the FINISHED state.
   *
   * @throws NullPointerException if words is null
   * @throws IllegalStateException if the client isn't in the PLAYING state
   **/
  public void setWords(WordList words) {
    if (words == null)
      throw new NullPointerException();

    if (state != State.PLAYING)
      throw new IllegalStateException();

    this.words = words;

    setState(State.FINISHED);
  }


  /** Returns the set of words that this client submitted to the server. **/
  public WordList getWords() {
    return words;
  }


  /**
   * This method allows the caller to store the list of words that <em>only
   * this client</em> found, as compared to all other clients.  It does not
   * compute the client's score from the word-list.
   *
   * @throws NullPointerException if filteredWords is null
   * @throws IllegalStateException if the client isn't in the FINISHED state
   **/
  public void setFilteredWords(WordList filteredWords) {
    if (filteredWords == null)
      throw new NullPointerException();

    if (state != State.FINISHED)
      throw new IllegalStateException();

    this.filteredWords = filteredWords;
  }


  /**
   * Returns the set of words that only this client found, as compared to the
   * other clients' submissions.
   **/
  public WordList getFilteredWords() {
    return filteredWords;
  }


  /** Returns this client's score. **/
  public int getScore() {
    return score;
  }


  /** Sets this client's score to the specified value. **/
  public void setScore(int val) {
    score = val;
  }


public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}
}

