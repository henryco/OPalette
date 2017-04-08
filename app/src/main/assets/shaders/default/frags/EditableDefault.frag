precision mediump float;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;

uniform sampler2D u_Texture0;

uniform vec3 u_addColor;
uniform vec3 u_minColor;
uniform vec3 u_maxColor;
uniform float u_alpha;
uniform float u_addBrightness;
uniform float u_contrast;
uniform float u_threshold;
uniform float u_gammaCorrection;
uniform float u_saturation;
uniform float u_lightness;
uniform float u_hue;

uniform int u_bwEnable;
uniform int u_thresholdEnable;
uniform int u_thresholdColor;


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


float correction(float color) {
    return 0.5 + u_addBrightness + (u_contrast * (color - 0.5));
}

void main() {
    vec4 color = texture2D(u_Texture0, v_TexCoordinate).rgba;
    if (color.a != 0.) {

        if (u_saturation != 0. || u_lightness != 0. || u_hue != 0.) {
            color.rgb = RGBToHSL(color.rgb);
            color.r += u_hue * 0.5; // [(H),s,l]
            color.g += u_saturation * color.g; // [h,(S),l]
            color.b += u_lightness * color.b; // [h,s,(L)]
            color.rgb = HSLToRGB(color.rgb);
        }

        color.r = pow(max(min(u_maxColor.r, correction(color.r + u_addColor.r)), u_minColor.r), u_gammaCorrection);
        color.g = pow(max(min(u_maxColor.g, correction(color.g + u_addColor.g)), u_minColor.g), u_gammaCorrection);
        color.b = pow(max(min(u_maxColor.b, correction(color.b + u_addColor.b)), u_minColor.b), u_gammaCorrection);
        color.a = u_alpha;
        if (u_bwEnable == 1 || u_thresholdEnable == 1) {
            float val = dot(vec3(1.), color.rgb) * 0.3333;

            if (u_thresholdEnable == 1) {
                if (u_thresholdColor == 0) color.rgb = val >= u_threshold ? vec3(1.) : vec3(0.);
                else if (u_thresholdColor == 1) color = val >= u_threshold ? color : vec4(0.);
            }
            if (u_bwEnable == 1) color.rgb = vec3(val);
        }
    }
    gl_FragColor = color;
}