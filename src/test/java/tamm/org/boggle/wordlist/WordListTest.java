package tamm.org.boggle.wordlist;

import java.io.File;
import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import tamm.org.boggle.board.WordList;

public class WordListTest {

	WordList wList;
	WordList a;
	WordList b;
	
	private final String fileName = "E:\\text.txt";

	@BeforeTest
	public void buildUp()
	{
		a = new WordList();
		a.addWord("FooA");
		a.addWord("BarA");
		a.addWord("Blah");
		
		b = new WordList();
		b.addWord("FooB");
		b.addWord("BarB");
		b.addWord("Blah");
		
		wList = new WordList();
	}
	
	@AfterTest
	public void tearDown()
	{
		a = new WordList();
		b = new WordList();
		wList = new WordList();
	}
	
	@Test
	public void assertWordListSizeIsZero() {
		Assert.assertEquals(wList.size(), 0);
	}

	@Test
	public void assertWordsAreAdded() {
		wList.addWord("foo");
		wList.addWord("bar");
		wList.addWord("baz");
		
		Assert.assertEquals(wList.size(), 3);

		Assert.assertTrue(wList.containsWord("Foo"));
		Assert.assertTrue(wList.containsWord("bar "));
		Assert.assertTrue(wList.containsWord(" Baz"));

		Assert.assertFalse(wList.containsWord("blah"));
	}

	@Test
	public void testReadWordsFromFile() throws FileNotFoundException {
		File file = new File(fileName);
		wList = new WordList(file);

		Assert.assertEquals(wList.size(), 3);
	}

	@Test(expectedExceptions = { FileNotFoundException.class })
	public void testMissingFile() throws FileNotFoundException {
		File f = new File("missing.txt");
		assert !f.exists();
		wList = new WordList(f);
	}
	
	@Test
	public void testSubtract()
	{
		a.subtract(b);
		Assert.assertEquals(2, a.size());
		Assert.assertFalse(a.containsWord("blah"));
	}
	
	@Test
	public void testAddAll()
	{
		a.addWordList(b);
		Assert.assertEquals(5, a.size());
		Assert.assertTrue(a.containsWord("FooB"));
	}
}
