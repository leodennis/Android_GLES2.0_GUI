package ldkproductions.com.example.gameelements;

import android.content.Context;
import android.graphics.Color;

import ldkproductions.com.example.data.CGPoint;
import ldkproductions.com.example.objects.GL_Background_Button;
import ldkproductions.com.example.objects.GL_Texture;
import ldkproductions.com.example.util.TextureHelper;

/**
 * Created by Leo on 13.11.2015.
 */
public class GL_Button extends GL_Texture {

    protected GL_Multiline_Text text = null;
    protected int textColor = Color.BLACK;

    protected int textureNormal = 0;
    protected int texturePressed = 0;

    protected boolean useTextureBackground = false;

    protected GL_Background_Button backgroundNormal = null;
    protected GL_Background_Button backgroundPressed = null;

    protected static final int DEFAULT_BUTTON_COLOR = Color.rgb(205, 205, 205);

    /**
     * Copy constructor
     * @param but the button to be copied
     */
    public GL_Button(GL_Button but) {
        super(but);
        this.text = new GL_Multiline_Text(but.text);
        this.textColor = but.textColor;
        this.textureNormal = but.textureNormal;
        this.texturePressed = but.texturePressed;
        this.useTextureBackground = but.useTextureBackground;
        this.backgroundNormal = but.backgroundNormal;
        this.backgroundPressed = but.backgroundPressed;
    }

    protected GL_Button(CGPoint pos) {
        super(pos.getX(), pos.getY());
    }

    public GL_Button() {
        super(0, 0);
        init(null, 0, 0, DEFAULT_BUTTON_COLOR, 20, 10, null, null, null);
    }

    public GL_Button(Integer backgroundColor) {
        super(0, 0);
        init(null, 0, 0, backgroundColor, 20, 10, null, null, null);
    }

    public GL_Button(int width, int height) {
        super(0, 0);
        init(null, 0, 0, DEFAULT_BUTTON_COLOR, width, height, null, null, null);
    }

    public GL_Button(Integer backgroundColor, int width, int height) {
        super(0, 0);
        init(null, 0, 0, backgroundColor, width, height, null, null, null);
    }

    public GL_Button(int width, int height, int x, int y) {
        super(x, y);
        init(null, 0, 0, DEFAULT_BUTTON_COLOR, width, height, null, null, null);
    }

    public GL_Button(Integer backgroundColor, int width, int height, int x, int y) {
        super(x, y);
        init(null, 0, 0, backgroundColor, width, height, null, null, null);
    }

    public GL_Button(int width, int height, String text) {
        super(0, 0);
        init(null, 0, 0, DEFAULT_BUTTON_COLOR, width, height, text, null, null);
    }

    public GL_Button(Integer backgroundColor, int width, int height, String text) {
        super(0, 0);
        init(null, 0, 0, backgroundColor, width, height, text, null, null);
    }

    public GL_Button(int width, int height, String text, int x, int y) {
        super(x, y);
        init(null, 0, 0, DEFAULT_BUTTON_COLOR, width, height, text, null, null);
    }

    public GL_Button(Integer backgroundColor, int width, int height, String text, int x, int y) {
        super(x, y);
        init(null, 0, 0, backgroundColor, width, height, text, null, null);
    }

    public GL_Button(int width, int height, String text, GL_Font font) {
        super(0, 0);
        init(null, 0, 0, DEFAULT_BUTTON_COLOR, width, height, text, font, null);
    }

    public GL_Button(Integer backgroundColor, int width, int height, String text, GL_Font font) {
        super(0, 0);
        init(null, 0, 0, backgroundColor, width, height, text, font, null);
    }

    public GL_Button(int width, int height, String text, GL_Font font, GL_Font smallFont) {
        super(0, 0);
        init(null, 0, 0, DEFAULT_BUTTON_COLOR, width, height, text, font, smallFont);
    }

    public GL_Button(Integer backgroundColor, int width, int height, String text, GL_Font font, GL_Font smallFont) {
        super(0, 0);
        init(null, 0, 0, backgroundColor, width, height, text, font, smallFont);
    }

