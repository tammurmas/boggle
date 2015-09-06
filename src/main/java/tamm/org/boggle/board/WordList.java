package tamm.org.boggle.board;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author Urmas
 *
 */
public class WordList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5544543279417459668L;
	/** a set to contain the possible words found **/
	private Set<String> wordList;
	
	/**
	 * Constructor that creates a new WordList instance with an empty set of words
	 */
	public WordList()
	{
		wordList = new HashSet<String>();
	}
	
	/**
	 * Constructor that takes a File instance as a parameter and adds it contents to the word list
	 * @param file - the input file
	 * @throws FileNotFoundException 
	 */
	public WordList(File file) throws FileNotFoundException
	{
		readWordsFromFile(file);
	}
	
	/**
	 * Create the word list from the contents of the file parameter
	 * @param file - the file to be used to populate the word list
	 * @throws FileNotFoundException 
	 */
	private void readWordsFromFile(File file) throws FileNotFoundException {
		if(file.exists() == false)
		{
			throw new FileNotFoundException();
		}
		
		wordList = new HashSet<String>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			
			while(true)
			{
				String line = in.readLine();
				if(line  != null)
				{
					addWord(preprocessWord(line));
				}
				else
				{
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * Return size of the word list
	 * @return int
	 */
	public int size()
	{
		return wordList.size();
	}
	
	/**
	 * Add a preprocessed word to the word list
	 * @param word - the word to be added
	 */
	public void addWord(String word)
	{
		String processed = preprocessWord(word);
		
		if(processed != "")
		{
			wordList.add(processed);
		}
	}
	
	/**
	 * Merge the two word lists
	 * @param b - the other word list to be merged
	 */
	public void addWordList(WordList b)
	{
		wordList.addAll(b.getWordList());
	}
	
	/**
	 * computes the set-difference between two WordList objects
	 * @param b - the second 
	 */
	public void subtract(WordList b)
	{
		Iterator<String> iter = b.getWordList().iterator();
		
		while(iter.hasNext())
		{
			wordList.remove(iter.next());
		}
	}
	
	/**
	 * Pre-process a word, change it to lower case and trim whitespace
	 * @param word - the word to be processed
	 * @return String
	 */
	private String preprocessWord(String word)
	{
		return word.toLowerCase().trim();
	}
	
	/**
	 * Check if the given word is contained in the word list
	 * @param word - the word to be checked
	 * @return boolean
	 */
	public boolean containsWord(String word)
	{
		return wordList.contains(preprocessWord(word));
	}
	
	/**
	 * Getter
	 * @return Set
	 */
	public Set<String> getWordList()
	{
		return wordList;
	}
	
	/**
	 * To return the contents of the word list
	 */
	@Override
	public String toString()
	{
		String words = "";
		for(String s: wordList)
		{
			words += s+"; ";
		}
		
		return words;
	}
}
