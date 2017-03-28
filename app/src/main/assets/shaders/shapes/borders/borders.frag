#version 100
precision mediump float;

uniform vec2 u_dimension;
uniform float u_size;
uniform vec4 u_color;

void main() {

    vec2 pos = gl_FragCoord.xy;
    if (pos.x < u_size || pos.x > u_dimension.x - u_size
        || pos.y < u_size || pos.y > u_dimension.y - u_size)
            gl_FragColor = u_color;
    else gl_FragColor = vec4(.0);
}
