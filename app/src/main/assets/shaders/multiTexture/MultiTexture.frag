precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate[5];

uniform sampler2D u_Texture0;
uniform sampler2D u_Texture1;
uniform sampler2D u_Texture2;
uniform sampler2D u_Texture3;
uniform sampler2D u_Texture4;
uniform sampler2D u_Texture5;
uniform sampler2D u_Texture6;
uniform sampler2D u_Texture7;
uniform sampler2D u_Texture8;
uniform sampler2D u_Texture9;
uniform int u_texNumb;


void main() {
    vec4 col1 = texture2D(u_Texture0, v_TexCoordinate[0]).rgba;
    vec4 col2 = texture2D(u_Texture1, v_TexCoordinate[1]).rgba;

    gl_FragColor = vec4(vec3(col2.rgb * col1.rgb), 1);
}
