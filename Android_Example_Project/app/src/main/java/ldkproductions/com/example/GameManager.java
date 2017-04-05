package ldkproductions.com.example;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

import ldkproductions.com.example.gameelements.GL_ActionEvent;
import ldkproductions.com.example.gameelements.GL_ActionListener;
import ldkproductions.com.example.gameelements.GL_Button;
import ldkproductions.com.example.gameelements.GL_Font;
import ldkproductions.com.example.gameelements.GL_Multiline_Text;
import ldkproductions.com.example.gameelements.GL_ScrollView;
import ldkproductions.com.example.gameelements.GL_Text;
import ldkproductions.com.example.objects.GL_Rectangle;
import ldkproductions.com.example.objects.GL_Shape;
import ldkproductions.com.example.programs.ProgramManager;
import ldkproductions.com.example.util.LoggerConfig;
import ldkproductions.com.example.util.PowOfTwoTex;
import ldkproductions.com.example.fonts.FontDroidSans100;

import static android.opengl.Matrix.orthoM;

/**
 * Created by Leo on 12.11.2015.
 */
public class GameManager implements GL_ActionListener{

    private static final String TAG = "GameManager";
    static GameManager gameManager = null;
    static Hashtable<Integer, PowOfTwoTex> textureDictionary = null;
    public static Context context;

    //I use a 1440x960 safe zone so that every screen has at least this dimension
    public static final int SAFE_ZONE_WIDTH = 1440;
    public static final int SAFE_ZONE_HEIGHT = 960;
    private int screenWidth = SAFE_ZONE_WIDTH;      //Will be set to real width later
    private int screenHeight = SAFE_ZONE_HEIGHT;    //Will be set to real height later
    private int displayPixelWidth;                  //The real pixel of the screen
    private int displayPixelHeight;                 //The real pixel of the screen

    private static final float[] projectionMatrix = new float[16];

    private GL_Font font;

    private ArrayList<GL_Shape> listOfObjects = new ArrayList<>();

    private Runnable show_toast = new Runnable() {
        public void run() {
            Toast.makeText(GameManager.context, "You clicked the button!", Toast.LENGTH_SHORT).show();
        }
    };

    public static GameManager getInstance() {
        if(gameManager == null) {
            gameManager = new GameManager();
            if(LoggerConfig.ON)
                Log.w(TAG, "gameManager Singleton created!");
        }
        return gameManager;
    }

    public static Hashtable<Integer, PowOfTwoTex> getTextureDictionary() {
        if(textureDictionary == null) {
            textureDictionary = new Hashtable<Integer, PowOfTwoTex>();
            if(LoggerConfig.ON)
                Log.w(TAG, "textureDictionary created!");
        }
        return  textureDictionary;
    }

    private GameManager()  {

    }

    public void surfaceChanged(int width, int height) {

        this.displayPixelWidth = width;
        this.displayPixelHeight = height;

        //use 960 x 1440 safe zone to support all screen sizes
        int newWidth;
        int newHeight;
        if (width >= height) { //muss noch ueberarbeitet werden mit genauem verhältniss
            newHeight = SAFE_ZONE_HEIGHT; // set to min high
            newWidth = (int) (((float)newHeight)*width/height);
        } else {
            newWidth = SAFE_ZONE_WIDTH; //set to min width
            newHeight = (int) (((float)newWidth)*height/width);
        }

        screenWidth = newWidth;
        screenHeight = newHeight;

        //init projection matrix for virtual coordination system
        orthoM(projectionMatrix, 0, 0, newWidth, newHeight, 0, 0, 1f); //Near, Far... to default for 2D
    }

    public void preloader() {
        ProgramManager.init(context);
    }

    public void initGameElements() {
        getTextureDictionary().clear();

        font = new FontDroidSans100(context);
        GL_Font.setDefaultFont(font);                   //Default font used for GL_Text, GL_Multiline_Text and GL_Button

        //You may also set a smaller font like FontDroidSans64 as a smaller font, so texts will be resized automatically
        //GL_Font.setDefaultSmallFont(FontDroidSans64);    //Default font used for GL_Multiline_Text autofit (used by e.g. GL_Button)

        GL_Rectangle background = new GL_Rectangle(0,0, screenWidth, screenHeight, Color.BLACK);
        GL_Text text = new GL_Text("Hello World!", font, screenWidth/2, 125, Color.WHITE, GL_Text.ALIGNMENT_X_AS_CENTRE, GL_Text.ALIGNMENT_Y_AS_CENTRE);
        text.setColor(Color.WHITE);
        GL_Button but = new GL_Button(Color.LTGRAY, screenWidth/4, screenHeight/4, "Click", 50, screenHeight/2);

        GL_ScrollView scroll = new GL_ScrollView(screenWidth/2, screenHeight/2, screenWidth/2, screenHeight/2, screenWidth, screenHeight, displayPixelWidth, displayPixelHeight);
        scroll.setBackgroundColor(Color.DKGRAY, 1.0f);
        GL_Multiline_Text text2 = new GL_Multiline_Text(context.getResources().getString(R.string.lorem_ipsum), font, scroll.getPosX(), scroll.getPosY(), GL_Text.ALIGNMENT_X_AS_LEFT, GL_Text.ALIGNMENT_Y_AS_TOP, Color.YELLOW);
        text2.activateAutofit(screenWidth);
        scroll.add(text2);
        scroll.scrollToTop();
        //scroll.alwaysShowScrollbars();

        listOfObjects.add(background);
        listOfObjects.add(text);
        listOfObjects.add(but);
        listOfObjects.add(scroll);

        but.addActionListener(this);
    }


    /**
     * First finger on the screen press down
     */
    public void handleTouchPress(int virtualX, int virtualY) {
        for (GL_Shape object : listOfObjects)
            object.checkTouchDown(virtualX, virtualY);
    }

    /**
     * First finger on the screen press up
     */
    public void handleTouchUp(int virtualX, int virtualY) {
        for (GL_Shape object : listOfObjects)
            object.checkTouchUp(virtualX, virtualY);
    }

    /**
     * First finger on the screen move
     */
    public void handleTouchMove(int virtualX, int virtualY) {
        for (GL_Shape object : listOfObjects)
            object.checkTouchMove(virtualX, virtualY);
    }

    /**
     * Pointer means second finger on the screen
     */
    public void handlePointerTouchPress(int virtualX, int virtualY) {

    }

    /**
     * Pointer means second finger on the screen
     */
    public void handlePointerTouchUp(int virtualX, int virtualY) {

    }

    /**
     * Pointer means second finger on the screen
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

    }


    public void onPause() {

    }

    public void onResume() {

    }


    /**
     * This will be called each time a frame is drawn
     */
    public void drawFrame() {
        for (GL_Shape object : listOfObjects)
            object.draw(projectionMatrix);
    }

    //---------------------------- Getters ------------------------------------//


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

    @Override
    public void actionPerformed(GL_ActionEvent e) {
        //You can check the source with e.getSource().equals()

        ((Activity) context).runOnUiThread(show_toast);
    }
}
