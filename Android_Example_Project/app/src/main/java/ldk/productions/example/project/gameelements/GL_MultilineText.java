package ldk.productions.example.project.gameelements;

import android.text.TextUtils;

import ldk.productions.example.project.exceptions.FontNotSetException;

import static ldk.productions.example.project.gameelements.GL_TextAlignment.ALIGNMENT_X_AS_LEFT;
import static ldk.productions.example.project.gameelements.GL_TextAlignment.ALIGNMENT_Y_AS_TOP;

/**
 * An OpenGL ES 2.0 multiline text label.
 *
 * Created by Leo on 26.11.2015.
 */
public class GL_MultilineText extends GL_Text {

    protected String[] textLines;   // the text split up into lines

    // a smaller font if the text does not fit using the normal font
    protected GL_Font smallFont = null;
    protected boolean useSmallFont = false;
    protected boolean autoChoseSmallFont = false;

    protected int lineTextHeight;   // the height of a single line
    protected int lineHeightDiff;   // saves how much the height of a text line was increased/decreased

    // fit the text automatically to a bounding box
    protected boolean autoFit = false;
    protected Integer autoBoundsWidth = null;
    protected Integer autoBoundsHeight = null;

    // used to specify where to draw each line
    protected int[][] negOffsets;
    protected int yDiff;

    protected static final char[] DELIMITERS = {' ', '-', '‐', '–', '/', '\\', '_', ':', '=', ';', ',', '.', '&', '+'};

    public GL_MultilineText(int x, int y, String text) {
        this(x, y, text, GL_Font.getDefaultFont(), ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP);
    }

    public GL_MultilineText(int x, int y, String text, GL_Font font) {
        this(x, y, text, font, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP);
    }

    public GL_MultilineText(int x, int y, String text, GL_TextAlignment xAlignment, GL_TextAlignment yAlignment) {
        this(x, y, text, GL_Font.getDefaultFont(), xAlignment, yAlignment);
    }

    public GL_MultilineText(int x, int y, String text, GL_Font font, GL_TextAlignment xAlignment, GL_TextAlignment yAlignment) {
        super(x,y, text, font, xAlignment, yAlignment);
        calculateOffsets();
    }

    /**
     * Copy constructor
     * @param obj the GL_Multiline_Text to be copied
     */
    public GL_MultilineText(GL_MultilineText obj) {
        super(obj);
        this.textLines = obj.textLines.clone();
        this.smallFont = obj.smallFont;
        this.useSmallFont = obj.useSmallFont;
        this.autoChoseSmallFont = obj.autoChoseSmallFont;
        this.lineTextHeight = obj.lineTextHeight;
        this.lineHeightDiff = obj.lineHeightDiff;
        this.autoFit = obj.autoFit;
        this.autoBoundsWidth = obj.autoBoundsWidth;
        this.autoBoundsHeight = obj.autoBoundsHeight;
        this.negOffsets = obj.negOffsets.clone();
        this.yDiff = obj.yDiff;
    }

    @Override
    public Object clone() {
        return new GL_MultilineText(this);
    }

    @Override
    protected void init() {
        if (this.text == null) {
            this.text = "";
        }

        this.textLines = text.split("\n");
        negOffsets = new int[2][textLines.length];

        if (font != null) {
            lineTextHeight = font.getMaxTextHeight() - font.getMaxDescent();
        }
    }

    @Override
    public void draw(float[] projectionMatrix) {
        if (visible) {
            GL_Font usedFont = getUsedFont();

            if (usedFont == null) throw new FontNotSetException();

            for (int i = 0; i < textLines.length; i++) {
                usedFont.drawText(textLines[i], pos.getX()-negOffsets[0][i], pos.getY()-negOffsets[1][i], color, projectionMatrix, scale);
            }
        }
    }

