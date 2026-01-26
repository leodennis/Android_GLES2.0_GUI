package ldk.productions.example.project.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

import ldk.productions.example.project.R;
import ldk.productions.example.project.fonts.FontArial72;
import ldk.productions.example.project.fonts.FontDroidSans100;
import ldk.productions.example.project.gameelements.GL_ActionEvent;
import ldk.productions.example.project.gameelements.GL_ActionListener;
import ldk.productions.example.project.gameelements.GL_Button;
import ldk.productions.example.project.gameelements.GL_Font;
import ldk.productions.example.project.gameelements.GL_MultilineText;
import ldk.productions.example.project.gameelements.GL_ScrollView;
import ldk.productions.example.project.gameelements.GL_Text;
import ldk.productions.example.project.objects.GL_Line;
import ldk.productions.example.project.objects.GL_Rectangle;
import ldk.productions.example.project.objects.GL_Shape;
import ldk.productions.example.project.objects.GL_Texture;
import ldk.productions.example.project.programs.ProgramManager;

import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.Matrix.orthoM;
import static ldk.productions.example.project.gameelements.GL_TextAlignment.ALIGNMENT_X_AS_CENTER;
import static ldk.productions.example.project.gameelements.GL_TextAlignment.ALIGNMENT_X_AS_LEFT;
import static ldk.productions.example.project.gameelements.GL_TextAlignment.ALIGNMENT_Y_AS_CENTER;
import static ldk.productions.example.project.gameelements.GL_TextAlignment.ALIGNMENT_Y_AS_BOTTOM_LINE;
import static ldk.productions.example.project.gameelements.GL_TextAlignment.ALIGNMENT_Y_AS_TOP;

/**
 * Created by Leo on 12.11.2015.
 *
 * Example game manager for an app using Open GL ES 2.0
 */
public class GameManager implements GL_ActionListener {

    private static final String TAG = "GameManager";
    static GameManager gameManager = null;
    static Hashtable<Integer, PowOfTwoTex> textureDictionary = null;
    public static Context context;

    public static final int SAFE_ZONE_WIDTH = 1440;
    public static final int SAFE_ZONE_HEIGHT = 960;
    private int screenWidth = SAFE_ZONE_WIDTH;
    private int screenHeight = SAFE_ZONE_HEIGHT;
    private int displayPixelWidth;
    private int displayPixelHeight;

    private static final float[] projectionMatrix = new float[16];

    // fonts
    private GL_Font font;

    private ArrayList<GL_Shape> listOfObjects = new ArrayList<>();

    private Runnable show_toast = () -> Toast.makeText(GameManager.context, "You clicked the button!", Toast.LENGTH_SHORT).show();

    public static GameManager getInstance() {
        if (gameManager == null) {
            gameManager = new GameManager();
            if (LoggerConfig.ON) {
                Log.w(TAG, "gameManager Singleton created!");
            }
        }
        return gameManager;
    }

    public static Hashtable<Integer, PowOfTwoTex> getTextureDictionary() {
        if (textureDictionary == null) {
            textureDictionary = new Hashtable<>();
            if (LoggerConfig.ON) {
                Log.w(TAG, "textureDictionary created!");
            }
        }
        return  textureDictionary;
    }

    private GameManager()  {

    }

    public void surfaceChanged(int width, int height) {
        this.displayPixelWidth = width;
        this.displayPixelHeight = height;

        // use 960 x 1440 safe zone to support all screen sizes
        int newWidth;
        int newHeight;
        newHeight = SAFE_ZONE_HEIGHT; // set to min high
        newWidth = (int) (((float)newHeight)*width/height);

        screenWidth = newWidth;
        screenHeight = newHeight;

        // init projection matrix for virtual coordination system
        orthoM(projectionMatrix, 0, 0, newWidth, newHeight, 0, 0, 1f); // near, far default for 2D
    }

    public void preloader() {
        // initialize Open GL ES components
        ProgramManager.init(context);
        GL_Shape.init(screenWidth, screenHeight, displayPixelWidth, displayPixelHeight);
    }

