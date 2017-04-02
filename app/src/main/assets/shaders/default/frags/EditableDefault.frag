precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;

uniform vec3 u_addColor;
uniform vec3 u_minColor;
uniform vec3 u_maxColor;
uniform vec3 u_thrColor;
uniform float u_alpha;
uniform float u_addBrightness;
uniform float u_contrast;
uniform float u_threshold;
uniform int u_bwEnable;
uniform int u_thresholdEnable;

float correction(float color) {
    return 0.5 + u_addBrightness + (u_contrast * (color - 0.5));
}

void main() {
    vec4 color = texture2D(u_Texture0, v_TexCoordinate).rgba;
    if (color.a != 0.) {
        color.r = max(min(u_maxColor.r, correction(color.r + u_addColor.r)), u_minColor.r);
        color.g = max(min(u_maxColor.g, correction(color.g + u_addColor.g)), u_minColor.g);
        color.b = max(min(u_maxColor.b, correction(color.b + u_addColor.b)), u_minColor.b);
        color.a = u_alpha;
        if (u_bwEnable == 1 || u_thresholdEnable == 1) {
            float val = dot(vec3(1.), color.rgb);
            if (u_thresholdEnable == 1) color.rgb = val >= u_threshold ? u_thrColor : vec3(0.);
            else color.rgb = vec3(val);
        }
    }
    gl_FragColor = color;
}