package net.henryco.opalette.api.glES.render.graphics.textures;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.Shader;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.bounds.Bounds2D;
import net.henryco.opalette.api.utils.bounds.OPallBounds;
import net.henryco.opalette.api.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.api.utils.bounds.observer.OPallBoundsHolder;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

import java.nio.FloatBuffer;

/**
 * Created by root on 14/02/17.
 */

public class Texture extends Shader implements OPallBoundsHolder<Bounds2D>, OPallTexture {



	protected final boolean[] textureFlip;
	protected final FloatBuffer texelBuffer;
	protected int textureData_ID;
	protected int textureGL_ID;
	protected Bitmap bitmap;
	public final Bounds2D bounds2D;


	public Texture(Context context, String shaderVert, String shaderFrag) {
		this(null, context, shaderVert, shaderFrag);
	}
	public Texture(Context context) {
		this(null, context);
	}
	public Texture(Context context, Filter filter) {
		this(null, context, filter);
	}
	public Texture(Context context, Filter filter, String shaderVert, String shaderFrag) {
		this(null, context, filter, shaderVert, shaderFrag);
	}
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
		textureFlip = new boolean[2];
		bounds2D = new Bounds2D()
				.setVertices(OPallBounds.vertices.FLAT_SQUARE_2D())
				.setOrder(OPallBounds.order.FLAT_SQUARE_2D())
				.setHolder(this);
		setBitmap(image, filter);
		setFlip(false, false);
	}




	@Override
	public Texture setBitmap(Bitmap image, Filter filterMin, Filter filterMag) {
		if (image == null || filterMin == null || filterMag == null) return this;
		bitmap = image;
		GLESUtils.glUseProgram(program, () -> {
			this.textureData_ID = OPallTexture.methods.loadTexture(image, filterMin, filterMag);
			this.textureGL_ID = getTextureUniformHandle(0);
			bounds2D.setUniSize(image.getWidth(), image.getHeight());
		});

		Log.d("texture", "<SET>");
		Log.d("texture", "SW: "+getScreenWidth() + "| SH: "+getScreenHeight());
		Log.d("texture", "W: "+ getWidth() + "| H: "+ getHeight());
		Log.d("texture", "</SET>");

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
	public Texture setFlip(boolean x, boolean y) {
		textureFlip[0] = x;
		textureFlip[1] = y;
		return this;
	}


	@Override
	public Texture setSize(int w, int h) {
		bounds2D.setSize(w, h);
		return this;
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
	protected void render(int glProgram, Camera2D camera, OPallConsumer<Integer> setter) {

		Log.d("texture", "<REND>");
		Log.d("texture", "SW: "+getScreenWidth() + "| SH: "+getScreenHeight());
		Log.d("texture", "W: "+ getWidth() + "| H: "+ getHeight());
		Log.d("texture", "</REND>");

		int positionHandle = getPositionHandle();
		int mTextureUniformHandle = textureGL_ID;
		int mTextureCoordinateHandle = getTextureCoordinateHandle();

		OPallTexture.methods.applyFlip(glProgram, textureFlip);

		GLESUtils.glUseVertexAttribArray(positionHandle, mTextureCoordinateHandle, (Runnable) () -> {

			setter.consume(glProgram);

			GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, bounds2D.vertexBuffer);
			GLES20.glVertexAttribPointer(mTextureCoordinateHandle, COORDS_PER_TEXEL, GLES20.GL_FLOAT, false, texelStride, texelBuffer);
			OPallTexture.methods.bindTexture(textureData_ID, mTextureUniformHandle);
			GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, bounds2D.getVertexCount(), GLES20.GL_UNSIGNED_SHORT, bounds2D.orderBuffer);

		});

	}


}
