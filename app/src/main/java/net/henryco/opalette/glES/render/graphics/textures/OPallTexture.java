package net.henryco.opalette.glES.render.graphics.textures;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.glES.render.graphics.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.shaders.OPallShader;
import net.henryco.opalette.utils.GLESUtils;
import net.henryco.opalette.utils.bounds.Bounds2D;
import net.henryco.opalette.utils.bounds.BoundsConsumer;
import net.henryco.opalette.utils.bounds.OPallBounds;
import net.henryco.opalette.utils.bounds.OPallBoundsHolder;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by root on 14/02/17.
 */

public class OPallTexture extends OPallShader implements OPallBoundsHolder<Bounds2D> {



	protected static final String DEF_SHADER = "shaders/default/Default";
	public static final int COORDS_PER_TEXEL = 2;
	public final static int texelStride = COORDS_PER_TEXEL * 4; // float = 4bytes
	protected final FloatBuffer texelBuffer;
	protected int textureGL_ID;
	public final Bounds2D bounds2D;
	private float screenWidth = 0, screenHeight = 0;





	public OPallTexture(Bitmap image, Context context) {
		this(image, context, filter.LINEAR);
	}
	public OPallTexture(Bitmap image, Context context, filter filter) {
		this(image, context, filter, DEF_SHADER + ".vert", DEF_SHADER + ".frag");
	}
	public OPallTexture(Bitmap image, Context context, filter filter, String shaderVert, String shaderFrag) {
		super(context, shaderVert, shaderFrag, 2);
		texelBuffer = GLESUtils.createFloatBuffer(new float[]{0,1, 0,0, 1,0, 1,1});
		bounds2D = new Bounds2D()
				.setVertices(OPallBounds.vertices.FLAT_SQUARE_2D())
				.setOrder(OPallBounds.order.FLAT_SQUARE_2D())
				.setHolder(this);
		setBitmap(image, filter);
	}
	public OPallTexture(Bitmap image, Context context, String shaderVert, String shaderFrag) {
		this(image, context, filter.LINEAR, shaderVert, shaderFrag);
	}







	public OPallTexture setBitmap(Bitmap image, filter filterMin, filter filterMag) {
		if (image != null && filterMin != null && filterMag != null) {
			this.textureGL_ID = GLESUtils.loadTexture(image, filterMin.type, filterMag.type);
			bounds2D.setSize(image.getWidth(), image.getHeight());
			bounds2D.setDefSize(image.getWidth(), image.getHeight());
		}
		return this;
	}
	public OPallTexture setBitmap(Bitmap image, filter filter) {
		return setBitmap(image, filter, filter);
	}






	@Override
	public OPallTexture bounds(BoundsConsumer<Bounds2D> processor) {
		bounds2D.apply(processor);
		return this;
	}

	@Override
	public OPallTexture updateBounds() {
		bounds2D.generateVertexBuffer(screenWidth, screenHeight);
		return this;
	}


	@Override
	public void setScreenDim(float w, float h) {
		screenWidth = w;
		screenHeight = h;
		if (w != 0 && h != 0) updateBounds();
	}






	@Override
	protected void render(int glProgram, OPallCamera2D camera) {

		int positionHandle = getPositionHandle();
		int mTextureUniformHandle = getTextureUniformHandle(0);
		int mTextureCoordinateHandle = getTextureCoordinateHandle();

		GLES20.glEnableVertexAttribArray(positionHandle);
		GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, bounds2D.vertexBuffer);



		uponRender(glProgram, positionHandle, textureGL_ID, mTextureUniformHandle, mTextureCoordinateHandle,
				bounds2D.vertexBuffer, bounds2D.orderBuffer, texelBuffer, camera);

		GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, bounds2D.getVertexCount(), GLES20.GL_UNSIGNED_SHORT, bounds2D.orderBuffer);
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


}
