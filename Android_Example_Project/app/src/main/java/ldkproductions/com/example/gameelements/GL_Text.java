package ldkproductions.com.example.gameelements;

import android.graphics.Color;

import ldkproductions.com.example.data.CGPoint;
import ldkproductions.com.example.exceptions.FontNotSetException;
import ldkproductions.com.example.objects.GL_Shape;
import ldkproductions.com.example.objects.GL_Texture;

/**
 * Created by Leo on 26.11.2015.
 */
public class GL_Text extends GL_Texture {
    protected String text;
    protected GL_Font font;
    protected int xAlignment;
    protected int yAlignment;
    protected int color;

    protected int xNegOffset;
    protected int yNegOffset;

    protected int yDrawTextOffsetSave; //Save the draw offset for calculations

    protected float scale = 1.0f;

    public static final int ALIGNMENT_X_AS_LEFT = 0;
    public static final int ALIGNMENT_X_AS_RIGHT = 1;
    public static final int ALIGNMENT_X_AS_CENTRE = 2;
    public static final int ALIGNMENT_Y_AS_CENTRE = 2;
    public static final int ALIGNMENT_Y_AS_TRUE_CENTRE = 3;
    public static final int ALIGNMENT_Y_AS_TOP = 0;
    public static final int ALIGNMENT_Y_AS_BOTTOM = 1;
    public static final int ALIGNMENT_Y_AS_BOTTOM_LINE = 4;

    public GL_Text(String text) {
        super(0,0);
        init(text, null, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, Color.BLACK);
    }

    public GL_Text(String text, GL_Font font) {
        super(0,0);
        init(text, font, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, Color.BLACK);
    }

    public GL_Text(String text, int color) {
        super(0,0);
        init(text, null, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, color);
    }

    public GL_Text(String text, GL_Font font, int color) {
        super(0,0);
        init(text, font, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, color);
    }

    public GL_Text(String text, int x, int y) {
        super(x,y);
        init(text, null, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, Color.BLACK);
    }

    public GL_Text(String text, GL_Font font, int x, int y) {
        super(x,y);
        init(text, font, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, Color.BLACK);
    }

    public GL_Text(String text, int x, int y, int color) {
        super(x,y);
        init(text, null, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, color);
    }

    public GL_Text(String text, GL_Font font, int x, int y, int color) {
        super(x,y);
        init(text, font, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, color);
    }

    public GL_Text(String text, int x, int y, int xAlignment, int yAlignment) {
        super(x,y);
        init(text, null, xAlignment, yAlignment, Color.BLACK);
    }

    public GL_Text(String text, GL_Font font, int x, int y, int xAlignment, int yAlignment) {
        super(x,y);
        init(text, font, xAlignment, yAlignment, Color.BLACK);
    }

    public GL_Text(String text, int x, int y, int xAlignment, int yAlignment, int color) {
        super(x, y);
        init(text, null, xAlignment, yAlignment, color);
    }

    public GL_Text(String text, GL_Font font, int x, int y, int xAlignment, int yAlignment, int color) {
        super(x, y);
        init(text, font, xAlignment, yAlignment, color);
    }

    private void init(String text, GL_Font font, int xAlignment, int yAlignment, int color) {
        this.text = text;
        this.font = font;
        this.xAlignment = xAlignment;
        this.yAlignment = yAlignment;
        this.color = color;
        calculateOffsets();
    }

    public void setPosition(int x, int y, int xAlignment, int yAlignment) {
        this.xAlignment = xAlignment;
        this.yAlignment = yAlignment;
        super.setPosition(x, y);
        calculateOffsets();
    }

    public void setAlignments(int xAlignment, int yAlignment) {
        this.xAlignment = xAlignment;
        this.yAlignment = yAlignment;
        calculateOffsets();
    }

    public int getXleft() {
        return pos.getX()-xNegOffset;
    }

