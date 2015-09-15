package tamm.org.boggle.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BoggleBoard implements Serializable{
	
	/**
	 * Serializable id
	 */
	private static final long serialVersionUID = 1016130578633869867L;
	public static final int DEFAULT_SIZE = 4;
	private static final List<String> letters = new ArrayList<String>(Arrays.asList("A",
			"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Qu", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
	
	private final int size;
	private String[][] fieldValues;
	
	/**
	 * The default constructor that uses the defaultSize value
	 */
	public BoggleBoard()
	{
		this(DEFAULT_SIZE);
	}
	
	/**
	 * Constructor that takes a value and creates a value*value size board
	 * @param s
	 */
	public BoggleBoard(int s)
	{
		size = s;
		fieldValues = new String[size][size];
		generateBoard();
	}
	
	/**
	 * Populates the board with values
	 */
	private void generateBoard()
	{
		for (int i = 0; i<size; i++)
		{
			for (int j = 0; j<size; j++)
			{
				String l = randomLetter();
				fieldValues[i][j] = l;
			}
		}
	}
	
	/**
	 * Helper to generate random letters
	 * @return String random letter
	 */
	private String randomLetter()
	{
		Random rand = new Random();
		return letters.get(rand.nextInt(letters.size()));
	}
	
	/**
	 * Getter for retrieving the letters list
	 * @return
	 */
	public List<String> getLetters()
	{
		return letters;
	}
	
	/**
	 * Get the value of an individual cell
	 * @param x
	 * @param y
	 * @return String cell value
	 */
	public String getCell(int x, int y)
	{
		return fieldValues[x][y];
	}
	
	/**
	 * Getter for the size instance variable
	 * @return int size
	 */
	public int getSize()
	{
		return size;
	}
	
	/**
	 * Getter for the default size of the board
	 * @return int default size
	 */
	public int getDefaultSize()
	{
		return DEFAULT_SIZE;
	}
	
	/**
	 * Getter for the values of the board
	 * @return String[][] values
	 */
	public String[][] getFieldValues()
	{
		return fieldValues;
	}
}
