package ldkproductions.com.example.programs;

import android.content.Context;

import static android.opengl.GLES20.glUseProgram;

/**
 * Created by Leo on 26.11.2015.
 */
public class ProgramManager {
    public static ColorShaderProgram colorShaderProgram;
    public static FontShaderProgram fontShaderProgram;
    public static TextureShaderProgram textureShaderProgram;

    private static ShaderProgram usedProgram;

    public static void init(Context context) {
        colorShaderProgram = new ColorShaderProgram(context);
        fontShaderProgram = new FontShaderProgram(context);
        textureShaderProgram = new TextureShaderProgram(context);
    }

    public static void useProgram(ShaderProgram program) {
        usedProgram = program;
        // Set the current OpenGL shader program to the program.
        usedProgram.useProgram();
    }

    public static void setColorShaderUniforms(float[] matrix) {
        colorShaderProgram.setUniforms(matrix);
    }

    public static void setTextureShaderUniforms(float[] uMatrix, float[] pMatrix, int textureId) {
        textureShaderProgram.setUniforms(uMatrix, pMatrix, textureId);
    }

    public static void setFontShaderUniforms(float[] uMatrix, float[] pMatrix, int uColor, int textureId) {
        fontShaderProgram.setUniforms(uMatrix, pMatrix, uColor, textureId);
    }

    public static ShaderProgram getUsedProgram() {
        return usedProgram;
    }

}
