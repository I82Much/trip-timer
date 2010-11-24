package net.developmentality.triptimer;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TripTimer extends Activity {
	private Date startTime;
	private Date endTime;
	private Button startStopButton;
	
	private static final String TAG = "TripTimer";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startStopButton = (Button) findViewById(R.id.startStop);
    }
    
    public void toggleTime(View view) {
    	// Haven't started timing yet
    	if (startTime == null) {
    		startTime = new Date();
    		startStopButton.setText(R.string.stop);
    	}
    	else if (endTime == null) {
    		endTime = new Date();
    		int ms = (int) (endTime.getTime() - startTime.getTime());
    		int minutes = ms/(1000 * 60);
    		int seconds = ms/(1000);
    		
    		Log.i(TAG, "Trip started at " + startTime + " ended at " + endTime + " for a total of " + minutes + 
    				" minutes " + seconds + " seconds");
    		startStopButton.setText(R.string.reset);
    	}
    	// Reset button was pressed.
    	else {
    		startTime = null;
    		endTime = null;
    		startStopButton.setText(R.string.start);
    	}
    }
}