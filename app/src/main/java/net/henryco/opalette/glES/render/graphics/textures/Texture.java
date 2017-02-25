package net.henryco.opalette.glES.render.graphics.textures;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.glES.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.shaders.Shader;
import net.henryco.opalette.utils.GLESUtils;
import net.henryco.opalette.utils.bounds.Bounds2D;
import net.henryco.opalette.utils.bounds.OPallBounds;
import net.henryco.opalette.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.utils.bounds.observer.OPallBoundsHolder;

import java.nio.FloatBuffer;

/**
 * Created by root on 14/02/17.
 */

public class Texture extends Shader implements OPallBoundsHolder<Bounds2D>, OPallTexture {




	protected final FloatBuffer texelBuffer;
	protected int textureGL_ID;
	public final Bounds2D bounds2D;




	public Texture(Bitmap image, Context context, String shaderVert, String shaderFrag) {
		this(image, context, Filter.LINEAR, shaderVert, shaderFrag);
	}
	public Texture(Bitmap image, Context context) {
		this(image, context, Filter.LINEAR);
	}
	public Texture(Bitmap image, Context context, Filter filter) {
		this(image, context, filter, DEF_SHADER + ".vert", DEF_SHADER + ".frag");
	}

	public Texture(Bitmap image, Context context, Filter filter, String shaderVert, String shaderFrag) {
		super(context, shaderVert, shaderFrag, 2);
		texelBuffer = GLESUtils.createFloatBuffer(new float[]{0,1, 0,0, 1,0, 1,1});
		bounds2D = new Bounds2D()
				.setVertices(OPallBounds.vertices.FLAT_SQUARE_2D())
				.setOrder(OPallBounds.order.FLAT_SQUARE_2D())
				.setHolder(this);
		setBitmap(image, filter);
	}







	@Override
	public Texture setBitmap(Bitmap image, Filter filterMin, Filter filterMag) {
		if (image == null || filterMin == null || filterMag == null) return this;
		GLESUtils.glUseProgram(program, () -> {
			this.textureGL_ID = OPallTexture.methods.loadTexture(image, filterMin, filterMag);
			bounds2D.setUniSize(image.getWidth(), image.getHeight());
			image.recycle();
		});
		return this;
	}

	@Override
	public Texture setBitmap(Bitmap image, Filter filter) {
		return setBitmap(image, filter, filter);
	}
	@Override
	public Texture setBitmap(Bitmap image) {
		return setBitmap(image, Filter.LINEAR);
	}










	@Override
	public Texture bounds(BoundsConsumer<Bounds2D> processor) {
		bounds2D.apply(processor);
		return this;
	}

	@Override
	public Texture updateBounds() {
		bounds2D.generateVertexBuffer(getScreenWidth(), getScreenHeight());
		return this;
	}

	@Override
	public void setScreenDim(float w, float h) {
		super.setScreenDim(w, h);
		if (w != 0 && h != 0) updateBounds();
	}





	@Override
	public int getWidth() {
		return (int) bounds2D.getWidth();
	}

	@Override
	public int getHeight() {
		return (int) bounds2D.getHeight();
	}




	@Override
	protected void render(int glProgram, OPallCamera2D camera) {

		int positionHandle = getPositionHandle();
		int mTextureUniformHandle = getTextureUniformHandle(0);
		int mTextureCoordinateHandle = getTextureCoordinateHandle();

		GLESUtils.glUseVertexAttribArray(positionHandle, mTextureCoordinateHandle, (Runnable) () -> {

			GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, bounds2D.vertexBuffer);
			GLES20.glVertexAttribPointer(mTextureCoordinateHandle, COORDS_PER_TEXEL, GLES20.GL_FLOAT, false, texelStride, texelBuffer);
			OPallTexture.methods.bindTexture(textureGL_ID, mTextureUniformHandle);
			GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, bounds2D.getVertexCount(), GLES20.GL_UNSIGNED_SHORT, bounds2D.orderBuffer);

		});

	}


}
