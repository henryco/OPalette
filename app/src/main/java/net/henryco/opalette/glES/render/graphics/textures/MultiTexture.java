package net.henryco.opalette.glES.render.graphics.textures;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.glES.render.graphics.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.shaders.Shader;
import net.henryco.opalette.utils.GLESUtils;
import net.henryco.opalette.utils.bounds.Bounds2D;
import net.henryco.opalette.utils.bounds.OPallBounds;
import net.henryco.opalette.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.utils.bounds.observer.OPallMultiBoundsHolder;

import java.nio.FloatBuffer;

/**
 * Created by HenryCo on 23/02/17.
 */

public class MultiTexture extends Shader implements OPallMultiBoundsHolder <Bounds2D>, OPallMultiTexture {




	protected static final String DEF_SHADER = "shaders/multiTexture/MultiTexture";
	protected final FloatBuffer texelBuffer;
	protected final int[] textureGL_ID;
	protected final int[] textureData_ID;
	public final Bounds2D[] bounds2D;
	private final int texNumb;
	private int focus;







	public MultiTexture(Context context) {
		this(context, DEF_SHADER + ".vert", DEF_SHADER + ".frag", 1);
	}
	public MultiTexture(Context context, int texNumb) {
		this(context, DEF_SHADER + ".vert", DEF_SHADER + ".frag", texNumb);
	}
	public MultiTexture(Context context, String VERT, String FRAG) {
		this(context, VERT, FRAG, 1);
	}

	public MultiTexture(Context context, String VERT, String FRAG, int texNumb) {
		super(context, VERT, FRAG, 2);
		texelBuffer = GLESUtils.createFloatBuffer(new float[]{0,1, 0,0, 1,0, 1,1});
		textureGL_ID = new int[texNumb];
		textureData_ID = new int[texNumb];
		bounds2D = new Bounds2D[texNumb];
		for (int i = 0; i < bounds2D.length; i++)
			bounds2D[i] = new Bounds2D().setVertices(OPallBounds.vertices.FLAT_SQUARE_2D())
					.setOrder(OPallBounds.order.FLAT_SQUARE_2D()).setHolder(this);
		this.texNumb = (texNumb <= 10 && texNumb > 0) ? texNumb : 1;
		this.focus = this.texNumb - 1;
	}







	public MultiTexture setFocusOn(int n) {
		this.focus = (n >= 0 && n < texNumb) ? n : texNumb;
		return this;
	}






	@Override
	public void setScreenDim(float w, float h) {
		super.setScreenDim(w, h);
		if (w != 0 && h != 0) updateBounds();
	}







	@Override
	public MultiTexture setBitmap(int n, Bitmap image, filter filterMin, filter filterMag) {
		if (image == null || filterMin == null || filterMag == null) return this;
		GLESUtils.glUseProgram(program, () -> {
			textureData_ID[n] = OPallTexture.methods.loadTexture(n, image, filterMin.type, filterMag.type);
			textureGL_ID[n] = getTextureUniformHandle(n);
			bounds2D[n].setUniSize(image.getWidth(), image.getHeight());
		});
		return this;
	}

	@Override
	public MultiTexture setBitmap(int n, Bitmap image, filter filter) {
		return setBitmap(n, image, filter, filter);
	}
	@Override
	public MultiTexture setBitmap(int n, Bitmap image) {
		return setBitmap(n, image, filter.LINEAR);
	}
	@Override
	public MultiTexture setBitmap(Bitmap image, filter filterMin, filter filterMag) {
		return setBitmap(focus, image, filterMin, filterMag);
	}
	@Override
	public MultiTexture setBitmap(Bitmap image, filter filter) {
		return setBitmap(focus, image, filter);
	}
	@Override
	public MultiTexture setBitmap(Bitmap image) {
		return setBitmap(focus, image);
	}









	@Override
	public MultiTexture bounds(int n, BoundsConsumer<Bounds2D> processor) {
		bounds2D[n].apply(processor);
		return this;
	}

	@Override
	public MultiTexture bounds(BoundsConsumer<Bounds2D> processor) {
		for (Bounds2D d : bounds2D) d.apply(processor);
		return this;
	}

	@Override
	public MultiTexture updateBounds(int n) {
		bounds2D[n].generateVertexBuffer(getScreenWidth(), getScreenHeight());
		return this;
	}

	@Override
	public MultiTexture updateBounds() {
		for (Bounds2D d : bounds2D)
			d.generateVertexBuffer(getScreenWidth(), getScreenHeight());
		return this;
	}







	@Override
	protected void render(int glProgram, OPallCamera2D camera) {

		int positionHandle = getPositionHandle();
		int mTextureCoordinateHandle = getTextureCoordinateHandle();

		GLESUtils.glUseVertexAttribArray(positionHandle, mTextureCoordinateHandle, (Runnable) () -> {

			GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, bounds2D[focus].vertexBuffer);
			GLES20.glVertexAttribPointer(mTextureCoordinateHandle, COORDS_PER_TEXEL, GLES20.GL_FLOAT, false, texelStride, texelBuffer);
			OPallTexture.methods.bindTextures(textureData_ID, textureGL_ID);
			GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, bounds2D[focus].getVertexCount(), GLES20.GL_UNSIGNED_SHORT, bounds2D[focus].orderBuffer);

		});

	}



}
