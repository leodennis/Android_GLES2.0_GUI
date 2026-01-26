package ldk.productions.example.project.programs;

import android.content.Context;

import ldk.productions.example.project.util.ShaderHelper;
import ldk.productions.example.project.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

abstract class ShaderProgram {
    // uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String P_MATRIX = "p_Matrix";    // pos
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_TEXT_COLOR = "u_TextColor";

    // attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    // shader program
    protected final int program;
    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        // compile the shaders and link the program
        program = ShaderHelper.buildProgram(
            TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
            TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId)
        );
    }        

    public void useProgram() {
        // set the current OpenGL shader program to this program
        glUseProgram(program);
    }
}
