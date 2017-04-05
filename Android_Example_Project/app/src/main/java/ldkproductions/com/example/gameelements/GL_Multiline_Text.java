package ldkproductions.com.example.gameelements;

import android.graphics.Color;
import android.text.TextUtils;

import ldkproductions.com.example.data.CGPoint;
import ldkproductions.com.example.exceptions.FontNotSetException;
import ldkproductions.com.example.objects.GL_Shape;
import ldkproductions.com.example.objects.GL_Texture;

/**
 * An OpenGL ES 2.0 multiline text label.
 *
 * Created by Leo on 26.11.2015.
 */
public class GL_Multiline_Text extends GL_Texture {


    protected String originalText;    //The original text how it was passed to the object
    protected String[] textLines;     //The text slitted into lines

    //A normal font and a smaller font if the text does not fit with the normal font
    protected GL_Font font;
    protected GL_Font smallFont = null;
    protected boolean useSmallFont = false;

    protected int lineTextHeight; //The height of a single line
    protected int lineHeightDiff; //Saves how much the height of a text line was increased/decreased

    protected int color;  //The color of the text

    protected float scale = 1.0f; //Scale of the text

    //AutoFit to fit the text automatically to a bounding box
    protected boolean autoFit = false;
    protected Integer autoBoundsWidth = null;
    protected Integer autoBoundsHeight = null;

    //Used to specify where to draw each line
    protected int[][] negOffsets;
    protected int yDiff;

    protected boolean fontChecked = false; //Whether the font has been checked or not

    //The alignment of the text
    protected int xAlignment;
    protected int yAlignment;

    public static final int ALIGNMENT_X_AS_LEFT = GL_Text.ALIGNMENT_X_AS_LEFT;
    public static final int ALIGNMENT_X_AS_RIGHT = GL_Text.ALIGNMENT_X_AS_RIGHT;
    public static final int ALIGNMENT_X_AS_CENTRE = GL_Text.ALIGNMENT_X_AS_CENTRE;
    public static final int ALIGNMENT_Y_AS_CENTRE = GL_Text.ALIGNMENT_Y_AS_CENTRE;
    public static final int ALIGNMENT_Y_AS_TRUE_CENTRE = GL_Text.ALIGNMENT_Y_AS_TRUE_CENTRE;
    public static final int ALIGNMENT_Y_AS_TOP = GL_Text.ALIGNMENT_Y_AS_TOP;
    public static final int ALIGNMENT_Y_AS_BOTTOM = GL_Text.ALIGNMENT_Y_AS_BOTTOM;
    public static final int ALIGNMENT_Y_AS_BOTTOM_LINE = GL_Text.ALIGNMENT_Y_AS_BOTTOM_LINE;

    public GL_Multiline_Text(GL_Multiline_Text txt) {
        super(txt);
        this.originalText = txt.originalText;
        this.textLines = txt.textLines.clone();
        this.font = txt.font;
        this.smallFont = txt.smallFont;
        this.useSmallFont = txt.useSmallFont;
        this.lineTextHeight = txt.lineTextHeight;
        this.lineHeightDiff = txt.lineHeightDiff;
        this.color = txt.color;
        this.autoFit = txt.autoFit;
        this.autoBoundsWidth = txt.autoBoundsWidth;
        this.autoBoundsHeight = txt.autoBoundsHeight;
        this.negOffsets = txt.negOffsets.clone();
        this.yDiff = txt.yDiff;
        this.fontChecked = txt.fontChecked;
        this.xAlignment = txt.xAlignment;
        this.yAlignment = txt.yAlignment;
        this.scale = txt.scale;
    }

    public GL_Multiline_Text(String text) {
        super(0,0);
        init(text, null, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, Color.BLACK);
    }

    public GL_Multiline_Text(String text, GL_Font font) {
        super(0,0);
        init(text, font, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, Color.BLACK);
    }

    public GL_Multiline_Text(String text, int color) {
        super(0,0);
        init(text, null, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, color);
    }

    public GL_Multiline_Text(String text, GL_Font font, int color) {
        super(0,0);
        init(text, font, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, color);
    }

    public GL_Multiline_Text(String text, int x, int y) {
        super(x,y);
        init(text, null, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, Color.BLACK);
    }

    public GL_Multiline_Text(String text, GL_Font font, int x, int y) {
        super(x,y);
        init(text, font, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, Color.BLACK);
    }

    public GL_Multiline_Text(String text, int x, int y, int color) {
        super(x,y);
        init(text, null, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, color);
    }

