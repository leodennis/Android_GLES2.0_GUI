package ldkproductions.com.example.gameelements;

import android.content.res.Resources;
import android.graphics.Color;
import android.opengl.GLES20;
import android.util.DisplayMetrics;
import android.util.Log;

import ldkproductions.com.example.data.CGPoint;
import ldkproductions.com.example.objects.GL_Rectangle;
import ldkproductions.com.example.objects.GL_Shape;
import ldkproductions.com.example.util.MatrixHelper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Leo K., LDK-Productions on 16.12.2015.
 * The GL_ScrollView is a container of GL_Shapes which are placed on a scroll-pane. The scroll-pane can be moved using touch gestures.
 */
public class GL_ScrollView extends GL_Shape {

    //private static String version = "V1.3";

    //Variables needed to distinguish between scroll and click gestures
    public static final int NORMAL_CLICK_DURATION = 200;
    private static float max_click_range = 12.0f;
    private boolean possibleClick = true;
    private boolean possibleScroll = true;

    //Used to convert virtual pixels to device independent pixels
    private float toDpi = 1.0f;

    //The dimensions of the GL_ScrollView
    private int width;
    private int height;

    //The dimensions of the area to be drawn (usually equals width and height)
    private int drawWidth;
    private int drawHeight;

    //The dimensions of the scroll-pane
    private int paneWidth;
    private int paneHeight;

    private int currentOffsetX;
    private int currentOffsetY;

    private int touchDownX = 0;
    private int touchDownY = 0;

    private int tmpMoveX = 0;
    private int tmpMoveY = 0;

    private int startOffsetX = 0;
    private int startOffsetY = 0;

    //If true the dimensions of the scroll-pane will be generated automatically
    private boolean autoPaneDimension = true;

    //Variables needed for the use of GL_SCISSOR_TEST and other functions
    private float deviceScaleX;
    private float deviceScaleY;
    private float devicePixelHeight;
    private float virtualScreenHeight;

    private boolean disableVerticalScroll = false;
    private boolean disableHorizontalScroll = false;

    //Variables to determine which scrollbar is to scroll
    private static int SCROLL_NONE = 0;
    private static int SCROLL_HORIZONTAL = 1;
    private static int SCROLL_VERTICAL = 2;
    private static int SCROLL_BOTH = 3;
    private int scrollState = SCROLL_NONE;

    //Variables for the background
    private GL_Rectangle background = null;
    private GL_Rectangle horizontalScrollBar = null;
    private GL_Rectangle verticalScrollBar = null;
    private int scrollBarColor = Color.GRAY;

    //Variables to determine when scrollbars are visible
    private boolean showScrollbarsAlways = false;
    private boolean showScrollbarsWhileScrolling = true;

    //Variables to make scrollbars stay longer after scrolling ad show when first created
    private long startedShowingScrollbars;
    private boolean scrollbarShowtimeOver = false;
    private static int TIME_SHOW_SCROLLBARS_LONGER_MS = 1650;

    //Other constants
    private static final int SCROLLBAR_THICKNESS_PARAMETER =  72; //Increase to make scrollbar smaller, decrease to make it thicker...
    public static final int CLICK_TOLERANCE_PARAMETER = 38; //Increase to make tolerance smaller, decrease to make it wider...

    //Settings so that the GL_ScrollView keeps scrolling a bit after touch up (just like normal ScrollViews).
    private boolean intelligentScrolling = true;
    private float scrollSpeedX = 0.0f;
    private float scrollSpeedY = 0.0f;
    private int lastMoveX_save = 0;
    private int lastMoveY_save = 0;
    private Long timeOfLastMove = Calendar.getInstance().getTimeInMillis();
    private Long timeOfMove = Calendar.getInstance().getTimeInMillis();

    //The container of GL_Shapes contained in the GL_ScrollView
    private ArrayList<GL_Shape> children = new ArrayList<GL_Shape>();

    /**
     * The constructor to initialize the GL_ScrollView.
     * @param pos The top left position.
     * @param width The width dimension.
     * @param height The height dimension.
     * @param virtualScreenWidth The width of your virtual coordination system.
     * @param virtualScreenHeight The height of your virtual coordination system.
     * @param devicePixelWidth The width of real pixels of the device.
     * @param devicePixelHeight The height of real pixels of the device.
     */
    public GL_ScrollView(CGPoint pos, int width, int height, float virtualScreenWidth, float virtualScreenHeight, float devicePixelWidth, float devicePixelHeight) {
        super(pos);
        this.width = width;
        this.height = height;
        this.drawWidth = width;
        this.drawHeight = height;
        initScrollView(virtualScreenWidth, virtualScreenHeight, devicePixelWidth, devicePixelHeight);
    }

