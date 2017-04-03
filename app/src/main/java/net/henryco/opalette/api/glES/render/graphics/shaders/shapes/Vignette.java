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

/**
 * Created by HenryCo on 03/04/17.
 */

public class Vignette extends OPallShapeBuffered {

	private static final String VERT_FILE = "#version 100\n" +
			"\n" +
			"attribute vec4 a_Position;\n" +
			"uniform mat4 u_MVPMatrix;\n" +
			"\n" +
			"void main() {\n" +
			"    gl_Position = u_MVPMatrix * a_Position;\n" +
			"}";
	private static final String FRAG_FILE = "#version 100\n" +
			"\n" +
			"precision mediump float;\n" +
			"\n" +
			"uniform vec2 u_dimension;\n" +
			"uniform float u_power;\n" +
			"uniform float u_radius;\n" +
			"uniform float u_activeRadius;\n" +
			"\n" +
			"void main() {\n" +
			"\n" +
			"    vec2 pos = ((gl_FragCoord.xy / u_dimension.xy) - vec2(.5)) * vec2(2.);\n" +
			"    float r = length(pos);\n" +
			"    if (r >= u_radius) {// 2.41 = 1 / (sqrt(2) − 1)\n" +
			"        gl_FragColor = vec4(vec3(0.), pow((u_activeRadius * (r - u_radius)), u_power));\n" +
			"    }\n" +
			"    else gl_FragColor = vec4(0.);\n" +
			"}";
	private static final String u_dimension = "u_dimension";
	private static final String u_power = "u_power";
	private static final String u_radius = "u_radius";
	private static final String u_activeRadius = "u_activeRadius";

	private static final float max = 5;

	private float power = max;
	private float radius = 1;
	private boolean active = true;

	public Vignette(float w, float h) {
		super(VERT_FILE, FRAG_FILE, w, h);
		setVisible(false);
	}
	public Vignette() {
		super(VERT_FILE, FRAG_FILE);
		setVisible(false);
	}

	/**
	 * @param p power of vignette, this range is normed.
	 */
	public Vignette setPower(float p) {
		power = max * (1 - p);
		update();
		return this;
	}

	public Vignette setRadius(float r) {
		this.radius = 1 - r;
		update();
		return this;
	}

	public float getRadius() {
		return 1 - radius;
	}

	public float getPower() {
		return 1f - (power / max);
	}

	public Vignette setActive(boolean active) {
		this.active = active;
		return this;
	}

	public boolean isActive() {
		return active;
	}

	@Override
	protected void render(int program, Camera2D camera) {
		GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), getScreenWidth(), getScreenHeight());
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_activeRadius), radius / (1.41f - radius));
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_power), Math.max(power, 0.5f));
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_radius), radius);
	}

}