    public int getYtop() {
        switch (yAlignment) {
            case ALIGNMENT_Y_AS_TOP:
                return pos.getY();
            case ALIGNMENT_Y_AS_BOTTOM:
                return pos.getY()-height;
            case ALIGNMENT_Y_AS_TRUE_CENTRE:
                return pos.getY() - height/2;
            case ALIGNMENT_Y_AS_BOTTOM_LINE:
                return (int) (pos.getY() - font.getMaxTextHeight()*scale + font.getMaxDescent()*scale + yDrawTextOffsetSave);
            default: //ALIGNMENT_Y_AS_CENTRE
                return (int) (pos.getY() - font.getMaxTextHeight()*scale + font.getMaxDescent()*scale + font.getNormalCapHeight()/2*scale + yDrawTextOffsetSave);
        }
    }

    @Override
    public CGPoint getPosTopLeft() {
        return new CGPoint(getXleft(), getYtop());
    }

    @Override
    public CGPoint getPosBottomRight() {
        return new CGPoint(getXleft()+width, getYtop()+height);
    }

    @Override
    protected boolean isPointInTexture(int x, int y) {
        return ((x >= getXleft() && x <= getXleft()+width)
                && (y >= getYtop() && y <= getYtop()+height));
    }

    @Override
    public boolean collidesWithRectangle(int x, int y, int w, int h) {
        return GL_Shape.alignedRectangleRectangleCollision(x, y, w, h, getXleft(), getYtop(), width, height);
    }

    @Override
    public void draw(float[] projectionMatrix) {
        if(isVisible()) {
            try {
                if(font == null) {
                    if (GL_Font.isDefaultFontSet()) { //Use default font
                        setFont(GL_Font.getDefaultFont());
                    } else {
                        throw new FontNotSetException();
                    }
                }
                font.drawText(text, pos.getX()-xNegOffset, pos.getY()-yNegOffset, color, projectionMatrix, scale);
            } catch (FontNotSetException e) {
                e.printStackTrace();
            }
        }
    }

    private void calculateOffsets() {
        if (font == null)
            return;
        width = font.getTextWidth(text);
        switch (xAlignment) {
            case ALIGNMENT_X_AS_LEFT:
                xNegOffset = 0;
                break;
            case ALIGNMENT_X_AS_RIGHT:
                xNegOffset = width;
                break;
            default: //ALIGNMENT_X_AS_CENTRE
                xNegOffset = width/2;
                break;
        }

        int[] h_data = font.getTextHeight(text);
        height = h_data[0];
        yDrawTextOffsetSave = h_data[1];
        yNegOffset = 0;
        switch (yAlignment) {
            case ALIGNMENT_Y_AS_TOP:
                yNegOffset += h_data[1];
                break;
            case ALIGNMENT_Y_AS_BOTTOM:
                yNegOffset += height + h_data[1];
                break;
            case ALIGNMENT_Y_AS_TRUE_CENTRE:
                yNegOffset += height/2 + h_data[1];
                break;
            case ALIGNMENT_Y_AS_BOTTOM_LINE:
                yNegOffset += font.getMaxTextHeight() - font.getMaxDescent();
                break;
            default: //ALIGNMENT_Y_AS_CENTRE
                yNegOffset += font.getMaxTextHeight() - font.getMaxDescent() - font.getNormalCapHeight()/2;
                break;
        }

        //Scale appropriately
        width = (int) (width * scale);
        height = (int) (height * scale);
        xNegOffset = (int) (xNegOffset * scale);
        yNegOffset = (int) (yNegOffset * scale);
        yDrawTextOffsetSave = (int) (yDrawTextOffsetSave * scale);
    }

    @Override
    public int getMiddleX() {
        return getXleft() + width/2;
    }
    @Override
    public int getMiddleY() {
        return getYtop() + height/2;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        calculateOffsets();
    }

    public GL_Font getFont() {
        return font;
    }

    public void setFont(GL_Font font) {
        this.font = font;
        calculateOffsets();
    }

    public int getxAlignment() {
        return xAlignment;
    }

    public void setxAlignment(int xAlignment) {
        this.xAlignment = xAlignment;
        calculateOffsets();
    }

    public int getyAlignment() {
        return yAlignment;
    }

    public void setyAlignment(int yAlignment) {
        this.yAlignment = yAlignment;
        calculateOffsets();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        calculateOffsets();
    }
}
