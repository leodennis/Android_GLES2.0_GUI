/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package ldkproductions.com.example;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView.Renderer;

public class ExampleRenderer implements Renderer {
    private final Context context;

    GameManager gameManager = null;

    private boolean initialized = false;

    public ExampleRenderer(Context context) {
        this.context = context;
        GameManager.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(1.0f, 0.2f, 0.2f, 0.0f);

        GameManager.context = context;
        if(gameManager == null)
            gameManager = GameManager.getInstance();

        if (initialized) {
            gameManager.preloader(); //Reload textures as context is (re) created
            gameManager.initGameElements();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);

        gameManager.surfaceChanged(width, height);

        //orthoM(projectionMatrix, 0, -1f, 1f, -ratio, ratio, -1f, 1f );
        //MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);


        //setIdentityM(modelMatrix, 0);
        //translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        //rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        //final float[] temp = new float[16];
        //multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        //System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);

        if (!initialized) { //Most likely it starts in Portrait
            initialized = true;
            gameManager.preloader(); //Reload textures as context is (re) created
            gameManager.initGameElements();
        }
    }

    public void handleTouchPress(float normalizedX, float normalizedY) {
        gameManager.handleTouchPress(gameManager.convertToVirtualCoordinateX(normalizedX),
                gameManager.convertToVirtualCoordinateY(normalizedY));
    }

    public void handleTouchUp(float normalizedX, float normalizedY) {
        gameManager.handleTouchUp(gameManager.convertToVirtualCoordinateX(normalizedX),
                gameManager.convertToVirtualCoordinateY(normalizedY));
    }

    public void handleTouchMove(float normalizedX, float normalizedY) {
        gameManager.handleTouchMove(gameManager.convertToVirtualCoordinateX(normalizedX),
                gameManager.convertToVirtualCoordinateY(normalizedY));
    }

    public void handlePointerTouchPress(float normalizedX, float normalizedY) {
        gameManager.handlePointerTouchPress(gameManager.convertToVirtualCoordinateX(normalizedX),
                gameManager.convertToVirtualCoordinateY(normalizedY));
    }

    public void handlePointerTouchUp(float normalizedX, float normalizedY) {
        gameManager.handlePointerTouchUp(gameManager.convertToVirtualCoordinateX(normalizedX),
                gameManager.convertToVirtualCoordinateY(normalizedY));
    }

    public void handlePointerTouchMove(float normalizedX, float normalizedY) {
        gameManager.handlePointerTouchMove(gameManager.convertToVirtualCoordinateX(normalizedX),
                gameManager.convertToVirtualCoordinateY(normalizedY));
    }

    public boolean onBackPressed() {
        if (gameManager == null)
            return true;
        return gameManager.onBackPressed();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        gameManager.onConfigurationChanged(newConfig);
    }

    public void onPause() {
        if (gameManager != null)
            gameManager.onPause();
    }

    public void onResume() {
        if (gameManager != null)
            gameManager.onResume();
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(initialized)
            gameManager.drawFrame();
    }
}