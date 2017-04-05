#version 100
precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate[5];

uniform sampler2D u_Texture0;
uniform sampler2D u_Texture1;
uniform int u_texNumb;

uniform float u_effectScale;

void main() {

    vec4 img_0 = texture2D(u_Texture0, v_TexCoordinate[0]);
    vec4 img_1 = texture2D(u_Texture0, v_TexCoordinate[0]);
    gl_FragColor = vec4(mix(img_0.rgb, img_1.rgb, vec3(u_effectScale)), 1.);
}