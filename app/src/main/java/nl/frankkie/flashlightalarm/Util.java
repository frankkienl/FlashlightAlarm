package nl.frankkie.flashlightalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author FrankkieNL
 */
public class Util {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    
    public static void setAlarm(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        
        Calendar cal = new GregorianCalendar();
        //cal.setTimeZone(TimeZone.getTimeZone("GMT")); //UTC
        cal.set(Calendar.HOUR_OF_DAY,prefs.getInt("h", 7));
        cal.set(Calendar.MINUTE,prefs.getInt("m", 30));
        cal.set(Calendar.SECOND,0);
        if (cal.before(new GregorianCalendar())){
            cal.add(Calendar.DAY_OF_YEAR, 1); //add one day, if the time has passed this day.
        }
        logTime(cal);
        long timeLong = cal.getTimeInMillis();
        Intent intent = new Intent(c, AlarmActivity.class);
        PendingIntent pendingAct = PendingIntent.getActivity(c, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Log.e("FlashlightAlarm", "Time: " + sdf.format(timeLong));
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            //setExact is 19+
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeLong, pendingAct);            
        } else {
            //set is 4+
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeLong, pendingAct);
        }
    }
    
    public static void logTime(Calendar cal){
        Log.e("FlashlightAlarm", "Time: " + cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH)+1 /*0=jan*/) + "-" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
    }

}
