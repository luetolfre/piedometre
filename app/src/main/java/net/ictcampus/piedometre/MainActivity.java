package net.ictcampus.piedometre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchListenerDarkLightMode();

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
     * @param view of the TrainingButton
     */
    public void startTrainingActivity(View view) {
        Intent training = new Intent(getApplicationContext(), TrainingActivity.class);
        Toast.makeText(getApplicationContext(), "training", Toast.LENGTH_SHORT).show();
        startActivity(training);
    }

    /**
     * Start of Stats Activity
     * @param view of the StatsButton
     */
    public void startStatsActivity(View view) {
        Intent stats = new Intent(getApplicationContext(), StatsActivity.class);
        Toast.makeText(getApplicationContext(), "stats", Toast.LENGTH_SHORT).show();
        startActivity(stats);
    }

    /**
     * Start the StepsActivity
     * @param view of the StepsButton
     */
    public void startStepsActivity(View view) {
        Intent stats = new Intent(getApplicationContext(), StepsActivity.class);
        Toast.makeText(getApplicationContext(), "steps", Toast.LENGTH_SHORT).show();
        startActivity(stats);
    }
}