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
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.FilterMatrices;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.OPallTexture;

import java.nio.FloatBuffer;

/**
 * Created by HenryCo on 20/03/17.
 */

public class ConvolveTexture extends OPallTextureExtended {


	private static final String u_noiseLevel = "u_noiseLevel";
	private static final String u_matrixSize = "u_matrixSize";
	private static final String u_matrix3 = "u_matrix3";
	private static final String u_matrix5 = "u_matrix5";
	private static final String u_texDim = "u_screenDim";
	private static final String u_enable = "u_enable";

	private float[] original_filter_matrix;
	private float[] work_filter_matrix;
	private float center_scale = 1;
	private float noiseLevel = 0;
	private int matrix_size = 0;
	private int matrix_sqrt_size = 0;
	private int scale_pow;

	private boolean enable = true;

	public ConvolveTexture(Context context) {
		this(context, Filter.LINEAR);
	}
	public ConvolveTexture(Context context, Filter filter) {
		super(context, filter, OPallTexture.DEF_SHADER+".vert", FRAG_DIR);
		init();
	}
	public ConvolveTexture(Bitmap image, Context context) {
		this(image, context, Filter.LINEAR);
	}
	public ConvolveTexture(Bitmap image, Context context, Filter filter) {
		super(image, context, filter, OPallTexture.DEF_SHADER+".vert", FRAG_DIR);
		init();
	}
	public ConvolveTexture() {
		super(OPallTexture.DEFAULT_VERT_FILE, FRAG_FILE);
		init();
	}
	public ConvolveTexture(Bitmap image) {
		this(image, Filter.LINEAR);
	}
	public ConvolveTexture(Bitmap image, Filter filter) {
		super(image, filter, OPallTexture.DEFAULT_VERT_FILE, FRAG_FILE);
		init();
	}


	private void init() {
		setFilterMatrix(FilterMatrices.m_identity());
		setScalePower(2);
	}

	@Override
	protected void render(int program, Camera2D camera) {
		GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_texDim), getWidth(), getHeight());
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_matrixSize), matrix_sqrt_size);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_noiseLevel), noiseLevel);
		GLES20.glUniform1i(GLES20.glGetUniformLocation(program, u_enable), enable ? 1 : 0);
		String matrixTarget;
		if (matrix_sqrt_size == 3) matrixTarget = u_matrix3;
		else matrixTarget = u_matrix5;
		GLES20.glUniform1fv(GLES20.glGetUniformLocation(program, matrixTarget), matrix_size, FloatBuffer.wrap(work_filter_matrix));
	}




	public ConvolveTexture setFilterMatrix(float ... matrix) {

		if (matrix.length == 0 || (matrix.length == 1 && matrix[0] == -1))
			matrix = FilterMatrices.m_identity();
		if (Math.sqrt(matrix.length) % 2 == 0)
			throw new RuntimeException(getClass().getName()
					+ ": Filter matrix dimension must be 3x3, 5x5, 7x7, 9x9 ...");
		original_filter_matrix = matrix;
		matrix_size = original_filter_matrix.length;
		matrix_sqrt_size = (int) Math.sqrt(matrix_size);
		return setEffectScale(center_scale);
	}

	public ConvolveTexture setEffectScale(final float s) {

		float sc = (float) Math.min(1, Math.pow(s, scale_pow));
		float[] matrix = new float[matrix_size];
		int center = (int) (0.5f * (matrix_sqrt_size - 1f));
		int cp = (center * matrix_sqrt_size) + center;
		for (int i = 0; i < matrix_size; i++) {
			matrix[i] = original_filter_matrix[i];
			if (i != cp) matrix[i] *= sc;
			else {
				float x = matrix[i];
				if (x != 0) {
					float rx = 1f / x;
					matrix[i] = rx + ((x - rx) * sc);
				} else matrix[i] = 1 - sc;
			}
		}
		work_filter_matrix = normalize(matrix);
		center_scale = s;
		return this;
	}

	public ConvolveTexture setScalePower(int scale_pow) {
		this.scale_pow = scale_pow;
		return this;
	}

	public float getEffectScale() {
		return center_scale;
	}


	private static float[] normalize(float[] matrix) {

		float sum = 0;
		for (float v : matrix) sum += v;
		if (sum == 0) return matrix;
		for (int i = 0; i < matrix.length; i++)
			matrix[i] /= sum;
		return matrix;
	}

	public ConvolveTexture setNoiseLevel(float noiseLevel) {
		this.noiseLevel = noiseLevel;
		return this;
	}

	public float getNoiseLevel() {
		return noiseLevel;
	}



	public ConvolveTexture setFilterEnable(boolean b) {
		this.enable = b;
		return this;
	}
	public boolean isFilterEnable() {
		return enable;
	}

	private static final String FRAG_DIR = OPallTexture.FRAG_DIR+"/ConvolveFilter.frag";
	private static final String FRAG_FILE = "precision highp float;\n" +
			"// necessary part\n" +
			"varying vec4 v_Position;\n" +
			"varying vec4 v_WorldPos;\n" +
			"varying vec2 v_TexCoordinate;\n" +
			"\n" +
			"uniform sampler2D u_Texture0;\n" +
			"\n" +
			"\n" +
			"// custom part\n" +
			"uniform float u_matrixSize;  // 3, 5\n" +
			"uniform float u_matrix3[9];\n" +
			"uniform float u_matrix5[25];\n" +
			"uniform float u_noiseLevel;\n" +
			"uniform vec2 u_screenDim;\n" +
			"uniform int u_enable; // 0 == false, 1... == true\n" +
			"\n" +
			"\n" +
			"float noise1f(vec2 co){\n" +
			"    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);\n" +
			"}\n" +
			"//http://stackoverflow.com/questions/12964279/whats-the-origin-of-this-glsl-rand-one-liner\n" +
			"vec3 noise3f(vec3 color) {\n" +
			"    if (u_noiseLevel > 0.01) {\n" +
			"		 float l = length(v_TexCoordinate);"+
			"        float r = noise1f(vec2(color.r, l));\n" +
			"        float g = noise1f(vec2(color.g, l));\n" +
			"        float b = noise1f(vec2(color.b, l));\n" +
			"        return mix(normalize(vec3(r, g, b)), color, vec3(1. - u_noiseLevel));\n" +
			"    }\n" +
			"    return color;\n" +
			"}\n" +
			"\n" +
			"void main() {\n" +
			"\n" +
			"    if (u_enable == 0) gl_FragColor = texture2D(u_Texture0, v_TexCoordinate).rgba;\n" +
			"    else {\n" +
			"        vec2 cor = vec2((u_matrixSize - 1.) / 2.);\n" +
			"        vec3 rgb = vec3(0.);\n" +
			"\n" +
			"        for (float i = 0.; i < u_matrixSize; i++) {\n" +
			"            for (float k = 0.; k < u_matrixSize; k++) {\n" +
			"                vec2 ipos = vec2(i - cor.x, k - cor.y);\n" +
			"                vec3 irgb = texture2D(u_Texture0, v_TexCoordinate + (ipos / u_screenDim)).rgb;\n" +
			"\n" +
			"                int n = int(i * u_matrixSize + k);\n" +
			"                if (u_matrixSize == 3.) irgb *= u_matrix3[n];\n" +
			"                else irgb *= u_matrix5[n];\n" +
			"\n" +
			"                rgb += irgb;\n" +
			"            }\n" +
			"        }\n" +
			"        gl_FragColor = vec4(noise3f(rgb), 1.);\n" +
			"    }\n" +
			"}";



}
