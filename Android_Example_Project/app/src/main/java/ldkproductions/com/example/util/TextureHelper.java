package ldkproductions.com.example.util;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.util.Log;

import ldkproductions.com.example.GameManager;

public class TextureHelper {
    private static final String TAG = "TextureHelper";

    /**
     * Loads a texture from a resource ID, returning the OpenGL ID for that
     * texture. Returns 0 if the load failed.
     *
     * @param context
     * @param resourceId
     * @return
     */
    public static PowOfTwoTex loadTexture(Context context, int resourceId) {

        //Check if texture already exists
        PowOfTwoTex tex = getTextureId(resourceId);
        if (tex != null)
            return tex;

        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not generate a new OpenGL texture object.");
            }
            return new PowOfTwoTex();
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        // Read in the resource
        Drawable myDrawable;
        /*
        if(android.os.Build.VERSION.SDK_INT >= 21){ // int LOLLIPOP
            myDrawable = context.getDrawable(resourceId);
        } else {
            myDrawable = context.getResources().getDrawable(resourceId);
        }
        */
        myDrawable = context.getResources().getDrawable(resourceId);

        tex = new PowOfTwoTex();

        //final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        final Bitmap bitmap = drawableToPow2Bitmap(myDrawable, tex);

        if (bitmap == null) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
            }

            glDeleteTextures(1, textureObjectIds, 0);
            return new PowOfTwoTex();
        }
        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        // Set filtering: a default must be set, or the texture will be
        // black.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.

        glGenerateMipmap(GL_TEXTURE_2D);

        // Recycle the bitmap, since its data has been loaded into
        // OpenGL.
        bitmap.recycle();

        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0);

        tex.setId(textureObjectIds[0]);
        saveTextureId(resourceId, tex); //save texture so it can be reused

        return tex;
    }

    private static PowOfTwoTex getTextureId(int resourceId) {
        return GameManager.getTextureDictionary().get(resourceId);
    }

    private static void saveTextureId(int resourceId, PowOfTwoTex tex) {
        GameManager.getTextureDictionary().put(resourceId, tex);
    }


    private static Bitmap drawableToPow2Bitmap(Drawable drawable, PowOfTwoTex tex) {
        Bitmap bitmap = null;

        if (drawable == null)
            return null;

        int realWidth = drawable.getIntrinsicWidth();
        int realHeight = drawable.getIntrinsicHeight();

        if (realWidth <= 0 || realHeight <= 0) {
            // Single color bitmap will be created of 1x1 pixel
            realHeight = 1;
            realWidth = 1;
        }

        tex.setRealDimensions(realWidth, realHeight);
        tex.setPow2Dimensions(nextPowOfTwo(realWidth), nextPowOfTwo(realHeight));

        bitmap = Bitmap.createBitmap(tex.getPow2Width(), tex.getPow2Height(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, realWidth, realHeight);
        drawable.draw(canvas);
        return bitmap;
    }

    private static int nextPowOfTwo(int z) {
        for (int i = 1; i <= 4096; i = i * 2) {
            if (i >= z)
                return i;
        }
        return z;
    }

}