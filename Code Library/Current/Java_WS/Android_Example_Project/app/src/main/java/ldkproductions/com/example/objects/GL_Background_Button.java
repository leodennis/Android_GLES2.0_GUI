package ldkproductions.com.example.objects;

import android.graphics.Color;
import android.util.Log;

import ldkproductions.com.example.data.CGPoint;

/**
 * Created by Leo on 11.12.2015.
 */
public class GL_Background_Button extends GL_Rectangle{

    private static final int outlineWidth = 8; //should be small number and even
    private static final int colorDiff = 35; //the RGB - difference between top of the buton and bottom
    private static final int colorPressedDiff = -40; //the RGB - difference between normal and pressed

    private GL_Line lineTop;
    private GL_Line lineLeft;
    private GL_Line lineRight;
    private GL_Line lineBottom;

    private int color;

    public GL_Background_Button(CGPoint pos, int width, int height, int color) {
        super(pos, width - outlineWidth, height - outlineWidth, color);

        this.color = color;

        int topColor = changeColor(color, colorDiff/2);
        int bottomColor = changeColor(color, -colorDiff/2);

        setColors(topColor, topColor, bottomColor, bottomColor, true);

        int bottomColorOutline = changeColor(color, 1-colorDiff);

        lineTop = new GL_Line(pos.getX(), pos.getY()+outlineWidth/2, pos.getX()+width,  pos.getY()+outlineWidth/2, bottomColor);
        lineLeft = new GL_Line(pos.getX()+outlineWidth/2, pos.getY(), pos.getX()+outlineWidth/2,  pos.getY()+height);
        lineLeft.setColors(bottomColor, bottomColorOutline, true);
        lineRight = new GL_Line(pos.getX()+width-outlineWidth/2, pos.getY(), pos.getX()+width-outlineWidth/2,  pos.getY()+height);
        lineRight.setColors(bottomColor, bottomColorOutline, true);
        lineBottom = new GL_Line(pos.getX(), pos.getY()+height-outlineWidth/2, pos.getX()+width,  pos.getY()+height-outlineWidth/2, bottomColorOutline);

        lineTop.setStrokeWidth(((float)outlineWidth));
        lineLeft.setStrokeWidth(((float)outlineWidth));
        lineRight.setStrokeWidth(((float)outlineWidth));
        lineBottom.setStrokeWidth(((float)outlineWidth));
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

        if (r > 255)
            r = 255;
        else if (r < 0)
            r = 0;

        if (g > 255)
            g = 255;
        else if (g < 0)
            g = 0;

        if (b > 255)
            b = 255;
        else if (b < 0)
            b = 0;

        return Color.rgb(r, g, b);
    }

    public static int getPressedColor(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        int pressColor;

        if (Math.abs(g-r) < 50 && Math.abs(g-b) < 50 && Math.abs(b-r) < 50) {
            pressColor = changeColor(color, colorPressedDiff);
        } else if (((g <= r) && (b < g) || (r <= g) && (b < r)) && Math.abs(g-r) < 50) {

            pressColor = changeColor(color, 0, colorPressedDiff, 0);
        /*} else if (Math.abs(r-g) < 50 && Math.abs(g-r) < 50)  {
            if (g < 128) {
                pressColor = changeColor(color, 0, colorPressedDiff, 0);
            } else {
                pressColor = changeColor(color, -colorPressedDiff/2, colorPressedDiff/2, colorPressedDiff*3);
            }*/
        } else {
            pressColor = changeColor(color, colorPressedDiff);

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
        lineTop.setPosition(pos.getX(), pos.getY() + outlineWidth / 2);
        lineTop.setPos2(new CGPoint(pos.getX() + width, pos.getY() + outlineWidth / 2));

        lineLeft.setPosition(pos.getX() + outlineWidth / 2, pos.getY());
        lineLeft.setPos2(new CGPoint(pos.getX() + outlineWidth / 2, pos.getY() + height));

        lineRight.setPosition(pos.getX() + width - outlineWidth / 2, pos.getY());
        lineRight.setPos2(new CGPoint(pos.getX() + width - outlineWidth / 2, pos.getY() + height));

        lineBottom.setPosition(pos.getX(), pos.getY()+height-outlineWidth/2);
        lineBottom.setPos2(new CGPoint(pos.getX()+width,  pos.getY()+height-outlineWidth/2));
    }

    public int getColor() {
        return this.color;
    }


}
