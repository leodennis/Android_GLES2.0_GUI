package ldk.productions.example.project.objects;

import android.graphics.Color;

import ldk.productions.example.project.data.CGPoint;

/**
 * Created by Leo on 11.12.2015.
 */
public class GL_Background_Button extends GL_Rectangle {

    private static final int OUTLINE_WIDTH = 8; // should be small number and even
    private static final int COLOR_DIFF = 35; // the RGB difference between top of the button and bottom
    private static final int COLOR_PRESSED_DIFF = -40; // the RGB difference between normal and pressed

    private GL_Line lineTop;
    private GL_Line lineLeft;
    private GL_Line lineRight;
    private GL_Line lineBottom;

    private int color;

    public GL_Background_Button(CGPoint pos, int width, int height, int color) {
        super(pos, width - OUTLINE_WIDTH, height - OUTLINE_WIDTH, color);

        this.color = color;

        int topColor = changeColor(color, COLOR_DIFF /2);
        int bottomColor = changeColor(color, -COLOR_DIFF /2);

        setColors(topColor, topColor, bottomColor, bottomColor, true);

        int bottomColorOutline = changeColor(color, 1- COLOR_DIFF);

        lineTop = new GL_Line(pos.getX(), pos.getY()+ OUTLINE_WIDTH /2, pos.getX()+width,  pos.getY()+ OUTLINE_WIDTH /2, bottomColor);
        lineLeft = new GL_Line(pos.getX()+ OUTLINE_WIDTH /2, pos.getY(), pos.getX()+ OUTLINE_WIDTH /2,  pos.getY()+height);
        lineLeft.setColors(bottomColor, bottomColorOutline, true);
        lineRight = new GL_Line(pos.getX()+width- OUTLINE_WIDTH /2, pos.getY(), pos.getX()+width- OUTLINE_WIDTH /2,  pos.getY()+height);
        lineRight.setColors(bottomColor, bottomColorOutline, true);
        lineBottom = new GL_Line(pos.getX(), pos.getY()+height- OUTLINE_WIDTH /2, pos.getX()+width,  pos.getY()+height- OUTLINE_WIDTH /2, bottomColorOutline);

        lineTop.setStrokeWidth(((float) OUTLINE_WIDTH));
        lineLeft.setStrokeWidth(((float) OUTLINE_WIDTH));
        lineRight.setStrokeWidth(((float) OUTLINE_WIDTH));
        lineBottom.setStrokeWidth(((float) OUTLINE_WIDTH));
    }

    /**
     * Copy constructor
     * @param obj the GL_Background_Button to be copied
     */
    public GL_Background_Button(GL_Background_Button obj) {
        super(obj);
        this.lineTop = obj.lineTop;
        this.lineLeft = obj.lineLeft;
        this.lineRight = obj.lineRight;
        this.lineBottom = obj.lineBottom;
    }

    @Override
    public Object clone() {
        return new GL_Background_Button(this);
    }

    @Override
    public void draw(float[] projectionMatrix){
        if (visible) {
            super.draw(projectionMatrix);
            lineTop.draw(projectionMatrix);
            lineLeft.draw(projectionMatrix);
            lineRight.draw(projectionMatrix);
            lineBottom.draw(projectionMatrix);
        }
    }

    private static int changeColor(int color, int change) {
        return changeColor(color, change, change, change);
    }

    private static int changeColor(int color, int r_change, int g_change, int b_change) {
        int r = Color.red(color)+r_change;
        int g = Color.green(color)+g_change;
        int b = Color.blue(color)+b_change;

        if (r > MAXIMUM_RBG_COLOR_VALUE) {
            r = (int) MAXIMUM_RBG_COLOR_VALUE;
        } else if (r < 0) {
            r = 0;
        }

        if (g > MAXIMUM_RBG_COLOR_VALUE) {
            g = (int) MAXIMUM_RBG_COLOR_VALUE;
        } else if (g < 0) {
            g = 0;
        }

        if (b > MAXIMUM_RBG_COLOR_VALUE) {
            b = (int) MAXIMUM_RBG_COLOR_VALUE;
        } else if (b < 0) {
            b = 0;
        }

        return Color.rgb(r, g, b);
    }

    public static int getPressedColor(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        int pressColor;

        if (Math.abs(g-r) < 50 && Math.abs(g-b) < 50 && Math.abs(b-r) < 50) {
            pressColor = changeColor(color, COLOR_PRESSED_DIFF);
        } else if (((g <= r) && (b < g) || (r <= g) && (b < r)) && Math.abs(g-r) < 50) {
            pressColor = changeColor(color, 0, COLOR_PRESSED_DIFF, 0);
        } else {
            pressColor = changeColor(color, COLOR_PRESSED_DIFF);
        }

        return pressColor;
    }
    @Override
    public void setPosX(int x) {
        super.setPosX(x);
        updateLinePositions();
    }

    @Override
    public void setPosY(int y) {
        super.setPosY(y);
        updateLinePositions();
    }

    protected void updateLinePositions() {
        lineTop.setPosition(pos.getX(), pos.getY() + OUTLINE_WIDTH / 2);
        lineTop.setPos2(new CGPoint(pos.getX() + width, pos.getY() + OUTLINE_WIDTH / 2));

        lineLeft.setPosition(pos.getX() + OUTLINE_WIDTH / 2, pos.getY());
        lineLeft.setPos2(new CGPoint(pos.getX() + OUTLINE_WIDTH / 2, pos.getY() + height));

        lineRight.setPosition(pos.getX() + width - OUTLINE_WIDTH / 2, pos.getY());
        lineRight.setPos2(new CGPoint(pos.getX() + width - OUTLINE_WIDTH / 2, pos.getY() + height));

        lineBottom.setPosition(pos.getX(), pos.getY()+height- OUTLINE_WIDTH /2);
        lineBottom.setPos2(new CGPoint(pos.getX()+width,  pos.getY()+height- OUTLINE_WIDTH /2));
    }

    public int getColor() {
        return this.color;
    }

}
