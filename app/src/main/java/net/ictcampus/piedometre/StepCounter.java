package net.ictcampus.piedometre;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

/**
 * <h3> Steps Counter </h3>
 * Class responsible for initializing the STEP_COUNTER
 *
 * @author luetolfre
 * @version 1.0
 * @since 2020-06-11
 */
public class StepCounter implements SensorEventListener {

    Context context;
    SensorManager sensorManager;
    Sensor stepSensor;
    /**
     * Integer of the current steps on the Sensor
     */
    private int currentSteps;

    /**
     * Initioalizes a Step Counter
     * @param context where it is started
     * @param sensorManager that should open the sensor
     */
    public StepCounter(Context context, SensorManager sensorManager) {
        this.context = context;
        if (sensorManager != null){
            this.sensorManager = sensorManager;
            this.stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (stepSensor != null) {
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            } else {
                Toast.makeText(context, "You don't have a Step Counter Sensor!",  Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(context, "You don't have a Sensor Manager!",  Toast.LENGTH_LONG).show();
        }
        // Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        // sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * When the sensor changes the current Steps taken will be saved
     * @param event when sensor is changed
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        this.currentSteps = (int) event.values[0];
    }

    /**
     * When the accuracy of the sensor changes nothing happens
     * @param sensor that changes
     * @param accuracy int
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public int getCurrentSteps() {
        return currentSteps;
    }



}
