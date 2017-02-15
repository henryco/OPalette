package net.henryco.opalette.utils;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by HenryCo on 13/02/17.
 */

public class GLESUtils {


	public static final class Color {

		public float r, g, b, a;
		public Color(int r255, int g255, int b255, int a255) {
			this(r255 / 255f, g255 / 255f, b255 / 255f, a255 / 255f);
		}
		public Color(int r255, int g255, int b255) {
			this(r255, g255, b255, 255);
		}
		public Color(float r, float g, float b, float a) {
			this.r = (r >= 0 && r <= 1) ? r : 0;
			this.g = (g >= 0 && g <= 1) ? g : 0;
			this.b = (b >= 0 && b <= 1) ? b : 0;
			this.a = (a >= 0 && a <= 1) ? a : 0;
		}
		public Color(float r, float g, float b) {
			this(r, g, b, 1f);
		}
	}




    public static final String a_Position = "a_Position";
    public static final String u_MVPMatrix = "u_MVPMatrix";
	public static final String u_Texture_n = "u_Texture";
	public static final String a_TexCoordinate = "a_TexCoordinate";

	public static String defTextureN(int numb) {
		return u_Texture_n + numb;
	}

	/**
	 * Initialize vertex byte buffer for shape coordinates with parameters
	 * (number of coordinate values * 4 bytes per float)<br>
     * and use the device hardware's native byte order.<br>
     * @param arr - coordinates
     * @return FloatBuffer
     */
    public static FloatBuffer createFloatBuffer(float[] arr) {
        if (arr == null) return null;
        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(arr.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(arr).position(0);
        return vertexBuffer;
    }

    public static ShortBuffer createShortBuffer(short[] arr) {
        if (arr == null) return null;
        ShortBuffer orderBuffer = ByteBuffer.allocateDirect(arr.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        orderBuffer.put(arr).position(0);
        return orderBuffer;
    }

    public static ByteBuffer createByteBuffer(byte[] arr) {
        ByteBuffer buffer = ByteBuffer.allocate(arr.length)
                .order(ByteOrder.nativeOrder())
                .put(arr);
        buffer.position(0);
        return buffer;
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public static void clear(float r, float g, float b, float a) {
        GLES20.glClearColor(r, g, b, a);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    public static void clear() {
        clear(0f, 0f, 0f, 0f);
    }


	public static int loadTexture(final Bitmap bitmap, final int filter_min, final int filter_mag) {
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
