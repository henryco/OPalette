precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;

uniform vec2 u_dimension;
uniform float u_cellSize;
uniform float u_margin;
uniform int u_numb;

void main() {

    vec2 pos = gl_FragCoord.xy / u_dimension;
    pos.y = pos.y;

    gl_FragColor = texture2D(u_Texture0, pos).rgba;
//    gl_FragColor = vec4(vec3(1. - pos.y), 1.);

}