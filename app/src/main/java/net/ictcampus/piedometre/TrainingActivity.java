package net.ictcampus.piedometre;

import androidx.appcompat.app.AppCompatActivity;

import net.ictcampus.piedometre.util.listener.OnSwipeTouchListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * <h3> Training Activity </h3>
 * represents the Activity where you can start the training
 *
 * @author luetolfre
 * @version 1.0
 * @since 2020-06-11
 */
public class TrainingActivity extends AppCompatActivity {
    // ---- CONSTANTS ----
    private static final String CURRENT_TRAINING = "currentTraining";
    private static final String TRAININGS = "trainings";
    private static final String START_TIME = "startTime";
    private static final String MEASURED_TIME = "measuredTime";
    private static final String TIMER_STARTED = "timerStarted";
    private static final String CURRENT_TYPE = "currentType";

    // -- VIEWS
    private TextView trainingToggle;
    private TextView trainingSpeed;
    private TextView trainingTimer;
    private Button startButton;

    // -- TYPE --
    private final List<Integer> trainingTypeList = Arrays.asList(R.string.training_type_running, R.string.training_type_cycling, R.string.training_type_hiking);
    private int currentIndex;
    private String currentType = "running";
    private final int options = trainingTypeList.size();

    private StepCounter stepCounter;
    private double stepLength; // in m
    private int startStepCount = 0;
    private int measuredStepCount;
    private double averageSpeed = 5.23;

    // -- TIMER --
    private Handler handler;
    private long startTime;
    private long measuredTime = 0L;
    private boolean timerIsRunning = false;

    /**
     * finds the views, assigns new instances to variables and sets listeners
     * @param savedInstanceState bundle
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        // -- SPEED --
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        this.stepCounter = new StepCounter(this, sensorManager);

        // find views
        trainingToggle = findViewById(R.id.textViewTrainingTypeToggle);
        // ---- VIEWS ----
        ProgressBar progressBarTrainingType = findViewById(R.id.progressBarTrainingType);
        trainingSpeed = findViewById(R.id.textViewTrainingSpeedNumber);
        trainingTimer = findViewById(R.id.chronometerTrainingTimer);
        startButton = findViewById(R.id.buttonStartTraining);
        Button endButton = findViewById(R.id.buttonEndTraining);

        // create new instances
        handler = new Handler();
        measuredTime = 0L;
        updateTimer(measuredTime);

        // set listeners
        progressBarTrainingType.setOnTouchListener(new OnSwipeTouchListener(TrainingActivity.this) {
            public void onSwipeRight() {
                currentIndex ++;
                int index = Math.floorMod(currentIndex, options);
                trainingToggle.setText(trainingTypeList.get(index));
            }
            public void onSwipeLeft() {
                currentIndex--;
                int index = Math.floorMod(currentIndex, options);
                trainingToggle.setText(trainingTypeList.get(index));
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerIsRunning){
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTraining();
            }
        });
    }

    /**
     * starts the timer
     */
    private void startTimer() {
        startTime = SystemClock.uptimeMillis();
        startStepCount = stepCounter.getCurrentSteps();
        currentType = String.valueOf(trainingToggle.getText());
        timerIsRunning = true;
        startButton.setText(R.string.pause_training);
        // start runnable
        handler.post(timeRunnable);
    }

    /**
     * pauses the timer
     */
    private void pauseTimer() {
        handler.removeCallbacks(timeRunnable);
        measuredTime = SystemClock.uptimeMillis() - startTime + measuredTime;
        measuredStepCount = stepCounter.getCurrentSteps() - startStepCount + measuredStepCount;
        timerIsRunning = false;
        startButton.setText(R.string.start_training);
    }

    /**
     * ends the timer/training
     */
    private void endTraining() {
        handler.removeCallbacks(timeRunnable);
        measuredTime = SystemClock.uptimeMillis() - startTime + measuredTime;
        measuredStepCount = stepCounter.getCurrentSteps() - startStepCount + measuredStepCount;
        averageSpeed = measuredStepCount*stepLength/measuredTime*36;
        updateTimer(measuredTime);
        updateSpeed(averageSpeed);
        putTrainingIntoPrefs(currentType, measuredStepCount, averageSpeed, measuredTime);
        Toast.makeText(getApplicationContext(), String.format("ended : %s", formatTime(measuredTime)), Toast.LENGTH_SHORT).show();
        startButton.setText(R.string.start_training);
        timerIsRunning = false;
        measuredTime = 0L;
    }

    /**
     * Adds the training into a String Set of the name current date
     * @param type String of the trainings type
     * @param stepCount integer of the steps taken
     * @param speed double of average speed in km/h
     * @param duration long trainings duration in ms
     */
    private void putTrainingIntoPrefs(String type, int stepCount, double speed, long duration){
        SharedPreferences prefs = getSharedPreferences(TRAININGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        String currentDate = dateFormat.format(new Date());

        Set<String> training = new HashSet<>();
        training.add("steps:" + stepCount); // integer as a string
        training.add("type:"+ type); // running, cycling or hiking
        training.add("speed:"+ speed); // integer as a string (km/h)
        training.add("time:"+ duration); // long as a string (ms)

        editor.putStringSet(currentDate, training);
        editor.apply();
    }

    /**
     * runnable that updates the time on the textView
     */
    private final Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = SystemClock.uptimeMillis() - startTime + measuredTime;
            // in km/h
            double currentSpeed = (stepLength) * (stepCounter.getCurrentSteps() - startStepCount + measuredStepCount) / currentTime;
            updateTimer(currentTime);
            updateSpeed(currentSpeed);
            if (timerIsRunning){
                handler.post(this);
            }
        }
    };

    /**
     * puts the time to the training timer text view
     * @param time in milliseconds
     */
    private void updateTimer(Long time){
        trainingTimer.setText(formatTime(time));
    }

    /**
     * puts the speed to the trainings speed text view
     * @param speed double in cm/ms
     */
    private void updateSpeed(double speed){
        trainingSpeed.setText(String.format(Locale.getDefault(),"%.3f",speed*36));
    }

    /**
     * formats a time from milliseconds to a string MM:ss:mm
     * @param milliSecondTime that will be formatted
     * @return String in the format MM:ss:mm
     */
    public String formatTime(Long milliSecondTime){
        int seconds = (int) (milliSecondTime/1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int milliseconds = (int)(milliSecondTime % 100);
        return String.format(Locale.getDefault(),"%02d:%02d:%02d", minutes, seconds, milliseconds);
    }


    /**
     * OnStart method gets the saved variables from the sharedPreferences
     * & lets the clock run at the saved time.
     */
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences(CURRENT_TRAINING, MODE_PRIVATE);
        timerIsRunning = prefs.getBoolean(TIMER_STARTED, false);
        startTime = prefs.getLong(START_TIME, 0L);
        measuredTime = prefs.getLong(MEASURED_TIME, 0L);
        currentType = prefs.getString("currentType", "running");
        // set the timer
        if (timerIsRunning){
            handler.post(timeRunnable);
        } else {
            trainingTimer.setText(formatTime(startTime));
        }
        SharedPreferences profilePrefs = getSharedPreferences("profile", MODE_PRIVATE);
        stepLength = Integer.parseInt(profilePrefs.getString("stepLength", "80"));
    }

    /**
     * OnStop method saves the important variables to the shared Preferences
     */
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences(CURRENT_TRAINING, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(START_TIME, startTime);
        editor.putLong(MEASURED_TIME, measuredTime);
        editor.putBoolean(TIMER_STARTED, timerIsRunning);
        editor.putString(CURRENT_TYPE, currentType);

        editor.apply();
    }
}
