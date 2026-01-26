package ldk.productions.example.project;

import android.content.Context;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView.Renderer;
import android.view.Menu;
import android.view.MenuItem;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ldk.productions.example.project.util.GameManager;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

public class GameRenderer implements Renderer {
    private final Context context;

    GameManager gameManager = null;

    private boolean initialized = false;

    public GameRenderer(Context context) {
        this.context = context;
        GameManager.context = context;
        this.initialized = false;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(1.0f, 0.2f, 0.2f, 0.0f);

        GameManager.context = context;
        if (gameManager == null) {
            gameManager = GameManager.getInstance();
        }

        if (initialized) {
            gameManager.preloader(); // reload textures as context is (re) created
            gameManager.initGameElements();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // set the OpenGL viewport to fill the entire surface
        glViewport(0, 0, width, height);

        gameManager.surfaceChanged(width, height);

        if (!initialized) {
            initialized = true;
            gameManager.preloader(); // reload textures as context is (re) created
            gameManager.initGameElements();
        }
    }

    public void handleTouchPress(float normalizedX, float normalizedY) {
        gameManager.handleTouchPress(gameManager.convertToVirtualCoordinateX(normalizedX), gameManager.convertToVirtualCoordinateY(normalizedY));
    }

    public void handleTouchUp(float normalizedX, float normalizedY) {
        gameManager.handleTouchUp(gameManager.convertToVirtualCoordinateX(normalizedX), gameManager.convertToVirtualCoordinateY(normalizedY));
    }

    public void handleTouchMove(float normalizedX, float normalizedY) {
        gameManager.handleTouchMove(gameManager.convertToVirtualCoordinateX(normalizedX), gameManager.convertToVirtualCoordinateY(normalizedY));
    }

    public void handlePointerTouchPress(float normalizedX, float normalizedY) {
        gameManager.handlePointerTouchPress(gameManager.convertToVirtualCoordinateX(normalizedX), gameManager.convertToVirtualCoordinateY(normalizedY));
    }

    public void handlePointerTouchUp(float normalizedX, float normalizedY) {
        gameManager.handlePointerTouchUp(gameManager.convertToVirtualCoordinateX(normalizedX), gameManager.convertToVirtualCoordinateY(normalizedY));
    }

    public void handlePointerTouchMove(float normalizedX, float normalizedY) {
        gameManager.handlePointerTouchMove(gameManager.convertToVirtualCoordinateX(normalizedX), gameManager.convertToVirtualCoordinateY(normalizedY));
    }

    public boolean onBackPressed() {
        if (gameManager == null) return true;

        return gameManager.onBackPressed();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (gameManager != null) {
            gameManager.onConfigurationChanged(newConfig);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // provide implementation in GameManager if needed
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // provide implementation in GameManager if needed
        return false;
    }

    public void onPause() {
        // provide implementation in GameManager if needed
    }

    public void onResume() {
        // provide implementation in GameManager if needed
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        // clear the rendering surface
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (initialized) {
            gameManager.drawFrame();
        }
    }
}