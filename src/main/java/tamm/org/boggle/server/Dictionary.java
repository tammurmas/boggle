package tamm.org.boggle.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a dictionary of English words 
 * SOURCE: http://svnweb.freebsd.org/csrg/share/dict/words?revision=61569&view=co
 * 
 * @author Urmas
 *
 */
public class Dictionary {

	private final Set<String> words = new HashSet<String>();
	private final String fileName = "dictionary_en.txt";

	/**
	 * Constructor that reads in the words from a file, specified by the fileName
	 */
	public Dictionary() {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(getClass().getClassLoader().getResource(fileName).toURI())))) {
			String line = null;

			while ((line = reader.readLine()) != null) {
				words.add(line.toLowerCase());
			}
		} catch (IOException | URISyntaxException e) {
			System.err.println("Problem creating dictionary!");
			e.printStackTrace();
		}
	}

	/**
	 * Returns the set of words that make up the dictionary
	 * @return
	 */
	public Set<String> getWords() {
		return words;
	}

}
