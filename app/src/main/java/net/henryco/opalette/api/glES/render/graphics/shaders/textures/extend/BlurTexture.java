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
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.FilterMatrices;
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.TouchLines;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 * Created by HenryCo on 07/04/17.
 */

public class BlurTexture extends OPallTextureExtended {

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
			"//custom part\n" +
			"uniform vec3 u_line[2]; // Ax + By + C = 0\n" +
			"uniform vec2 u_dimension;\n" +
			"uniform float u_power;\n" +
			"\n" +
			"uniform float u_matrixSize;  // 3, 5\n" +
			"uniform float u_matrix3[9];\n" +
			"uniform float u_matrix5[25];\n" +
			"\n" +
			"void main() {\n" +
			"\n" +
			"    vec2 pos = v_TexCoordinate.xy;\n" +
			"    vec2 point = vec2(pos.x, 1. - pos.y) * u_dimension;\n" +
			"    float scale = 1.;\n" +
			"    vec4 pointColor = texture2D(u_Texture0, pos);\n" +
			"\n" +
			"    if (pointColor.a != 0.) {\n" +
			"\n" +
			"        float py1 = (-1.) * ((u_line[0].x * point.x) + u_line[0].z) / u_line[0].y;\n" +
			"        float py2 = (-1.) * ((u_line[1].x * point.x) + u_line[1].z) / u_line[1].y;\n" +
			"        if ((point.y > py1 && point.y < py2) || (point.y > py2 && point.y < py1)) {\n" +
			"            float d1 = (abs((u_line[0].x * point.x) + (u_line[0].y * point.y) + u_line[0].z)) / sqrt(u_line[0].x*u_line[0].x + u_line[0].y*u_line[0].y);\n" +
			"            float d2 = (abs((u_line[1].x * point.x) + (u_line[1].y * point.y) + u_line[1].z)) / sqrt(u_line[1].x*u_line[1].x + u_line[1].y*u_line[1].y);\n" +
			"            float d = 2. * min(d1, d2) / (d1 + d2);\n" +
			"            scale = pow((1. - d), u_power);\n" +
			"        }\n" +
			"\n" +
			"        vec2 cor = vec2((u_matrixSize - 1.) / 2.);\n" +
			"        vec3 rgb = vec3(0.);\n" +
			"\n" +
			"        for (float i = 0.; i < u_matrixSize; i++) {\n" +
			"            for (float k = 0.; k < u_matrixSize; k++) {\n" +
			"                vec2 ipos = vec2(i - cor.x, k - cor.y);\n" +
			"                vec3 irgb = texture2D(u_Texture0, v_TexCoordinate + (ipos / u_dimension)).rgb;\n" +
			"                int n = int(i * u_matrixSize + k);\n" +
			"                if (u_matrixSize == 3.) irgb *= u_matrix3[n];\n" +
			"                else irgb *= u_matrix5[n];\n" +
			"                rgb += irgb;\n" +
			"            }\n" +
			"        }\n" +
			"        gl_FragColor = vec4(mix(pointColor.rgb, rgb, scale), 1.);\n" +
			"    } else gl_FragColor = vec4(0.);\n" +
			"}";
	private static final String u_line = "u_line";
	private static final String u_dimension = "u_dimension";
	private static final String u_power = "u_power";
	private static final String u_matrixSize = "u_matrixSize";
	private static final String u_matrix3 = "u_matrix3";
	private static final String u_matrix5 = "u_matrix5";

	private static final float MAX_POWER = 4;
	private static final float DEF_POWER = 2;
	private final TouchLines touchLines;

	private float power = 2;

	private int matrix_sqrt_size;
	private float[] matrix;
	private boolean pointsVisible;

	private boolean fistTime = true;

	public BlurTexture(Filter filter) {
		super(filter, VERT, FRAG);
		setFlip(false, true);
		setBlurMatrix(FilterMatrices.m_diagShatter());
		touchLines = new TouchLines();
		pointsVisible = false;
	}

	public BlurTexture() {
		super(VERT, FRAG);
		setFlip(false, true);
		setBlurMatrix(FilterMatrices.m_diagShatter());
		touchLines = new TouchLines();
		pointsVisible = false;
	}

	public BlurTexture(Bitmap image) {
		super(image, VERT, FRAG);
		setFlip(false, true);
		setBlurMatrix(FilterMatrices.m_diagShatter());
		touchLines = new TouchLines();
		pointsVisible = false;
	}

	public BlurTexture(Bitmap image, Filter filter) {
		super(image, filter, VERT, FRAG);
		setFlip(false, true);
		setBlurMatrix(FilterMatrices.m_diagShatter());
		touchLines = new TouchLines();
		pointsVisible = false;
	}


	public BlurTexture setDefaultSize(float w, float h) {
		touchLines.setDefaultSize(w, h);
		touchLines.reset();
		return this;
	}


	private float innerW, innerH;

	@Override
	public void setScreenDim(float w, float h) {
		if (fistTime) {
			super.setScreenDim(w, h);
			setDefaultSize(w, h);
		}
		touchLines.setScreenDim(w, h);
		innerW = w;
		innerH = h;
		fistTime = false;
	}

	public BlurTexture setBlurMatrix(float[] matrix) {
		float size_sqrt = (float) Math.sqrt(matrix.length);
		if (size_sqrt % 2 == 0) throw new RuntimeException(getClass().getName()
				+ ": Filter matrix dimension must be 3x3, 5x5, 7x7, 9x9 ...");
		matrix_sqrt_size = (int) size_sqrt;
		this.matrix = FilterMatrices.normalize(matrix);
		return this;
	}


	public BlurTexture resetPower() {
		this.power = DEF_POWER;
		return this;
	}

	public float getPower() {
		return 1f - (power / MAX_POWER);
	}

	public BlurTexture setPower(float power) {
		this.power = (1f - power) * MAX_POWER;
		return this;
	}

	public boolean isPointsVisible() {
		return pointsVisible;
	}

	public BlurTexture setPointsVisible(boolean pointsVisible) {
		this.pointsVisible = pointsVisible;
		this.touchLines.setVisible(pointsVisible);
		return this;
	}

	public BlurTexture setPoints(float x1, float y1, float x2, float y2) {
		touchLines.setPoints(x1, y1, x2, y2);
		return this;
	}

	public BlurTexture setPoints(float[] p1, float[] p2) {
		touchLines.setPoints(p1, p2);
		return this;
	}

	public BlurTexture reset() {
		touchLines.reset();
		return this;
	}


	@Override
	public synchronized void render(Camera2D camera2D, OPallConsumer<Integer> setter) {
		super.render(camera2D, setter);
		if (pointsVisible) touchLines.render(camera2D);
	}

	@Override
	protected void render(int program, Camera2D camera) {
		GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), innerW, innerH);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_matrixSize), matrix_sqrt_size);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_power), power);
		GLES20.glUniform3fv(GLES20.glGetUniformLocation(program, u_line), 2, touchLines.getCoefficients(), 0);
		String matrixTarget;
		if (matrix_sqrt_size == 3) matrixTarget = u_matrix3;
		else matrixTarget = u_matrix5;
		GLES20.glUniform1fv(GLES20.glGetUniformLocation(program, matrixTarget), matrix_sqrt_size * matrix_sqrt_size, FloatBuffer.wrap(matrix));
	}

	@Override
	public String toString() {
		return "BlurTexture{" +
				"matrix_sqrt_size=" + matrix_sqrt_size +
				", matrix=" + Arrays.toString(matrix) +
				'}';
	}
}
