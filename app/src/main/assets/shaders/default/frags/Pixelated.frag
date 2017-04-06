#version 100
precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;

uniform float u_pixelsNumb;
uniform vec2 u_dPix;

void main() {

	vec4 color = vec4(0.);
	color = texture2D(u_Texture0, v_TexCoordinate);
	if (color.a != 0.) {

        float dx = u_dPix.x * (1.0 / u_pixelsNumb);
        float dy = u_dPix.y * (1.0 / u_pixelsNumb);
        vec2 pixel = vec2(dx * floor(v_TexCoordinate.x / dx), dy * floor(v_TexCoordinate.y / dy));
        color = texture2D(u_Texture0, pixel);
	}
	gl_FragColor = color;
}
