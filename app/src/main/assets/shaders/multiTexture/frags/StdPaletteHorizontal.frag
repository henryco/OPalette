#version 100
precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate[5];

uniform sampler2D u_Texture0;
uniform sampler2D u_Texture1;
uniform sampler2D u_Texture2;
uniform sampler2D u_Texture3;
uniform sampler2D u_Texture4;

uniform vec2 u_dimension;
uniform int u_texNumb;
uniform vec3 u_line[2]; // Ax + By + C = 0

void main() {

    float trueHeight = 0.0;
    vec3 p_col = vec3(0.0);
    vec3 coeff[2];
    coeff[0] = u_line[0];
    coeff[1] = u_line[1];

    for (float y = 0.0; y < u_dimension.y; y += 1.0) {

        vec2 pointNormed = vec2(v_TexCoordinate[1].s, y / u_dimension.y);
        vec2 point = vec2(pointNormed.x * u_dimension.x, u_dimension.y * (1. - pointNormed.y));
        vec4 pointColor = texture2D(u_Texture0, pointNormed).rgba;

        if (pointColor.a != 0.0) {

            for (int i = 0; i < 2; i++) {
                if (coeff[i].x == 0.) coeff[i].x += 0.1;
                if (coeff[i].y == 0.) coeff[i].y += 0.1;
            }

            float py1 = (-1.) * ((coeff[0].x * point.x) + coeff[0].z) / coeff[0].y;
            float py2 = (-1.) * ((coeff[1].x * point.x) + coeff[1].z) / coeff[1].y;

            if ((point.y > py1 && point.y < py2) || (point.y > py2 && point.y < py1)) {
                p_col += pointColor.rgb;
                trueHeight += 1.0;
            }
        }
    }

    vec3 color = p_col / max(trueHeight, 1.0);
    gl_FragColor = vec4(color, 1.0);
}