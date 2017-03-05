
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


void main() {

    vec3 p_col = vec3(0);

    for (float y = 0.0; y < u_dimension.y; y += 1.0) {
        vec2 point = vec2(v_TexCoordinate[1].s, y / u_dimension.y);
        vec4 p_c = texture2D(u_Texture0, point).rgba;
        p_col += p_c.rgb;

    }

    vec3 color = p_col / u_dimension.y;
    gl_FragColor = vec4(color, 1);


}