package nl.frankkie.flashlightalarmclock;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 *
 * @author FrankkieNL
 */
public class AlarmActivity extends Activity {

    Context thisAct;
    Flash flash = new Flash();
    Handler handler = new Handler();
    boolean isOn = false;
    boolean go = true; //should do flashing, kill-switch
    public static long FLASHING_DELAY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        turnScreenOn();

        initUI();
        startFlashing();
    }

    public void turnScreenOn() {
        //Turn screen on
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire(1000);

//        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
//        KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
//        keyguardLock.disableKeyguard();

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        );

    }

    public void initUI() {
        setContentView(R.layout.alarm);
        findViewById(R.id.alarm_stop_flashing).setKeepScreenOn(true);
        findViewById(R.id.alarm_stop_flashing).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                flash.off();
                go = false;
                findViewById(R.id.alarm_stop_flashing).setKeepScreenOn(false);
                finish(); //kill activity
            }
        });

    }

    public void startFlashing() {
        handler.postDelayed(new Runnable() {

            public void run() {
                if (!go) {
                    flash.off();
                    findViewById(R.id.alarm_stop_flashing).setKeepScreenOn(false);
                    return;
                }
                if (isOn) {
                    flash.off();
                    isOn = false;
                } else {
                    flash.on();
                    isOn = true;
                }
                startFlashing(); //next flash
            }
        }, FLASHING_DELAY);
    }

    @Override
    protected void onResume() {
        super.onResume(); //To change body of generated methods, choose Tools | Templates.
        flash.open();
    }

    @Override
    protected void onPause() {
        super.onPause(); //To change body of generated methods, choose Tools | Templates.
        flash.close();
        findViewById(R.id.alarm_stop_flashing).setKeepScreenOn(false);
    }

}
