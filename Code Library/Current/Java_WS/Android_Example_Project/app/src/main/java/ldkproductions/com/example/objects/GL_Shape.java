package ldkproductions.com.example.objects;

import ldkproductions.com.example.data.CGPoint;
import ldkproductions.com.example.data.VertexArray;
import ldkproductions.com.example.gameelements.GL_ActionEvent;
import ldkproductions.com.example.gameelements.GL_ActionListener;
import ldkproductions.com.example.gameelements.GL_MotionEvent;
import ldkproductions.com.example.gameelements.GL_TouchListener;

import java.util.ArrayList;

/**
 * Created by Leo K., LDK-Productions on 05.12.2015.
 * The top class of all drawable elements.
 */
abstract public class GL_Shape {


    protected CGPoint pos;              //the position of the GL_Shape (usually the top left corner)
    protected boolean visible = true;   //states if the GL_Shape will be drawn

    protected VertexArray vertexArray;  //the vertex array to be passed to the vertex shader

    //for touch and click events
    private ArrayList<GL_ActionListener> actionListeners = new ArrayList<GL_ActionListener>();
    private ArrayList<GL_TouchListener> touchListeners = new ArrayList<GL_TouchListener>();

    //Click states
    protected final static int CLICK_STATE_CANCELLED = 0;
    protected final static int CLICK_STATE_NOT = 1;
    protected final static int CLICK_STATE_DOWN = 2;
    protected final static int CLICK_STATE_YES = 3;
    protected int clickState = CLICK_STATE_NOT;

    /**
     * Copy constructor
     * @param shape the shape to be copied
     */
    protected GL_Shape(GL_Shape shape, boolean copyListeners) {
        this.pos = new CGPoint(shape.getPosX(), shape.getPosY());
        this.visible = shape.visible;
        this.vertexArray = shape.vertexArray;
        if (copyListeners) {
            this.actionListeners.addAll(shape.actionListeners);
            this.touchListeners.addAll(shape.touchListeners);
        }
    }

    protected GL_Shape(CGPoint pos) {
        this.pos = pos;
    }

    protected GL_Shape(int x, int y) {
        this.pos = new CGPoint(x, y);
    }

    public void setPosX(int x) {
        this.pos.x = x;
    }
    public void setPosY(int y) {
        this.pos.y = y;
    }

    public void setPosition(int x, int y) {
        setPosX(x);
        setPosY(y);
    }

    public int getPosX() {
        return pos.getX();
    }
    public int getPosY() {
        return pos.getY();
    }

