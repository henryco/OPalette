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

/**
 * Created by HenryCo on 07/04/17.
 */

public class FilterMatrices {

	public static float[] normalize(float[] matrix) {

		float sum = 0;
		for (float v : matrix) sum += v;
		if (sum == 0) return matrix;
		for (int i = 0; i < matrix.length; i++)
			matrix[i] /= sum;
		return matrix;
	}

	public static float[] m_identity(){
		return new float[] {
				0, 0, 0,
				0, 1, 0,
				0, 0, 0
		};
	}
	public static float[] m_boxBlur() {
		return new float[] {
				1, 1, 1, 1, 1,
				1, 1, 1, 1, 1,
				1, 1, 1, 1, 1,
				1, 1, 1, 1, 1,
				1, 1, 1, 1, 1
		};
	}
	public static float[] m_blur() {
		return new float[] {
				0, 0, 1, 0, 0,
				0, 1, 1, 1, 0,
				1, 1, 1, 1, 1,
				0, 1, 1, 1, 0,
				0, 0, 1, 0, 0
		};
	}
	public static float[] m_gaussianBlur() {
		return new float[] {
				0.0398f,	0.03995f,	0.04f,		0.03995f,	0.0398f,
				0.03995f,	0.0401f,	0.04015f,	0.0401f,	0.03995f,
				0.04f,		0.04015f,	0.0402f,	0.04015f,	0.04f,
				0.03995f,	0.0401f,	0.04015f,	0.0401f,	0.03995f,
				0.0398f,	0.03995f,	0.04f,		0.03995f,	0.0398f,
		};
	}
	public static float[] m_sharpen() {
		return new float[] {
				-1, -1, -1, -1, -1,
				-1,  2,  2,  2, -1,
				-1,  2,  8,  2, -1,
				-1,  2,  2,  2, -1,
				-1, -1, -1, -1, -1
		};
	}
	public static float[] m_sharpen1() {
		return new float[] {
				0, -1, 0,
				-1, 5, -1,
				0, -1, 0
		};
	}
	public static float[] m_sharpen2() {
		return new float[] {
				-1, -1, -1,
				-1, 0, -1,
				-1, -1, -1
		};
	}
	public static float[] m_sharpen3() {
		return new float[]{
				-1, -1, -1,
				-1, 8, -1,
				-1, -1, -1
		};
	}
	public static float[] m_sharpen4() {
		return new float[]{
				1, -2, 1,
				-2, 5, -2,
				1, -2, 1
		};
	}
	public static float[] m_sharpen5() {
		return new float[] {
				-1, -1, -1, -1, -1,
				-1, 3, 4, 3, -1,
				-1, 4, 13, 4, -1,
				-1, 3, 4, 3, -1,
				-1, -1, -1, -1, -1
		};
	}
	public static float[] m_emboss1() {
		return new float[] {
				-2, -1, 0,
				-1, 1, 1,
				0, 1, 2
		};
	}
	public static float[] m_emboss2() {
		return new float[] {
				-2, 0, 0,
				0, 1, 0,
				0, 0, 2
		};
	}
	public static float[] m_diagShatter() {
		return new float[] {
				1, 0, 0, 0, 1,
				0, 0, 0, 0, 0,
				0, 0, 0, 0, 0,
				0, 0, 0, 0, 0,
				1, 0, 0, 0, 1
		};
	}
	public static float[] m_horizontalMotionBlur() {
		return new float[] {
				0, 0, 0, 0, 0,
				0, 0, 0, 0, 0,
				2, 3, 4, 5, 6,
				0, 0, 0, 0, 0,
				0, 0, 0, 0, 0
		};
	}
	public static float[] m_unsharp() {
		return new float[] {
				1, 4, 6, 4, 1,
				4, 16, 24, 16, 4,
				6, 24, -476, 24, 6,
				4, 16, 24, 16, 4,
				1, 4, 6, 4, 1
		};
	}
}
