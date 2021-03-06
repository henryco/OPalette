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

package net.henryco.opalette.api.utils;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Created by HenryCo on 13/02/17.
 */

public class GLESUtils {






	public static final class Color {

		public static final Color BLACK = new Color(0.f,0.f,0.f,1.f);
		public static final Color TRANSPARENT = new Color(0.f,0.f,0.f, 0.f);
		public static final Color WHITE = new Color(1.f,1.f,1.f,1.f);
		public static final Color PINK = new Color(.9f,.1f,.5f, 1.f);
		public static final Color RED = new Color(1.f, 0.f, 0.f,1.f);
		public static final Color BLUE = new Color(0.f, 0.f,1.f,1.f);
		public static final Color LIME = new Color(0.f,1.f,0.f,1.f);
		public static final Color SILVER = new Color(.75f, .75f, .75f, 1f);
		public static final Color GREY = new Color(.5f, .5f, .5f, 1f);
		public static final Color TEAL = new Color(.0f, .5f, .5f, 1f);
		public static final Color GREEN = new Color(0.f,.5f,0.f,1.f);
		public static final Color YELLOW = new Color(1.f,1.f,.0f,1.f);


		public static final Color[] COLORS = {
				Color.BLACK,
				Color.BLUE,
				Color.GREEN,
				Color.GREY,
				Color.LIME,
				Color.PINK,
				Color.RED,
				Color.SILVER,
				Color.TEAL,
				Color.TRANSPARENT,
				Color.WHITE,
				Color.YELLOW
		};



		public float r, g, b, a;
		public Color(int i) {
			this.set(i);
		}
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
		public Color(Color other) {
			set(other);
		}
		public Color() {this(WHITE);}
		public void set(Color color) {
			this.r = color.r;
			this.g = color.g;
			this.b = color.b;
			this.a = color.a;
		}
		public void set(float r, float g, float b, float a) {
			set(new Color(r, g, b, a));
		}
		public void set(int r, int g, int b, int a) {
			set(new Color(r, g, b, a));
		}
		public void set(int i) {
			set(android.graphics.Color.red(i), android.graphics.Color.green(i), android.graphics.Color.blue(i), android.graphics.Color.alpha(i));
		}
		public int hex() {
			return android.graphics.Color.argb((int) (a * 255), (int) (r * 255), (int) (g * 255), (int) (b * 255));
		}

		@Override
		public String toString() {
			return "Color{" +
					"r=" + r +
					", g=" + g +
					", b=" + b +
					", a=" + a +
					'}';
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Color color = (Color) o;

			if (Float.compare(color.r, r) != 0) return false;
			if (Float.compare(color.g, g) != 0) return false;
			if (Float.compare(color.b, b) != 0) return false;
			return Float.compare(color.a, a) == 0;

		}

		@Override
		public int hashCode() {
			int result = (r != +0.0f ? Float.floatToIntBits(r) : 0);
			result = 31 * result + (g != +0.0f ? Float.floatToIntBits(g) : 0);
			result = 31 * result + (b != +0.0f ? Float.floatToIntBits(b) : 0);
			result = 31 * result + (a != +0.0f ? Float.floatToIntBits(a) : 0);
			return result;
		}
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

	public static IntBuffer createIntBuffer(int[] arr) {
		if (arr == null) return null;
		IntBuffer orderBuffer = ByteBuffer.allocateDirect(arr.length * 4)
				.order(ByteOrder.nativeOrder())
				.asIntBuffer();
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

    public static void clear(float r, float g, float b, float a) {
        GLES20.glClearColor(r, g, b, a);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    }

    public static void clear() {
        clear(0f, 0f, 0f, 0f);
    }
	public static void clear(Color color) {
		clear(color.r, color.g, color.b, color.a);
	}

	public static void glEnableVertexAttribArray(int ... atr) {
		for (int i : atr) GLES20.glEnableVertexAttribArray(i);
	}


	public static void glDisableVertexAttribArray(int ... atr) {
		for (int i : atr) GLES20.glDisableVertexAttribArray(i);
	}

	public static void glUseProgram(int program, Runnable runnable) {
		GLES20.glUseProgram(program);
		runnable.run();
		GLES20.glUseProgram(0);
	}

	/**
	 * <br>Invoke glEnableVertexAttribArray(...), <br>
	 *     do action and then invoke glDisableVertexAttribArray(...). <br><br>
	 * <b>Example: </b> <br>glUseVertexAttribArray( p1, p2, (Runnable) () -> doStuff() );
	 * @param ob array of objects, where: <br>
	 *           All elements except last are (int) glAttributes, <br>
	 *           Last element is (Runnable) action.
	 */
	public static void glUseVertexAttribArray(Object ... ob) {
		int n = ob.length;
		for (int i = 0; i < n - 1; i++) GLES20.glEnableVertexAttribArray(i);
		((Runnable) ob[n-1]).run();
		for (int i = 0; i < n - 1; i++) GLES20.glDisableVertexAttribArray(i);
	}

	public static void glBlendFunc(int src, int dst, Runnable run) {
		GLES20.glBlendFunc(src, dst);
		GLES20.glEnable(GLES20.GL_BLEND);
		run.run();
		GLES20.glDisable(GLES20.GL_BLEND);
	}

}
