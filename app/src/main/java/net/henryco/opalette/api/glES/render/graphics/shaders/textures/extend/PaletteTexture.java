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

import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.OPallMultiTexture;

/**
 * Created by HenryCo on 31/03/17.
 */

public class PaletteTexture extends OPallMultiTextureExtended {

	public static final int TYPE_HORIZONTAL = 0;
	public static final int TYPE_VERTICAL = 1;
	private static String getFrag(int type) {
		if (type == TYPE_HORIZONTAL) return FRAG_PROGRAM_HORIZONTAL;
		else if (type == TYPE_VERTICAL) return "";
		else return "";
	}


	private float barEnd = 0;
	private float barStart = 0;
	private final float[] dimension = {0,0};
	private float[] externalLineCoeffs = {};

	public PaletteTexture(final int type) {
		super(VERT_PROGRAM, getFrag(type), 2);
	}

	public PaletteTexture(int type, int w, int h) {
		this(type);
		setScreenDim(w, h);
		setDimension(w, h);
	}


	@Override
	protected void render(int program, Camera2D camera) {
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_barEnd), barEnd);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_barStart), barStart);
		GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), dimension[0], dimension[1]);
		GLES20.glUniform3fv(GLES20.glGetUniformLocation(program, u_line), 2, externalLineCoeffs, 0);
	}


	public PaletteTexture setStart(float start) {
		this.barStart = start;
		return this;
	}
	public PaletteTexture setEnd(float end) {
		this.barEnd = end;
		return this;
	}
	public PaletteTexture setDimension(float w, float h) {
		dimension[0] = w;
		dimension[1] = h;
		return this;
	}
	public PaletteTexture setRangeLineCoeffs(float[] coeffs) {
		this.externalLineCoeffs = coeffs;
		return this;
	}

	private static final String u_barEnd = "u_barEnd";
	private static final String u_barStart = "u_barStart";
	private static final String u_dimension = "u_dimension";
	private static final String u_line = "u_line";

	private static final String VERT_PROGRAM = OPallMultiTexture.DEFAULT_VERT_FILE;
	private static final String FRAG_PROGRAM_HORIZONTAL = "#version 100\n" +
					"precision mediump float;\n" +
					"\n" +
					"varying vec4 v_Position;\n" +
					"varying vec4 v_WorldPos;\n" +
					"varying vec2 v_TexCoordinate[5];\n" +
					"\n" +
					"uniform sampler2D u_Texture0;\n" +
					"uniform sampler2D u_Texture1;\n" +
					"uniform sampler2D u_Texture2;\n" +
					"uniform sampler2D u_Texture3;\n" +
					"uniform sampler2D u_Texture4;\n" +
					"\n" +
					"uniform vec2 u_dimension;\n" +
					"uniform int u_texNumb;\n" +
					"uniform float u_barStart;\n" +
					"uniform float u_barEnd;\n" +
					"uniform vec3 u_line[2]; // Ax + By + C = 0\n" +
					"\n" +
					"void main() {\n" +
					"\n" +
					"    float trueHeight = 0.0;\n" +
					"    vec3 p_col = vec3(0.0);\n" +
					"\n" +
					"    for (float y = 0.0; y < u_dimension.y; y += 1.0) {\n" +
					"\n" +
					"        vec2 pointNormed = vec2(v_TexCoordinate[1].s, y / u_dimension.y);\n" +
					"        vec2 point = vec2(pointNormed.x * u_dimension.x, u_dimension.y * (1. - pointNormed.y));\n" +
					"        vec4 pointColor = texture2D(u_Texture0, pointNormed).rgba;\n" +
					"\n" +
					"        if (pointColor.a != 0.0 && y > 0. && (y >= u_barEnd || y <= u_barStart)) {\n" +
					"\n" +
					"            float py1 = (-1.) * ((u_line[0].x * point.x) + u_line[0].z) / u_line[0].y;\n" +
					"            float py2 = (-1.) * ((u_line[1].x * point.x) + u_line[1].z) / u_line[1].y;\n" +
					"\n" +
					"            if ((point.y > py1 && point.y < py2) || (point.y > py2 && point.y < py1)) {\n" +
					"                p_col += pointColor.rgb;\n" +
					"                trueHeight += 1.0;\n" +
					"            }\n" +
					"        }\n" +
					"    }\n" +
					"\n" +
					"    vec3 color = p_col / max(trueHeight, 1.0);\n" +
					"    gl_FragColor = vec4(color, 1.0);\n" +
					"}";
}
