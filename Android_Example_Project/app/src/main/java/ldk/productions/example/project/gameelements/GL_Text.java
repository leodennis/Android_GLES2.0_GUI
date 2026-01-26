package ldk.productions.example.project.gameelements;

import android.graphics.Color;

import ldk.productions.example.project.data.CGPoint;
import ldk.productions.example.project.exceptions.FontNotSetException;
import ldk.productions.example.project.objects.GL_Shape;
import ldk.productions.example.project.objects.GL_Texture;

import static ldk.productions.example.project.gameelements.GL_TextAlignment.ALIGNMENT_X_AS_LEFT;
import static ldk.productions.example.project.gameelements.GL_TextAlignment.ALIGNMENT_Y_AS_TOP;

/**
 * Created by Leo on 26.11.2015.
 */
public class GL_Text extends GL_Texture {
    protected String text;
    protected GL_Font font;
    protected GL_TextAlignment xAlignment;
    protected GL_TextAlignment yAlignment;
    protected int color = Color.BLACK;

    protected int xNegOffset;
    protected int yNegOffset;

    protected int yDrawTextOffsetSave;  // save the draw offset for calculations

    protected float scale = 1.0f;

    public GL_Text(int x, int y, String text) {
        this(x, y, text, GL_Font.getDefaultFont(), ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP);
    }

    public GL_Text(int x, int y, String text, GL_Font font) {
        this(x, y, text, font, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP);
    }

    public GL_Text(int x, int y, String text, GL_TextAlignment xAlignment, GL_TextAlignment yAlignment) {
        this(x, y, text, GL_Font.getDefaultFont(), xAlignment, yAlignment);
    }

    public GL_Text(int x, int y, String text, GL_Font font, GL_TextAlignment xAlignment, GL_TextAlignment yAlignment) {
        super(x,y);

        this.text = text;
        this.font = font;
        this.xAlignment = xAlignment;
        this.yAlignment = yAlignment;

        init();
    }

    /**
     * Copy constructor
     * @param obj the GL_Text to be copied
     */
    public GL_Text(GL_Text obj) {
        super(obj);
        this.text = obj.text;
        this.font = obj.font;
        this.xAlignment = obj.xAlignment;
        this.yAlignment = obj.yAlignment;
        this.color = obj.color;
        this.xNegOffset = obj.xNegOffset;
        this.yNegOffset = obj.yNegOffset;
        this.yDrawTextOffsetSave = obj.yDrawTextOffsetSave;
        this.scale = obj.scale;
    }

    @Override
    public Object clone() {
        return new GL_Text(this);
    }

    protected void init() {
        calculateOffsets();
    }

    @Override
    public void draw(float[] projectionMatrix) {
        if (visible) {
            if (font == null) throw new FontNotSetException();

            font.drawText(text, pos.getX()-xNegOffset, pos.getY()-yNegOffset, color, projectionMatrix, scale);
        }
    }

    @Override
    protected boolean isPointInTexture(int x, int y) {
        return ((x >= getXleft() && x <= getXleft()+width) && (y >= getYtop() && y <= getYtop()+height));
    }

    @Override
    public boolean collidesWithRectangle(int x, int y, int w, int h) {
        return GL_Shape.alignedRectangleRectangleCollision(x, y, w, h, getXleft(), getYtop(), width, height);
    }

    protected void calculateOffsets() {
        if (font == null) return;

        width = font.getTextWidth(text);
        switch (xAlignment) {
            case ALIGNMENT_X_AS_LEFT:
                xNegOffset = 0;
                break;
            case ALIGNMENT_X_AS_RIGHT:
                xNegOffset = width;
                break;
            case ALIGNMENT_X_AS_CENTER:
                xNegOffset = width/2;
                break;
            default:
                throw new RuntimeException("Unknown text alignment for x: " + xAlignment);
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
            case ALIGNMENT_Y_AS_TRUE_CENTER:
                yNegOffset += height/2 + h_data[1];
                break;
            case ALIGNMENT_Y_AS_BOTTOM_LINE:
                yNegOffset += font.getMaxTextHeight() - font.getMaxDescent();
                break;
            case ALIGNMENT_Y_AS_CENTER:
                yNegOffset += font.getMaxTextHeight() - font.getMaxDescent() - font.getNormalCapHeight()/2;
                break;
            default:
                throw new RuntimeException("Unknown text alignment for y: " + xAlignment);
        }

        // scale appropriately
        width = (int) (width * scale);
        height = (int) (height * scale);
        xNegOffset = (int) (xNegOffset * scale);
        yNegOffset = (int) (yNegOffset * scale);
        yDrawTextOffsetSave = (int) (yDrawTextOffsetSave * scale);
    }

    public void setPosition(int x, int y, GL_TextAlignment xAlignment, GL_TextAlignment yAlignment) {
        super.setPosition(x, y);
        setAlignments(xAlignment, yAlignment);
    }

    public void setAlignments(GL_TextAlignment xAlignment, GL_TextAlignment yAlignment) {
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
            case ALIGNMENT_Y_AS_TRUE_CENTER:
                return pos.getY() - height/2;
            case ALIGNMENT_Y_AS_BOTTOM_LINE:
                return (int) (pos.getY() - font.getMaxTextHeight()*scale + font.getMaxDescent()*scale + yDrawTextOffsetSave);
            case ALIGNMENT_Y_AS_CENTER:
                return (int) (pos.getY() - font.getMaxTextHeight()*scale + font.getMaxDescent()*scale + font.getNormalCapHeight()/2*scale + yDrawTextOffsetSave);
            default:
                throw new RuntimeException("Unknown text alignment for y: " + xAlignment);
        }
    }

    @Override
    public CGPoint getPosTopLeft() {
        return new CGPoint(getXleft(), getYtop());
    }

    @Override
    public CGPoint getPosBottomRight() {
        return new CGPoint(getXleft() + width, getYtop() + height);
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

    public GL_TextAlignment getXAlignment() {
        return xAlignment;
    }

    public void setXAlignment(GL_TextAlignment xAlignment) {
        this.xAlignment = xAlignment;
        calculateOffsets();
    }

    public GL_TextAlignment getYAlignment() {
        return yAlignment;
    }

    public void setYAlignment(GL_TextAlignment yAlignment) {
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
