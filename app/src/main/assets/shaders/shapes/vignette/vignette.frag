#version 100

precision mediump float;

uniform vec2 u_dimension;
uniform float u_power;

void main() {

    vec2 pos = ((gl_FragCoord.xy / u_dimension.xy) - vec2(.5)) * vec2(2.);
    float r = length(pos);
    if (r >= 1.)
        gl_FragColor = vec4(vec3(0.), pow((2.41 * (r - 1.)), u_power));
    else gl_FragColor = vec4(0.);
}
