package net.henryco.opalette.glES.render.graphics.camera;

import android.opengl.GLES20;
import android.opengl.Matrix;

import net.henryco.opalette.utils.Utils;

/**
 * Created by HenryCo on 23/02/17.
 */

public final class OPallCameraMatrix {

	//Model View Projection Matrix
	public final float[] mMVPMatrix;
	public final float[] mProjectionMatrix;
	public final float[] mViewMatrix;

	public final Utils.HoldXYZ eye;
	public final Utils.HoldXYZ center;
	public final Utils.HoldXYZ up;
	public final Utils.HoldXYZ rotation;


	public OPallCameraMatrix() {
		mMVPMatrix = new float[16];
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
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		Matrix.setRotateEulerM(mMVPMatrix, 0, 180 + rotation.x, rotation.y, rotation.z);
	}



}
