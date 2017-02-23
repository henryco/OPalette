package net.henryco.opalette.glES.render.graphics.textures;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.glES.render.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.shaders.OPallShader;
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
	protected final float[] getVertices() {
		return GLESUtils.Vertices.vertices.FLAT_SQUARE_2D();
	}

	@Override
	protected final short[] getOrder() {
		return GLESUtils.Vertices.order.FLAT_SQUARE_2D();
	}




	protected static final String DEF_SHADER = "shaders/default/Default";
	public static final int COORDS_PER_TEXEL = 2;
	public final static int texelStride = COORDS_PER_TEXEL * 4; // float = 4bytes
	protected final FloatBuffer texelBuffer;
	protected int textureGL_ID;
	protected float width = 0, height = 0,
			x = 0, y = 0, scale = 1;

	private float bitmapWidth, bitmapHeight;


	public OPallTexture(Bitmap image, Context context) {
		this(image, context, filter.LINEAR);
	}
	public OPallTexture(Bitmap image, Context context, filter filter) {
		this(image, context, filter, DEF_SHADER + ".vert", DEF_SHADER + ".frag");
	}
	public OPallTexture(Bitmap image, Context context, filter filter, String shaderVert, String shaderFrag) {
		super(context, shaderVert, shaderFrag, 2);
		texelBuffer = GLESUtils.createFloatBuffer(new float[]{0,1, 0,0, 1,0, 1,1});
		setBitmap(image, filter);
	}
	public OPallTexture(Bitmap image, Context context, String shaderVert, String shaderFrag) {
		this(image, context, filter.LINEAR, shaderVert, shaderFrag);
	}


	public OPallTexture setBitmap(Bitmap image, filter filterMin, filter filterMag) {
		if (image != null && filterMin != null && filterMag != null) {
			this.textureGL_ID = GLESUtils.loadTexture(image, filterMin.type, filterMag.type);
			width = image.getWidth();
			height = image.getHeight();
			bitmapWidth = width;
			bitmapHeight = height;
		}
		return this;
	}
	public OPallTexture setBitmap(Bitmap image, filter filter) {
		return setBitmap(image, filter, filter);
	}


	public OPallTexture setBounds(float x, float y, float w, float h, float scale) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.scale = scale;
		recalculateVerts();
		return this;
	}
	public OPallTexture setBounds(float x, float y, float w, float h){
		return setBounds(x, y, w, h, scale);
	}
	public OPallTexture setPosition(float x, float y) {
		return setBounds(x, y, width, height, scale);
	}
	public OPallTexture setWidth(float w) {
		return setBounds(x, y, w, height, scale);
	}
	public OPallTexture setHeight(float h) {
		return setBounds(x, y, width, h, scale);
	}
	public OPallTexture setSize(float w, float h) {
		return setBounds(x, y, w, h, scale);
	}
	public OPallTexture setScale(float scale) {
		return setBounds(x, y, width, height, scale);
	}


	public OPallTexture resetBounds(boolean full) {
		generateVertexBuffer(getVertices());
		width = full ? 0 : bitmapWidth;
		height = full ? 0 : bitmapHeight;
		scale = 1;
		x = 0;
		y = 0;
		return this;
	}
	public OPallTexture resetBounds() {
		return resetBounds(false);
	}


	protected void recalculateVerts() {
		generateVertexBuffer(GLESUtils.Vertices.calculate(getVertices(), 0, 0, width, height, getScrDim()[0], getScrDim()[1], scale));
	}





	@Override
	protected void render(int glProgram, int positionHandle, FloatBuffer vertexBuffer, ShortBuffer orderBuffer, OPallCamera2D camera) {

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
							  FloatBuffer vertexBuffer, ShortBuffer orderBuffer, FloatBuffer texelBuffer, OPallCamera2D camera) {

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle);
		GLES20.glUniform1i(mTextureUniformHandle, 0);
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, COORDS_PER_TEXEL, GLES20.GL_FLOAT, false, texelStride, texelBuffer);
	}


	@Override
	public void setScrDim(float w, float h) {
		super.setScrDim(w, h);
		if (w != 0 && h != 0) recalculateVerts();
	}
}
