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

package net.henryco.opalette.api.glES.render.graphics.shaders.shapes;

import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 28/03/17.
 */

public class Borders extends OPallShapeBuffered {

	private static final String VERT = "#version 100\n" +
			"\n" +
			"attribute vec4 a_Position;\n" +
			"uniform mat4 u_MVPMatrix;\n" +
			"\n" +
			"void main() {\n" +
			"    gl_Position = u_MVPMatrix * a_Position;\n" +
			"}";
	private static final String FRAG = "#version 100\n" +
			"precision mediump float;\n" +
			"\n" +
			"uniform vec2 u_dimension;\n" +
			"uniform float u_size;\n" +
			"uniform vec4 u_color;\n" +
			"\n" +
			"void main() {\n" +
			"\n" +
			"    vec2 pos = gl_FragCoord.xy;\n" +
			"    if (pos.x < u_size || pos.x > u_dimension.x - u_size\n" +
			"        || pos.y < u_size || pos.y > u_dimension.y - u_size)\n" +
			"            gl_FragColor = u_color;\n" +
			"    else gl_FragColor = vec4(.0);\n" +
			"}";
	private static final String u_dimension = "u_dimension";
	private static final String u_size = "u_size";
	private static final String u_color = "u_color";


	private static final float size_scale = 0.1f;

	public final GLESUtils.Color color = new GLESUtils.Color();
	private float size;

	public Borders() {
		this(0, 0);
	}
	public Borders(int w, int h) {
		super(VERT, FRAG);
		create(w, h);
		setSize(0.2f)
				.setColor(GLESUtils.Color.WHITE)
				.setVisible(false);
	}

	public Borders setSize(float size) {
		this.size = size;
		update();
		return this;
	}


	@Override
	public void setScreenDim(float w, float h) {
		super.setScreenDim(w, h);
		update();
	}

	public Borders setColor(GLESUtils.Color color) {
		this.color.set(color);
		update();
		return this;
	}

	public float getSize() {
		return size;
	}


	@Override
	protected void render(int program, Camera2D camera) {
		float bSize = Math.max(getScreenHeight(), getScreenWidth()) * getSize() * size_scale;
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_size), bSize);
		GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), getScreenWidth(), getScreenHeight());
		GLES20.glUniform4f(GLES20.glGetUniformLocation(program, u_color), color.r, color.g, color.b, color.a);
	}


}
