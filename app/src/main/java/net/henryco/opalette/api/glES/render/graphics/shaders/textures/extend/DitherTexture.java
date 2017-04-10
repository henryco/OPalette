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

import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;

/**
 * Created by HenryCo on 10/04/17.
 */

public class DitherTexture extends OPallTextureExtended {


	public static final int TYPE_NAN = -1;
	public static final int TYPE_2x2 = 0;
	public static final int TYPE_4x4 = 1;
	public static final int TYPE_8x8 = 2;

	private int type;


	public DitherTexture() {
		this(TYPE_8x8);
	}
	public DitherTexture(Filter filter, int type) {
		super(filter, VERT, FRAG);
		setFlip(false, true);
		setFilterType(type);
	}

	public DitherTexture(int type) {
		super(VERT, FRAG);
		setFlip(false, true);
		setFilterType(type);
	}

	public DitherTexture(Bitmap image, int type) {
		super(image, VERT, FRAG);
		setFlip(false, true);
		setFilterType(type);
	}

	public DitherTexture(Bitmap image, Filter filter, int type) {
		super(image, filter, VERT, FRAG);
		setFlip(false, true);
		setFilterType(type);
	}


	public DitherTexture setFilterType(int type) {
		this.type = type;
		return this;
	}

	public int getFilterType() {
		return this.type;
	}


