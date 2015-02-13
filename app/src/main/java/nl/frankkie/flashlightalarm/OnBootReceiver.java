package nl.frankkie.flashlightalarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *
 * @author frankkie
 */
public class OnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        //Set Alarm
        Util.setAlarm(arg0);
    }

}