    public CGPoint getPos() {
        return this.pos;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    abstract protected void generateVertexArray();
    abstract public void draw(float[] projectionMatrix);

    abstract public boolean collidesWithPoint(int x, int y);
    abstract public boolean collidesWithRectangle(int x, int y, int w, int h);

    abstract public CGPoint getPosTopLeft();
    abstract public CGPoint getPosBottomRight();


    // --------------------- Touch and click listener functions --------------------- //

    public boolean checkTouchDown(int x, int y) {
        GL_MotionEvent.setTouchDownTimeNow();
        if (collidesWithPoint(x, y)) {
            fireMotionEvent(x, y, GL_MotionEvent.ACTION_DOWN);
            clickState = CLICK_STATE_DOWN;
            return true;
        }
        clickState = CLICK_STATE_NOT;
        return false;
    }

    public boolean checkTouchUp(int x, int y) {
        if (clickState == CLICK_STATE_CANCELLED)
            return false;
        if (collidesWithPoint(x, y)) {
            fireMotionEvent(x, y, GL_MotionEvent.ACTION_UP);
            if(clickState == CLICK_STATE_DOWN) {
                clickState = CLICK_STATE_YES;
                fireActionPerformed();
            } else
                clickState = CLICK_STATE_NOT;
            return  true;
        }
        clickState = CLICK_STATE_NOT;
        return false;
    }

    public void cancelTouch() {
        clickState = CLICK_STATE_CANCELLED;
    }

    public void resetTouch() {
        clickState = CLICK_STATE_NOT;
    }

    public boolean checkTouchMove(int x, int y) {
        if (clickState != CLICK_STATE_CANCELLED &&
                collidesWithPoint(x, y)) {
            fireMotionEvent(x, y, GL_MotionEvent.ACTION_MOVE);
            return true;
        }
        return false;
    }

    protected void fireMotionEvent(int x, int y, int action) {
        GL_MotionEvent e = new GL_MotionEvent(this, x, y, action);
        for (GL_TouchListener touchListener : touchListeners) {
            touchListener.onTouchEvent(e);
        }
    }

    protected void fireActionPerformed() {
        for (GL_ActionListener actionListener : actionListeners) {
            actionListener.actionPerformed(new GL_ActionEvent(this));
        }
    }
    public void addActionListener(GL_ActionListener actionListener) {
        if(! actionListeners.contains(actionListener))
            actionListeners.add(actionListener);
    }

    public void removeActionListener(GL_ActionListener actionListener) {
        actionListeners.remove(actionListener);
    }

    public void removeAllActionListeners() {
        actionListeners.clear();
    }

    public void addTouchListener(GL_TouchListener touchListener) {
        if(! touchListeners.contains(touchListener))
            touchListeners.add(touchListener);
    }

    public void removeTouchListener(GL_TouchListener touchListener) {
        touchListeners.remove(touchListener);
    }

    public void removeAllTouchListeners() {
        touchListeners.clear();
    }




    // --------------------- General static helper methods --------------------- //


    /**
     * Tells you which side of the halfplane (formed by the line between p2 and p3) p1 is.
     * @param p1 The point to be checked.
     * @param p2 A point of the triangle.
     * @param p3 A point of the triangle.
     * @return The side on which the point is located.
     */
    private static float sign(CGPoint p1, CGPoint p2, CGPoint p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    /**
     * Checks whether a point is inside a triangle or not.
     * @param pt The point to be checked.
     * @param v1 A Point of the triangle.
     * @param v2 A Point of the triangle.
     * @param v3 A Point of the triangle.
     * @return True if the point is inside, else false.
     */
    public static boolean isPointInTriangle (CGPoint pt, CGPoint v1, CGPoint v2, CGPoint v3) {
        //Logarithm based on answer of Kornel Kisielewicz
        // on http://stackoverflow.com/questions/2049582/how-to-determine-a-point-in-a-2d-triangle
        boolean b1, b2, b3;

        b1 = sign(pt, v1, v2) < 0.0f;
        b2 = sign(pt, v2, v3) < 0.0f;
        b3 = sign(pt, v3, v1) < 0.0f;

        return ((b1 == b2) && (b2 == b3));
    }

    public static boolean isPointInAlignedRectangle(int x, int y,  CGPoint pos_rect, int width, int height) {
        return ((x >= pos_rect.getX() && x <= pos_rect.getX()+width) && (y>= pos_rect.getY() && y <= pos_rect.getY()+height));
    }

    /**
     * Checks whether a point is inside a rectangle or not.
     * @param p The point to be checked.
     * @param a One of three points of the rectangle.
     * @param b One of three points of the rectangle.
     * @param d One of three points of the rectangle.
     * @return True if the point is inside, else false.
     */
    public static boolean pointRectangleCollision(CGPoint p, CGPoint a, CGPoint b, CGPoint d) {
        /*
                A.____
                 |    ----.B
                |         |
               |   .M    |

              |         |
             D.____    |
                   ----.C

        MM of coordinates (x,y) is inside the rectangle iff
        (0<AM⋅AB<AB⋅AB)∧(0<AM⋅AD<AD⋅AD)
        (scalar product of vectors)
        */

        CGPoint AM = new CGPoint(p.x - a.x, p.y - a.y);
        CGPoint AB = new CGPoint(b.x - a.x, b.y - a.y);
        CGPoint AD = new CGPoint(d.x - a.x, d.y - a.y);

        float AMxAB = AM.x*AB.x + AM.y*AB.y;
        float ABxAB = AB.x*AB.x + AB.y*AB.y;
        float AMxAD = AM.x*AD.x + AM.y*AD.y;
        float ADxAD = AD.x*AD.x + AD.y*AD.y;

        if ((0 < AMxAB && AMxAB < ABxAB) && (0 < AMxAD && AMxAD < ADxAD))
            return true;

        return false;
    }

    /**
     * Helper method for lineLineCollision;
     */
    private static boolean between(double a, double b, double c) {
        return a-0.0000001 <= b && b <= c+0.0000001;
    }

    /**
     * Determines if line1(x1, y1, x2, y2) intersects line2(x3, y3, x4, y4).
     * From https://gist.github.com/gordonwoodhull/50eb65d2f048789f9558
     * Which is adapted from http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect/1968345#1968345
     * @return True if the lines intersect, else false.
     */
    public static boolean lineLineCollision(float x1,float y1,float x2,float y2, float x3,float y3,float x4,float y4) {
        double x=((x1*y2-y1*x2)*(x3-x4)-(x1-x2)*(x3*y4-y3*x4)) / (double)
                ((x1-x2)*(y3-y4)-(y1-y2)*(x3-x4));
        double y=((x1*y2-y1*x2)*(y3-y4)-(y1-y2)*(x3*y4-y3*x4)) / (double)
                ((x1-x2)*(y3-y4)-(y1-y2)*(x3-x4));
        if (Double.isNaN(x)||Double.isNaN(y)) {
            return false;
        } else {
            if (x1>=x2) {
                if (!between(x2, x, x1)) {return false;}
            } else {
                if (!between(x1, x, x2)) {return false;}
            }
            if (y1>=y2) {
                if (!between(y2, y, y1)) {return false;}
            } else {
                if (!between(y1, y, y2)) {return false;}
            }
            if (x3>=x4) {
                if (!between(x4, x, x3)) {return false;}
            } else {
                if (!between(x3, x, x4)) {return false;}
            }
            if (y3>=y4) {
                if (!between(y4, y, y3)) {return false;}
            } else {
                if (!between(y3, y, y4)) {return false;}
            }
        }

        //Intersection at x, y
        return true;
    }

    public static boolean alignedRectangleRectangleCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        return (x1 <= x2 + w2 &&
                x1 + w1 > x2 &&
                y1 <= y2 + h2 &&
                y1 + h1 > y2);
    }

    public static boolean isPointInCircle(int px, int py, int x, int y, int r) {
        return squared(px - x) + squared(py - y) < r*r;
    }

    public static boolean circleCircleCollision(int x1, int y1, int r1, int x2, int y2, int r2) {
        return squared(x1 - x2) + squared(y1 - y2) < squared(r1 + r2);
    }

    public static boolean alignedRectangleCircleCollision(int x, int y, int w, int h, int xc, int yc, int r) {
        int circleDistance_x = Math.abs(xc - (x+w/2));
        int circleDistance_y = Math.abs(yc - (y+h/2));

        if (circleDistance_x > (w/2 + r)) { return false; }
        if (circleDistance_y > (h/2 + r)) { return false; }

        if (circleDistance_x <= (w/2)) { return true; }
        if (circleDistance_y <= (h/2)) { return true; }

        int cornerDistance_sq = squared(circleDistance_x - w/2) +
                squared(circleDistance_y - h/2);

        return (cornerDistance_sq <= (r*r));
    }

    private static int squared(int n) {
        return n*n;
    }

}
