package ldkproductions.com.example.data;

import android.content.Context;
import android.opengl.GLES20;

import ldkproductions.com.example.objects.GL_Texture;
import ldkproductions.com.example.programs.FontShaderProgram;
import ldkproductions.com.example.programs.ProgramManager;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by Leo on 17.11.2015.
 */
public class FontAtlas extends GL_Texture {

    private Letter[] letterList;
    
    public FontAtlas(Context context, int size, int resourceId) {
         super(context, 0, 0, size, size, resourceId);
    }

    public void setLetterList(Letter[] letterList) {
        this.letterList = letterList;
    }

    private Letter getLetter(char c) {
        for (int i=0; i<letterList.length; i++) {
            if(letterList[i].getLetter() == c)
                return letterList[i];
        }
        return null;
    }

    public int getLetterWidth(char c, boolean lastInLine) {
        for (int i = 0; i < letterList.length; i++) {
            Letter l = letterList[i];
            if (l.getLetter() == c) {
                if (lastInLine)
                    return l.getWidth() + l.getxDiff();
                return l.getWidth() + l.getxDiff() + l.getSpaceR();
            }
        }
        return 0;
    }

    public int[] getLetterHeight(char c) {
        for (int i=0; i<letterList.length; i++) {
            Letter l = letterList[i];
            if(l.getLetter() == c) {
                int[] splitHeight = new int[2];
                splitHeight[0] = l.getHeight(); //Height
                splitHeight[1] = l.getyDiff();  //yDiff
                return splitHeight;
            }
        }

        return null;
    }


    public int drawLetter(char c, int x, int y, int color, float[] projectionMatrix, boolean lastInLine, float scale) {
        Letter l = getLetter(c);
        if(l != null) {
            return drawLetter(l, x, y, color, projectionMatrix, lastInLine, scale);
        }
        return 0;
    }

    public int drawLetter(Letter l, int x, int y, int color, float[] projectionMatrix, boolean lastInLine, float scale) {
        if (isVisible()) {

            /*
            new GL_Rectangle(x, y + l.getyDiff(), l.getWidth()+ l.getxDiff()+ l.getSpaceR(), l.getHeight(), Color.rgb(255, 128, 0)).draw(projectionMatrix);
            new GL_Rectangle(x + l.getxDiff(), y + l.getyDiff(), l.getWidth(), l.getHeight(), Color.RED).draw(projectionMatrix);
            */

            //Activate Alpha
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            GLES20.glEnable(GLES20.GL_BLEND);

            float x1 = ((float)l.getRealX())/(width);
            float x2 = ((float)(l.getRealX() +l.getWidth()))/width;

            float y1 = ((float)l.getRealY())/height;
            float y2 = ((float)(l.getRealY() +l.getHeight()))/height;

            float[] p_matix = {
                    //Order x,      y,      z,      w
                    1.f,    0.f,    0.f,    0.f ,
                    0.f,    1.f,    0.f,    0.f ,
                    0.f,    0.f,    1.f,    0.f,
                    x+l.getxDiff()*scale,  y+l.getyDiff()*scale,  0.f,    1.f
            };

            float[] vertex_data = {
                    // Order of coordinates: X, Y, S, T

                    // Triangles
                    0,                      l.getHeight()*scale,    x1, y2, //bottom left
                    l.getWidth()*scale,     l.getHeight()*scale,    x2, y2, //bottom right
                    0,                      0,                      x1, y1, //top left
                    l.getWidth()*scale,     0,                      x2, y1  //top right
            };

            VertexArray frameVertexArray = new VertexArray(vertex_data);

            prepareToDraw(p_matix, frameVertexArray, color, projectionMatrix);
            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

            GLES20.glDisable(GLES20.GL_BLEND); //Deactivate Alpha

            if (lastInLine)
                return (int) (l.getWidth()*scale + l.getxDiff()*scale);
            return (int) (l.getWidth()*scale + l.getxDiff()*scale + l.getSpaceR()*scale);
        }
        return 0;
    }

    protected void prepareToDraw(float[] p_matix, VertexArray vertArr, int color, float[] projectionMatrix) {
        ProgramManager.useProgram(ProgramManager.fontShaderProgram);
        ProgramManager.setFontShaderUniforms(projectionMatrix, p_matix, color, texture);
        bindData(vertArr, ProgramManager.fontShaderProgram);
    }

    public void bindData(VertexArray vertArray, FontShaderProgram fontProgram) {
        vertArray.setVertexAttribPointer(
                0,
                fontProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                fontProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }
}
