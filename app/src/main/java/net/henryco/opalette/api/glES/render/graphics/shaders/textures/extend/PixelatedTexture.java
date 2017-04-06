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
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.OPallTexture;

/**
 * Created by HenryCo on 06/04/17.
 */

public class PixelatedTexture extends OPallTextureExtended {

	private static final String VERT = OPallTexture.DEFAULT_VERT_FILE;
	private static final String FRAG = "#version 100\n" +
			"precision mediump float;\n" +
			"\n" +
			"varying vec4 v_Position;\n" +
			"varying vec4 v_WorldPos;\n" +
			"varying vec2 v_TexCoordinate;\n" +
			"\n" +
			"uniform sampler2D u_Texture0;\n" +
			"\n" +
			"uniform float u_pixelsNumb;\n" +
			"uniform vec2 u_dPix;\n" +
			"\n" +
			"void main() {\n" +
			"\n" +
			"\tvec4 color = vec4(0.);\n" +
			"\tcolor = texture2D(u_Texture0, v_TexCoordinate);\n" +
			"\tif (color.a != 0.) {\n" +
			"\n" +
			"        float dx = u_dPix.x * (1.0 / u_pixelsNumb);\n" +
			"        float dy = u_dPix.y * (1.0 / u_pixelsNumb);\n" +
			"        vec2 pixel = vec2(dx * floor(v_TexCoordinate.x / dx), dy * floor(v_TexCoordinate.y / dy));\n" +
			"        color = texture2D(u_Texture0, pixel);\n" +
			"\t}\n" +
			"\tgl_FragColor = color;\n" +
			"}";
	private static final String u_pixelsNumb = "u_pixelsNumb";
	private static final String u_dPix = "u_dPix";

	private float pixelsNumb = 512f;
	private float pixel_dx = 10f;
	private float pixel_dy = 10f;


	public PixelatedTexture(Filter filter) {
		super(filter, VERT, FRAG);
		setFlip(false, true);
	}

	public PixelatedTexture() {
		super(VERT, FRAG);
		setFlip(false, true);
	}

	public PixelatedTexture(Bitmap image) {
		super(image, VERT, FRAG);
		setFlip(false, true);
	}

	public PixelatedTexture(Bitmap image, Filter filter) {
		super(image, filter, VERT, FRAG);
		setFlip(false, true);
	}


	public float getPixelsNumb() {
		return pixelsNumb;
	}

	public float getPixelQuantum() {
		return pixel_dx;
	}

	public PixelatedTexture setPixelsNumb(float pixelsNumb) {
		this.pixelsNumb = pixelsNumb;
		return this;
	}

	public PixelatedTexture setPixelQuantum(float dxy) {
		this.pixel_dx = dxy;
		this.pixel_dy = dxy;
		return this;
	}


	@Override
	protected void render(int program, Camera2D camera) {
		System.out.println(pixelsNumb);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_pixelsNumb), pixelsNumb);
		GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dPix), pixel_dx, pixel_dy);
	}
}
