package net.ictcampus.piedometre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.EventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ckeckListener();

    }


    public void ckeckListener() {
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

    public ProgressBar getProgressBarSteps() {
        ProgressBar progressBarSteps = findViewById(R.id.progressbarSteps);
        return progressBarSteps;
    }

    public void onClick(View view) {
        System.out.println("clikc");
    }

    /**
     * Start of Trainig Activity
     * @param view
     */

    public void startTrainingActivity(View view) {
        //bla
        Intent training = new Intent(getApplicationContext(), TrainingActivity.class);
        startActivity(training);
    }

    /**
     * Start of Stats Activity
     * @param view
     */
    public void startStatsActivity(View view) {
        //bla
        Intent stats = new Intent(getApplicationContext(), StatsActivity.class);
        startActivity(stats);
    }




    /*
    private void setOnItemListener(ProgressBar progressBar) {
        AdapterView.OnItemClickListener onListClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), .class);
                String selected = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
                intent.putExtra("kanton", selected);
                startActivity(intent);
            }
        };

        progressBar.setOnItemClickListener(onListClickListener);
    }*/
}