    public GL_Button(int width, int height, String text, GL_Font font, int x, int y) {
        super(x, y);
        init(null, 0, 0, DEFAULT_BUTTON_COLOR, width, height, text, font, null);
    }

    public GL_Button(Integer backgroundColor, int width, int height, String text, GL_Font font, int x, int y) {
        super(x, y);
        init(null, 0, 0, backgroundColor, width, height, text, font, null);
    }

    public GL_Button(int width, int height, String text, GL_Font font, GL_Font smallFont, int x, int y) {
        super(x, y);
        init(null, 0, 0, DEFAULT_BUTTON_COLOR, width, height, text, font, smallFont);
    }

    public GL_Button(Integer backgroundColor, int width, int height, String text, GL_Font font, GL_Font smallFont, int x, int y) {
        super(x, y);
        init(null, 0, 0, backgroundColor, width, height, text, font, smallFont);
    }

    public GL_Button(Context context, int resourceId) {
        super(0, 0);
        init(context, resourceId, 0, null, 20, 10, null, null, null);
    }

    public GL_Button(Context context, int resourceId, int resourceId_pressed) {
        super(0, 0);
        init(context, resourceId, resourceId_pressed, null, 20, 10, null, null, null);
    }

    public GL_Button(Context context, int resourceId, int resourceId_pressed, int width, int height) {
        super(0, 0);
        init(context, resourceId, resourceId_pressed, null, width, height, null, null, null);
    }

    public GL_Button(Context context, int resourceId, int resourceId_pressed, int width, int height, int x, int y) {
        super(x, y);
        init(context, resourceId, resourceId_pressed, null, width, height, null, null, null);
    }

    public GL_Button(Context context, int resourceId, int resourceId_pressed, int width, int height, String text) {
        super(0, 0);
        init(context, resourceId, resourceId_pressed, null, width, height, text, null, null);
    }

    public GL_Button(Context context, int resourceId, int resourceId_pressed, int width, int height, String text, int x, int y) {
        super(x, y);
        init(context, resourceId, resourceId_pressed, null, width, height, text, null, null);
    }

    public GL_Button(Context context, int resourceId, int resourceId_pressed, int width, int height, String text, GL_Font font) {
        super(0, 0);
        init(context, resourceId, resourceId_pressed, null, width, height, text, font, null);
    }

    public GL_Button(Context context, int resourceId, int resourceId_pressed, int width, int height, String text, GL_Font font, GL_Font smallFont) {
        super(0, 0);
        init(context, resourceId, resourceId_pressed, null, width, height, text, font, smallFont);
    }

    public GL_Button(Context context, int resourceId, int resourceId_pressed, int width, int height, String text, GL_Font font, int x, int y) {
        super(x, y);
        init(context, resourceId, resourceId_pressed, null, width, height, text, font, null);
    }

    public GL_Button(Context context, int resourceId, int resourceId_pressed, int width, int height, String text, GL_Font font, GL_Font smallFont, int x, int y) {
        super(x, y);
        init(context, resourceId, resourceId_pressed, null, width, height, text, font, smallFont);
    }

    protected void init(Context context, int resourceId, int resourceId_pressed,
                        Integer backgroundColor,
                        int width, int height,
                        String text, GL_Font font, GL_Font smallFont) {

        this.width = width;
        this.height = height;
        if (backgroundColor == null) {
            setBackgroundTexture(context, resourceId, resourceId_pressed);
        } else {
            initColor(backgroundColor);
        }

        if (text != null)
            initText(text, font, smallFont);
    }

    protected void initText(String text, GL_Font font, GL_Font smallFont) {
        initText(text, font, smallFont, width, height);
    }

    protected void initText(String text, GL_Font font, GL_Font smallFont, int bounds_width, int bounds_height) {
        if (this.text == null) {
            this.text = new GL_Multiline_Text(text, font, this.getMiddleX(), this.getMiddleY(),
                    GL_Multiline_Text.ALIGNMENT_X_AS_CENTRE, GL_Multiline_Text.ALIGNMENT_Y_AS_CENTRE);
        } else {
            if (text != null)
                this.text.setText(text);
            if (font != null)
                this.text.setNormalFont(font);
        }

        this.setTextColor(textColor);
        if (smallFont != null)
            this.text.setSmallerFont(smallFont);
        this.text.activateAutofit(bounds_width, bounds_height);
    }

