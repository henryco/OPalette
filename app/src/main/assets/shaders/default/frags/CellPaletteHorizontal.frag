precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;
uniform vec2 u_dimension;
uniform float u_margin;
uniform int u_numb;

void main() {


    gl_FragColor = texture2D(u_Texture0, v_TexCoordinate).rgba;
}