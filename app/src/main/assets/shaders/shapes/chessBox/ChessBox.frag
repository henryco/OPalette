#version 100

precision mediump float;

uniform vec4 u_color[2];
uniform float u_cellSize;

void main() {
    vec2 position = gl_FragCoord.st / u_cellSize;
    float px_m = (mod(ceil(position.x), 2.));
    float py_m = (mod(ceil(position.y), 2.));

    if ((px_m != 0. && py_m != 0.) || (px_m == 0. && py_m == 0.))
        gl_FragColor = u_color[0];
    else
        gl_FragColor = u_color[1];
}
