package nl.frankkie.flashlightalarm;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {

    Context thisAct;
    Flash flash = new Flash();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisAct = this;
        setContentView(R.layout.main);

        Button btn = (Button) findViewById(R.id.btn_set_time);
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(thisAct);
                int h = prefs.getInt("h", 7); //some default values
                int m = prefs.getInt("m", 30);
                TimePickerDialog dialog = new TimePickerDialog(thisAct, callback, h, m, true);
                dialog.show();
            }
        });

        findViewById(R.id.btn_flash_on).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                flash.on();
            }
        });
        findViewById(R.id.btn_flash_off).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                flash.off();
            }
        });
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
    }

    TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(thisAct);
            prefs.edit().putInt("h", hourOfDay).putInt("m", minute).commit();
            Util.setAlarm(thisAct);
            Toast.makeText(thisAct, "Saved.", Toast.LENGTH_LONG).show();
        }
    };
}
