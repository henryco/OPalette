precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;


uniform vec3 u_addColor;
uniform vec3 u_minColor;
uniform vec3 u_maxColor;
uniform float u_alpha;
uniform float u_addBrightness;


void main() {

    vec4 color = texture2D(u_Texture0, v_TexCoordinate).rgba;

    if (color.a != 0.) {
        color.r = max(min(u_maxColor.r, color.r + u_addBrightness + u_addColor.r), u_minColor.r);
        color.g = max(min(u_maxColor.g, color.g + u_addBrightness + u_addColor.g), u_minColor.g);
        color.b = max(min(u_maxColor.b, color.b + u_addBrightness + u_addColor.b), u_minColor.b);
        color.a = u_alpha;
    }

    gl_FragColor = color;
}