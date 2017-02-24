package net.henryco.opalette.glES.render.graphics.textures;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import net.henryco.opalette.glES.render.graphics.shaders.OPallShader;


/**
 * Created by HenryCo on 23/02/17.
 */

public interface OPallTexture extends OPallShader {


	/*	Requested in *.frag file:
	 *
	 *		precision mediump float;
	 *
	 *		varying vec4 v_Position;
	 *		varying vec4 v_WorldPos;
	 *		varying vec2 v_TexCoordinate;
	 *
	 *		uniform sampler2D u_Texture0;
	 *
	 *		void main() {
	 *			...
	 *			gl_FragColor = vec4(...);
	 *		}
	 */


	enum filter {
		LINEAR(GLES20.GL_LINEAR),
		NEAREST(GLES20.GL_NEAREST);

		public int type;
		filter(int type) {
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







	OPallTexture setBitmap(Bitmap image, filter filterMin, filter filterMag);
	OPallTexture setBitmap(Bitmap image, filter filter);
	OPallTexture setBitmap(Bitmap image);







	final class methods {


		public static int loadTexture(int n, final Bitmap bitmap, final int filter_min, final int filter_mag) {
			final int[] textureHandle = new int[1];
			GLES20.glGenTextures(1, textureHandle, 0);
			if (textureHandle[0] != 0) {
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, filter_min);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, filter_mag);
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, n);
				return textureHandle[0];
			}
			throw new RuntimeException("Error loading texture.");
		}


		public static int loadTexture(final Bitmap bitmap, final int filter_min, final int filter_mag) {
			return loadTexture(0, bitmap, filter_min, filter_mag);
		}


		public static void bindTexture(int n, int textureDataHandle, int mTextureUniformHandle) {
			GLES20.glActiveTexture(ACTIVE_TEXTURES[n]);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle);
			GLES20.glUniform1i(mTextureUniformHandle, n);
		}


		public static void bindTexture(int textureDataHandle, int mTextureUniformHandle) {
			bindTexture(0, textureDataHandle, mTextureUniformHandle);
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