	@Override
	protected void render(int program, Camera2D camera) {
		float w = getWidth() == 0 ? getScreenWidth() : getWidth();
		float h = getHeight() == 0 ? getScreenHeight() : getHeight();
		GLES20.glUniform1i(GLES20.glGetUniformLocation(program, u_type), type);
		GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), w, h);
	}


	private static final String u_type = "u_type";
	private static final String u_dimension = "u_dimension";
	private static final String VERT = DEFAULT_VERT_FILE;
	private static final String FRAG = "#version 100\n" +
			"precision mediump float;\n" +
			"\n" +
			"// Author of those dither bayer matrix algorithms\n" +
			"// (dither2x2, dither4x4, dither8x8) implementation is \"hughsk\"\n" +
			"// Code under MIT License, author's web page you can find here:\n" +
			"// https://github.com/hughsk/glsl-dither\n" +
			"\n" +
			"varying vec4 v_Position;\n" +
			"varying vec4 v_WorldPos;\n" +
			"varying vec2 v_TexCoordinate;\n" +
			"\n" +
			"uniform sampler2D u_Texture0;\n" +
			"\n" +
			"uniform vec2 u_dimension;\n" +
			"uniform int u_type; // 0 - 2x2, 1 - 4x4, 2 - 8x8\n" +
			"\n" +
			"\n" +
			"float dither2x2(vec2 position, float brightness) {\n" +
			"  int x = int(mod(position.x, 2.0));\n" +
			"  int y = int(mod(position.y, 2.0));\n" +
			"  int index = x + y * 2;\n" +
			"  float limit = 0.0;\n" +
			"\n" +
			"  if (x < 8) {\n" +
			"    if (index == 0) limit = 0.25;\n" +
			"    if (index == 1) limit = 0.75;\n" +
			"    if (index == 2) limit = 1.00;\n" +
			"    if (index == 3) limit = 0.50;\n" +
			"  }\n" +
			"\n" +
			"  return brightness < limit ? 0.0 : 1.0;\n" +
			"}\n" +
			"float dither4x4(vec2 position, float brightness) {\n" +
			"  int x = int(mod(position.x, 4.0));\n" +
			"  int y = int(mod(position.y, 4.0));\n" +
			"  int index = x + y * 4;\n" +
			"  float limit = 0.0;\n" +
			"\n" +
			"  if (x < 8) {\n" +
			"    if (index == 0) limit = 0.0625;\n" +
			"    if (index == 1) limit = 0.5625;\n" +
			"    if (index == 2) limit = 0.1875;\n" +
			"    if (index == 3) limit = 0.6875;\n" +
			"    if (index == 4) limit = 0.8125;\n" +
			"    if (index == 5) limit = 0.3125;\n" +
			"    if (index == 6) limit = 0.9375;\n" +
			"    if (index == 7) limit = 0.4375;\n" +
			"    if (index == 8) limit = 0.25;\n" +
			"    if (index == 9) limit = 0.75;\n" +
			"    if (index == 10) limit = 0.125;\n" +
			"    if (index == 11) limit = 0.625;\n" +
			"    if (index == 12) limit = 1.0;\n" +
			"    if (index == 13) limit = 0.5;\n" +
			"    if (index == 14) limit = 0.875;\n" +
			"    if (index == 15) limit = 0.375;\n" +
			"  }\n" +
			"\n" +
			"  return brightness < limit ? 0.0 : 1.0;\n" +
			"}\n" +
			"float dither8x8(vec2 position, float brightness) {\n" +
			"  int x = int(mod(position.x, 8.0));\n" +
			"  int y = int(mod(position.y, 8.0));\n" +
			"  int index = x + y * 8;\n" +
			"  float limit = 0.0;\n" +
			"\n" +
			"  if (x < 8) {\n" +
			"    if (index == 0) limit = 0.015625;\n" +
			"    if (index == 1) limit = 0.515625;\n" +
			"    if (index == 2) limit = 0.140625;\n" +
			"    if (index == 3) limit = 0.640625;\n" +
			"    if (index == 4) limit = 0.046875;\n" +
			"    if (index == 5) limit = 0.546875;\n" +
			"    if (index == 6) limit = 0.171875;\n" +
			"    if (index == 7) limit = 0.671875;\n" +
			"    if (index == 8) limit = 0.765625;\n" +
			"    if (index == 9) limit = 0.265625;\n" +
			"    if (index == 10) limit = 0.890625;\n" +
			"    if (index == 11) limit = 0.390625;\n" +
			"    if (index == 12) limit = 0.796875;\n" +
			"    if (index == 13) limit = 0.296875;\n" +
			"    if (index == 14) limit = 0.921875;\n" +
			"    if (index == 15) limit = 0.421875;\n" +
			"    if (index == 16) limit = 0.203125;\n" +
			"    if (index == 17) limit = 0.703125;\n" +
			"    if (index == 18) limit = 0.078125;\n" +
			"    if (index == 19) limit = 0.578125;\n" +
			"    if (index == 20) limit = 0.234375;\n" +
			"    if (index == 21) limit = 0.734375;\n" +
			"    if (index == 22) limit = 0.109375;\n" +
			"    if (index == 23) limit = 0.609375;\n" +
			"    if (index == 24) limit = 0.953125;\n" +
			"    if (index == 25) limit = 0.453125;\n" +
			"    if (index == 26) limit = 0.828125;\n" +
			"    if (index == 27) limit = 0.328125;\n" +
			"    if (index == 28) limit = 0.984375;\n" +
			"    if (index == 29) limit = 0.484375;\n" +
			"    if (index == 30) limit = 0.859375;\n" +
			"    if (index == 31) limit = 0.359375;\n" +
			"    if (index == 32) limit = 0.0625;\n" +
			"    if (index == 33) limit = 0.5625;\n" +
			"    if (index == 34) limit = 0.1875;\n" +
			"    if (index == 35) limit = 0.6875;\n" +
			"    if (index == 36) limit = 0.03125;\n" +
			"    if (index == 37) limit = 0.53125;\n" +
			"    if (index == 38) limit = 0.15625;\n" +
			"    if (index == 39) limit = 0.65625;\n" +
			"    if (index == 40) limit = 0.8125;\n" +
			"    if (index == 41) limit = 0.3125;\n" +
			"    if (index == 42) limit = 0.9375;\n" +
			"    if (index == 43) limit = 0.4375;\n" +
			"    if (index == 44) limit = 0.78125;\n" +
			"    if (index == 45) limit = 0.28125;\n" +
			"    if (index == 46) limit = 0.90625;\n" +
			"    if (index == 47) limit = 0.40625;\n" +
			"    if (index == 48) limit = 0.25;\n" +
			"    if (index == 49) limit = 0.75;\n" +
			"    if (index == 50) limit = 0.125;\n" +
			"    if (index == 51) limit = 0.625;\n" +
			"    if (index == 52) limit = 0.21875;\n" +
			"    if (index == 53) limit = 0.71875;\n" +
			"    if (index == 54) limit = 0.09375;\n" +
			"    if (index == 55) limit = 0.59375;\n" +
			"    if (index == 56) limit = 1.0;\n" +
			"    if (index == 57) limit = 0.5;\n" +
			"    if (index == 58) limit = 0.875;\n" +
			"    if (index == 59) limit = 0.375;\n" +
			"    if (index == 60) limit = 0.96875;\n" +
			"    if (index == 61) limit = 0.46875;\n" +
			"    if (index == 62) limit = 0.84375;\n" +
			"    if (index == 63) limit = 0.34375;\n" +
			"  }\n" +
			"\n" +
			"  return brightness < limit ? 0.0 : 1.0;\n" +
			"}\n" +
			"\n" +
			"float lum(vec3 color) {\n" +
			"    return (0.299*color.r + 0.587*color.g + 0.114*color.b);\n" +
			"}\n" +
			"\n" +
			"void main() {\n" +
			"\n" +
			"    vec4 color = texture2D(u_Texture0, v_TexCoordinate).rgba;\n" +
			"    vec2 uv_pos = ((v_TexCoordinate.xy + vec2(1.)) * vec2(0.5)) / u_dimension;\n" +
			"    if (color.a != 0.) {\n" +
			"//        float b = dot(vec3(1.), color.rgb) * 0.3333;\n" +
			"        float b = lum(color.rgb);\n" +
			"        if (u_type == 0) color.rgb *= vec3(dither2x2(gl_FragCoord.xy, b));\n" +
			"        else if (u_type == 1) color.rgb *= vec3(dither4x4(gl_FragCoord.xy, b));\n" +
			"        else if (u_type == 2) color.rgb *= vec3(dither8x8(gl_FragCoord.xy, b));\n" +
			"    }\n" +
			"    gl_FragColor = vec4(min(vec3(1.), color.rgb * 2.), 1.);\n" +
			"}";
}
