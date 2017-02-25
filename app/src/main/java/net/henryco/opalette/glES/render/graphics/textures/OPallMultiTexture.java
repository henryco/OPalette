package net.henryco.opalette.glES.render.graphics.textures;

import android.graphics.Bitmap;
import android.opengl.GLES20;

/**
 * Created by HenryCo on 24/02/17.
 */

public interface OPallMultiTexture extends OPallTexture {


	/*	Requested in *.frag file:
	 *
	 *		precision mediump float;
	 *
	 *		varying vec4 v_Position;
	 *		varying vec4 v_WorldPos;
	 *		varying vec2 v_TexCoordinate;
	 *
	 *		uniform sampler2D u_Texture0;
	 *		uniform sampler2D u_Texture1;
	 *		.
	 *		.
	 *		.
	 *		uniform sampler2D u_Texture(n); // {0, 1, ..., n}
	 *
	 *		void main() {
	 *			...
	 *			gl_FragColor = vec4(...);
	 *		}
	 */



	String DEF_SHADER = methods.createDefaultShader();





	OPallMultiTexture setBitmap(int n, Bitmap image, Filter filterMin, Filter filterMag);
	OPallMultiTexture setBitmap(int n, Bitmap image, Filter filter);
	OPallMultiTexture setBitmap(int n, Bitmap image);





	final class methods {


		private static String createDefaultShader() {
			// TODO
			return "shaders/multiTexture/MultiTexture";
		}


		public static void bindTextures(int texNumb, int[] textureDataHandle, int[] mTextureUniformHandle) {
			for (int i = 0; i < texNumb; i++)
				GLES20.glUniform1i(mTextureUniformHandle[i], i);
			for (int i = 0; i < texNumb; i++) {
				GLES20.glActiveTexture(ACTIVE_TEXTURES[i]);
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle[i]);
			}
		}


		public static void bindTextures(int[] textureDataHandle, int[] mTextureUniformHandle) {
			if (textureDataHandle.length != mTextureUniformHandle.length)
				throw new IllegalArgumentException("Arrays length must be the same!");
			bindTextures(textureDataHandle.length, textureDataHandle, mTextureUniformHandle);
		}


	}
}
