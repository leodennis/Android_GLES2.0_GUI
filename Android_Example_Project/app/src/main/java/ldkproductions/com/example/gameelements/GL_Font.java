package ldkproductions.com.example.gameelements;

import android.content.Context;

import ldkproductions.com.example.data.FontAtlas;

import java.util.ArrayList;

/**
 * Created by LDK-Productions on 17.11.2015.
 */
public class GL_Font {
    protected ArrayList<FontAtlas> fontAtlasList = new ArrayList<FontAtlas>();

    protected Context context;

    protected int spaceBetweenChars;
    protected int whitespaceWidth;
    protected int maxTextHeight;
    protected int maxDescent;
    protected int normalCapHeight;

    private static GL_Font defaultFont = null;
    private static GL_Font defaultSmallFont = null;

    public static GL_Font savedFont1 = null;
    public static GL_Font savedFont2 = null;
    public static GL_Font savedFont3 = null;

    public GL_Font(Context context) {
        this.context = context;

        initFont();
    }

    public void drawText(String text, int x, int y, int color, float[] projectionMatrix, float scale) {
        for (int i = 0; i < text.length(); i++) {
            x += drawChar(text.charAt(i), x, y, color, projectionMatrix, i == text.length()-1, scale); // + 2*spaceBetweenChars;
        }
    }

    public int getTextWidth(String text) {
        int w = 0;
        for (int i = 0; i < text.length(); i++) {
            w += getLetterWidth(text.charAt(i), i == text.length()-1); // + 2*spaceBetweenChars;
        }
        return w;
    }

    /**
     *  A method to calculate the height consumption af the text
     *
     * @param text The string to be displayed
     * @return int[2] A two dimensional int array where [0] is the max height of the text and
     * [1] is the unused space over it
     */
    public int[] getTextHeight(String text) {
        if (text.matches("\\s*")) {
            int normalTopY = maxTextHeight-maxDescent-normalCapHeight;
            int[] whitespaceHeight = {normalCapHeight, normalTopY};
            return whitespaceHeight;
        }
        int[] textHeight = {0, 0};
        int topY = maxTextHeight;
        int bottomY = 0;
        for(char c : text.toCharArray()) {
            int[] h = getLetterHeight(c);
            if(h != null) {
                if(h[0]+h[1] > bottomY)
                    bottomY = h[0]+h[1];
                if(h[1] < topY)
                    topY = h[1];
            }
        }
        textHeight[0] = bottomY - topY;
        textHeight[1] = topY;
        return textHeight;
    }

    private int getLetterWidth(char c, boolean lastInLine) {
        if(c == ' ')
            return whitespaceWidth;
        for (FontAtlas atlas : fontAtlasList) {
            int w = atlas.getLetterWidth(c, lastInLine);
            if(w > 0)
                return w;
        }
        return 0;
    }

    private int[] getLetterHeight(char c) {
        if(c == ' ')
            return null;
        for (FontAtlas atlas : fontAtlasList) {
            int[] h = atlas.getLetterHeight(c);
            if(h != null)
                return h;
        }
        return null;
    }

    private int drawChar(char c, int x, int y, int color, float[] projectionMatrix, boolean lastInLine, float scale) {
        if(c == ' ')
            return whitespaceWidth;
        if(c == '\t')
            return 8 * whitespaceWidth;
        for (FontAtlas atlas : fontAtlasList) {
            int w = atlas.drawLetter(c, x, y, color, projectionMatrix, lastInLine, scale);
                if (w > 0)
                    return w;
        }
        return 0;
    }

    public int getMaxTextHeight() {
        return maxTextHeight;
    }

    public int getMaxDescent() {
        return maxDescent;
    }

    public int getNormalCapHeight() {
        return normalCapHeight;
    }

    public int getNormalTextHeight() {
        return maxTextHeight-maxDescent;
    }

    public static GL_Font getDefaultFont() {
        return GL_Font.defaultFont;
    }

    public static void setDefaultFont(GL_Font defaultFont) {
        GL_Font.defaultFont = defaultFont;
    }

    public static boolean isDefaultFontSet() {
        return GL_Font.defaultFont != null;
    }

    public static GL_Font getDefaultSmallFont() {
        return GL_Font.defaultSmallFont;
    }

    public static void setDefaultSmallFont(GL_Font defaultSmallFont) {
        GL_Font.defaultSmallFont = defaultSmallFont;
    }

    public static boolean isDefaultSmallFontSet() {
        return GL_Font.defaultSmallFont != null;
    }

    protected void initFont() {
        maxTextHeight = 84;
        whitespaceWidth = 14;
        spaceBetweenChars = 4;

    }
}
