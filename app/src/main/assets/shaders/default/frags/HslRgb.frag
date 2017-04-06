precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;

uniform float u_saturation;
uniform float u_lightness;


// [RED, GREEN, BLUE] to [HUE, SATURATION, LIGHTNESS]

vec3 RGBToHSL(in vec3 color) {
	vec3 hsl; // init to 0 to avoid warnings ? (and reverse if + remove first part)

	float fmin = min(min(color.r, color.g), color.b);    //Min RGB
	float fmax = max(max(color.r, color.g), color.b);    //Max RGB
	float d = fmax - fmin;             //Delta RGB value

	hsl.z = (fmax + fmin) / 2.0; // Lum

	if (d == 0.0) {	//This is a gray, no chroma...
		hsl.x = 0.0;	// Hue
		hsl.y = 0.0;	// Sat
	} else {

	    if (hsl.z < 0.5) hsl.y = d / (fmax + fmin); // Sat
		else hsl.y = d / (2.0 - fmax - fmin); // Sat

		float dR = (((fmax - color.r) / 6.0) + (d / 2.0)) / d;
		float dG = (((fmax - color.g) / 6.0) + (d / 2.0)) / d;
		float dB = (((fmax - color.b) / 6.0) + (d / 2.0)) / d;

		if (color.r == fmax ) hsl.x = dB - dG; // Hue
		else if (color.g == fmax) hsl.x = (1.0 / 3.0) + dR - dB; // Hue
		else if (color.b == fmax) hsl.x = (2.0 / 3.0) + dG - dR; // Hue

		if (hsl.x < 0.0) hsl.x += 1.0; // Hue
		else if (hsl.x > 1.0) hsl.x -= 1.0; // Hue
	}
	return hsl;
}


float HueToRGB(in float f1, in float f2, in float hue) {
	if (hue < 0.0) hue += 1.0;
	else if (hue > 1.0) hue -= 1.0;
	float res;
	if ((6.0 * hue) < 1.0) res = f1 + (f2 - f1) * 6.0 * hue;
	else if ((2.0 * hue) < 1.0) res = f2;
	else if ((3.0 * hue) < 2.0) res = f1 + (f2 - f1) * ((2.0 / 3.0) - hue) * 6.0;
	else res = f1;
	return res;
}

vec3 HSLToRGB(in vec3 hsl) {
	vec3 rgb;

	if (hsl.y == 0.0) rgb = vec3(hsl.z); // Luminance
	else {
		float f2;

		if (hsl.z < 0.5) f2 = hsl.z * (1.0 + hsl.y);
		else f2 = (hsl.z + hsl.y) - (hsl.y * hsl.z);

		float f1 = 2.0 * hsl.z - f2;

		rgb.r = HueToRGB(f1, f2, hsl.x + (1.0/3.0));
		rgb.g = HueToRGB(f1, f2, hsl.x);
		rgb.b= HueToRGB(f1, f2, hsl.x - (1.0/3.0));
	}

	return rgb;
}

void main() {
    gl_FragColor = texture2D(u_Texture0, v_TexCoordinate).rgba;
}
