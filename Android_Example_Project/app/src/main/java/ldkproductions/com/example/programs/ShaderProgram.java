package ldkproductions.com.example.programs;

import static android.opengl.GLES20.glUseProgram;
import android.content.Context;

import ldkproductions.com.example.util.ShaderHelper;
import ldkproductions.com.example.util.TextResourceReader;

abstract class ShaderProgram {
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String P_MATRIX = "p_Matrix"; //pos
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_TEXT_COLOR = "u_TextColor";

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    // Shader program
    protected final int program;
    protected ShaderProgram(Context context, int vertexShaderResourceId,
        int fragmentShaderResourceId) {
        // Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(
            TextResourceReader.readTextFileFromResource(
                context, vertexShaderResourceId),
            TextResourceReader.readTextFileFromResource(
                context, fragmentShaderResourceId));
    }        

    public void useProgram() {
        // Set the current OpenGL shader program to this program.
        glUseProgram(program);
    }
}
