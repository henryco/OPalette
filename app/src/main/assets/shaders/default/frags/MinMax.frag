#version 100
precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;

uniform vec2 u_size;
uniform int u_type; // 0 - min, 1 - max
uniform float u_kernel;
uniform float u_power;

void main() {
    vec4 texColor = texture2D(u_Texture0, v_TexCoordinate).rgba;
    if (texColor.a != 0. && (u_type == 0 || u_type == 1)) {
        vec2 pos = v_TexCoordinate.xy;
        float s = (u_kernel - 1.) * 0.5;
        bool type = u_type == 0;

        vec3 col_rgb = type ? vec3(1.) : vec3(0.);
        for (float i = 0.; i < u_kernel; i += 1.) {
            for (float k = 0.; k < u_kernel; k += 1.) {
                float si = (i + s) / u_size.x;
                float sk = (k + s) / u_size.y;
                vec3 rgb = texture2D(u_Texture0, vec2(pos.x - si, pos.y - sk)).rgb;
                col_rgb = type ? min(col_rgb, rgb) : max(col_rgb, rgb);
            }
        }
        gl_FragColor = vec4(mix(texColor.rgb, col_rgb, u_power).rgb, 1.);
    }   else gl_FragColor = texColor;
}