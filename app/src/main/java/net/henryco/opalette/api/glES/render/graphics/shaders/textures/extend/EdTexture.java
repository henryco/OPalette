/*
 *   /*
 *    * Copyright (C) Henryk Timur Domagalski
 *    *
 *    * Licensed under the Apache License, Version 2.0 (the "License");
 *    * you may not use this file except in compliance with the License.
 *    * You may obtain a copy of the License at
 *    *
 *    *      http://www.apache.org/licenses/LICENSE-2.0
 *    *
 *    * Unless required by applicable law or agreed to in writing, software
 *    * distributed under the License is distributed on an "AS IS" BASIS,
 *    * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    * See the License for the specific language governing permissions and
 *    * limitations under the License.
 *
 */

package net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.OPallTexture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;
import net.henryco.opalette.api.utils.lambda.functions.OPallFunction;

/**
 * Created by HenryCo on 10/03/17.
 */

public class EdTexture extends OPallTextureExtended  {

	public final GLESUtils.Color add = new GLESUtils.Color(GLESUtils.Color.TRANSPARENT.hex());
	public final GLESUtils.Color min = new GLESUtils.Color(GLESUtils.Color.TRANSPARENT.hex());
	public final GLESUtils.Color max = new GLESUtils.Color(GLESUtils.Color.WHITE.hex());

	private float addBrightness = 0;
	private float threshold = 0.5f;
	private float alpha = 1;
	private float contrast = 1;
	private float gammaCorrection = 1;
	private float addSaturation = 0;
	private float addLightness = 0;
	private float addHue = 0;

	private boolean bwEnable = false;
	private boolean thresholdEnable = false;
	private boolean thresholdColored = false;

	public EdTexture(Context context) {
		this(context, Filter.LINEAR);
	}
	public EdTexture(Context context, Filter filter) {
		super(context, filter, OPallTexture.DEF_SHADER+".vert", FRAG_DIR);
	}
	public EdTexture(Bitmap image, Context context) {
		this(image, context, Filter.LINEAR);
	}
	public EdTexture(Bitmap image, Context context, Filter filter) {
		super(image, context, filter, OPallTexture.DEF_SHADER+".vert", FRAG_DIR);
	}
	public EdTexture() {
		super(OPallTexture.DEFAULT_VERT_FILE, FRAG_FILE);
	}
	public EdTexture(Bitmap image) {
		this(image, Filter.LINEAR);
	}
	public EdTexture(Bitmap image, Filter filter) {
		super(image, filter, OPallTexture.DEFAULT_VERT_FILE, FRAG_FILE);
	}


	public EdTexture minColor(OPallConsumer<GLESUtils.Color> min) {
		min.consume(this.min);
		return this;
	}
	public EdTexture maxColor(OPallConsumer<GLESUtils.Color> max) {
		max.consume(this.max);
		return this;
	}
	public EdTexture addColor(OPallConsumer<GLESUtils.Color> add) {
		add.consume(this.add);
		return this;
	}

	public EdTexture brightness(OPallFunction<Float, Float> brightness) {
		addBrightness = brightness.apply(addBrightness);
		return this;
	}

	public EdTexture setAlpha(float a) {
		this.alpha = a;
		return this;
	}

	public EdTexture setGammaCorrection(float c) {
		this.gammaCorrection = c;
		return this;
	}

	public EdTexture setContrast(float c) {
		this.contrast = c;
		return this;
	}

	public boolean isBwEnable() {
		return bwEnable;
	}

	public EdTexture setBwEnable(boolean bwEnable) {
		this.bwEnable = bwEnable;
		return this;
	}

	public EdTexture setAddHue(float hue) {
		this.addHue = hue;
		return this;
	}

	public EdTexture setAddLightness(float lightness) {
		this.addLightness = lightness;
		return this;
	}

	public EdTexture setAddSaturation(float saturation) {
		this.addSaturation = saturation;
		return this;
	}

	public EdTexture setThresholdColored(boolean colored) {
		this.thresholdColored = colored;
		return this;
	}

	public boolean isThresholdColored() {
		return thresholdColored;
	}

	public float getHue() {
		return addHue;
	}

	public float getSaturation() {
		return addSaturation;
	}
	public float getLightness() {
		return addLightness;
	}

	public float getGammaCorrection() {
		return gammaCorrection;
	}

	public float getBrightness() {
		return addBrightness;
	}