    @Override
    protected void calculateOffsets() {
        // TODO: This function needs some refactoring as it is too long and complicated

        GL_Font calcFont = getUsedFont();

        if (calcFont == null) return;

        width = 0;
        for (int i = 0; i < textLines.length; i++) { // calculate (max) width
            int tmpWidth = calcFont.getTextWidth(textLines[i]);
            if (tmpWidth > width) {
                width = tmpWidth;
            }
        }

        for (int i = 0; i < textLines.length; i++) {
            int tmpWidth = calcFont.getTextWidth(textLines[i]);
            switch (xAlignment) {
                case ALIGNMENT_X_AS_LEFT:
                    negOffsets[0][i] = 0;
                    break;
                case ALIGNMENT_X_AS_RIGHT:
                    negOffsets[0][i] = tmpWidth;
                    break;
                case ALIGNMENT_X_AS_CENTER:
                    negOffsets[0][i] = tmpWidth/2;
                    break;
                default:
                    throw new RuntimeException("Unknown text alignment for x: " + xAlignment);
            }
        }

        //region Calculate height
        int[] first_h_data = calcFont.getTextHeight(textLines[0]);  // height of first line
        if (textLines.length == 1) {
            height = first_h_data[0]; // just height
        } else {
            height = lineTextHeight - first_h_data[1]; // first
            height += (textLines.length-2) * lineTextHeight; // middle
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
            case ALIGNMENT_Y_AS_TRUE_CENTER:
                yDiff = -height/2;
                break;
            case ALIGNMENT_Y_AS_BOTTOM_LINE:
                int[] last_h_data = calcFont.getTextHeight(textLines[textLines.length-1]);
                int lastHeightPlusMax = calcFont.getMaxTextHeight() - calcFont.getMaxDescent();
                int lastHeightPlus = last_h_data[0] + last_h_data[1];
                yDiff = -height + Math.max(0, lastHeightPlus - lastHeightPlusMax);
                break;
            case ALIGNMENT_Y_AS_CENTER:
                yDiff = -1 * (textLines.length/2 * lineTextHeight - first_h_data[1] + calcFont.getMaxTextHeight() - calcFont.getMaxDescent() - calcFont.getNormalCapHeight()/2);
                if (textLines.length % 2 == 0) { // even number of lines
                    yDiff += lineTextHeight /2;
                }
                break;
            default:
                throw new RuntimeException("Unknown text alignment for x: " + xAlignment);
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
        for (int i = 0; i < textLines.length; i++) {
            negOffsets[0][i] = (int) (negOffsets[0][i] * scale);
            negOffsets[1][i] = (int) (negOffsets[1][i] * scale);
        }
        yDiff = (int) (yDiff * scale);
        //endregion
    }

    @Override
    public int getXleft() {
        int maxXNegOffset = 0;
        for (int i = 0; i < negOffsets[0].length; i++) {
            if (negOffsets[0][i] > maxXNegOffset) {
                 maxXNegOffset = negOffsets[0][i];
            }
        }

        return pos.getX() - maxXNegOffset;
    }

    @Override
    public int getYtop() {
        return pos.getY() + yDiff;
    }

    private void checkFont() {
        if (!useSmallFont) {
            if (font == null) {
                if (GL_Font.isDefaultFontSet()) { // use default font
                    setFont(GL_Font.getDefaultFont());
                } else {
                    throw new FontNotSetException();
                }
            }
        } else {
            if (smallFont == null) {
                if (GL_Font.isDefaultSmallFontSet()) { // use default font
                    setSmallerFont(GL_Font.getDefaultSmallFont());
                } else {
                    throw new FontNotSetException();
                }
            }
        }
    }

    @Override
    public int getMiddleX() {
        return getXleft() + width/2;
    }
    @Override
    public int getMiddleY() {
        return getYtop() + height/2;
    }

    public String getFormattedText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textLines.length; i++) {
            if (i != 0) {
                sb.append("\n");
            }
            sb.append(textLines[i]);
        }
        return sb.toString();
    }

    @Override
    public void setText(String text) {
        this.text = text;
        this.textLines = text.split("\n");
        negOffsets = new int[2][textLines.length];
        if (autoFit) {
            calculateLines();
        }
        calculateOffsets();
    }

    public GL_Font getUsedFont() {
        if (useSmallFont) {
            return smallFont;
        }
        return font;
    }

    @Override
    public void setFont(GL_Font font) {
        this.font = font;
        lineTextHeight = font.getMaxTextHeight()-font.getMaxDescent();
        if (autoFit) {
            calculateLines();
        }
        calculateOffsets();
    }

    public void setSmallerFont(GL_Font smallerFont) {
        boolean fontWasSet = this.smallFont != null;
        this.smallFont = smallerFont;
        if (this.useSmallFont) {
            if (!fontWasSet) {
                changeFont(true);
            }
            if (autoFit) {
                calculateLines();
            }
            calculateOffsets();
        }
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
        calculateLines(true);
    }

    private void calculateLines(boolean restAutoChoseSmallFont) {
        // TODO: This function needs some refactoring as it is too long and complicated

        if (restAutoChoseSmallFont && autoChoseSmallFont) {
            autoChoseSmallFont = false;
            useSmallerFont(false);
        }
        GL_Font calcFont = getUsedFont();

        if (calcFont == null) return;

        for (int i = 0; i < textLines.length; i++) {
            String nextLine = "";
            while (calcFont.getTextWidth(textLines[i]) > autoBoundsWidth) {
                boolean inclusiveDelimiter = false;
                char delimiter = ' ';
                int index = -1;

                for (int di = 0; di< DELIMITERS.length; di++) {
                    delimiter = DELIMITERS[di];
                    if (di > 0) {
                        inclusiveDelimiter = true;
                    }
                    index = textLines[i].substring(0, textLines[i].length()-1).lastIndexOf(delimiter);
                    if (index > 0) {
                        break;
                    } else {
                        index = -1;
                    }
                }
                if (index >= 0 ) { // there is at least one whitespace
                    if (index != textLines[i].length()-1) { // the whitespace is not the last char in the line
                        if(!TextUtils.isEmpty(nextLine) && !inclusiveDelimiter) {
                            nextLine = ' ' + nextLine; // add whitespace if not first word
                        }

                        nextLine = textLines[i].substring(index+1) + nextLine;

                        if (inclusiveDelimiter) {
                            textLines[i] = textLines[i].substring(0,index+1);
                        } else {
                            textLines[i] = textLines[i].substring(0,index);
                        }

                    }
                } else { // word is too long for boundaries
                    String subString = textLines[i];
                    for (index = textLines[i].length()-1; index >= 1; index--) {
                        subString = textLines[i].substring(0, index);
                        if (calcFont.getTextWidth(subString) <= autoBoundsWidth || subString.length() <= 1) {
                            break;
                        }
                    }
                    if (index < textLines[i].length()) {
                        if (!TextUtils.isEmpty(nextLine)) {
                            nextLine = " " + nextLine; // add whitespace if not first word
                        }
                        nextLine = textLines[i].substring(index) + nextLine;
                    }

                    textLines[i] = subString;
                    break;
                }
            }
            if (!TextUtils.isEmpty(nextLine)) {
                addTextLine(nextLine, i+1); //also calls calculateOffsets
            }

            if (autoBoundsHeight != null && height > autoBoundsHeight && useSmallFont == false &&
                    (smallFont != null || GL_Font.isDefaultSmallFontSet())) { // try to use a smaller font if too high
                this.textLines = text.split("\n"); // restore original String
                if (smallFont == null && GL_Font.isDefaultSmallFontSet()) {
                    setSmallerFont(GL_Font.getDefaultSmallFont());
                }
                autoChoseSmallFont = true;
                useSmallerFont(true);
                calculateLines(false);
                return;
            }
        }
    }

    public void addTextLine(String text) {
        addTextLine(text, textLines.length);
        this.text = this.text + "\n" + text;
        calculateOffsets();
    }

    private void addTextLine(String text, int pos) {
        if (textLines != null) {
            String[] newTextLines = new String[textLines.length+1];
            int index = 0;

            for (int i = 0; i < textLines.length; i++) {
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
        if (smallFont != null && this.useSmallFont == false && useSmallFont == true) {
            changeFont(true);
        } else if (smallFont != null && this.useSmallFont == true && useSmallFont == false) {
            changeFont(false);
        }
        this.useSmallFont = useSmallFont;
    }

    private void changeFont(boolean toSmallFont) {
        this.useSmallFont = toSmallFont;

        // change line height proportional
        int newLineTextHeight = getUsedFont().getMaxTextHeight()-getUsedFont().getMaxDescent();
        lineHeightDiff = lineHeightDiff * newLineTextHeight  / (lineTextHeight-lineHeightDiff);
        lineTextHeight = newLineTextHeight + lineHeightDiff;

        calculateOffsets();
    }

    public int getLineTextHeight() {
        return lineTextHeight;
    }

    public void setLineTextHeight(int lineTextHeight) {
        this.lineHeightDiff += lineTextHeight - this.lineTextHeight; // save difference
        this.lineTextHeight = lineTextHeight;
        calculateOffsets();
    }

    public void increaseLineTextHeight(int value) {
        this.lineHeightDiff += value; // save difference
        this.lineTextHeight += value;
        calculateOffsets();
    }

    public void decreaseLineTextHeight(int value) {
        this.lineHeightDiff -= value; // save difference
        this.lineTextHeight -= value;
        calculateOffsets();
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

}
