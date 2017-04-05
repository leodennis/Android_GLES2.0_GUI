uniform mat4 u_Matrix;
uniform mat4 p_Matrix; //pos

attribute vec4 a_Position;  
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;

void main() {
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_Matrix * p_Matrix * a_Position;
}          