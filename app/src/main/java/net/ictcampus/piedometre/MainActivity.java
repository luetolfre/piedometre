package net.ictcampus.piedometre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor myStepSensor;
    TextView textViewSteps;

    /**
     * Variables to the Shared Preferences
     */
    public static final String SHARED_PREFS = "profile";
    public static final String NAME = "name";
    public static final String WEIGHT = "weight";
    public static final String STEPLENGTH = "steplength";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        switchListenerDarkLightMode();

        // first off, check if its a fresh install of the App
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //shows the information dialog
            openEditDialog();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();


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
     * method updates the textViewSteps and the progressBar in the Main Activity by accessing the SensorEventListener event
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        //System.out.println(event.values[0]);
        Log.v("sensor", String.valueOf((int) event.values[0]));
        textViewSteps.setText(String.valueOf((int) event.values[0]));
        updateProgressBar((int) event.values[0]);
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


    public void updateProgressBar(int progress) {

    }

    /**
     * due to the onClick on the floating action button, the AlertDialog gets called to display the text input
     */
    public void openEditDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // input fields
        final EditText inputName = new EditText(this);
        final EditText inputWeight = new EditText(this);
        final EditText inputSteplength = new EditText(this);


        // Set up the buttons (and a title), on Cancel dismiss()
        builder.setTitle(R.string.FirstEditDialogTitle).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // puts the SharedPreferences profile onto the editor
                SharedPreferences profile = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = profile.edit();

                // read from the input and check on valid information
                if (inputName.getText().toString().length() < 2 || inputWeight.getText().toString().length() < 2 || inputSteplength.getText().toString().length() < 2) {
                    // builder.setCancelable(false);
                    Toast.makeText(MainActivity.this, "Enter valid information", Toast.LENGTH_LONG).show();
                    openEditDialog();
                } else {
                    // update the current information in the SharedPreferences profile
                    editor.putString(NAME, inputName.getText().toString());
                    editor.putString(WEIGHT, inputWeight.getText().toString());
                    editor.putString(STEPLENGTH, inputSteplength.getText().toString());
                    editor.apply();

                    // with updated activity_profile, the dialog can get closed
                    dialog.cancel();
                }
            }
        });

        // instance to access SharedPreferences
        SharedPreferences profile = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // linear layout to set the inputs
        final LinearLayout inputs = new LinearLayout(this);
        inputs.setOrientation(LinearLayout.VERTICAL);

        // Set up the text input field for the TextInput to change profile information
        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        inputName.setHint(profile.getString(NAME, "Your Name"));
        inputs.addView(inputName);

        inputWeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputWeight.setHint(profile.getString(WEIGHT, "Weight in kg"));
        inputs.addView(inputWeight);

        inputSteplength.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputSteplength.setHint(profile.getString(STEPLENGTH, "Steplength in cm"));
        inputs.addView(inputSteplength);

        builder.setView(inputs);
        AlertDialog alertDialog = builder.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }
}