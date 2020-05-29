package net.ictcampus.piedometre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor myStepSensor;
    TextView textViewSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchListenerDarkLightMode();

        textViewSteps = findViewById(R.id.textViewCount);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;
        myStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        if (myStepSensor != null) {
            mSensorManager.registerListener(this, myStepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    /**
     * Add Listener to the SwitchButton to change Dark/Light Mode
     */
    private void switchListenerDarkLightMode() {
        Switch switchDark = findViewById(R.id.switchDark);
        switchDark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }


    public void onTestClick(View view) {
        System.out.println("clikc");
    }

    /**
     * Start of Trainig Activity
     *
     * @param view of the TrainingButton
     */
    public void startTrainingActivity(View view) {
        Intent training = new Intent(getApplicationContext(), TrainingActivity.class);
        startActivity(training);
    }

    /**
     * Start of Stats Activity
     *
     * @param view of the StatsButton
     */
    public void startStatsActivity(View view) {
        Intent stats = new Intent(getApplicationContext(), StatsActivity.class);
        Toast.makeText(getApplicationContext(), "stats", Toast.LENGTH_SHORT).show();
        startActivity(stats);
    }

    /**
     * Start the ProfileActivity
     *
     * @param view of the ProfileButton
     */
    public void startProfileActivity(View view) {
        Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
        Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
        startActivity(profile);

    }

    /**
     * method updates the textViewSteps in the Main Activity by accessing the SensorEventListener event
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        //System.out.println(event.values[0]);
        Log.v("hello", String.valueOf((int) event.values[0]));
        textViewSteps.setText(String.valueOf((int) event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    @Override
    protected void onPause() {
        super.onPause();
        // take off the Listener of the Sensor (while Sensor is counting by itself in the BG, powersaving reasons)
        mSensorManager.unregisterListener(this, myStepSensor);
    }

    @Override
    public void onResume() {
        super.onResume();
        // take over the Sensor and checkin a Listener
    }

}