    public GL_Multiline_Text(String text, GL_Font font, int x, int y, int color) {
        super(x,y);
        init(text, font, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP, color);
    }

    public GL_Multiline_Text(String text, int x, int y, int xAlignment, int yAlignment) {
        super(x,y);
        init(text, null, xAlignment, yAlignment, Color.BLACK);
    }

    public GL_Multiline_Text(String text, GL_Font font, int x, int y, int xAlignment, int yAlignment) {
        super(x,y);
        init(text, font, xAlignment, yAlignment, Color.BLACK);
    }

    public GL_Multiline_Text(String text, int x, int y, int xAlignment, int yAlignment, int color) {
        super(x, y);
        init(text, null, xAlignment, yAlignment, color);
    }

    public GL_Multiline_Text(String text, GL_Font font, int x, int y, int xAlignment, int yAlignment, int color) {
        super(x, y);
        init(text, font, xAlignment, yAlignment, color);
    }

    private void init(String text, GL_Font font, int xAlignment, int yAlignment, int color) {
        this.originalText = text;
        this.textLines = text.split("\n");
        negOffsets = new int[2][textLines.length];
        this.font = font;
        this.xAlignment = xAlignment;
        this.yAlignment = yAlignment;
        this.color = color;
        if (font != null)
            lineTextHeight = font.getMaxTextHeight()-font.getMaxDescent();
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
        if (!fontChecked)
            checkFont();
        int maxXNegOffset = 0;
        for (int i=0; i<negOffsets[0].length; i++) {
            if (negOffsets[0][i] > maxXNegOffset) {
                 maxXNegOffset = negOffsets[0][i];
            }
        }
        int x = pos.getX()-maxXNegOffset;
        return pos.getX()-maxXNegOffset;
    }

