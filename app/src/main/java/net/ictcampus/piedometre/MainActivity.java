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
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <h3> Main Activity </h3>
 * Main activity represents the start screen. It shows four circles, one of them is shows the steps.
 * The other 3 Circles are Buttons which lead to other activities
 *
 * extends the AppCompatActivity and therefore overrides the methods of the Android lifecycle
 * implements the SensorEventListener and therefore overrides the Sensor Changed methods
 *
 * @author doriera & luetolfre
 * @version 1.0
 * @since 2020-06-11
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    /**
     * Constant name of the profile shared preferences
     */
    private static final String PROFILE_SHARED_PREFS = "profile";
    /**
     * Constant name for the username entry
     */
    private static final String NAME = "name";
    /**
     * Constant name for the weight entry
     */
    private static final String WEIGHT = "weight";
    /**
     * Constant name for the stepLength entry
     */
    private static final String STEP_LENGTH = "stepLength";
    /**
     * Constant name for the daily steps
     */
    private static final String DAILY_STEPS = "dailySteps";
    /**
     * The Manager which is used to gain access to the sensors
     */
    private SensorManager mSensorManager;
    /**
     * Sensor TYPE_STEP_COUNTER
     * Constant Value: 19 (0x00000013)
     */
    private Sensor myStepSensor;
    /**
     * TextView that is constantly updated with the taken steps
     */
    private TextView textViewSteps;
    /**
     * Circular ProgressBar that is constantly updated with the taken steps
     */
    private ProgressBar progressBarSteps;


    /**
     * The onCreate Method initializes the static class Variables
     * @param savedInstanceState Bundle of saved Instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // inserts a switch to change between dark and light mode
        switchListenerDarkLightMode();

        // find Views that will have to be updated
        textViewSteps = findViewById(R.id.textViewCount);
        progressBarSteps = findViewById(R.id.progressbarSteps);

        updateProgressBar(0);
    }

    /**
     * The onResume Method is called after pausing the App or while starting the App after onCreate & onStart
     * It checks for it being the first run, and opens the window to enter profile information
     * It looks for the Sensor manager, the Sensor and registers a listener if it is available
     */
    @Override
    public void onResume() {
        super.onResume();
        // first off, check if its a fresh install of the App
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){// || profile.getString(WEIGHT, "10").equals("WEIGHT")) {
            //shows the information dialog
            openEditDialog();
        }
        // initialize the Sensor Manager & give Feedback if the app is not usable
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null){
            myStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (myStepSensor != null) {
                mSensorManager.registerListener(this, myStepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have a Step Counter Sensor!",  Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "You don't have a Sensor Manager!",  Toast.LENGTH_LONG).show();
        }

    }

    /**
     * The onPause Method unregisters the listener to save power while the app is not active
     */
    @Override
    protected void onPause() {
        super.onPause();
        // take off the Listener of the Sensor (Sensor is counting by itself in the BG)
        mSensorManager.unregisterListener(this, myStepSensor);
    }

    /**
     * Start of Training Activity
     * @param view of the TrainingButton
     */
    @SuppressWarnings("unused")
    public void startTrainingActivity(View view) {
        Intent training = new Intent(getApplicationContext(), TrainingActivity.class);
        startActivity(training);
    }

    /**
     * Start of Stats Activity
     * @param view of the StatsButton
     */
    @SuppressWarnings("unused")
    public void startStatsActivity(View view) {
        Intent stats = new Intent(getApplicationContext(), StatsActivity.class);
        startActivity(stats);
    }

    /**
     * Start the ProfileActivity
     * @param view of the ProfileButton
     */
    @SuppressWarnings("unused")
    public void startProfileActivity(View view) {
        Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(profile);
    }

    /**
     * method updates the textViewSteps and the progressBar in the Main Activity by accessing the SensorEventListener event
     * @param event that the sensor has changed
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        updateProgressBar((int) event.values[0]);
        textViewSteps.setText(String.valueOf((int) event.values[0]));
    }

    /**
     * Method is called whenever the accuracy of the STEP_COUNTER changes
     * @param sensor STEP_COUNTER_SENSOR
     * @param accuracy int
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
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

    /**
     * Method updates the progressBar in .xml for more visual information on steps taken
     * @param stepProgress int value of steps counted
     */
    private void updateProgressBar(int stepProgress) {
        // access the shared preferences to check the dailySteps goal
        SharedPreferences profile = getSharedPreferences(PROFILE_SHARED_PREFS, Context.MODE_PRIVATE);

        int dailySteps = Integer.parseInt(profile.getString(DAILY_STEPS, "10"));
        // the progress in percents
        int progressInPercent = (stepProgress * 100 / dailySteps);
        if (progressInPercent > 100) {
            progressBarSteps.setProgress(100);
        }
        else progressBarSteps.setProgress(Math.max(progressInPercent, 20));
    }

    /**
     * Initializes an Alert Dialog to input profile information
     * due to the onClick on the floating action button, the AlertDialog gets called to display the text input
     */
    private void openEditDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // input fields
        final EditText inputName = new EditText(this);
        final EditText inputWeight = new EditText(this);
        final EditText inputStepLength = new EditText(this);
        final EditText inputDailySteps = new EditText(this);

        // Set up the buttons (and a title), on Cancel dismiss()
        builder.setTitle(R.string.FirstEditDialogTitle).setMessage(R.string.FirstEditDialogMessage).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // puts the SharedPreferences profile onto the editor
                SharedPreferences profile = getSharedPreferences(PROFILE_SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = profile.edit();

                // read from the input and check on valid information
                if (inputName.getText().toString().length() < 2 ||
                        (inputWeight.getText().toString().length() < 2 ||
                        inputStepLength.getText().toString().length() < 2 ||
                        inputDailySteps.getText().toString().length() < 2 )) {

                    // builder.setCancelable(false);
                    Toast.makeText(MainActivity.this, "Enter valid information", Toast.LENGTH_LONG).show();
                    openEditDialog();

                } else {
                    // update the current information in the SharedPreferences profile
                    editor.putString(NAME, inputName.getText().toString());
                    editor.putString(WEIGHT, inputWeight.getText().toString());
                    editor.putString(STEP_LENGTH, inputStepLength.getText().toString());
                    editor.putString(DAILY_STEPS, inputDailySteps.getText().toString());
                    editor.apply();
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();

                    // with updated activity_profile, the dialog can get closed
                    dialog.cancel();
                }
            }
        });

        // instance to access SharedPreferences
        SharedPreferences profile = getSharedPreferences(PROFILE_SHARED_PREFS, Context.MODE_PRIVATE);

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

        inputStepLength.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputStepLength.setHint(profile.getString(STEP_LENGTH, "Step length in cm"));
        inputs.addView(inputStepLength);

        inputDailySteps.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputDailySteps.setHint(profile.getString(DAILY_STEPS, "Steps a Day Goal"));
        inputs.addView(inputDailySteps);

        builder.setView(inputs);
        AlertDialog alertDialog = builder.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }
}