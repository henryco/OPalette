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

package net.henryco.opalette.api.glES.render.graphics.units.palette;

import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.OPallTexture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 08/03/17.
 */

public class CellPaletter implements OPallRenderable {


	private static final String VERT_FILE = OPallTexture.DEFAULT_VERT_FILE;
	private static final String[] FRAG_FILE = {

			"precision highp float;\n" +
					"\n" +
					"varying vec4 v_Position;\n" +
					"varying vec4 v_WorldPos;\n" +
					"varying vec2 v_TexCoordinate;\n" +
					"\n" +
					"uniform sampler2D u_Texture0;\n" +
					"\n" +
					"uniform vec2 u_dimension;\n" +
					"uniform float u_cellSize;\n" +
					"uniform float u_margin;\n" +
					"uniform float u_numb;\n" +
					"uniform vec2 u_activeDim;\n" +
					"\n" +
					"void main() {\n" +
					"\n" +
					"    vec2 pos = gl_FragCoord.xy / u_dimension;\n" +
					"    if (pos.x <= u_activeDim.x && pos.y <= u_activeDim.y && pos.x >= 0. && pos.y >= 0.) {\n" +
					"\n" +
					"        float n = floor(gl_FragCoord.x / u_cellSize);\n" +
					"\n" +
					"        vec2 margin = vec2(\n" +
					"            (n == 0.) ? u_margin : u_margin * .5,\n" +
					"            (n == u_numb - 1.) ? u_margin : u_margin * .5\n" +
					"        );\n" +
					"\n" +
					"        vec2 st_ed = vec2(n, n + 1.) * u_cellSize;\n" +
					"        vec2 mrgnL = vec2(st_ed.x, st_ed.x + margin.x);\n" +
					"        vec2 mrgnR = vec2(st_ed.y - margin.y, st_ed.y);\n" +
					"\n" +
					"        if ((gl_FragCoord.x >= mrgnL.x && gl_FragCoord.x <= mrgnL.y) ||\n" +
					"            (gl_FragCoord.x >= mrgnR.x && gl_FragCoord.x <= mrgnR.y))\n" +
					"                gl_FragColor = vec4(0.);\n" +
					"        else if (texture2D(u_Texture0, pos).a != 0.) {\n" +
					"            vec3 color = vec3(0.);\n" +
					"            for (float i = st_ed.x; i < st_ed.y; i += 1.) {\n" +
					"                vec2 point = vec2(i / u_dimension.x, pos.y);\n" +
					"                color += texture2D(u_Texture0, point).rgb;\n" +
					"            }\n" +
					"            gl_FragColor = vec4(color / u_cellSize, 1.);\n" +
					"        } else gl_FragColor = vec4(0.);\n" +
					"    } else gl_FragColor = vec4(0.);\n" +
					"}"
			,
			"precision highp float;\n" +
					"\n" +
					"varying vec4 v_Position;\n" +
					"varying vec4 v_WorldPos;\n" +
					"varying vec2 v_TexCoordinate;\n" +
					"\n" +
					"uniform sampler2D u_Texture0;\n" +
					"\n" +
					"uniform vec2 u_dimension;\n" +
					"uniform float u_cellSize;\n" +
					"uniform float u_margin;\n" +
					"uniform float u_numb;\n" +
					"uniform vec2 u_activeDim;\n" +
					"\n" +
					"void main() {\n" +
					"\n" +
					"    vec2 pos = gl_FragCoord.xy / u_dimension;\n" +
					"\n" +
					"    if (pos.x >= 0. && pos.y >= 0. && pos.x <= u_activeDim.x && pos.y <= u_activeDim.y) {\n" +
					"        float n = floor(gl_FragCoord.y / u_cellSize);\n" +
					"\n" +
					"        vec2 margin = vec2(\n" +
					"            (n == 0.) ? u_margin : u_margin * .5,\n" +
					"            (n == u_numb - 1.) ? u_margin : u_margin * .5\n" +
					"        );\n" +
					"\n" +
					"        vec2 st_ed = vec2(n, n + 1.) * u_cellSize;\n" +
					"        vec2 mrgnB = vec2(st_ed.x, st_ed.x + margin.x);\n" +
					"        vec2 mrgnT = vec2(st_ed.y - margin.y, st_ed.y);\n" +
					"\n" +
					"        if ((gl_FragCoord.y >= mrgnB.x && gl_FragCoord.y <= mrgnB.y) ||\n" +
					"            (gl_FragCoord.y >= mrgnT.x && gl_FragCoord.y <= mrgnT.y))\n" +
					"                gl_FragColor = vec4(0.);\n" +
					"        else if (texture2D(u_Texture0, pos).a != 0.) {\n" +
					"            vec3 color = vec3(0.);\n" +
					"            for (float i = st_ed.x; i < st_ed.y; i += 1.) {\n" +
					"                vec2 point = vec2(pos.x, i / u_dimension.y);\n" +
					"                color += texture2D(u_Texture0, point).rgb;\n" +
					"            }\n" +
					"            gl_FragColor = vec4(color / u_cellSize, 1.);\n" +
					"        } else gl_FragColor = vec4(0.);\n" +
					"    } else gl_FragColor = vec4(0.);\n" +
					"}"
	};

	private static final String u_cellSize = "u_cellSize";
	private static final String u_margin = "u_margin";
	private static final String u_dimension = "u_dimension";
	private static final String u_numb = "u_numb";
	private static final String u_activeDim = "u_activeDim";

	public enum CellType {

		Horizontal(0),
		Vertical(1);

		public int type;
		CellType(int type) {
			this.type = type;
		}
	}

	private final float[] active_reg = {0,0};
	private boolean fistTime = true;
	private float margin_pct = 0.1f;
	private float width = 0;
	private float height = 0;
	private int numb = 4;
	private int type = 0;

	private FrameBuffer buffer;
	private Texture texture;



	public CellPaletter(CellType type, int w, int h) {
		texture = new Texture(VERT_FILE, FRAG_FILE[type.type]);
		create(w, h);
		setScreenDim(w, h);
		this.type = type.type;
	}

	
	public void generate(Texture source, Camera2D camera2D) {

		float cellSize = active_reg[type] / numb;
		float margin = cellSize * margin_pct;

		texture.set(source);
		buffer.beginFBO(() -> texture.render(camera2D, program -> {
			GLESUtils.clear();
			GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_cellSize), cellSize);
			GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_margin), margin);
			GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), width, height);
			GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_activeDim), active_reg[0], active_reg[1]);
			GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_numb), numb);
		}));
	}



	public CellPaletter setCellNumb(int n) {
		this.numb = n;
		return this;
	}

	public CellPaletter setMargin_pct(float m) {
		this.margin_pct = m;
		return this;
	}

	public CellPaletter create(float w, float h) {
		buffer = OPallFBOCreator.FrameBuffer();
		buffer.createFBO((int) w, (int) h, false);
		texture.setScreenDim(w, h);
		fistTime = true;
		return this;
	}



	@Override
	public void render(Camera2D camera) {
		buffer.render(camera);
	}



	@Override
	public void setScreenDim(float w, float h) {
		this.active_reg[0] = w;
		this.active_reg[1] = h;
		if (fistTime) {
			this.width = w;
			this.height = h;
		}
		fistTime = false;
	}

	@Override
	public int getWidth() {
		return (int) width;
	}

	@Override
	public int getHeight() {
		return (int) height;
	}
}
