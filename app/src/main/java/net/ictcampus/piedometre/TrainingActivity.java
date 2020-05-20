package net.ictcampus.piedometre;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.ictcampus.piedometre.listeners.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrainingActivity extends AppCompatActivity {

    private ProgressBar progressBartrainingType;
    private TextView trainingToggle;
    private List<Integer> trainingTypeList = Arrays.asList(R.string.training_type_running, R.string.training_type_cycling, R.string.training_type_hiking);
    private int currentIndex;
    private int options = trainingTypeList.size();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        trainingToggle = findViewById(R.id.textViewTrainingTypeToggle);
        progressBartrainingType = findViewById(R.id.progressBarTrainingType);
        progressBartrainingType.setOnTouchListener(new OnSwipeTouchListener(TrainingActivity.this) {
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
    }
}
