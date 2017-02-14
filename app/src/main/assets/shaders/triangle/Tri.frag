precision mediump float;

uniform vec4 u_Color;

void main() {

    gl_FragColor = u_Color;
    float b = gl_FragCoord.s;
    b *= 0.001;
    gl_FragColor.b = b;
}
