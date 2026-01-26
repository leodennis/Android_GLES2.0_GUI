package ldk.productions.example.project.gameelements;

import android.content.Context;
import android.graphics.Color;

import ldk.productions.example.project.data.CGPoint;
import ldk.productions.example.project.objects.GL_Background_Button;
import ldk.productions.example.project.objects.GL_Texture;
import ldk.productions.example.project.util.PowOfTwoTex;
import ldk.productions.example.project.util.TextureHelper;

import static ldk.productions.example.project.gameelements.GL_TextAlignment.ALIGNMENT_X_AS_CENTER;
import static ldk.productions.example.project.gameelements.GL_TextAlignment.ALIGNMENT_Y_AS_CENTER;

/**
 * Created by Leo on 13.11.2015.
 */
public class GL_Button extends GL_Texture {

    protected GL_MultilineText text = null;
    protected int textColor = Color.BLACK;

    protected PowOfTwoTex textureNormal;
    protected PowOfTwoTex texturePressed;

    protected boolean useTextureBackground = false;

    protected GL_Background_Button backgroundNormal = null;
    protected GL_Background_Button backgroundPressed = null;

    protected static final int DEFAULT_BUTTON_COLOR = Color.rgb(205, 205, 205);

    protected GL_Button(CGPoint pos) {
        super(pos.getX(), pos.getY());
    }

    public GL_Button(int x, int y, int width, int height) {
        this(x, y, width, height, DEFAULT_BUTTON_COLOR);
    }

    public GL_Button(int x, int y, int width, int height, Integer backgroundColor) {
        super(x, y);

        this.width = width;
        this.height = height;

        initColor(backgroundColor);
    }

    public GL_Button(int x, int y, int width, int height, Context context, int resourceId, int resourceId_pressed) {
        super(x, y);

        this.width = width;
        this.height = height;

        setBackgroundTexture(context, resourceId, resourceId_pressed);
    }

    /**
     * Copy constructor
     * @param obj the GL_Button to be copied
     */
    public GL_Button(GL_Button obj) {
        super(obj);
        this.text = new GL_MultilineText(obj.text);
        this.textColor = obj.textColor;
        this.textureNormal = obj.textureNormal;
        this.texturePressed = obj.texturePressed;
        this.useTextureBackground = obj.useTextureBackground;
        this.backgroundNormal = obj.backgroundNormal;
        this.backgroundPressed = obj.backgroundPressed;
    }

    @Override
    public Object clone() {
        return new GL_Button(this);
    }

    protected void initText(String text, GL_Font font, GL_Font smallFont) {
        initText(text, font, smallFont, width, height);
    }

    protected void initText(String text, GL_Font font, GL_Font smallFont, int bounds_width, int bounds_height) {
        if (this.text == null) {
            if (font == null) {
                font = GL_Font.getDefaultFont();
            }
            this.text = new GL_MultilineText(this.getMiddleX(), this.getMiddleY(), text, font, ALIGNMENT_X_AS_CENTER, ALIGNMENT_Y_AS_CENTER);
        } else {
            if (text != null) {
                this.text.setText(text);
            }
            if (font != null) {
                this.text.setFont(font);
            }
        }

        this.setTextColor(textColor);
        if (smallFont != null) {
            this.text.setSmallerFont(smallFont);
        }
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
        if (resourceId_pressed != 0) {
            texturePressed = TextureHelper.loadTexture(context, resourceId_pressed);
        }

        useTextureBackground = true;
    }

    public void setText(GL_MultilineText text) {
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
        initText(this.text.getText(), font, smallFont);
    }

    public String getText() {
        return text.getText();
    }

    public GL_MultilineText getTextView() {
        return this.text;
    }

    @Override
    public boolean checkTouchDown(int x, int y) {
        boolean state =  super.checkTouchDown(x, y);
        if (state && texturePressed != null)
            if (useTextureBackground) {
                changeToTexturePressed();
            }

        return state;
    }

    protected void changeToTexturePressed() {
        texture = texturePressed;
    }

    @Override
    public boolean checkTouchUp(int x, int y) {
        boolean state =  super.checkTouchUp(x, y);
        if (useTextureBackground) {
            changeToTextureNormal();
        }
        return state;
    }

    protected void changeToTextureNormal() {
        texture = textureNormal;
    }

    @Override
    public void cancelTouch() {
        super.cancelTouch();
        if (useTextureBackground) {
            changeToTextureNormal();
        }
    }

    @Override
    public void resetTouch() {
        super.resetTouch();
        if (useTextureBackground) {
            changeToTextureNormal();
        }
    }

    @Override
    public void draw(float[] projectionMatrix) {
        if (useTextureBackground) {
            super.draw(projectionMatrix);
        } else if (visible) {
            if (clickState == CLICK_STATE_DOWN) {
                backgroundPressed.draw(projectionMatrix);
            } else {
                backgroundNormal.draw(projectionMatrix);
            }
        }

        if (visible && text != null) {
            text.draw(projectionMatrix);
        }
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
            backgroundPressed = new GL_Background_Button(pos, width, height, backgroundPressed.getColor());
        }
    }

    public PowOfTwoTex getTextureNormal() {
        return textureNormal;
    }

    public PowOfTwoTex getTexturePressed() {
        return texturePressed;
    }
}
