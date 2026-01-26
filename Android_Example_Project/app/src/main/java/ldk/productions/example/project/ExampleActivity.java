package ldk.productions.example.project;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.core.os.BuildCompat;

import ldk.productions.example.project.util.LoggerConfig;

public class ExampleActivity extends Activity {
    /**
     * Hold a reference to our GLSurfaceView
     */

    private static final String TAG = "Activity";
    private final GameRenderer gameRenderer = new GameRenderer(this);
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (BuildCompat.isAtLeastT()) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    this::onBackPressed
            );
        }

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setFocusable(true);
        glSurfaceView.setFocusableInTouchMode(true);

        // Check if the system supports OpenGL ES 2.0.
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager
                .getDeviceConfigurationInfo();
        // Even though the latest emulator supports OpenGL ES 2.0,
        // it has a bug where it doesn't set the reqGlEsVersion so
        // the above check doesn't work. The below will detect if the
        // app is running on an emulator, and assume that it supports
        // OpenGL ES 2.0.
        final boolean supportsEs2 =
                configurationInfo.reqGlEsVersion >= 0x20000
                        || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86")));

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(gameRenderer);

            rendererSet = true;

            // preserve context if possible to avoid delays
            if (Build.VERSION.SDK_INT >= 11) {
                glSurfaceView.setPreserveEGLContextOnPause(true);
            }
        } else {
            /*
             * This is where you could create an OpenGL ES 1.x compatible
             * renderer if you wanted to support both ES 1 and ES 2. Since
             * we're not doing anything, the app will crash if the device
             * doesn't support OpenGL ES 2.0. If we publish on the market, we
             * should also add the following to AndroidManifest.xml:
             *
             * <uses-feature android:glEsVersion="0x00020000"
             * android:required="true" />
             *
             * This hides our app from those devices which don't support OpenGL
             * ES 2.0.
             */
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
            return;
        }

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            private static final int INVALID_ID = -1;
            private int primaryId = INVALID_ID;
            private int pointerId = INVALID_ID;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {

                    final int fingerIndex = event.getActionIndex();
                    final int fingerId = event.getPointerId(fingerIndex);

                    // Convert touch coordinates into normalized device
                    // coordinates, keeping in mind that Android's Y
                    // coordinates are inverted.
                    final float normalizedX = (event.getX(fingerIndex) / (float) v.getWidth()) * 2 - 1;
                    final float normalizedY = (event.getY(fingerIndex) / (float) v.getHeight()) * 2 - 1;

                    final int eventAction = event.getActionMasked();

                    if (eventAction == MotionEvent.ACTION_MOVE) {
                        try {
                            if (primaryId != INVALID_ID) {
                                final int primaryIndex = event.findPointerIndex(primaryId);
                                final float normalizedX2 =
                                        (event.getX(primaryIndex) / (float) v.getWidth()) * 2 - 1;
                                final float normalizedY2 =
                                        (event.getY(primaryIndex) / (float) v.getHeight()) * 2 - 1;
                                glSurfaceView.queueEvent(new Runnable() {
                                    @Override
                                    public void run() {
                                        gameRenderer.handleTouchMove(normalizedX2, normalizedY2);
                                    }
                                });
                            }
                        } catch (IllegalArgumentException e) {
                            //Happens (as example) when taking a screenshot through "Palm swipe to capture"
                            primaryId = INVALID_ID;
                            if (LoggerConfig.ON) Log.e(TAG, "Invalid pointer index, ignored move");
                        }

                        try {
                            if (pointerId != INVALID_ID) {
                                final int index = event.findPointerIndex(pointerId);
                                final float normalizedX2 =
                                        (event.getX(index) / (float) v.getWidth()) * 2 - 1;
                                final float normalizedY2 =
                                        (event.getY(index) / (float) v.getHeight()) * 2 - 1;
                                glSurfaceView.queueEvent(new Runnable() {
                                    @Override
                                    public void run() {
                                        gameRenderer.handlePointerTouchMove(normalizedX2, normalizedY2);
                                    }
                                });
                            }
                        } catch (IllegalArgumentException e) {
                            // happens (as example) when taking a screenshot through "Palm swipe to capture"
                            pointerId = INVALID_ID;
                            if (LoggerConfig.ON) Log.e(TAG, "Invalid pointer index, ignored move");
                        }
                    } else if (eventAction == MotionEvent.ACTION_DOWN || eventAction == MotionEvent.ACTION_POINTER_DOWN) {
                        if (primaryId == INVALID_ID) {
                            primaryId = fingerId;
                            glSurfaceView.queueEvent(new Runnable() {
                                @Override
                                public void run() {
                                    gameRenderer.handleTouchPress(normalizedX, normalizedY);
                                }
                            });
                        } else if (pointerId == INVALID_ID) {
                            pointerId = fingerId;
                            glSurfaceView.queueEvent(new Runnable() {
                                @Override
                                public void run() {
                                    gameRenderer.handlePointerTouchPress(normalizedX, normalizedY);
                                }
                            });
                        }
                    } else if (eventAction == MotionEvent.ACTION_UP || eventAction == MotionEvent.ACTION_POINTER_UP) {
                        if (fingerId == primaryId) {
                            primaryId = INVALID_ID;
                            glSurfaceView.queueEvent(new Runnable() {
                                @Override
                                public void run() {
                                    gameRenderer.handleTouchUp(normalizedX, normalizedY);
                                }
                            });
                        } else if (fingerId == pointerId) {
                            pointerId = INVALID_ID;
                            glSurfaceView.queueEvent(new Runnable() {
                                @Override
                                public void run() {
                                    gameRenderer.handlePointerTouchUp(normalizedX, normalizedY);
                                }
                            });
                        }

                        if (eventAction == MotionEvent.ACTION_UP) {
                            // last pointer left screen so make sure the IDs are reset (to prevent errors)
                            primaryId = INVALID_ID;
                            pointerId = INVALID_ID;
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }

        });

        setContentView(glSurfaceView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (rendererSet) {
            glSurfaceView.queueEvent(() -> gameRenderer.onConfigurationChanged(newConfig));
        }
    }

    @Override
    public void onBackPressed() {
        if (!rendererSet || gameRenderer.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (rendererSet) {
            glSurfaceView.onPause();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (rendererSet) {
            gameRenderer.onPause();
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet) {
            gameRenderer.onResume();
            glSurfaceView.onResume();
        }
    }
}