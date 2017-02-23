package net.henryco.opalette.glES.render.graphics.textures;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by HenryCo on 23/02/17.
 */

public interface OPallTexture {


	enum filter {
		LINEAR(GLES20.GL_LINEAR),
		NEAREST(GLES20.GL_NEAREST);

		public int type;
		filter(int type) {
			this.type = type;
		}
	}



	int COORDS_PER_TEXEL = 2;
	int texelStride = COORDS_PER_TEXEL * 4; // float = 4bytes


	final class methods {
		static int loadTexture(final Bitmap bitmap, final int filter_min, final int filter_mag) {
			final int[] textureHandle = new int[1];
			GLES20.glGenTextures(1, textureHandle, 0);
			if (textureHandle[0] != 0) {
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, filter_min);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, filter_mag);
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
				return textureHandle[0];
			}
			throw new RuntimeException("Error loading texture.");
		}
	}

}
