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

public class MinMaxTexture extends OPallTextureExtended {


	private static final String VERT = DEFAULT_VERT_FILE;
	private static final String FRAG = "#version 100\n" +
			"precision mediump float;\n" +
			"\n" +
			"varying vec4 v_Position;\n" +
			"varying vec4 v_WorldPos;\n" +
			"varying vec2 v_TexCoordinate;\n" +
			"\n" +
			"uniform sampler2D u_Texture0;\n" +
			"\n" +
			"uniform vec2 u_size;\n" +
			"uniform int u_type; // 0 - min, 1 - max\n" +
			"uniform float u_kernel;\n" +
			"uniform float u_power;\n" +
			"\n" +
			"void main() {\n" +
			"\n" +
			"    vec4 texColor = texture2D(u_Texture0, v_TexCoordinate).rgba;\n" +
			"    if (texColor.a != 0. && (u_type == 0 || u_type == 1)) {\n" +
			"        vec2 pos = v_TexCoordinate.xy;\n" +
			"        float s = (u_kernel - 1.) * 0.5;\n" +
			"        bool type = u_type == 0;\n" +
			"\n" +
			"        vec3 col_rgb = type ? vec3(1.) : vec3(0.);\n" +
			"        for (float i = 0.; i < u_kernel; i += 1.) {\n" +
			"            for (float k = 0.; k < u_kernel; k += 1.) {\n" +
			"                float si = (i + s) / u_size.x;\n" +
			"                float sk = (k + s) / u_size.y;\n" +
			"                vec3 rgb = texture2D(u_Texture0, vec2(pos.x - si, pos.y - sk)).rgb;\n" +
			"                col_rgb = type ? min(col_rgb, rgb) : max(col_rgb, rgb);\n" +
			"            }\n" +
			"        }\n" +
			"        gl_FragColor = vec4(mix(texColor.rgb, col_rgb, u_power).rgb, 1.);\n" +
			"    }   else gl_FragColor = texColor;\n" +
			"}";
	private static final String u_size = "u_size";
	private static final String u_type = "u_type";
	private static final String u_kernel = "u_kernel";
	private static final String u_power = "u_power";

	public static final int TYPE_NONE = -1;
	public static final int TYPE_EROSION = 0;
	public static final int TYPE_DILATION = 1;
	private float effectScale = 0;
	private int kernelSize = 3;
	private int type = TYPE_NONE;

	private boolean firstTime = true;

	public MinMaxTexture() {
		this(TYPE_NONE);
	}
	public MinMaxTexture(Filter filter, int type) {
		super(filter, VERT, FRAG);
		setFlip(false, true);
		setFilterType(type);
	}

	public MinMaxTexture(int type) {
		super(VERT, FRAG);
		setFlip(false, true);
		setFilterType(type);
	}

	public MinMaxTexture(Bitmap image, int type) {
		super(image, VERT, FRAG);
		setFlip(false, true);
		setFilterType(type);
	}

	public MinMaxTexture(Bitmap image, Filter filter, int type) {
		super(image, filter, VERT, FRAG);
		setFlip(false, true);
		setFilterType(type);
	}


	public MinMaxTexture setFilterType(int type) {
		this.type = type;
		return this;
	}

	public MinMaxTexture setKernelSize(int kernelSize) {
		this.kernelSize = Math.min(Math.max(3, kernelSize), 9);
		return this;
	}

	public MinMaxTexture setEffectScale(float effectScale) {
		this.effectScale = Math.min(Math.max(effectScale, 0), 1);
		return this;
	}

	public float getEffectScale() {
		return effectScale;
	}

	public int getKernelSize() {
		return kernelSize;
	}

	public int getType() {
		return type;
	}

	@Override
	public void setScreenDim(float w, float h) {
		if (firstTime) super.setScreenDim(w, h);
		firstTime = false;
	}

	@Override
	protected void render(int program, Camera2D camera) {

		float w = getWidth() == 0 ? getScreenWidth() : getWidth();
		float h = getHeight() == 0 ? getScreenHeight() : getHeight();

		GLES20.glUniform1i(GLES20.glGetUniformLocation(program, u_type), type);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_kernel), kernelSize);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_power), effectScale);
		GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_size), w, h);
	}
}
