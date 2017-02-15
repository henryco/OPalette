package net.henryco.opalette.graphicsCore.glES.render.camera;

import android.opengl.GLES20;
import android.opengl.Matrix;

import net.henryco.opalette.utils.GLESUtils;


/**
 * Created by root on 13/02/17.
 */




public class OPallCamera {

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


    public final float[] mMVPMatrix;
    public final float[] mProjectionMatrix;
    public final float[] mViewMatrix;

    public final HoldXYZ eye;
    public final HoldXYZ center;
    public final HoldXYZ up;


	public float zoom = 1;
	private int width, height;
	private float flipFacX = 1, flipFacY = 1;


	public OPallCamera(int width, int height, boolean flip) {
		mMVPMatrix = new float[16];
		mProjectionMatrix = new float[16];
        mViewMatrix = new float[16];
        eye = new HoldXYZ(0, 0, -3);
        center = new HoldXYZ(0, 0, 0);
        up = new HoldXYZ(0, 1, 0);
		this.width = width;
		this.height = height;
		if (flip) flipXY();
	}

	public OPallCamera(int width, int height) {
		this(width, height, false);
	}

	public OPallCamera(boolean flip) {
		this(0, 0, flip);
	}

	public OPallCamera() {
		this(false);
	}

    private int program;

    public OPallCamera set(int width, int height){
		this.width = width;
		this.height = height;
        return this;
    }

    public OPallCamera update(int program) {

		GLES20.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		Matrix.frustumM(mProjectionMatrix, 0, -ratio * flipFacX / zoom, ratio * flipFacX / zoom, -1 * flipFacY / zoom, 1 * flipFacY / zoom, 3, 1);
		// mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7

        Matrix.setLookAtM(mViewMatrix, 0, eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, GLESUtils.u_MVPMatrix), 1, false, mMVPMatrix, 0);

        return this;
    }

    public OPallCamera update() {
        return update(program);
    }

    public OPallCamera setProgram(int program) {
        this.program = program;
        return this;
    }

	public OPallCamera setZoom(float z) {
		zoom = z >= 0 ? z : zoom;
		return this;
	}

	public OPallCamera flipXY() {
		return flipX().flipY();
	}

	public OPallCamera flipX() {
		flipFacX *= (-1);
		return this;
	}

	public OPallCamera flipY() {
		flipFacY *= (-1);
		return this;
	}

	public OPallCamera translateX(float x) {
		eye.x += x;
		center.x += x;
		return this;
	}
	public OPallCamera translateY(float y) {
		eye.y += y;
		center.y += y;
		return this;
	}
	public OPallCamera translateZ(float z) {
		eye.z += z;
		center.z += z;
		return this;
	}

	public OPallCamera translateXY(float x, float y) {
		return translateX(x).translateY(y);
	}

	public OPallCamera translateXYZ(float x, float y, float z) {
		return translateXY(x, y).translateZ(z);
	}

	public OPallCamera setPosX(float x) {
		eye.x = x;
		center.x = x;
		return this;
	}

	public OPallCamera setPosY(float y) {
		eye.y = y;
		center.y = y;
		return this;
	}

	public OPallCamera setPosZ(float z) {
		eye.z = z;
		center.z = z;
		return this;
	}

	public OPallCamera setPosXY(float x, float y) {
		return setPosX(x).setPosY(y);
	}

	public OPallCamera setPosXYZ(float x, float y, float z) {
		return setPosXY(x, y).setPosZ(z);
	}


	/**
	 * 	<b>Don't work</b>
	 *	@deprecated
	 */
	public OPallCamera rotate(float pitch, float yaw) {
		//	center.x = eye.x + ( float ) Math.cos( Math.toRadians( pitch ) ) * ( float ) Math.cos( Math.toRadians( yaw ) );
		center.y = eye.y - ( float ) Math.sin( Math.toRadians( pitch ) );
	//	center.z = eye.z + ( float ) Math.cos( Math.toRadians( pitch ) ) * ( float ) Math.sin( Math.toRadians( yaw ) );
		return this;
	}
}

