package net.developmentality.triptimer;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TripTimer extends Activity {
	private Date startTime;
	private Date pauseTimeStart;
	private Date pauseTimeEnd;
	private Date endTime;
	
	private long elapsedPauseTime = 0;
	
	private long startUptimeMs;
	
	private enum TimerState {
		NOT_STARTED,
		RUNNING,
		PAUSED,
		STOPPED
	}
	
	private TimerState curState = TimerState.NOT_STARTED;
	
	
	private Button startPauseButton;
	private Button stopButton;
	
	private TextView digitalClock;
	private Handler mHandler = new Handler();
	
	private static final String TAG = "TripTimer";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startPauseButton = (Button) findViewById(R.id.start);
        stopButton = (Button) findViewById(R.id.stop);
        digitalClock = (TextView) findViewById(R.id.digitalClock);
    }
    
    // modified from http://developer.android.com/resources/articles/timed-ui-updates.html
    private Runnable mUpdateTimeTask = new Runnable() {
 	   public void run() {
 		   final long start = startUptimeMs;
 		   // Subtract out the paused time
 	       long millis =SystemClock.uptimeMillis() - start - elapsedPauseTime;
 	       digitalClock.setText(minutesSecondsFormat(millis));
 	       mHandler.postAtTime(this,
 	    		   SystemClock.uptimeMillis() + 1000);
 	   }
 	};
    
    public void startOrPause(View view) {
    	switch (curState) {
	    	case NOT_STARTED:
	    		startClock();
	    		break;
	    	case RUNNING:
	    		pauseClock();
	    		break;
	    	case PAUSED:
	    		resumeClock();
	    		break;
	    	case STOPPED:
	    		resetClock();
	    		break;
    	}
    }
    
 // modified from http://developer.android.com/resources/articles/timed-ui-updates.html
    private void startClock() {
    	startTime = new Date();
		startUptimeMs = SystemClock.uptimeMillis();
		startPauseButton.setText(R.string.pause);
        mHandler.postDelayed(mUpdateTimeTask, 100);
        stopButton.setEnabled(true);
        curState = TimerState.RUNNING;
    }
    
    private void pauseClock() {
    	pauseTimeStart = new Date();
    	startPauseButton.setText(R.string.resume);
    	mHandler.removeCallbacks(mUpdateTimeTask);
    	curState = TimerState.PAUSED;
    }
    
    private void resumeClock() {
    	pauseTimeEnd = new Date();
    	elapsedPauseTime += pauseTimeEnd.getTime() - pauseTimeStart.getTime();
    	startPauseButton.setText(R.string.pause);
        mHandler.postDelayed(mUpdateTimeTask, 100);
        stopButton.setEnabled(true);
        curState = TimerState.RUNNING;
    }
    
    private void resetClock() {
    	startTime = null;
		endTime = null;
		pauseTimeStart = null;
		pauseTimeEnd = null;
		
		startPauseButton.setText(R.string.start);
		digitalClock.setText(R.string.empty_time);
		elapsedPauseTime = 0;
		
		curState = TimerState.NOT_STARTED;
    }
    
    private void stopClock() {
		endTime = new Date();
		
		
		// If we are paused and we stop without resuming first, we never update
		// the amount of time paused.
		if (curState == TimerState.PAUSED) {
			elapsedPauseTime += System.currentTimeMillis() - pauseTimeStart.getTime();
		}
		
		int ms = (int) (endTime.getTime() - startTime.getTime() - elapsedPauseTime);
		String pausedTime = minutesSecondsFormat(elapsedPauseTime);
		String netTime = minutesSecondsFormat(ms);

		
		Log.i(TAG, "Trip started at " + startTime + " ended at " + endTime + " for a net total of " + netTime + " accounting for a total of " + pausedTime + " paused.");
		startPauseButton.setText(R.string.reset);

    	mHandler.removeCallbacks(mUpdateTimeTask);
    	stopButton.setEnabled(false);
    	curState = TimerState.STOPPED;
    }
    
    private String minutesSecondsFormat(long ms) {
    	int seconds = (int) (ms / 1000);
       int minutes = seconds / 60;
       seconds     = seconds % 60;
       
       if (seconds < 10) {
    	   return "" + minutes + ":0" + seconds;
       } else {
    	   return "" + minutes + ":" + seconds;            
       }
    }
    
    
    public void stop(View view) {
    	stopClock();
    }
    
    public void toggleTime(View view) {
    	
    }
    public void togglePause(View view) {
    	
    }
}