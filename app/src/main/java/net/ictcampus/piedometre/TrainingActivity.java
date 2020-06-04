package net.ictcampus.piedometre;

import androidx.appcompat.app.AppCompatActivity;

import net.ictcampus.piedometre.util.listener.OnSwipeTouchListener;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
 * @since 2020-05-28
 */
public class TrainingActivity extends AppCompatActivity {
    // ---- CONSTANTS ----
    private static final String CURRENT_TRAINING = "currentTraining";
    private static final String TRAININGS = "trainings";
    private static final String STARTTIME = "startTime";
    private static final String MEASUREDTIME = "measuredTime";
    private static final String TIMERSTARTED = "timerStarted";

    private static final String CURRENT_DATE = "currentDate";
    private static final String TYPE = "trainingsType";
    private static final String SPEED = "trainingsSpeed";
    private static final String DURATION = "duration";
    private static final String STEP_COUNT = "trainingsStepCount";


    // ---- VIEWS ----
    private ProgressBar progressBarTrainingType;
    private TextView trainingToggle;
    private TextView trainingSpeed;
    private TextView trainingTimer;
    private Button startButton;
    private Button endButton;

    // -- TYPE --
    private List<Integer> trainingTypeList = Arrays.asList(R.string.training_type_running, R.string.training_type_cycling, R.string.training_type_hiking);
    private int currentIndex;
    private int currentType;
    private int options = trainingTypeList.size();

    // -- SPEED --
    private double stepLength = 75; // in cm
    private int startStepCount = 0;
    private int endStepCount = 1000;
    private int speed; // in km/h?? in m/s??

    // -- TIMER --
    private Handler handler;
    long startTime;
    long currentTime;
    long measuredTime = 0L;
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
        putTrainingIntoPrefs();
        putTrainingIntoPrefs();



        // find views
        trainingToggle = findViewById(R.id.textViewTrainingTypeToggle);
        progressBarTrainingType = findViewById(R.id.progressBarTrainingType);
        trainingSpeed = findViewById(R.id.textViewTrainingSpeedNumber);
        trainingTimer = findViewById(R.id.chronometerTrainingTimer);
        startButton = findViewById(R.id.buttonStartTraining);
        endButton = findViewById(R.id.buttonEndTraining);

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
        Toast.makeText(getApplicationContext(), String.format("started : %s", formatTime(measuredTime)), Toast.LENGTH_SHORT).show();
        timerIsRunning = true;
        currentType = trainingTypeList.get(currentIndex);
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
        Toast.makeText(getApplicationContext(), String.format("paused : %s", formatTime(measuredTime)), Toast.LENGTH_SHORT).show();
        timerIsRunning = false;
        startButton.setText(R.string.start_training);
        Log.v("starttime", String.valueOf(startTime));
    }

    /**
     * ends the timer/training
     */
    private void endTraining() {
        handler.removeCallbacks(timeRunnable);
        Log.v(MEASUREDTIME, String.valueOf(measuredTime));
        measuredTime = SystemClock.uptimeMillis() - startTime + measuredTime;
        updateTimer(measuredTime);
        // TODO : Do something with measured time
        Toast.makeText(getApplicationContext(), String.format("ended : %s", formatTime(measuredTime)), Toast.LENGTH_SHORT).show();
        Log.v(MEASUREDTIME, String.valueOf(measuredTime));
        startButton.setText(R.string.start_training);
        timerIsRunning = false;
        measuredTime = 0L;
    }

    private void putTrainingIntoPrefs(){
        SharedPreferences prefs = getSharedPreferences(TRAININGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        String currentDate = dateFormat.format(new Date());

        Set<String> training = new HashSet<>();
        training.add("stepCount"); // stringlike integer
        training.add("type"); // running, cycling or hiking
        training.add("speed"); // stringlike integer
        training.add("duration"); // stringlike timeformat 00:00:00 MM:ss:ms

        editor.putStringSet(currentDate, training);
        editor.apply();
    }

    /**
     * runnable that updates the time on the textView
     */
    public Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            currentTime = SystemClock.uptimeMillis() - startTime + measuredTime;
            updateTimer(currentTime);
            if (timerIsRunning){
                handler.post(this);
            }
        }
    };

    /**
     * puts the time to the trainingtimer
     * @param time in milliseconds
     */
    private void updateTimer(Long time){
        trainingTimer.setText(formatTime(time));
    }

    /**
     * formats a time from milliseconds to a string MM:ss:mm
     * @param milliSecondTime that will be formated
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

        timerIsRunning = prefs.getBoolean(TIMERSTARTED, false);
        startTime = prefs.getLong(STARTTIME, 0L);
        measuredTime = prefs.getLong(MEASUREDTIME, 0L);
        // set the timer
        if (timerIsRunning){
            handler.post(timeRunnable);
        } else {
            trainingTimer.setText(formatTime(startTime));
        }
    }

    /**
     * OnStop method saves the important variables to the shared Preferences
     */
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences(CURRENT_TRAINING, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(STARTTIME, startTime);
        editor.putLong(MEASUREDTIME, measuredTime);
        editor.putBoolean("timerStarted", timerIsRunning);

        editor.apply();
    }
}
