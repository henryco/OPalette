#version 100

precision mediump float;

uniform vec2 u_dimension;
uniform float u_power;
uniform float u_radius;
uniform float u_activeRadius;

void main() {

    vec2 pos = ((gl_FragCoord.xy / u_dimension.xy) - vec2(.5)) * vec2(2.);
    float r = length(pos);
    if (r >= u_radius) {// 2.41 = 1 / (sqrt(2) − 1)
        gl_FragColor = vec4(vec3(0.), pow((u_activeRadius * (r - u_radius)), u_power));
    }
    else gl_FragColor = vec4(0.);
}
