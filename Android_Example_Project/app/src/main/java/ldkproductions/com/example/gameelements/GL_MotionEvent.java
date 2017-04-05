package ldkproductions.com.example.gameelements;

import java.util.Calendar;

/**
 * Created by Leo on 05.12.2015.
 */
public class GL_MotionEvent {

    public static final int ACTION_DOWN = 0;
    public static final int ACTION_UP = 1;
    public static final int ACTION_MOVE = 2;

    private Object source;
    private int action;
    private long eventTime;

    private static long touchDownTime;

    private int x;
    private int y;

    public GL_MotionEvent(Object source, int x, int y, int action) {
        init(source, x, y, action);
    }

    private void init(Object source, int x, int y, int action) {
        this.source = source;
        this.x = x;
        this.y = y;
        this.action = action;
        this.eventTime = Calendar.getInstance().getTimeInMillis();
    }

    /**
     * Returns the time (in ms) when the user originally pressed down to start a stream of position events.
     */
    public static long getTouchDownTime() {
        return touchDownTime;
    }

    /**
     * Retrieve the time this event occurred, in the uptimeMillis() time base.
     */
    public long getEventTime() {
        return eventTime;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAction() {
        return action;
    }

    public Object getSource() {
        return source;
    }

    public static void setTouchDownTimeNow() {
        GL_MotionEvent.touchDownTime = Calendar.getInstance().getTimeInMillis();
    }

}