    /**
     * The constructor to initialize the GL_ScrollView.
     * @param x The x top left position.
     * @param y The y top left position.
     * @param width The width dimension.
     * @param height The height dimension.
     * @param virtualScreenWidth The width of your virtual coordination system.
     * @param virtualScreenHeight The height of your virtual coordination system.
     * @param devicePixelWidth The width of real pixels of the device.
     * @param devicePixelHeight The height of real pixels of the device.
     */
    public GL_ScrollView(int x, int y, int width, int height, float virtualScreenWidth, float virtualScreenHeight, float devicePixelWidth, float devicePixelHeight) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.drawWidth = width;
        this.drawHeight = height;
        initScrollView(virtualScreenWidth, virtualScreenHeight, devicePixelWidth, devicePixelHeight);
    }

    /*
     * Init method for the GL_ScrollView.
     */
    private void initScrollView(float virtualScreenWidth, float virtualScreenHeight, float devicePixelWidth, float devicePixelHeight) {
        this.deviceScaleX = devicePixelWidth/(virtualScreenWidth);
        this.deviceScaleY = devicePixelHeight/(virtualScreenHeight);
        this.devicePixelHeight = devicePixelHeight;
        this.virtualScreenHeight = virtualScreenHeight;
        GL_ScrollView.max_click_range = (int) (virtualScreenHeight/CLICK_TOLERANCE_PARAMETER);
        startedShowingScrollbars = Calendar.getInstance().getTimeInMillis();
        initToDpiValue();
        generateDimension();
    }

    /**
     * Initializes the toDpi value.
     * Note: toDpi may not convert values to real DPI, is just helps make it device independent.
     */
    public void initToDpiValue() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        toDpi =  virtualScreenHeight / devicePixelHeight / (metrics.densityDpi / 160f);
    }

    /**
     * Sets the dimensions of the scroll-pane and deactivates automatic pane dimensions.
     * @param paneWidth The width of the scroll-pane where the children are on.
     * @param paneHeight The height of the scroll-pane where the children are on.
     */
    public void setPaneSize(int paneWidth, int paneHeight) {
        this.paneWidth = paneWidth;
        this.paneHeight = paneHeight;
        autoPaneDimension = false;
        generateScrollBars();

        checkPaneDimensions();
    }

    /**
     * Activates automatic pane dimensions.
     */
    public void activateAutoPaneSize() {
        this.autoPaneDimension = true;
        generateDimension();
    }

    /**
     * Adds a view element to the scroll-pane.
     * @param child The view element to be added.
     */
    public void add(GL_Shape child) {
        if (! children.contains(child)) {
            children.add(child);
            if (autoPaneDimension)
                generateDimension();
        }
    }

    /**
     * Removes a view element from the scroll-pane.
     * @param child The view element to be removed.
     */
    public void remove(GL_Shape child) {
        children.remove(child);
    }

    /**
     * This method automatically  generates the dimension of the scroll-pane so that every element is able to be visible.
     */
    private void generateDimension() {
        if (children.size() != 0) {
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;

            //Find the min and max for both x and y
            for (GL_Shape child: children) {
                CGPoint tl = child.getPosTopLeft();
                CGPoint br = child.getPosBottomRight();
                if (tl.getX() < minX)
                    minX = tl.getX();
                if (tl.getY() < minY)
                    minY = tl.getY();
                if (br.getX() > maxX)
                    maxX = br.getX();
                if (br.getY() > maxY)
                    maxY = br.getY();
            }

            this.paneWidth = maxX - minX;
            this.paneHeight = maxY - minY;

            if (pos.getX() > minX) {
                currentOffsetX = pos.getX() - minX;//scroll
                startOffsetX = currentOffsetX;
            } else {
                startOffsetX = 0;
                if (minX > pos.getX())
                    this.paneWidth += minX - pos.getX(); //extend pane
            }

            if (pos.getY() > minY) {
                currentOffsetY = pos.getY() - minY;//scroll
                startOffsetY = currentOffsetY;
            } else {
                startOffsetY = 0;
                if (minY > pos.getY())
                    this.paneHeight += minY - pos.getY(); //extend pane
            }

            //this.paneHeight += minY - pos.getY(); //This is most likely wrong!

            checkPaneDimensions();
        } else {
            this.paneWidth = drawWidth;
            this.paneHeight = drawHeight;
        }

        generateScrollBars();
    }

    /**
     * If scroll-pane is smaller that the GL_ScrollView, extend it
     */
    private void checkPaneDimensions() {
        if (this.paneWidth < drawWidth)
            this.paneWidth = drawWidth;
        if (this.paneHeight < drawHeight)
            this.paneHeight = drawHeight;
    }

    @Override
    protected void generateVertexArray() {
        //abstract method which had to be implemented - not used
    }

    @Override
    public boolean checkTouchDown(int x, int y) {
        boolean isInside = super.checkTouchDown(x, y);
        scrollSpeedX = 0.0f;
        scrollSpeedY = 0.0f;
        if (isInside) {

            //If not 2D scrolling; scrolling - axis has not been determined
            if (scrollState != SCROLL_BOTH)
                scrollState = SCROLL_NONE;

            //Save start of touch event
            this.touchDownX = x;
            this.touchDownY = y;

            //Init the movement between touch events
            this.tmpMoveX = touchDownX;
            this.tmpMoveY = touchDownY;

            //This touch down can be a click or a scroll gesture
            this.possibleClick = true;
            this.possibleScroll = true;

            //Translate the coordinates to pane coordinates
            int xScroll = x + currentOffsetX-startOffsetX;
            int yScroll = y + currentOffsetY-startOffsetY;

            //Pass the touch event if touch is within visible area
            if (GL_Shape.isPointInAlignedRectangle(x, y, pos, drawWidth, drawHeight)) {
                for (int i = 0; i<children.size(); i++) {
                    children.get(i).checkTouchDown(xScroll, yScroll);
                }
            }

            //Manage visibility of scrollbars
            scrollbarShowtimeOver = true;
            if(!showScrollbarsAlways && horizontalScrollBar != null)
                horizontalScrollBar.setVisible(true);
            if(!showScrollbarsAlways && verticalScrollBar != null)
                verticalScrollBar.setVisible(true);

        } else {
            //As the touch down was outside the GL_ScrollView it can't be a click or a scroll gesture
            this.possibleScroll = false;
            this.possibleClick = false;
        }
        return isInside;
    }

    @Override
    public boolean checkTouchUp(int x, int y) {
        boolean isInside = super.checkTouchUp(x, y);

        //Manage visibility of scrollbars
        if (!showScrollbarsAlways && showScrollbarsWhileScrolling) {
            //hide scrollbars in TIME_SHOW_SCROLLBARS_LONGER_MS
            scrollbarShowtimeOver = false;
            startedShowingScrollbars = Calendar.getInstance().getTimeInMillis();
        }

        if (isInside) {
            //Translate the coordinates to pane coordinates
            int xScroll = x + currentOffsetX-startOffsetX;
            int yScroll = y + currentOffsetY-startOffsetY;

            //Pass the touch event if touch is within visible area
            if (GL_Shape.isPointInAlignedRectangle(x, y, pos, drawWidth, drawHeight)) {
                for (int i = 0; i < children.size(); i++) {
                    children.get(i).checkTouchUp(xScroll, yScroll);
                }
            }
        }

        if (possibleScroll && ! possibleClick) {
            if (intelligentScrolling) {
                long time = timeOfMove - timeOfLastMove;
                if (time <= 0)
                    time = 1;
                scrollSpeedX = 2* lastMoveX_save / time;
                scrollSpeedY = 2* lastMoveY_save / time;
            }
        }

        return isInside;
    }

    @Override
    public boolean checkTouchMove(int x, int y) {
        boolean isInside = super.checkTouchMove(x, y);
        if (possibleClick) {
            //Check tolerances to help distinguish between touch and scroll
            if (Math.abs(touchDownX-x) > max_click_range*toDpi ||
                    Math.abs(touchDownY-y) > max_click_range*toDpi) { //If touch is not within click tolerance
                possibleClick = false;
                for (int i = 0; i < children.size(); i++) {
                    children.get(i).cancelTouch(); //Cancel touch so that no events are fired from any child
                }
            }
        }
        if (possibleClick && Calendar.getInstance().getTimeInMillis() - GL_MotionEvent.getTouchDownTime() <= NORMAL_CLICK_DURATION ||
                (disableHorizontalScroll && disableVerticalScroll)) { //click
            int xScroll = x + currentOffsetX-startOffsetX;
            int yScroll = y + currentOffsetY-startOffsetY;
            //Pass the touch event if touch is within visible area
            if (GL_Shape.isPointInAlignedRectangle(x, y, pos, drawWidth, drawHeight)) {
                for (int i = 0; i < children.size(); i++) {
                    children.get(i).checkTouchMove(xScroll, yScroll);
                }
            }
        } else if (possibleScroll){ //scroll

            //If 2D scrolling is deactivated, determine whether to scroll horizontal or vertical
            if (scrollState == SCROLL_NONE) {
                if (!disableHorizontalScroll && (disableVerticalScroll || Math.abs(tmpMoveX-x) > Math.abs(tmpMoveY-y)))
                    scrollState = SCROLL_HORIZONTAL;
                else
                    scrollState = SCROLL_VERTICAL;
            }

            //save tmp values
            lastMoveX_save = tmpMoveX-x;
            lastMoveY_save = tmpMoveY-y;

            //Scroll the distance between the last touch event
            if ((! disableHorizontalScroll) && scrollState != SCROLL_VERTICAL)
                currentOffsetX += tmpMoveX-x;
            if ((! disableVerticalScroll) && scrollState != SCROLL_HORIZONTAL)
                currentOffsetY += tmpMoveY-y;
            tmpMoveX = x;
            tmpMoveY = y;

            //update last move
            timeOfLastMove = timeOfMove;
            timeOfMove = Calendar.getInstance().getTimeInMillis();


            checkBoundaries();

        }
        return isInside;
    }

    /**
     * Makes sure it does not scroll to far
     */
    protected void checkBoundaries() {
    //Todo try replace it witch functions below
        if(currentOffsetX > paneWidth-drawWidth)
            scrollToRight();
        else if(currentOffsetX < 0)
            scrollToLeft();
        if(currentOffsetY > paneHeight-drawHeight)
            scrollToBottom();
        else if(currentOffsetY < 0)
            scrollToTop();
    }

    public boolean isScrolledToLeft() {
        return currentOffsetX <= 0;
    }

    public boolean isScrolledToRight() {
        return currentOffsetX >= paneWidth-drawWidth;
    }

    public boolean isScrolledToTop() {
        return currentOffsetY <= 0;
    }

    public boolean isScrolledToBottom() {
        return currentOffsetY >= paneHeight-drawHeight;
    }

    /**
     * This method manages the visibility of the scrollbars, after the touch-up event.
     */
    private void updateScrollbarTime() {
        if (!scrollbarShowtimeOver &&
                (Calendar.getInstance().getTimeInMillis() - startedShowingScrollbars > TIME_SHOW_SCROLLBARS_LONGER_MS)) {
            if (horizontalScrollBar != null)
                horizontalScrollBar.setVisible(false);
            if (verticalScrollBar != null)
                verticalScrollBar.setVisible(false);
            scrollbarShowtimeOver = false;
        }
    }

    /**
     * Intelligent scrolling helps improves the user experience through scrolling for a little bit
     * after the Touch-Up event according to users scroll-speed before.
     */
    private void autoScroll() {
        if (Math.abs(scrollSpeedX) > 1.0f || Math.abs(scrollSpeedY) > 1.0f) {

            //Scroll the distance between the last touch event
            if ((! disableHorizontalScroll) && scrollState != SCROLL_VERTICAL)
                currentOffsetX += scrollSpeedX;
            if ((! disableVerticalScroll) && scrollState != SCROLL_HORIZONTAL)
                currentOffsetY += scrollSpeedY;

            scrollSpeedX = scrollSpeedX * 0.85f;
            scrollSpeedY = scrollSpeedY * 0.85f;

            checkBoundaries();
        }
    }

    /**
     * This method is to be called to draw the GL_ScrollView and it's components.
     * @param projectionMatrix The projection matrix for the vertex-shader to translate the custom coordination system to the OpenGl ES coordination system.
     */
    public void draw(float[] projectionMatrix) {

        if (!showScrollbarsAlways && showScrollbarsWhileScrolling) {
            updateScrollbarTime();
        }

        if (intelligentScrolling) {
            autoScroll();
        }

        if (visible) {
            if (background != null)
                background.draw(projectionMatrix);

            //Movement matrix, to be multiplied with the projection matrix
            float[] m_matix = {
                    //Order x,      y,      z,      w
                    1.f,    0.f,    0.f,    0.f ,
                    0.f,    1.f,    0.f,    0.f ,
                    0.f,    0.f,    1.f,    0.f,
                    -currentOffsetX+startOffsetX,  -currentOffsetY+startOffsetY,  0.f,    1.f
            };

            //Multiply the matrices, based on your implementation you may have to change the order
            float[] movedProjectionMatrix = MatrixHelper.gldMultMatrix(projectionMatrix, m_matix);

            //Translate the custom OpenGL coordination system to device coordination system
            int scissorWidth = (int) (drawWidth * deviceScaleX);
            int scissorHeight = (int) (drawHeight * deviceScaleY);
            int scissorPosX = (int) (pos.x * deviceScaleX);
            int scissorPosY = (int) (devicePixelHeight - pos.y * deviceScaleY - scissorHeight); //the y-axis is reversed

            //Use GL_SCISSOR_TEST to limit the drawing to the size of the GL_ScrollView
            GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
            GLES20.glScissor(scissorPosX, scissorPosY, scissorWidth, scissorHeight);

            //Draw every child on the scroll-pane
            for (int i = 0; i < children.size(); i++) {
                children.get(i).draw(movedProjectionMatrix);
            }

            GLES20.glDisable(GLES20.GL_SCISSOR_TEST);

            //Draw scrollbars if needed
            if (!disableVerticalScroll && verticalScrollBar != null && paneHeight != drawHeight) {
                int scrollBarPosition = drawHeight*currentOffsetY/paneHeight;
                //Movement matrix
                float[] m_matix2 = {
                        //Order x,      y,      z,      w
                        1.f,    0.f,    0.f,    0.f ,
                        0.f,    1.f,    0.f,    0.f ,
                        0.f,    0.f,    1.f,    0.f,
                        0,  scrollBarPosition,  0.f,    1.f
                };
                verticalScrollBar.draw(MatrixHelper.gldMultMatrix(projectionMatrix, m_matix2));
            }
            if (!disableHorizontalScroll && horizontalScrollBar != null && paneWidth != drawWidth) {
                int scrollBarPosition = drawWidth*currentOffsetX/paneWidth;
                //Movement matrix
                float[] m_matix2 = {
                        //Order x,      y,      z,      w
                        1.f,    0.f,    0.f,    0.f ,
                        0.f,    1.f,    0.f,    0.f ,
                        0.f,    0.f,    1.f,    0.f,
                        scrollBarPosition,  0,  0.f,    1.f
                };
                horizontalScrollBar.draw(MatrixHelper.gldMultMatrix(projectionMatrix, m_matix2));
            }

        }
    }

    public void scrollToTop() {
        currentOffsetY = 0;
    }
    public void scrollToBottom() {
        currentOffsetY = paneHeight-drawHeight;
    }
    public void scrollToLeft() {
        currentOffsetX = 0;
    }
    public void scrollToRight() {
        currentOffsetX = paneWidth-drawWidth;
    }

    @Override
    public boolean collidesWithPoint(int x, int y) {
        return ((x >= pos.getX() && x <= pos.getX()+width) && (y >= pos.getY() && y <= pos.getY()+height));
    }

    @Override
    public boolean collidesWithRectangle(int x, int y, int w, int h) {
        return (pos.x <= x + w &&
                pos.x + width > x &&
                pos.y <= y + h &&
                height + pos.y > y);
    }

    @Override
    public CGPoint getPosTopLeft() {
        return pos;
    }

    @Override
    public void setPosX(int x) {
        super.setPosX(x);
        if (background != null)
            background.setPosX(x);
    }

    @Override
    public void setPosY(int y) {
        super.setPosX(y);
        if (background != null)
            background.setPosX(y);
    }

    @Override
    public CGPoint getPosBottomRight() {
        return new CGPoint(pos.x + width, pos.y + height);
    }

    /**
     * Sets the transparency of the background.
     * @param color The color of the background, format #AARRGGBB
     * @param alpha Value between 0.0 (total transparency) and 1.0 (not transparent).
     */
    public void setBackgroundColor(int color, float alpha) {
        if (background == null)
            background = new GL_Rectangle(pos, width, height, color);
        else
            background.setColor(color);

        background.setAlpha(alpha);
    }

    /**
     * Sets the color of the scrollbars.
     * @param scrollBarColor
     */
    public void setScrollBarColor(int scrollBarColor) {
        this.scrollBarColor = scrollBarColor;
        if (horizontalScrollBar != null)
            horizontalScrollBar.setColor(this.scrollBarColor);
        if (verticalScrollBar != null)
            verticalScrollBar.setColor(this.scrollBarColor);
    }

    /**
     * Activates 2D scrolling.
     * This makes it possible to scroll horizontal and vertical at the same time.
     * This is deactivated by default.
     */
    public void enable2Dscrolling() {
        scrollState = SCROLL_BOTH;
    }

    /**
     * Deactivates 2D scrolling.
     * This makes it possible to only scroll horizontal or vertical per touchdown.
     */
    public void disable2Dscrolling() {
        scrollState = SCROLL_NONE;
    }

    public boolean isHorizontalScrollDisabled() {
        return disableHorizontalScroll;
    }

    public void disableHorizontalScroll() {
        this.disableHorizontalScroll = true;
    }

    public void enableHorizontalScroll() {
        this.disableHorizontalScroll = false;
    }

    public boolean isVerticalScrollDisabled() {
        return disableVerticalScroll;
    }

    public void disableVerticalScroll() {
        this.disableVerticalScroll = true;
    }

    public void enableVerticalScroll() {
        this.disableVerticalScroll = false;
    }

    /**
     * Activates/deactivates the visibility of the scrollbars only while scrolling.
     * @param showScrollbars
     */
    public void showScrollbars(boolean showScrollbars) {
        this.showScrollbarsWhileScrolling = showScrollbars;
        this.showScrollbarsAlways = false;
        generateScrollBars();
    }

    /**
     * This method deactivates the visibility of the scrollbars.
     */
    public void alwaysShowScrollbars() {
        this.showScrollbarsAlways = true;
        this.showScrollbarsWhileScrolling = true;
        generateScrollBars();
    }

    /**
     * This method makes the scrollbars always visible.
     */
    public void neverShowScrollbars() {
        this.showScrollbarsAlways = false;
        this.showScrollbarsWhileScrolling = false;
        generateScrollBars();
    }

    /**
     * This method generates the scrollbars based on their visibility.
     */
    private void generateScrollBars() {
        if (showScrollbarsWhileScrolling || showScrollbarsAlways) {
            int scrollBarWidth = drawWidth*drawWidth/paneWidth;
            int scrollBarHeight = drawHeight*drawHeight/paneHeight;
            int scrollBarThickness =  (int) (virtualScreenHeight/SCROLLBAR_THICKNESS_PARAMETER/toDpi);
            horizontalScrollBar = new GL_Rectangle(pos.getX(), pos.getY()+drawHeight-((int) (scrollBarThickness*1.5)), scrollBarWidth, scrollBarThickness, scrollBarColor);
            verticalScrollBar = new GL_Rectangle(pos.getX()+drawWidth- ((int) (scrollBarThickness*1.5)), pos.getY(), scrollBarThickness, scrollBarHeight, scrollBarColor);
            if (scrollbarShowtimeOver && !showScrollbarsAlways) {
                horizontalScrollBar.setVisible(false);
                verticalScrollBar.setVisible(false);
            }
        } else {
            horizontalScrollBar = null;
            verticalScrollBar = null;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDrawWidth() {
        return drawWidth;
    }

    /**
     * Sets the width of the area of the scroll-pane to be drawn.
     * @param drawWidth The width of the area of the scroll-pane to be drawn.
     */
    public void setDrawWidth(int drawWidth) {
        this.drawWidth = drawWidth;
        if (autoPaneDimension)
            generateDimension();
        else
            checkPaneDimensions();
        generateScrollBars();
    }

    public int getDrawHeight() {
        return drawHeight;
    }

    /**
     * Sets the height of the area of the scroll-pane to be drawn.
     * @param drawHeight The height of the area of the scroll-pane to be drawn.
     */
    public void setDrawHeight(int drawHeight) {
        this.drawHeight = drawHeight;
        if (autoPaneDimension)
            generateDimension();
        else
            checkPaneDimensions();
        generateScrollBars();
    }

    public int getScrollOffsetX() {
        return currentOffsetX;
    }

    public int getScrollOffsetY() {
        return currentOffsetY;
    }

    /**
     * Returns the background of the ScrollView. Returns null if no background color is set
     *
     * @return Returns the background of the ScrollView.
     */
    public GL_Rectangle getBackground() {
        return background;
    }

    /**
     * Intelligent scrolling (normally activated) helps you scroll faster because it keeps scrolling (a little bit)
     * after the Touch-Up event according to the scrolled speed before.
     *
     * @param intelligentScrolling State whether it is activated or not.
     */
    public void setIntelligentScrolling(boolean intelligentScrolling) {
        this.intelligentScrolling = intelligentScrolling;
    }
}
