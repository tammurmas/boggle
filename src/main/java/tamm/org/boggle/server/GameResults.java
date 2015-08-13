package tamm.org.boggle.server;

import java.util.*;

/**
 * This class is used as a wrapper for all of the game-results of a single round
 * of Boggle.
 **/
public class GameResults {

	/**
	 * This hash-map contains the information about each client that
	 * participated in the most recent round of Boggle. Each client's
	 * information is associated with the client's unique name, as specified in
	 * the call to {@link BoggleServer#startGame}.
	 **/
	private HashMap<String, ClientInfo> clientResults;

	/** Construct a new empty game-results object. **/
	public GameResults() {
		clientResults = new HashMap<String, ClientInfo>();
	}

	/**
	 * This method clears out the game results object so it can be reused for
	 * the next round of play.
	 **/
	public void clear() {
		clientResults.clear();
	}

	/**
	 * This method is used to store all results from players that participated
	 * in the most recent round. It also computes the set of valid words that
	 * each player actually found, and computes each player's score.
	 **/
	public void computeResults(List<ClientInfo> playerResults) {
		// TODO!

		// For each client, construct a word-list containing the words that only
		// that client found. Store this filtered word-list on that client's
		// ClientInfo object using the setFilteredWords(...) method.
		//
		// Once you have found the words that each client has found, compute the
		// score for that client, using the filtered-words list and the
		// following
		// score chart:
		//
		// Word-Length Points
		// 3 1
		// 4 1
		// 5 2
		// 6 3
		// 7 5
		// 8+ 11
		//
		// You can use the setScore(...) method to store the client's score.
	}

	/**
	 * Returns an unmodifiable version of the {@link #clientResults} map, which
	 * contains all of the game results for the current round of Boggle.
	 **/
	public Map<String, ClientInfo> getClientResults() {
		return Collections.unmodifiableMap(clientResults);
	}

	/**
	 * Returns an unmodifiable set of the client-names that are contained within
	 * the game-results object.
	 **/
	public Set<String> getClientNames() {
		return Collections.unmodifiableSet(clientResults.keySet());
	}

	/**
	 * Returns the client-information object for the specified client, or
	 * <tt>null</tt> if the name is unrecognized.
	 **/
	public ClientInfo getClientResult(String clientName) {
		return clientResults.get(clientName);
	}
}