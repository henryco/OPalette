#version 100
precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;

uniform float u_radius;
uniform float u_sqrSize;
uniform float u_halfSize;
uniform vec2 u_dimension;

void main() {

    vec4 color = texture2D(u_Texture0, v_TexCoordinate);
    if (color.a != 0.) {
        vec2 pos = v_TexCoordinate * u_dimension;

        float xc = (floor(pos.x / u_sqrSize) * u_sqrSize) + u_halfSize;
        float yc = (floor(pos.y / u_sqrSize) * u_sqrSize) + u_halfSize;
        float dist = length(vec2(pos.x - xc, pos.y - yc));

        if (dist <= u_radius) gl_FragColor = color;
        else gl_FragColor = vec4(0.);
    } else gl_FragColor = vec4(0.);
}
