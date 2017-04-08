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
 * Created by HenryCo on 08/04/17.
 */

public class BubbleTexture extends OPallTextureExtended {

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
			"uniform float u_radius;\n" +
			"uniform float u_sqrSize;\n" +
			"uniform float u_halfSize;\n" +
			"uniform vec2 u_dimension;\n" +
			"\n" +
			"void main() {\n" +
			"\n" +
			"    vec4 color = texture2D(u_Texture0, v_TexCoordinate);\n" +
			"    if (color.a != 0.) {\n" +
			"        vec2 pos = v_TexCoordinate * u_dimension;\n" +
			"\n" +
			"        float xc = (floor(pos.x / u_sqrSize) * u_sqrSize) + u_halfSize;\n" +
			"        float yc = (floor(pos.y / u_sqrSize) * u_sqrSize) + u_halfSize;\n" +
			"        float dist = length(vec2(pos.x - xc, pos.y - yc));\n" +
			"\n" +
			"        if (dist <= u_radius) gl_FragColor = color;\n" +
			"        else gl_FragColor = vec4(0.);\n" +
			"    } else gl_FragColor = vec4(0.);\n" +
			"}";
	private static final String u_radius = "u_radius";
	private static final String u_sqrSize = "u_sqrSize";
	private static final String u_halfSize = "u_halfSize";
	private static final String u_dimension = "u_dimension";


	private float radius = 0.75f;
	private float square = 50f;



	public BubbleTexture() {
		super(VERT, FRAG);
		setFlip(false, true);

	}

	public BubbleTexture(Filter filter) {
		super(filter, VERT, FRAG);
		setFlip(false, true);

	}

	public BubbleTexture(Bitmap image) {
		super(image, VERT, FRAG);
		setFlip(false, true);

	}

	public BubbleTexture(Bitmap image, Filter filter) {
		super(image, filter, VERT, FRAG);
		setFlip(false, true);

	}


	public float getRadius() {
		return radius;
	}

	public BubbleTexture setRadius(float radius) {
		this.radius = radius;
		return this;
	}

	public float getSquare() {
		return square;
	}

	public BubbleTexture setSquare(float square) {
		this.square = square;
		return this;
	}


	@Override
	protected void render(int program, Camera2D camera) {
		float half = square * 0.5f;
		GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), getScreenWidth(), getScreenHeight());
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_radius), radius * half);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_sqrSize), square);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_halfSize), half);
	}
}
