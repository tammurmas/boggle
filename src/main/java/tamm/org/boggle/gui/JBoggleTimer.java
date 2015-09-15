package tamm.org.boggle.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.event.EventListenerList;

/**
 * This is a simple "Boggle timer" class that can be used to track the time
 * remaining in a round of Boggle.
 * <p>
 * This timer is very easy to use:
 * 
 * <pre>
 *   JBoggleTimer timer;
 *   ...
 *   timer.setTimeRemaining(3 * 60);  // 3 minutes
 *   timer.startTimer();
 * </pre>
 * <p>
 * When the timer hits 0, it fires an action event that listeners can register
 * for.
 **/
public class JBoggleTimer extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5888537103525090825L;

	/** The string displayed in the user interface. **/
	private static final String REMAINING = "Time Remaining:  ";

	/** Timer for updating the user interface until time is up. **/
	private Timer timer;

	/**
	 * The action-command in the action fired by this component when the timer
	 * completes.
	 **/
	private String command;

	/** The total time that the timer is specified for. **/
	private int totalSeconds;

	/** The time (in milliseconds) when the Boggle timer was started. **/
	private long startTime;

	/**
	 * This inner class handles events from the internal timer, to update the
	 * user interface and to figure out when time has run out.
	 **/
	private class TimerHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			long currTime = System.currentTimeMillis();

			int elapsed = (int) (currTime - startTime);

			String timeString = "0:00";
			if (elapsed < totalSeconds * 1000) {
				// The time is still going. Generate a time-string for the
				// elapsed
				// time.
				timeString = getTimeString(totalSeconds * 1000 - elapsed);
			} else {
				// The time has fully elapsed. Stop the timer and fire an event.
				timer.stop();
				fireActionPerformed();
			}

			setText(REMAINING + timeString);
		}
	}

	/**
	 * Construct a new Boggle timer for use in a user interface.
	 **/
	public JBoggleTimer() {
		super(REMAINING + "0:00", JLabel.CENTER);

		setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

		// Create a timer that fires every 0.1 seconds, and register our
		// own action-event listener for the timer's events.
		timer = new Timer(100, new TimerHandler());
	}

	/**
	 * A helper method that turns an "elapsed time" in milliseconds, into a
	 * string of the format "m:ss". The seconds part is always two digits.
	 **/
	private String getTimeString(int elapsedTime) {
		int seconds = elapsedTime / 1000;
		int minutes = seconds / 60;
		seconds = seconds % 60;

		String result = minutes + ":";
		if (seconds < 10)
			result = result + "0" + seconds;
		else
			result = result + seconds;

		return result;
	}

	/**
	 * Set the amount of time remaining on the timer. The argument is in
	 * seconds; for example, Boggle timers are typically 3 minutes, so this can
	 * be set to 180 for 3 minutes.
	 **/
	public void setTimeRemaining(int seconds) {
		totalSeconds = seconds;

		// Update the UI to show the time remaining. We pass 0 for the "elapsed
		// time" since the clock hasn't been started yet.
		setText(REMAINING + getTimeString(0));
	}

	/**
	 * This method starts the timer counting down. There is no "stop timer"
	 * method because it isn't presently needed in the current functionality.
	 **/
	public void startTimer() {
		startTime = System.currentTimeMillis();
		timer.start();
	}

	/**
	 * Adds an action-listener to this component so that it will receive action
	 * events when the timer runs out.
	 **/
	public void addActionListener(ActionListener lis) {
		listenerList.add(ActionListener.class, lis);
	}

	/**
	 * Removes an action-listener so that it won't receive events from this
	 * component.
	 **/
	public void removeActionListener(ActionListener lis) {
		listenerList.remove(ActionListener.class, lis);
	}

	/**
	 * This method sets the action-command that is included on the action-events
	 * fired by this component.
	 **/
	public void setActionCommand(String command) {
		this.command = command;
	}

	/**
	 * This helper method is used to fire an action event when the Boggle timer
	 * runs out.
	 **/
	protected void fireActionPerformed() {
		// Guaranteed to return a non-null array
	     Object[] listeners = listenerList.getListenerList();
	     
	     //the fired event
	     setActionCommand("game_over_event");
	     ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
	     
	     // Process the listeners last to first, notifying
	     // those that are interested in this event
	     for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==ActionListener.class) {
	             // Lazily create the event:
	             ((ActionListener)listeners[i+1]).actionPerformed(e);
	             break;
	         }
	     }
	     
	     //clear out event listeners
	     listenerList = new EventListenerList();
	}
}
