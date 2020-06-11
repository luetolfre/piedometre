package net.ictcampus.piedometre;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * in this class, the formatTime() method gets tested from the class TrainingActivity
 * it uses a Long value which is then formatted and parsed to a String
 * in this case, one hour
 */
public class TrainingActivityUnitTest {

    @Test
    public void formatTimeTest() {
        TrainingActivity testTraining = new TrainingActivity();
        assertEquals("60:00:00", testTraining.formatTime(3600000L));
    }

}