precision mediump float;

uniform vec4 u_TextColor;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
  
void main() {
    vec4 tmpColor = texture2D(u_TextureUnit, v_TextureCoordinates);
    gl_FragColor = vec4(u_TextColor.r, u_TextColor.g, u_TextColor.b,  tmpColor.a*u_TextColor.a);
}