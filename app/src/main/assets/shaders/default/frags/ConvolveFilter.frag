precision highp float;
// necessary part
varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;


// custom part
uniform float u_matrixSize;  // 3, 5
uniform float u_matrix3[9];
uniform float u_matrix5[25];
uniform float u_noiseLevel;
uniform vec2 u_screenDim;
uniform int u_enable; // 0 == false, 1... == true


//http://stackoverflow.com/questions/12964279/whats-the-origin-of-this-glsl-rand-one-liner
float noise1f(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

vec3 noise3f(vec3 color) {
    if (u_noiseLevel > 0.01) {
        float l = length(v_TexCoordinate);
        float r = noise1f(vec2(color.r, l));
        float g = noise1f(vec2(color.g, l));
        float b = noise1f(vec2(color.b, l));
        return mix(vec3(r, g, b), color, vec3(1. - u_noiseLevel));
    }
    return color;
}

void main() {

    if (u_enable == 0) gl_FragColor = texture2D(u_Texture0, v_TexCoordinate).rgba;
    else {
        vec2 cor = vec2((u_matrixSize - 1.) / 2.);
        vec3 rgb = vec3(0.);

        for (float i = 0.; i < u_matrixSize; i++) {
            for (float k = 0.; k < u_matrixSize; k++) {
                vec2 ipos = vec2(i - cor.x, k - cor.y);
                vec3 irgb = texture2D(u_Texture0, v_TexCoordinate + (ipos / u_screenDim)).rgb;

                int n = int(i * u_matrixSize + k);
                if (u_matrixSize == 3.) irgb *= u_matrix3[n];
                else irgb *= u_matrix5[n];

                rgb += irgb;
            }
        }
        gl_FragColor = vec4(noise3f(rgb), 1.);
    }
}
