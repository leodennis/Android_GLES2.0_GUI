package ldkproductions.com.example.programs;

import android.content.Context;
import android.graphics.Color;
import ldkproductions.com.example.R;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class FontShaderProgram extends ShaderProgram {
    // Uniform locations
    private final int uMatrixLocation;
    private final int pMatrixLocation; //pos
    private final int uTextureUnitLocation;
    private final int uTextColorLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public FontShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader,
            R.raw.font_fragment_shader);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        pMatrixLocation = glGetUniformLocation(program, P_MATRIX); //pos
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        uTextColorLocation = glGetUniformLocation(program, U_TEXT_COLOR);
        
        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = 
            glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] uMatrix, float[] pMatrix, int uColor, int textureId) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, uMatrix, 0);
        glUniformMatrix4fv(pMatrixLocation, 1, false, pMatrix, 0); //pos
        glUniform4f(uTextColorLocation, Color.red(uColor)/255.f, Color.green(uColor)/255.f,
                Color.blue(uColor)/255.f, Color.alpha(uColor)/255.f); //color

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0);

        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }

    public int getTextColorAttributeLocation() {
        return uTextColorLocation;
    }
}
