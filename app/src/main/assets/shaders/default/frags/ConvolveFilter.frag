precision mediump float;

// necessary part
varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;


// custom part
uniform float u_matrixSize;  // 3, 5
uniform float u_matrix3[9];
uniform float u_matrix5[25];
uniform vec2 u_screenDim;


void main() {

    vec2 pos = gl_FragCoord.xy;
    vec2 cor = vec2((u_matrixSize - 1.) / 2.);
    vec3 rgb = vec3(0.);

    for (float i = 0.; i < u_matrixSize; i++) {
        for (float k = 0.; k < u_matrixSize; k++) {
            vec2 ipos = vec2(i - cor.x, k - cor.y);
            vec3 irgb = texture2D(u_Texture0, pos + ipos).rgb;

            int n = int(i * u_matrixSize + k);
            if (u_matrixSize == 3.) irgb *= u_matrix3[n];
            else irgb *= u_matrix5[n];

            rgb += irgb;
        }
    }

    gl_FragColor = vec4(rgb, 1.);
}