	public float getAlpha() {
		return alpha;
	}

	public float getThreshold() {
		return threshold;
	}

	public float getContrast() {
		return contrast;
	}

	public EdTexture setThreshold(float threshold) {
		this.threshold = threshold;
		return this;
	}

	public boolean isThresholdEnable() {
		return thresholdEnable;
	}

	public EdTexture setThresholdEnable(boolean thresholdEnable) {
		this.thresholdEnable = thresholdEnable;
		return this;
	}

	@Override
	protected void render(int program, Camera2D camera) {
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "u_alpha"), alpha);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "u_addBrightness"), addBrightness);
		GLES20.glUniform3f(GLES20.glGetUniformLocation(program, "u_addColor"), add.r, add.g, add.b);
		GLES20.glUniform3f(GLES20.glGetUniformLocation(program, "u_minColor"), min.r, min.g, min.b);
		GLES20.glUniform3f(GLES20.glGetUniformLocation(program, "u_maxColor"), max.r, max.g, max.b);
		GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "u_bwEnable"), bwEnable ? 1 : 0);
		GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "u_thresholdColor"), thresholdColored ? 1 : 0);
		GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "u_thresholdEnable"), thresholdEnable ? 1 : 0);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "u_gammaCorrection"), 1f / gammaCorrection);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "u_saturation"), addSaturation);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "u_lightness"), addLightness);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "u_threshold"), threshold);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "u_contrast"), contrast);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "u_hue"), addHue);
	}



	private static final String FRAG_DIR = OPallTexture.FRAG_DIR+"/EditableDefault.frag";
	private static final String FRAG_FILE = "precision mediump float;\n" +
			"\n" +
			"varying vec4 v_Position;\n" +
			"varying vec4 v_WorldPos;\n" +
			"varying vec2 v_TexCoordinate;\n" +
			"\n" +
			"uniform sampler2D u_Texture0;\n" +
			"\n" +
			"uniform vec3 u_addColor;\n" +
			"uniform vec3 u_minColor;\n" +
			"uniform vec3 u_maxColor;\n" +
			"uniform float u_alpha;\n" +
			"uniform float u_addBrightness;\n" +
			"uniform float u_contrast;\n" +
			"uniform float u_threshold;\n" +
			"uniform float u_gammaCorrection;\n" +
			"uniform float u_saturation;\n" +
			"uniform float u_lightness;\n" +
			"uniform float u_hue;\n" +
			"\n" +
			"uniform int u_bwEnable;\n" +
			"uniform int u_thresholdEnable;\n" +
			"uniform int u_thresholdColor;\n" +
			"\n" +
			"\n" +
			"vec3 RGBToHSL(in vec3 color) {\n" +
			"\tvec3 hsl; // init to 0 to avoid warnings ? (and reverse if + remove first part)\n" +
			"\n" +
			"\tfloat fmin = min(min(color.r, color.g), color.b);    //Min RGB\n" +
			"\tfloat fmax = max(max(color.r, color.g), color.b);    //Max RGB\n" +
			"\tfloat d = fmax - fmin;             //Delta RGB value\n" +
			"\n" +
			"\thsl.z = (fmax + fmin) / 2.0; // Lum\n" +
			"\n" +
			"\tif (d == 0.0) {\t//This is a gray, no chroma...\n" +
			"\t\thsl.x = 0.0;\t// Hue\n" +
			"\t\thsl.y = 0.0;\t// Sat\n" +
			"\t} else {\n" +
			"\n" +
			"\t    if (hsl.z < 0.5) hsl.y = d / (fmax + fmin); // Sat\n" +
			"\t\telse hsl.y = d / (2.0 - fmax - fmin); // Sat\n" +
			"\n" +
			"\t\tfloat dR = (((fmax - color.r) / 6.0) + (d / 2.0)) / d;\n" +
			"\t\tfloat dG = (((fmax - color.g) / 6.0) + (d / 2.0)) / d;\n" +
			"\t\tfloat dB = (((fmax - color.b) / 6.0) + (d / 2.0)) / d;\n" +
			"\n" +
			"\t\tif (color.r == fmax ) hsl.x = dB - dG; // Hue\n" +
			"\t\telse if (color.g == fmax) hsl.x = (1.0 / 3.0) + dR - dB; // Hue\n" +
			"\t\telse if (color.b == fmax) hsl.x = (2.0 / 3.0) + dG - dR; // Hue\n" +
			"\n" +
			"\t\tif (hsl.x < 0.0) hsl.x += 1.0; // Hue\n" +
			"\t\telse if (hsl.x > 1.0) hsl.x -= 1.0; // Hue\n" +
			"\t}\n" +
			"\treturn hsl;\n" +
			"}\n" +
			"\n" +
			"\n" +
			"float HueToRGB(in float f1, in float f2, in float hue) {\n" +
			"\tif (hue < 0.0) hue += 1.0;\n" +
			"\telse if (hue > 1.0) hue -= 1.0;\n" +
			"\tfloat res;\n" +
			"\tif ((6.0 * hue) < 1.0) res = f1 + (f2 - f1) * 6.0 * hue;\n" +
			"\telse if ((2.0 * hue) < 1.0) res = f2;\n" +
			"\telse if ((3.0 * hue) < 2.0) res = f1 + (f2 - f1) * ((2.0 / 3.0) - hue) * 6.0;\n" +
			"\telse res = f1;\n" +
			"\treturn res;\n" +
			"}\n" +
			"\n" +
			"vec3 HSLToRGB(in vec3 hsl) {\n" +
			"\tvec3 rgb;\n" +
			"\n" +
			"\tif (hsl.y == 0.0) rgb = vec3(hsl.z); // Luminance\n" +
			"\telse {\n" +
			"\t\tfloat f2;\n" +
			"\n" +
			"\t\tif (hsl.z < 0.5) f2 = hsl.z * (1.0 + hsl.y);\n" +
			"\t\telse f2 = (hsl.z + hsl.y) - (hsl.y * hsl.z);\n" +
			"\n" +
			"\t\tfloat f1 = 2.0 * hsl.z - f2;\n" +
			"\n" +
			"\t\trgb.r = HueToRGB(f1, f2, hsl.x + (1.0/3.0));\n" +
			"\t\trgb.g = HueToRGB(f1, f2, hsl.x);\n" +
			"\t\trgb.b= HueToRGB(f1, f2, hsl.x - (1.0/3.0));\n" +
			"\t}\n" +
			"\n" +
			"\treturn rgb;\n" +
			"}\n" +
			"\n" +
			"\n" +
			"float correction(float color) {\n" +
			"    return 0.5 + u_addBrightness + (u_contrast * (color - 0.5));\n" +
			"}\n" +
			"\n" +
			"void main() {\n" +
			"    vec4 color = texture2D(u_Texture0, v_TexCoordinate).rgba;\n" +
			"    if (color.a != 0.) {\n" +
			"\n" +
			"        if (u_saturation != 0. || u_lightness != 0. || u_hue != 0.) {\n" +
			"            color.rgb = RGBToHSL(color.rgb);\n" +
			"            color.r += u_hue * 0.5; // [(H),s,l]\n" +
			"            color.g += u_saturation * color.g; // [h,(S),l]\n" +
			"            color.b += u_lightness * color.b; // [h,s,(L)]\n" +
			"            color.rgb = HSLToRGB(color.rgb);\n" +
			"        }\n" +
			"\n" +
			"        color.r = pow(max(min(u_maxColor.r, correction(color.r + u_addColor.r)), u_minColor.r), u_gammaCorrection);\n" +
			"        color.g = pow(max(min(u_maxColor.g, correction(color.g + u_addColor.g)), u_minColor.g), u_gammaCorrection);\n" +
			"        color.b = pow(max(min(u_maxColor.b, correction(color.b + u_addColor.b)), u_minColor.b), u_gammaCorrection);\n" +
			"        color.a = u_alpha;\n" +
			"        if (u_bwEnable == 1 || u_thresholdEnable == 1) {\n" +
			"            float val = dot(vec3(1.), color.rgb) * 0.3333;\n" +
			"\n" +
			"            if (u_thresholdEnable == 1) {\n" +
			"                if (u_thresholdColor == 0) color.rgb = val >= u_threshold ? vec3(1.) : vec3(0.);\n" +
			"                else if (u_thresholdColor == 1) color = val >= u_threshold ? color : vec4(0.);\n" +
			"            }\n" +
			"            if (u_bwEnable == 1) color.rgb = vec3(val);\n" +
			"        }\n" +
			"    }\n" +
			"    gl_FragColor = color;\n" +
			"}";

}