    public void initGameElements() {
        clearGLMemory();

        font = new FontDroidSans100(context);
        GL_Font fontSmall = new FontArial72(context);
        GL_Font.setDefaultFont(font);   // Default font used for GL_Text, GL_Multiline_Text and GL_Button
        GL_Font.setDefaultSmallFont(fontSmall);

        //You may also set a smaller font like FontDroidSans64 as a smaller font, so texts will be resized automatically
        //GL_Font.setDefaultSmallFont(FontDroidSans64); // Default font used for GL_Multiline_Text autofit (used by e.g. GL_Button)
        GL_Rectangle background = new GL_Rectangle(0,0, screenWidth, screenHeight, Color.BLACK);
        GL_Text text = new GL_Text(screenWidth/2, 125, "Hello World!", font, ALIGNMENT_X_AS_CENTER, ALIGNMENT_Y_AS_CENTER);
        text.setColor(Color.WHITE);
        GL_Button but = new GL_Button(50, screenHeight/2, screenWidth/4, screenHeight/4, Color.LTGRAY);
        but.setText("Click");

        GL_ScrollView scroll = new GL_ScrollView(screenWidth/2, screenHeight/2, screenWidth/2, screenHeight/2);
        scroll.setBackgroundColor(Color.DKGRAY, 1.0f);
        GL_MultilineText text2 = new GL_MultilineText(scroll.getPosX(), scroll.getPosY(), context.getResources().getString(R.string.lorem_ipsum), font, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_TOP);
        text2.setColor(Color.YELLOW);
        text2.activateAutofit(screenWidth);
        scroll.add(text2);
        scroll.scrollToTop();
        //scroll.alwaysShowScrollbars();

        GL_Texture texture = new GL_Texture(context, 200, 800, 100, 100,  R.drawable.android);

        listOfObjects.add(background);
        listOfObjects.add(text);
        listOfObjects.add(but);
        listOfObjects.add(scroll);
        listOfObjects.add(texture);


        GL_Rectangle rect = new GL_Rectangle(0, 0, screenWidth, screenHeight, Color.WHITE);
        GL_Line l1 = new GL_Line(400, 0, 400, 800);
        GL_Line l1_2 = new GL_Line(0, 400, 800, 400);
        l1.setColor(Color.RED);
        l1_2.setColor(Color.RED);
        l1.setStrokeWidth(4);
        l1_2.setStrokeWidth(4);
        GL_MultilineText t1 = new GL_MultilineText(400, 400, "Test test test test Hey Hello Worldg", font, ALIGNMENT_X_AS_LEFT, ALIGNMENT_Y_AS_BOTTOM_LINE);
        t1.setColor(Color.BLACK);
        t1.setSmallerFont(fontSmall);
        t1.activateAutofit(700, 250);

        GL_Rectangle outerRect = new GL_Rectangle(t1.getXleft(), t1.getYtop(), t1.getWidth(), t1.getHeight(), Color.BLUE);
        outerRect.setAlpha(0.34f);

        //listOfObjects.add(rect);
        listOfObjects.add(l1);
        listOfObjects.add(l1_2);
        listOfObjects.add(outerRect);
        listOfObjects.add(t1);

        but.addActionListener(this);
    }

    /**
     * First finger on the screen press down
     */
    public void handleTouchPress(int virtualX, int virtualY) {
        for (GL_Shape object : listOfObjects) {
            object.checkTouchDown(virtualX, virtualY);
        }
    }

    /**
     * First finger on the screen press up
     */
    public void handleTouchUp(int virtualX, int virtualY) {
        for (GL_Shape object : listOfObjects) {
            object.checkTouchUp(virtualX, virtualY);
        }
    }

    /**
     * First finger on the screen move
     */
    public void handleTouchMove(int virtualX, int virtualY) {
        for (GL_Shape object : listOfObjects) {
            object.checkTouchMove(virtualX, virtualY);
        }
    }

    /**
     * Pointer means second finger on the screen, down
     */
    public void handlePointerTouchPress(int virtualX, int virtualY) {

    }

    /**
     * Pointer means second finger on the screen, up
     */
    public void handlePointerTouchUp(int virtualX, int virtualY) {

    }

    /**
     * Pointer means second finger on the screen, move
     */
    public void handlePointerTouchMove(int virtualX, int virtualY) {

    }

    /**
     * The back button has been pressed
     */
    public boolean onBackPressed() {
        return true;
    }

    /**
     * The screen configuration has changed
     */
    public void onConfigurationChanged(Configuration newConfig) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        surfaceChanged(metrics.widthPixels, metrics.heightPixels);
    }

    public void onPause() {

    }

    public void onResume() {

    }

    /**
     * This will be called each time a frame is drawn
     */
    public void drawFrame() {
        for (GL_Shape object : listOfObjects) {
            object.draw(projectionMatrix);
        }
    }

    public void clearGLMemory() {
        if (textureDictionary == null) {
            return;
        }

        for (PowOfTwoTex tex : textureDictionary.values()) {
            tex.invalidate();
            int[] textureObjectIds = new int[1];
            textureObjectIds[0] = tex.getId();
            glDeleteTextures(1, textureObjectIds, 0);
        }
        textureDictionary.clear();
        textureDictionary = null;
    }

    @Override
    public void actionPerformed(GL_ActionEvent e) {
        //You can check the source with e.getSource().equals()

        ((Activity) context).runOnUiThread(show_toast);
    }

    //------------------------------- Getters ---------------------------------------//

    public Context getContext() {
        return context;
    }

    public float[] getProjectionMatrix() {
        return projectionMatrix;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int convertToVirtualCoordinateX(float x) {
        return (int) ((x+1.f)*screenWidth)/2;
    }

    public int convertToVirtualCoordinateY(float y) {
        return (int) ((y+1.f)* screenHeight)/2;
    }

}
