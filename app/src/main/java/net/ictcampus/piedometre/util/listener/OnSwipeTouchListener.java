package net.ictcampus.piedometre.util.listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * <h3> On Swipe Touch Listener </h3>
 * represents a Listener for detecting swiping touches
 *
 * @author doriera & luetolfre
 * @version 1.0
 * @since 2020-05-28
 */
public class OnSwipeTouchListener implements OnTouchListener {

    /**
     * for detection of gestures
     */
    private final GestureDetector gestureDetector;

    /**
     * Initializes a new SwipeTouch Listener
     * @param context where the listener is set in
     */
    public OnSwipeTouchListener (Context context){
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    /**
     * Listens for touches on the specified view
     * @param view that is being touched
     * @param event of a motion
     * @return boolean it it was just a touch
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    /**
     * <h5> Gesture Listener </h5>
     * represents a Listener for detecting simple gestures
     *
     * @author doriera & luetolfre
     * @version 1.0
     * @since 2020-05-28
     */
    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    protected void onSwipeRight() {
    }

    protected void onSwipeLeft() {
    }
}
