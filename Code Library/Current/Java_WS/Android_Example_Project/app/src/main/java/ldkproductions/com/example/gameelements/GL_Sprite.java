package ldkproductions.com.example.gameelements;

import android.content.Context;
import android.opengl.GLES20;

import ldkproductions.com.example.data.CGPoint;
import ldkproductions.com.example.data.VertexArray;
import ldkproductions.com.example.objects.GL_Texture;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by Leo on 17.11.2015.
 */
public class GL_Sprite extends GL_Texture {

    private int frameCnt;       //number of frames of sprite
    private int frameStep;      //number of frames for iteration
    private CGPoint speed;      //moving speed of sprite
    private int frameNr;        //current frame
    private int cnt;            //internal counter

    public GL_Sprite(Context context, int x, int y, int w, int h, int resourceId) {
        super(context, x, y, w, h, resourceId);
        initSprite(1, 1000, new CGPoint());
    }

    public GL_Sprite(Context context, int x, int y, int w, int h, int resourceId, int fcnt, int fstep, CGPoint sxy) {
        super(context, x, y, w, h, resourceId);
        initSprite(fcnt, fstep, sxy);
    }


    private void initSprite(int fcnt, int fstep, CGPoint sxy) {
        this.frameCnt = fcnt;
        this.frameStep = fstep;
        this.speed = sxy;
        this.frameNr = 0;
        this.cnt = 0;
    }

    private int updateFrame() {
        if(frameStep == cnt) {
            cnt = 0;
            frameNr++;
            if(frameNr > frameCnt-1) {
                frameNr = 0;
            }
        }
        cnt++;
        return frameNr;
    }

    @Override
    public void draw(float[] projectionMatrix) {
        pos.x += speed.x;
        pos.y += speed.y;
        drawFrame(updateFrame(), ((float) originalWidth)/frameCnt, pos, projectionMatrix);
    }

    private void drawFrame(int frameNr, float frameWidth, CGPoint pos, float[] projectionMatrix) {
        if(isVisible()) {
            //Activate Alpha
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            GLES20.glEnable(GLES20.GL_BLEND);

            float txW =  frameWidth/pow2width;
            float x1 = txW*frameNr;
            float x2 = x1 + txW; //=txW*(frameNr+1)

            //Position martix
            float[] p_matix = {
            //Order x,      y,      z,      w
                    1.f,    0.f,    0.f,    0.f ,
                    0.f,    1.f,    0.f,    0.f ,
                    0.f,    0.f,    1.f,    0.f,
                    pos.x,  pos.y,  0.f,    1.f
            };

            float[] vertex_data = {
                    // Order of coordinates: X, Y, S, T

                    // Triangles

                    0,           height,    x1, ((float) originalHeight)/pow2height, //bottom left
                    width,       height,    x2, ((float) originalHeight)/pow2height, //bottom right
                    0,           0,         x1, 0, //top left
                    width,       0,         x2, 0 //top right
            };

            VertexArray frameVertexArray = new VertexArray(vertex_data);

            prepareToDraw(p_matix, frameVertexArray, projectionMatrix);

            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

            GLES20.glDisable(GLES20.GL_BLEND); //Deactivate Alpha
        }
    }

    public CGPoint getSpeed() {
        return speed;
    }

    public void setSpeed(CGPoint speed) {
        this.speed = speed;
    }
}
