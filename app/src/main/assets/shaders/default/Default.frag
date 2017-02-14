precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;

void main() {
    vec4 col = texture2D(u_Texture0, v_TexCoordinate).rgba;
    gl_FragColor = col;
    //gl_FragColor = vec4(0,0,0,0);
}
