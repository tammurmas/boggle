package tamm.org.boggle.wordlist;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BoggleBoardTest {
	
	public BoggleBoard board = new BoggleBoard(6);
	
  @Test
  public void testDefaultConstructor() {
    board = new BoggleBoard();
    Assert.assertEquals(board.getSize(), board.getDefaultSize());
  }
  
  @Test
  public void testConstructor() {
    Assert.assertEquals(board.getSize(), board.getFieldValues().length);
    Assert.assertEquals(board.getSize(), board.getFieldValues()[0].length);
  }
  
  @Test
  public void testBoardIsCorrectlyInitialized()
  {
	  Assert.assertTrue(checkCellValues(board));
	  Assert.assertTrue(checkCellValues(new BoggleBoard()));
  }

  public boolean checkCellValues(BoggleBoard board)
  {
	  String[][] cells = board.getFieldValues();
	  
	  
	  for(int i=0; i<board.getSize(); i++)
	  {
		  for(int j=0; j<board.getSize(); j++)
		  {
			  if(cells[i][j] == null)
			  {
				  return false;
			  }
			  
			  if(cells[i][j] != board.getCell(i, j))
			  {
				  return false;
			  }
			  
			  if(!board.getLetters().contains(board.getCell(i, j)))
			  {
				  return false;
			  }
		  }  
	  }
	  
	  return true;
  }

}
