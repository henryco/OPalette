#version 100
precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;

//custom part
uniform vec3 u_line[2]; // Ax + By + C = 0
uniform vec2 u_dimension;
uniform float u_power;

uniform float u_matrixSize;  // 3, 5
uniform float u_matrix3[9];
uniform float u_matrix5[25];

void main() {

    vec2 pos = v_TexCoordinate.xy;
    vec2 point = vec2(pos.x, 1. - pos.y) * u_dimension;
    float scale = 1.;
    vec4 pointColor = texture2D(u_Texture0, pos);

    if (pointColor.a != 0.) {

        float py1 = (-1.) * ((u_line[0].x * point.x) + u_line[0].z) / u_line[0].y;
        float py2 = (-1.) * ((u_line[1].x * point.x) + u_line[1].z) / u_line[1].y;
        if ((point.y > py1 && point.y < py2) || (point.y > py2 && point.y < py1)) {
            float d1 = (abs((u_line[0].x * point.x) + (u_line[0].y * point.y) + u_line[0].z)) / sqrt(u_line[0].x*u_line[0].x + u_line[0].y*u_line[0].y);
            float d2 = (abs((u_line[1].x * point.x) + (u_line[1].y * point.y) + u_line[1].z)) / sqrt(u_line[1].x*u_line[1].x + u_line[1].y*u_line[1].y);
            float d = 2. * min(d1, d2) / (d1 + d2);
            scale = pow((1. - d), u_power);
        }

        vec2 cor = vec2((u_matrixSize - 1.) / 2.);
        vec3 rgb = vec3(0.);

        for (float i = 0.; i < u_matrixSize; i++) {
            for (float k = 0.; k < u_matrixSize; k++) {
                vec2 ipos = vec2(i - cor.x, k - cor.y);
                vec3 irgb = texture2D(u_Texture0, v_TexCoordinate + (ipos / u_dimension)).rgb;
                int n = int(i * u_matrixSize + k);
                if (u_matrixSize == 3.) irgb *= u_matrix3[n];
                else irgb *= u_matrix5[n];
                rgb += irgb;
            }
        }
        gl_FragColor = vec4(mix(pointColor.rgb, rgb, scale), 1.);
    } else gl_FragColor = vec4(0.);
}

