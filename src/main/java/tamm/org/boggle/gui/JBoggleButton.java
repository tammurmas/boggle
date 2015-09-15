package tamm.org.boggle.gui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

public class JBoggleButton extends JButton {

	private static final long serialVersionUID = 1L;
	
	//border to represent a button that cannot be selected
	private static final Border UNAVAILABLE_BORDER = BorderFactory.createLineBorder(Color.GRAY, 3);
	//border to represent a button that can be selected
	private static final Border AVAILABLE_BORDER = BorderFactory.createLineBorder(Color.GREEN, 3);
	//border to represent a button that has been selected
	private static final Border SELECTED_BORDER = BorderFactory.createLineBorder(Color.RED, 3);
	
	//font to represent a button that is unavailable for selection
	private static final Font UNAVAILABLE_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 30);
	//font to represent a button that is available for selection or has already been selected
	private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 30);
	
	
	private State state;
	
	/**
	 * Enum to represent the state of the button
	 * @author Dell
	 *
	 */
	public enum State
	{
		UNAVAILABLE,
		AVAILABLE,
		SELECTED
	}
	
	/**
	 * Constructor for creating a new button with the given text value
	 * @param text - the textual value of the button
	 */
	public JBoggleButton(String text)
	{
		setText(text);
		setState(State.AVAILABLE);
		setFont(DEFAULT_FONT);
	}
	
	/**
	 * Getter for the button's state
	 * @return
	 */
	public State getState()
	{
		return state;
	}
	
	/**
	 * Setter for the button's state
	 * @param s - the state
	 */
	public void setState(State s)
	{
		state = s;
		switch (s) {
		case AVAILABLE:
			setBorder(AVAILABLE_BORDER);
			setFont(DEFAULT_FONT);
			setEnabled(true);
			break;
		case UNAVAILABLE:
			setBorder(UNAVAILABLE_BORDER);
			setFont(UNAVAILABLE_FONT);
			setEnabled(false);
			break;
		case SELECTED:
			setBorder(SELECTED_BORDER);
			setFont(DEFAULT_FONT);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Returns true if the given is in the vicinity of the parameter button
	 * @param button - the button to be checked
	 * @return boolean
	 */
	public boolean inVicinity(JBoggleButton button)
	{
		if(getX()+getSize().getWidth()==button.getX() && getY()==button.getY())
		{
			return true;
		}
		else if(getX()-getSize().getWidth()==button.getX() && getY()==button.getY())
		{
			return true;
		}
		else if(getY()+getSize().getHeight()==button.getY() && getX()==button.getX())
		{
			return true;
		}
		else if(getY()-getSize().getHeight()==button.getY() && getX()==button.getX())
		{
			return true;
		}
		else if(getX()+getSize().getWidth()==button.getX() && getY()-getSize().getHeight()==button.getY())
		{
			return true;
		}
		else if(getX()-getSize().getWidth()==button.getX() && getY()-getSize().getHeight()==button.getY())
		{
			return true;
		}
		else if(getX()+getSize().getWidth()==button.getX() && getY()+getSize().getHeight()==button.getY())
		{
			return true;
		}
		else if(getX()-getSize().getWidth()==button.getX() && getY()+getSize().getHeight()==button.getY())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
