package net.henryco.opalette.graphicsCore.glES.render.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.graphicsCore.glES.render.camera.OPallCamera;
import net.henryco.opalette.graphicsCore.glES.render.shaders.pure.OPallShader;
import net.henryco.opalette.utils.GLESUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by root on 14/02/17.
 */

public class OPallTexture extends OPallShader {

	public enum filter {
		LINEAR(GLES20.GL_LINEAR),
		NEAREST(GLES20.GL_NEAREST);

		private int type;
		filter(int type) {
			this.type = type;
		}
	}

	@Override
	protected float[] getVertices() {
		return new float[]{
				-1, 1,
				-1, -1,
				1, -1,
				1, 1
		};
	}

	@Override
	protected short[] getOrder() {
		return new short[]{
				0, 1, 2,
				0, 2, 3
		}; // triangle1 + triangle2 = square
	}

	/* VERTEX MATRIX:
	 * 	|0 3|
	 * 	|1 2|
	 */



	private static final String DEF_SHADER = "shaders/default/Default";
	public static final int COORDS_PER_TEXEL = 2;
	public final static int texelStride = COORDS_PER_TEXEL * 4; // 1float = 4bytes
	private final int textureGL_ID;
	private final FloatBuffer texelBuffer;


	public OPallTexture(Bitmap image, Context context) {
		this(image, context, filter.LINEAR);
	}

	public OPallTexture(Bitmap image, Context context, filter filter) {
		this(image, context, filter, DEF_SHADER + ".vert", DEF_SHADER + ".frag");
	}

	public OPallTexture(Bitmap image, Context context, filter filter, String shaderVert, String shaderFrag) {
		super(context, shaderVert, shaderFrag, 2);
		textureGL_ID = GLESUtils.loadTexture(image, filter.type, filter.type);
		texelBuffer = GLESUtils.createFloatBuffer(new float[]{0,1, 0,0, 1,0, 1,1});
	}

	public OPallTexture(Bitmap image, Context context, String shaderVert, String shaderFrag) {
		this(image, context, filter.LINEAR, shaderVert, shaderFrag);
	}






	@Override
	protected void render(int glProgram, int positionHandle, FloatBuffer vertexBuffer, ShortBuffer orderBuffer, OPallCamera camera) {

		camera.update();

		GLES20.glEnableVertexAttribArray(positionHandle);
		GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

		int mTextureUniformHandle = GLES20.glGetUniformLocation(glProgram, GLESUtils.defTextureN(0));
		int mTextureCoordinateHandle = GLES20.glGetAttribLocation(glProgram, GLESUtils.a_TexCoordinate);

		uponRender(glProgram, positionHandle, textureGL_ID, mTextureUniformHandle, mTextureCoordinateHandle, vertexBuffer, orderBuffer, texelBuffer, camera);

		GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, vertexCount, GLES20.GL_UNSIGNED_SHORT, orderBuffer);
		GLES20.glDisableVertexAttribArray(positionHandle);
		GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);

	}

	/**
	 * Optional for Override,
	 * remember to use <b>super.uponRender(...);</b>
	 */
	protected void uponRender(int glProgram, int positionHandle, int textureDataHandle, int mTextureUniformHandle,
							  int mTextureCoordinateHandle,
							  FloatBuffer vertexBuffer, ShortBuffer orderBuffer, FloatBuffer texelBuffer, OPallCamera camera) {

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle);
		GLES20.glUniform1i(mTextureUniformHandle, 0);
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, COORDS_PER_TEXEL, GLES20.GL_FLOAT, false, texelStride, texelBuffer);
	}


}