    protected void initColor(int color) {
        backgroundNormal = new GL_Background_Button(pos, width, height, color);
        backgroundPressed = new GL_Background_Button(pos, width, height, GL_Background_Button.getPressedColor(color));
        useTextureBackground = false;
    }

    public void setBackgroundTexture(Context context, int resourceId, int resourceId_pressed) {
        initTexture(context, width, height, resourceId);

        textureNormal = texture;
        if (resourceId_pressed != 0)
            texturePressed = TextureHelper.loadTexture(context, resourceId_pressed).getId();
        useTextureBackground = true;
    }


    public  void setText(GL_Multiline_Text text) {
        this.text = text;
    }

    public  void setText(String text) {
        setText(text, null, null);
    }

    public  void setText(String text, GL_Font font) {
        setText(text, font, null);
    }

    public void setText(String text, GL_Font font, GL_Font smallFont) {
        initText(text, font, smallFont);
    }

    /**
     * Changes the used font of the text.
     * @param font the normal font
     * @param smallFont @nullable the small font
     */
    public void setFont(GL_Font font, GL_Font smallFont) {
        initText(this.text.getOriginalText(), font, smallFont);
    }

    public String getText() {
        return text.getOriginalText();
    }

    public GL_Multiline_Text getTextView() {
        return this.text;
    }

    @Override
    public boolean checkTouchDown(int x, int y) {
        boolean state =  super.checkTouchDown(x, y);
        if(state && texturePressed != 0)
            if (useTextureBackground)
                changeToTexturePressed();

        return state;
    }

    protected void changeToTexturePressed() {
        texture = texturePressed;
    }

    @Override
    public boolean checkTouchUp(int x, int y) {
        boolean state =  super.checkTouchUp(x, y);
        if (useTextureBackground)
            changeToTextureNormal();
        return state;
    }

    protected void changeToTextureNormal() {
        texture = textureNormal;
    }

    @Override
    public void cancelTouch() {
        super.cancelTouch();
        if (useTextureBackground)
            changeToTextureNormal();
    }

    @Override
    public void resetTouch() {
        super.resetTouch();
        if (useTextureBackground)
            changeToTextureNormal();
    }

    @Override
    public void draw(float[] projectionMatrix) {
        if (useTextureBackground) {
            super.draw(projectionMatrix);
        } else if(visible) {
            if (clickState == CLICK_STATE_DOWN)
                backgroundPressed.draw(projectionMatrix);
            else
                backgroundNormal.draw(projectionMatrix);
        }

        if(visible && text != null)
            text.draw(projectionMatrix);
    }

    public Integer getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        text.setColor(textColor);
    }

    public void setBackgroundColor(int backgroundColor) {
        initColor(backgroundColor);
    }

    @Override
    public void setPosX(int x) {
        super.setPosX(x);
        if (backgroundNormal != null) {
            backgroundNormal.setPosX(x);
        }
        if (backgroundPressed != null) {
            backgroundPressed.setPosX(x);
        }
        if (text != null) {
            text.setPosX(this.getMiddleX());
        }
    }

    @Override
    public void setPosY(int y) {
        super.setPosY(y);
        if (backgroundNormal != null) {
            backgroundNormal.setPosY(y);
        }
        if (backgroundPressed != null) {
            backgroundPressed.setPosY(y);
        }
        if (text != null) {
            text.setPosY(this.getMiddleY());
        }
    }

    @Override
    public void resize(int width, int height) {
        if (useTextureBackground) {
            super.resize(width, height);
        } else {
            this.width = width;
            this.height = height;
            backgroundNormal = new GL_Background_Button(pos, width, height, backgroundNormal.getColor());
            backgroundPressed = new GL_Background_Button(pos, width, height,backgroundPressed.getColor());
        }
    }

}
