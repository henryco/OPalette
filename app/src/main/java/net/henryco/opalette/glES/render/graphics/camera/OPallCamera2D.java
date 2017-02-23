package net.henryco.opalette.glES.render.graphics.camera;

import android.opengl.GLES20;
import android.opengl.Matrix;


/**
 * Created by root on 13/02/17.
 */

/**
 * GL camera class, tuned for 2D, but implements some 3D functionality
 */
public class OPallCamera2D {

    public static final class HoldXYZ {
        public float x;
        public float y;
        public float z;

        public HoldXYZ(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            return "HoldXYZ{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }

	//Model View Projection Matrix
	private final float[] mMVPMatrix;
	private final float[] mProjectionMatrix;
	private final float[] mViewMatrix;

    private final HoldXYZ eye;
	private final HoldXYZ center;
	private final HoldXYZ up;
	private final HoldXYZ rot;



	private float zoom = 1;
	private int width, height;
	private float
			flipFacX = 1, flipFacY = 1,
			pxFacX = 1, pxFacY = 1;





	public OPallCamera2D(int width, int height, boolean flip) {
		mMVPMatrix = new float[16];
		mProjectionMatrix = new float[16];
        mViewMatrix = new float[16];
        eye = new HoldXYZ(0, 0, -3);
        center = new HoldXYZ(0, 0, 0);
        up = new HoldXYZ(0, 1, 0);
		rot = new HoldXYZ(0, 0, 0);
		if (flip) flipXY();
		set(width, height);
	}
	public OPallCamera2D(int width, int height) {
		this(width, height, false);
	}
	public OPallCamera2D(boolean flip) {
		this(0, 0, flip);
	}
	public OPallCamera2D() {
		this(false);
	}






    public OPallCamera2D set(int width, int height){
		this.width = width;
		this.height = height;
		System.out.println(width + " : " + height);
		pxFacX = 1 / (width * 0.5f);
		pxFacY = 1 / (height * 0.5f);
		System.out.println(pxFacX + " :: " + pxFacY);
        return this;
    }




    public OPallCamera2D update() {

		GLES20.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		Matrix.frustumM(mProjectionMatrix, 0, -ratio * flipFacX / zoom, ratio * flipFacX / zoom, -1 * flipFacY / zoom, 1 * flipFacY / zoom, 3, 1);
        Matrix.setLookAtM(mViewMatrix, 0, eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		Matrix.setRotateEulerM(mMVPMatrix, 0, 180 + rot.x, rot.y, rot.z);
		//OPallShader.applyCameraMatrix(program, mMVPMatrix);
        //return mMVPMatrix;
		return this;
    }

	public final float[] getMVPMatrix() {
		return mMVPMatrix;
	}


	public OPallCamera2D setZoom(float z) {
		zoom = z >= 0 ? z : zoom;
		return this;
	}




	public OPallCamera2D flipXY() {
		return flipX().flipY();
	}
	public OPallCamera2D flipX() {
		flipFacX *= (-1);
		return this;
	}
	public OPallCamera2D flipY() {
		flipFacY *= (-1);
		return this;
	}







	public OPallCamera2D translateX_absolute(float x) {
		eye.x += x;
		center.x += x;
		return this;
	}
	public OPallCamera2D translateY_absolute(float y) {
		eye.y += y;
		center.y += y;
		return this;
	}
	public OPallCamera2D translateZ_absolute(float z) {
		eye.z += z;
		center.z += z;
		return this;
	}
	public OPallCamera2D translateXY_absolute(float x, float y) {
		return translateX_absolute(x).translateY_absolute(y);
	}
	public OPallCamera2D translateXYZ_absolute(float x, float y, float z) {
		return translateXY_absolute(x, y).translateZ_absolute(z);
	}





	public OPallCamera2D translateX(float x_px) {
		return translateX_absolute(x_px * pxFacX);
	}
	public OPallCamera2D translateY(float y_px) {
		return translateY_absolute(y_px * pxFacY);
	}
	public OPallCamera2D translateXY(float x_px, float y_px) {
		return translateX(x_px).translateY(y_px);
	}





	public OPallCamera2D setPosX_absolute(float x) {
		eye.x = x;
		center.x = x;
		return this;
	}
	public OPallCamera2D setPosY_absolute(float y) {
		eye.y = y;
		center.y = y;
		return this;
	}
	public OPallCamera2D setPosZ_absolute(float z) {
		eye.z = z;
		center.z = z;
		return this;
	}
	public OPallCamera2D setPosXY_absolute(float x, float y) {
		return setPosX_absolute(x).setPosY_absolute(y);
	}
	public OPallCamera2D setPosXYZ_absolute(float x, float y, float z) {
		return setPosXY_absolute(x, y).setPosZ_absolute(z);
	}





	public OPallCamera2D setPosX(float x_px) {
		return setPosX_absolute(x_px * pxFacX);
	}
	public OPallCamera2D setPosY(float y_px) {
		return setPosY_absolute(y_px * pxFacY);
	}
	public OPallCamera2D setPosXY(float x_px, float y_px) {
		return setPosX(x_px).setPosY(y_px);
	}








	public OPallCamera2D rotateX(float deg) {
		return rotate(deg, 0, 0);
	}
	public OPallCamera2D rotateY(float deg) {
		return rotate(0, deg, 0);
	}
	public OPallCamera2D rotateZ(float deg) {
		return rotate(0, 0, deg);
	}
	public OPallCamera2D rotate(float deg_x, float deg_y, float deg_z) {
		rot.x += deg_x;
		rot.y += deg_y;
		rot.z += deg_z;
		return this;
	}
	public OPallCamera2D setRotationX(float deg) {
		return setRotation(deg, rot.y, rot.z);
	}
	public OPallCamera2D setRotationY(float deg) {
		return setRotation(rot.x, deg, rot.z);
	}
	public OPallCamera2D setRotationZ(float deg) {
		return setRotation(rot.x, rot.y, deg);
	}
	public OPallCamera2D setRotation(float deg_x, float deg_y, float deg_z) {
		return rotate(deg_x - rot.x, deg_y - rot.y, deg_z - rot.z);
	}

}

