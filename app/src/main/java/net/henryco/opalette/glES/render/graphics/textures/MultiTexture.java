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

public class MultiTexture extends Shader implements OPallMultiBoundsHolder <Bounds2D>, OPallTexture {



	protected static final String DEF_SHADER = "shaders/multiTexture/MultiTexture";
	protected final FloatBuffer texelBuffer;
	protected final int[] textureGL_ID;
	public final Bounds2D[] bounds2D;
	private final int texNumb;
	private int focus;





	public MultiTexture(Context context, String VERT, String FRAG, int texNumb) {
		this(context, filter.LINEAR, VERT, FRAG, texNumb);
	}
	public MultiTexture(Context context, int texNumb) {
		this(context, DEF_SHADER + ".vert", DEF_SHADER + ".frag", texNumb);
	}
	public MultiTexture(Context context, filter filter, String VERT, String FRAG, int texNumb) {
		super(context, VERT, FRAG, 2);
		texelBuffer = GLESUtils.createFloatBuffer(new float[]{0,1, 0,0, 1,0, 1,1});
		textureGL_ID = new int[texNumb];
		bounds2D = new Bounds2D[texNumb];
		for (int i = 0; i < bounds2D.length; i++)
			bounds2D[i] = new Bounds2D().setVertices(OPallBounds.vertices.FLAT_SQUARE_2D())
					.setOrder(OPallBounds.order.FLAT_SQUARE_2D()).setHolder(this);
		this.texNumb = (texNumb <= 10 && texNumb > 0) ? texNumb : 10;
		this.focus = texNumb - 1;
	}
	public MultiTexture(Context context, filter filter, int texNumb) {
		this(context, filter, DEF_SHADER + ".vert", DEF_SHADER + ".frag", texNumb);
	}






	public MultiTexture setBitmap(int n, Bitmap image, filter filterMin, filter filterMag) {
		if (image != null && filterMin != null && filterMag != null) {
			textureGL_ID[n] = OPallTexture.methods.loadTexture(image, filterMin.type, filterMag.type);
			bounds2D[n].setUniSize(image.getWidth(), image.getHeight());
		}
		return this;
	}
	public MultiTexture setBitmap(int n, Bitmap image, filter filter) {
		return setBitmap(n, image, filter, filter);
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

		OPallTexture.methods.glEnableVertexAttribArray(positionHandle, mTextureCoordinateHandle);
		GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, bounds2D[focus].vertexBuffer);
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, COORDS_PER_TEXEL, GLES20.GL_FLOAT, false, texelStride, texelBuffer);


		for (int i = 0; i < texNumb; i++) {

			int mTextureUniformHandle = getTextureUniformHandle(i);
			OPallTexture.methods.bindTexture(i, textureGL_ID[i], mTextureUniformHandle);
			GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, bounds2D[i].getVertexCount(), GLES20.GL_UNSIGNED_SHORT, bounds2D[i].orderBuffer);
		}


		OPallTexture.methods.glDisableVertexAttribArray(positionHandle, mTextureCoordinateHandle);
	}



}
