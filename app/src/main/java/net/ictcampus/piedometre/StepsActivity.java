package net.ictcampus.piedometre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.logging.Logger;

public class StepsActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mStepSensor;
    private TextView mTextView;
    private SensorEventListener mSensorEventListener;
    private boolean sensoring = false;

    // Constant Value: 19 (0x00000013)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        mTextView = findViewById(R.id.textViewStepsActivity);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        List<Sensor> liste = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor s : liste) {
            Log.v( "Sensors", String.valueOf(s));
        }

    }

    protected void onResume() {
        super.onResume();
        sensoring = true;
        if (mStepSensor != null && mStepSensor.isWakeUpSensor()) {
            mSensorManager.registerListener(mSensorEventListener, mStepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("SensorReturn", String.valueOf(mSensorManager.registerListener(mSensorEventListener, mStepSensor, SensorManager.SENSOR_DELAY_NORMAL)));
            Log.v("hello", "Hello from the other side");

        } else {
            Toast.makeText(this, "Sensor not found or not waken up", Toast.LENGTH_SHORT).show();
            System.out.println("Sensor not found");
        }
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        sensoring = false;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        System.out.println(event.values[0]);
        Log.v("hello", String.valueOf(event.values[0]));
        mTextView.setText(String.valueOf(event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
