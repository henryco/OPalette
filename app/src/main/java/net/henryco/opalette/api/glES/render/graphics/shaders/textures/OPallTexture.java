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

package net.henryco.opalette.api.glES.render.graphics.shaders.textures;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import net.henryco.opalette.api.glES.render.graphics.shaders.OPallShader;


/**
 * Created by HenryCo on 23/02/17.
 */

public interface OPallTexture extends OPallShader {


	String DEFAULT_VERT_FILE =
			"attribute vec4 a_Position;\n" +
			"attribute vec2 a_TexCoordinate;\n" +
			"\n" +
			"varying vec4 v_Position;\n" +
			"varying vec4 v_WorldPos;\n" +
			"varying vec2 v_TexCoordinate;\n" +
			"\n" +
			"uniform mat4 u_MVPMatrix;\n" +
			"uniform vec2 u_Flip;\n" +
			"\n" +
			"vec2 flip(vec2 f, vec2 tex) {\n" +
			"\n" +
			"    float x = min(1., f.x + 1.) - f.x * tex.x;\n" +
			"    float y = min(1., f.y + 1.) - f.y * tex.y;\n" +
			"    return vec2(x, y);\n" +
			"}\n" +
			"\n" +
			"void main() {\n" +
			"\n" +
			"    v_Position = a_Position;\n" +
			"    v_WorldPos = u_MVPMatrix * a_Position;\n" +
			"    v_TexCoordinate = flip(u_Flip, a_TexCoordinate);\n" +
			"    gl_Position = v_WorldPos;\n" +
			"}";



	String DEFAULT_FRAG_FILE =
			"precision mediump float;\n" +
			"\n" +
			"varying vec4 v_Position;\n" +
			"varying vec4 v_WorldPos;\n" +
			"varying vec2 v_TexCoordinate;\n" +
			"\n" +
			"uniform sampler2D u_Texture0;\n" +
			"\n" +
			"\n" +
			"void main() {\n" +
			"    gl_FragColor = texture2D(u_Texture0, v_TexCoordinate).rgba;\n" +
			"}";





	enum Filter {
		LINEAR(GLES20.GL_LINEAR),
		NEAREST(GLES20.GL_NEAREST);

		public int type;
		Filter(int type) {
			this.type = type;
		}
	}


	int[] ACTIVE_TEXTURES = {
			GLES20.GL_TEXTURE0,
			GLES20.GL_TEXTURE1,
			GLES20.GL_TEXTURE2,
			GLES20.GL_TEXTURE3,
			GLES20.GL_TEXTURE4,
			GLES20.GL_TEXTURE5,
			GLES20.GL_TEXTURE6,
			GLES20.GL_TEXTURE7,
			GLES20.GL_TEXTURE8,
			GLES20.GL_TEXTURE9
	};




	int COORDS_PER_TEXEL = 2;
	int texelStride = COORDS_PER_TEXEL * 4; // float = 4bytes



	String DEF_SHADER = methods.createDefaultShader();
	String FRAG_DIR = SHADERS_DIR+"/default/frags";
	String u_Flip = "u_Flip";



	OPallTexture setBitmap(Bitmap image, Filter filterMin, Filter filterMag);
	OPallTexture setBitmap(Bitmap image, Filter filter);
	OPallTexture setBitmap(Bitmap image);
	OPallTexture setFlip(boolean x, boolean y);
	OPallTexture setSize(float w, float h);
	OPallTexture setRegion(float x, float y, float width, float height);
	OPallTexture setTextureDataHandle(int textureDataHandle);
	OPallTexture setRotation(float angle);
	int getTextureDataHandle();
	float getRotation();
	boolean[] getFlip();

	final class methods {


		private static String createDefaultShader() {
			// TODO
			return SHADERS_DIR+"/default/Default";
		}


		public static void applyFlip(int program, boolean[] xy) {
			applyFlip(program, xy[0], xy[1]);
		}


		public static void applyFlip(int program, boolean x, boolean y) {
			GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_Flip), flipValue(x), flipValue(y));
		}


		public static int flipValue(boolean f) {
			return f ? 1 : -1;
		}


		public static int loadTexture(int n, final Bitmap bitmap, final int filter_min, final int filter_mag) {
			final int[] textureHandle = new int[1];
			GLES20.glGenTextures(1, textureHandle, 0);
			if (textureHandle[0] != 0) {
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, filter_min);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, filter_mag);
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0);
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, n);

				return textureHandle[0];
			}
			throw new RuntimeException("Error loading texture, probably lost GL_context or bad pass arguments");
		}


		public static int loadTexture(final Bitmap bitmap, final int filter_min, final int filter_mag) {
			return loadTexture(0, bitmap, filter_min, filter_mag);
		}


		public static int loadTexture(final Bitmap bitmap, final Filter filter_min, final Filter filter_mag) {
			return loadTexture(0, bitmap, filter_min, filter_mag);
		}


		public static int loadTexture(int n, final Bitmap bitmap, final Filter filter_min, final Filter filter_mag) {
			return loadTexture(n, bitmap, filter_min.type, filter_mag.type);
		}


		public static void bindTexture(int n, int textureDataHandle, int mTextureUniformHandle) {
			GLES20.glActiveTexture(ACTIVE_TEXTURES[n]);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle);
			GLES20.glUniform1i(mTextureUniformHandle, n);
		}


		public static void bindTexture(int textureDataHandle, int mTextureUniformHandle) {
			bindTexture(0, textureDataHandle, mTextureUniformHandle);
		}

	}

}
