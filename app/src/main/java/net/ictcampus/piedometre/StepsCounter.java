package net.ictcampus.piedometre;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Class responsible for initializing the STEP_COUNTER
 *
 * @author luetolfre
 * @version 1.0
 * @since 2020-06-11
 */
public class StepsCounter implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int currentSteps;

    public StepsCounter(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        this.sensorManager.registerListener(this, this.stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        this.currentSteps = (int) event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public int getCurrentSteps() {
        return currentSteps;
    }



}
