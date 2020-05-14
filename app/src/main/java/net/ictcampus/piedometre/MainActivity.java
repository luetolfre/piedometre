package net.ictcampus.piedometre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.EventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public ProgressBar getProgressBarSteps() {
        ProgressBar progressBarSteps = findViewById(R.id.progressbarSteps);
        return progressBarSteps;
    }

    public void onClick(View view) {
        System.out.println("clikc");
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