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

package net.henryco.opalette.api.glES.camera;

import android.opengl.GLES20;
import android.opengl.Matrix;

import net.henryco.opalette.api.utils.Utils;

/**
 * Created by HenryCo on 23/02/17.
 */

public class OPallCameraMatrix {

	//Model View Projection Matrix
	public final float[] mMVPMatrix;
	public final float[] mProjectionMatrix;
	public final float[] mViewMatrix;
	public final float[] mRotMatrix;
	public final float[] mVProxyMatrix;

	public final Utils.HoldXYZ eye;
	public final Utils.HoldXYZ center;
	public final Utils.HoldXYZ up;
	public final Utils.HoldXYZ rotation;


	public OPallCameraMatrix() {
		mRotMatrix = new float[16];
		mMVPMatrix = new float[16];
		mVProxyMatrix = new float[16];
		mProjectionMatrix = new float[16];
		mViewMatrix = new float[16];
		eye = new Utils.HoldXYZ(0, 0, -3);
		center = new Utils.HoldXYZ(0, 0, 0);
		up = new Utils.HoldXYZ(0, 1, 0);
		rotation = new Utils.HoldXYZ(0, 0, 0);
	}


	public void update(int x, int y, int width, int height, float zoom, boolean flipX, boolean flipY) {

		int flipFacX = flipX ? 1 : -1;
		int flipFacY = flipY ? 1 : -1;

		GLES20.glViewport(x, y, width, height);
		float ratio = (float) width / height;
		Matrix.frustumM(mProjectionMatrix, 0, -ratio * flipFacX / zoom, ratio * flipFacX / zoom, -1 * flipFacY / zoom, flipFacY / zoom, 3, 1);
		Matrix.setLookAtM(mViewMatrix, 0, eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
		Matrix.setRotateEulerM(mRotMatrix, 0, -rotation.x, -rotation.y, -rotation.z);
		Matrix.multiplyMM(mVProxyMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mVProxyMatrix, 0, mRotMatrix, 0);
	}



	@Override
	public String toString() {
		return "OPallCameraMatrix{" +
				"eye=" + eye +
				", center=" + center +
				", up=" + up +
				", rotation=" + rotation +
				'}';
	}
}
