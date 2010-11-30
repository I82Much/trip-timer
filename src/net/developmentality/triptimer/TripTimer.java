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
	private Date appStartTime;
	private Date startTime;
	private Date endTime;
	
	private long startUptimeMs;
	
	
	private Button startStopButton;
	private TextView digitalClock;
	private Handler mHandler = new Handler();
	
	private static final String TAG = "TripTimer";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appStartTime = new Date();
        setContentView(R.layout.main);
        startStopButton = (Button) findViewById(R.id.startStop);
        digitalClock = (TextView) findViewById(R.id.digitalClock);
    }
    
    // modified from http://developer.android.com/resources/articles/timed-ui-updates.html
    private Runnable mUpdateTimeTask = new Runnable() {
 	   public void run() {
 		   final long start = startUptimeMs;//startTime.getTime();
 	       long millis =SystemClock.uptimeMillis() - start;
 	       int seconds = (int) (millis / 1000);
 	       int minutes = seconds / 60;
 	       seconds     = seconds % 60;
 	       
 	       if (seconds < 10) {
 	           digitalClock.setText("" + minutes + ":0" + seconds);
 	       } else {
 	    	  digitalClock.setText("" + minutes + ":" + seconds);            
 	       }
 	     
 	       mHandler.postAtTime(this,
 	               start + (((minutes * 60) + seconds + 1) * 1000));
 	   }
 	};
    
    // modified from http://developer.android.com/resources/articles/timed-ui-updates.html
    private void startClock() {
    	startTime = new Date();
		startUptimeMs = SystemClock.uptimeMillis();
		startStopButton.setText(R.string.stop);
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }
    
    private void stopClock() {
		endTime = new Date();
		int ms = (int) (endTime.getTime() - startTime.getTime());
		int minutes = ms/(1000 * 60);
		int seconds = ms/(1000);
		
		Log.i(TAG, "Trip started at " + startTime + " ended at " + endTime + " for a total of " + minutes + 
				" minutes " + seconds + " seconds");
		startStopButton.setText(R.string.reset);

    	mHandler.removeCallbacks(mUpdateTimeTask);
    }
    
    public void toggleTime(View view) {
    	// Haven't started timing yet
    	if (startTime == null) {
    		startClock();
    	}
    	else if (endTime == null) {
    		stopClock();
    	}
    	// Reset button was pressed.
    	else {
    		startTime = null;
    		endTime = null;
    		startStopButton.setText(R.string.start);
    	}
    }
}