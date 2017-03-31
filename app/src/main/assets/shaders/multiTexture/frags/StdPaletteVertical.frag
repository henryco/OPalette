#version 100
precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate[5];

uniform sampler2D u_Texture0;
uniform sampler2D u_Texture1;

uniform vec2 u_dimension;
uniform int u_texNumb;
uniform float u_barStart;
uniform float u_barEnd;
uniform vec3 u_line[2]; // Ax + By + C = 0

void main() {

    float trueWidth = 0.0;
    vec3 p_col = vec3(0.0);

    for (float x = 0.0; x < u_dimension.x; x += 1.0) {

        vec2 pointNormed = vec2(x / u_dimension.x, v_TexCoordinate[1].y);
        vec2 point = vec2(pointNormed.x * u_dimension.x, u_dimension.y * (1. - pointNormed.y));
        vec4 pointColor = texture2D(u_Texture0, pointNormed).rgba;

        if (pointColor.a != 0. && x > 0. && (x >= u_barEnd || x <= u_barStart)) {

            float py1 = (-1.) * ((u_line[0].x * point.x) + u_line[0].z) / u_line[0].y;
            float py2 = (-1.) * ((u_line[1].x * point.x) + u_line[1].z) / u_line[1].y;

            if ((point.y > py1 && point.y < py2) || (point.y > py2 && point.y < py1)) {
                p_col += pointColor.rgb;
                trueWidth += 1.0;
            }
        }

    }

    vec3 color = p_col / max(trueWidth, 1.0);
    gl_FragColor = vec4(color, 1.0);
}
