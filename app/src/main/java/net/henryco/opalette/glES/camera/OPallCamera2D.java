package net.henryco.opalette.glES.camera;


/**
 * Created by root on 13/02/17.
 */

/**
 * GL camera class, tuned for 2D, but implements some 3D functionality
 */
public class OPallCamera2D {


	private final OPallCameraMatrix matrix;

	private boolean flipX = true, flipY = true;
	private float pxFacX = 1, pxFacY = 1;
	private float zoom = 1;
	private int width, height;




	public OPallCamera2D(int width, int height, boolean flip) {
		matrix = new OPallCameraMatrix();
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

		matrix.update(0, 0, width, height, zoom, flipX, flipY);
		return this;
    }

	public final float[] getMVPMatrix() {
		return matrix.mMVPMatrix;
	}


	public OPallCamera2D setZoom(float z) {
		zoom = z >= 0 ? z : zoom;
		return this;
	}




	public OPallCamera2D flipXY() {
		return flipX().flipY();
	}
	public OPallCamera2D flipX() {
		flipX = !flipX;
		return this;
	}
	public OPallCamera2D flipY() {
		flipY = !flipY;
		return this;
	}







	public OPallCamera2D translateX_absolute(float x) {
		matrix.eye.x += x;
		matrix.center.x += x;
		return this;
	}
	public OPallCamera2D translateY_absolute(float y) {
		matrix.eye.y += y;
		matrix.center.y += y;
		return this;
	}
	public OPallCamera2D translateZ_absolute(float z) {
		matrix.eye.z += z;
		matrix.center.z += z;
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
		matrix.eye.x = x;
		matrix.center.x = x;
		return this;
	}
	public OPallCamera2D setPosY_absolute(float y) {
		matrix.eye.y = y;
		matrix.center.y = y;
		return this;
	}
	public OPallCamera2D setPosZ_absolute(float z) {
		matrix.eye.z = z;
		matrix.center.z = z;
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
		return rotate(deg,0,  0);
	}
	public OPallCamera2D rotateY(float deg) {
		return rotate(0, deg, 0);
	}
	public OPallCamera2D rotateZ(float deg) {
		return rotate(0,  0,deg);
	}
	public OPallCamera2D rotate(float deg_x, float deg_y, float deg_z) {
		matrix.rotation.x += deg_x;
		matrix.rotation.y += deg_y;
		matrix.rotation.z += deg_z;
		return this;
	}





	public OPallCamera2D setRotationX(float deg) {
		return setRotation(deg, matrix.rotation.y, matrix.rotation.z);
	}
	public OPallCamera2D setRotationY(float deg) {
		return setRotation(matrix.rotation.x, deg, matrix.rotation.z);
	}
	public OPallCamera2D setRotationZ(float deg) {
		return setRotation(matrix.rotation.x, matrix.rotation.y, deg);
	}
	public OPallCamera2D setRotation(float deg_x, float deg_y, float deg_z) {
		return rotate(deg_x - matrix.rotation.x, deg_y - matrix.rotation.y, deg_z - matrix.rotation.z);
	}

}