    public int getYtop() {
        if (!fontChecked)
            checkFont();
        return pos.getY() + yDiff;

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
    public boolean collidesWithRectangle(int x, int y, int w, int h) {
        return GL_Shape.alignedRectangleRectangleCollision(x,y,w,h, getXleft(), getYtop(), width, height);
    }

    @Override
    protected boolean isPointInTexture(int x, int y) {
        return ((x >= getXleft() && x <= getXleft()+width)
                && (y >= getYtop() && y <= getYtop()+height));
    }

    @Override
    public void draw(float[] projectionMatrix) {
        if (isVisible()) {
            if (!fontChecked)
                checkFont();

            GL_Font usedFont = getUsedFont();

            for (int i=0; i<textLines.length; i++) {
                usedFont.drawText(textLines[i], pos.getX()-negOffsets[0][i], pos.getY()-negOffsets[1][i], color, projectionMatrix, scale);
            }
        }
    }

    private void checkFont() {
        if (!useSmallFont) {
            if (font == null) {
                try {
                    if (GL_Font.isDefaultFontSet()) { //Use default font
                        setNormalFont(GL_Font.getDefaultFont());
                    } else {
                        throw new FontNotSetException();
                    }
                } catch (FontNotSetException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (smallFont == null) {
                try {
                    if (GL_Font.isDefaultSmallFontSet()) { //Use default font
                        setSmallerFont(GL_Font.getDefaultSmallFont());
                    } else {
                        throw new FontNotSetException();
                    }
                } catch (FontNotSetException e) {
                    e.printStackTrace();
                }
            }
        }
        fontChecked = true;
    }

    private void calculateOffsets() {
        GL_Font calcFont = getUsedFont();

        if (calcFont == null)
            return;

        width = 0;
        for (int i=0; i<textLines.length; i++) { //calculate (max) width
            int tmpWidth = calcFont.getTextWidth(textLines[i]);
            if (tmpWidth > width)
                width = tmpWidth;
        }

        for (int i=0; i<textLines.length; i++) {
            int tmpWidth = calcFont.getTextWidth(textLines[i]);
            switch (xAlignment) {
                case ALIGNMENT_X_AS_LEFT:
                    negOffsets[0][i] = 0;
                    break;
                case ALIGNMENT_X_AS_RIGHT:
                    negOffsets[0][i] = tmpWidth;
                    break;
                default: //ALIGNMENT_X_AS_CENTRE
                    negOffsets[0][i] = tmpWidth/2;
                    break;
            }
        }

        //region Calculate height
        int[] first_h_data = calcFont.getTextHeight(textLines[0]);  //Height of first line
        if (textLines.length == 1) {
            height = first_h_data[0]; //just height
       } else {
            height = lineTextHeight - first_h_data[1]; //first
            height += (textLines.length-2)* lineTextHeight; //middle
            int[] last_h_data = calcFont.getTextHeight(textLines[textLines.length-1]);
            height += last_h_data[1] + last_h_data[0];
        }
        //endregion

        //region Calculate yDiff
        switch (yAlignment) {
            case ALIGNMENT_Y_AS_TOP:
                yDiff = 0;
                break;
            case ALIGNMENT_Y_AS_BOTTOM:
                yDiff = -height;
                break;
            case ALIGNMENT_Y_AS_TRUE_CENTRE:
                yDiff = -height/2;
                break;
            case ALIGNMENT_Y_AS_BOTTOM_LINE:
                int[] last_h_data = calcFont.getTextHeight(textLines[textLines.length-1]);
                int lastHeightPlusMax = calcFont.getMaxTextHeight() - calcFont.getMaxDescent();
                int lastHeightPlus = last_h_data[0] + last_h_data[1];
                yDiff = -height + Math.max(0, lastHeightPlus - lastHeightPlusMax);
                break;
            default: //ALIGNMENT_Y_AS_CENTRE
                yDiff = -1 * (textLines.length/2 * lineTextHeight - first_h_data[1] + calcFont.getMaxTextHeight() - calcFont.getMaxDescent() - calcFont.getNormalCapHeight()/2);
                if (textLines.length % 2 == 0) { //Even number of lines
                    yDiff += lineTextHeight /2;
                }
                break;
        }
        //endregion

        //region Set negOffsets
        for (int i = 0; i < textLines.length; i++) {
            negOffsets[1][i] = -1 * (yDiff - first_h_data[1] + i*lineTextHeight);
        }
        //endregion

        //region Scale appropriately
        width = (int) (width * scale);
        height = (int) (height * scale);
        for (int i=0; i<textLines.length; i++) {
            negOffsets[0][i] = (int) (negOffsets[0][i] * scale);
            negOffsets[1][i] = (int) (negOffsets[1][i] * scale);
        }
        yDiff = (int) (yDiff * scale);
        //endregion

        fontChecked = true;
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
        String text = "";
        for (int i=0; i<textLines.length; i++) {
            if(i != 0)
                text = text + "\n";
            text = text + textLines[i];
        }
        return text;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setText(String text) {
        this.originalText = text;
        this.textLines = text.split("\n");
        if (autoFit)
            calculateLines();
        calculateOffsets();
    }

    public GL_Font getUsedFont() {
        if (useSmallFont)
            return smallFont;
        else
            return font;
    }

    public GL_Font getNormalFont() {
        return font;
    }

    public void setNormalFont(GL_Font font) {
        this.font = font;
        lineTextHeight = font.getMaxTextHeight()-font.getMaxDescent();
        if (autoFit)
            calculateLines();
        calculateOffsets();
    }

    public void setSmallerFont(GL_Font smallerFont) {
        boolean fontWasSet = this.smallFont != null;
        this.smallFont = smallerFont;
        if (this.useSmallFont) {
            if (!fontWasSet)
                changeFont(true);
            if (autoFit)
                calculateLines();
            calculateOffsets();
        }
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

    public void activateAutofit(int maxWidth) {
        activateAutofit(maxWidth, null);
    }

    public void activateAutofit(int maxWidth, Integer maxHeight) {
        autoFit = true;
        autoBoundsWidth =  maxWidth;
        autoBoundsHeight = maxHeight;
        calculateLines();
        calculateOffsets();
    }

    private void calculateLines() {
        GL_Font calcFont = getUsedFont();

        if (calcFont == null)
            return;

        for (int i = 0; i < textLines.length; i++) {
            String nextLine = "";
            while (calcFont.getTextWidth(textLines[i]) > autoBoundsWidth) {
                boolean inclusiveDelimiter = false;
                char[] delimiters = {' ', '-', '‐', '–', '/', '\\', '_', ':', '=', ';', ',', '.', '&', '+'};
                char delimiter = ' ';
                int index = -1;
                for (int di = 0; di<delimiters.length; di++) {
                    delimiter = delimiters[di];
                    if(di > 0)
                        inclusiveDelimiter = true;
                    index = textLines[i].substring(0,textLines[i].length()-1).lastIndexOf(delimiter);
                    if(index > 0) {
                        break;
                    } else {
                        index = -1;
                    }
                }
                if (index >= 0 ) { //There is at least one whitespace
                    if(index != textLines[i].length()-1) { //the whitespace is not the last char in the line
                        if(!TextUtils.isEmpty(nextLine) && !inclusiveDelimiter) {
                            nextLine = ' ' + nextLine; //Add whitespace if not first word
                        }

                        nextLine = textLines[i].substring(index+1, textLines[i].length()) + nextLine;

                        if (inclusiveDelimiter) {
                            textLines[i] = textLines[i].substring(0,index+1);
                        } else {
                            textLines[i] = textLines[i].substring(0,index);
                        }

                    }
                } else { //word is too long for boundaries
                    String subString = textLines[i];
                    for (index = textLines[i].length()-1; index >= 1; index--) {
                        subString = textLines[i].substring(0, index);
                        if(calcFont.getTextWidth(subString) <= autoBoundsWidth || subString.length() <= 1)
                            break;
                    }
                    if(index < textLines[i].length()) {
                        if(!TextUtils.isEmpty(nextLine)) {
                            nextLine = " " + nextLine; //Add whitespace if not first word
                        }
                        nextLine = textLines[i].substring(index, textLines[i].length()) + nextLine;
                    }

                    textLines[i] = subString;
                    break;
                }
            }
            if (!TextUtils.isEmpty(nextLine)) {
                addTextLine(nextLine, i+1); //also calls calculateOffsets
            }

            if (autoBoundsHeight != null && height > autoBoundsHeight && useSmallFont == false &&
                    (smallFont != null || GL_Font.isDefaultSmallFontSet())) { //try to use a smaller font if too high
                this.textLines = originalText.split("\n"); //restore original String
                if (smallFont == null && GL_Font.isDefaultSmallFontSet())
                    setSmallerFont(GL_Font.getDefaultSmallFont());
                useSmallerFont(true);
                calculateLines();
                return;
            }
        }
    }

    public void addTextLine(String text) {
        addTextLine(text, textLines.length);
        originalText = originalText + "\n" + text;
        calculateOffsets();
    }

    private void addTextLine(String text, int pos) {
        if (textLines != null) {
            String[] newTextLines = new String[textLines.length+1];
            int index = 0;
            for (int i=0; i<textLines.length; i++) {
                if (i == pos) {
                    newTextLines[i] = text;
                } else {
                    newTextLines[i] = textLines[index];
                    index++;
                }
            }
            if (pos == textLines.length) {
                newTextLines[textLines.length] = text;
            } else {
                newTextLines[textLines.length] = textLines[textLines.length-1];
            }
            textLines = newTextLines;
        } else {
            textLines = new String[1];
            textLines[0] = text;
        }
        negOffsets = new int[2][textLines.length];
        calculateOffsets();
    }

    public void useSmallerFont(boolean useSmallFont) {
        if(smallFont != null && this.useSmallFont == false && useSmallFont == true) {
            changeFont(true);
        } else if (smallFont != null && this.useSmallFont == true && useSmallFont == false) {
            changeFont(false);
        }
        this.useSmallFont = useSmallFont;
    }

    private void changeFont(boolean toSmallFont) {
        this.useSmallFont = toSmallFont;

        //Change line height proportional
        int newLineTextHeight = getUsedFont().getMaxTextHeight()-getUsedFont().getMaxDescent();
        lineHeightDiff = lineHeightDiff * newLineTextHeight  / (lineTextHeight-lineHeightDiff);
        lineTextHeight = newLineTextHeight + lineHeightDiff;

        calculateOffsets();
    }

    public int getLineTextHeight() {
        return lineTextHeight;
    }

    public void setLineTextHeight(int lineTextHeight) {
        this.lineHeightDiff += lineTextHeight - this.lineTextHeight; //save difference
        this.lineTextHeight = lineTextHeight;
    }

    public void increaseLineTextHeight(int value) {
        this.lineHeightDiff += value; //save difference
        this.lineTextHeight += value;
    }

    public void decreaseLineTextHeight(int value) {
        this.lineHeightDiff -= value; //save difference
        this.lineTextHeight -= value;
    }

    public int getLineCount() {
        return textLines.length;
    }

    public Integer getAutoBoundsWidth() {
        return autoBoundsWidth;
    }

    public Integer getAutoBoundsHeight() {
        return autoBoundsHeight;
    }

    @Override
    public int getHeight() {
        if (!fontChecked)
            checkFont();
        return super.getHeight();
    }

    @Override
    public int getWidth() {
        if (!fontChecked)
            checkFont();
        return super.getWidth();
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        calculateOffsets();
    }
}
