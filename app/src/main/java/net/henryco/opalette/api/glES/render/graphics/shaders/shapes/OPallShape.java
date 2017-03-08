package net.henryco.opalette.api.glES.render.graphics.shaders.shapes;

import android.content.Context;
import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.Shader;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.bounds.Bounds2D;
import net.henryco.opalette.api.utils.bounds.OPallBounds;
import net.henryco.opalette.api.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.api.utils.bounds.observer.OPallBoundsHolder;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

/**
 * Created by HenryCo on 07/03/17.
 */

public abstract class OPallShape extends Shader implements OPallBoundsHolder<Bounds2D> {

	protected final Bounds2D bounds2D = new Bounds2D();



	protected abstract void render(int program, Camera2D camera);



	public OPallShape(Context context, String VERT, String FRAG) {
		this(context, VERT, FRAG, 2);
	}
	public OPallShape(Context context, String VERT, String FRAG, int coordsPerVertex) {
		super(context, VERT, FRAG, coordsPerVertex);
		init();
	}
	public OPallShape(String vertex, String fragment) {
		this(vertex, fragment, 2);
	}
	public OPallShape(String vertex, String fragment, int coordsPerVertex) {
		super(vertex, fragment, coordsPerVertex);
		init();
	}

	protected void init() {
		this.bounds2D.setVertices(OPallBounds.vertices.FLAT_SQUARE_2D())
				.setOrder(OPallBounds.order.FLAT_SQUARE_2D())
				.setHolder(this);
	}

	public void render(Camera2D camera) {
		render(camera, program -> render(program, camera));
	}


	@Override
	protected void render(int glProgram, Camera2D camera, OPallConsumer<Integer> setter) {
		camera.backTranslate(() -> {
			camera.setPosXY_absolute(0,0);
			int positionHandle = getPositionHandle();
			GLESUtils.glUseVertexAttribArray(positionHandle, (Runnable) () -> {
				setter.consume(glProgram);
				GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, bounds2D.vertexBuffer);
				GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, bounds2D.getVertexCount(), GLES20.GL_UNSIGNED_SHORT, bounds2D.orderBuffer);
			});
		});
	}

	@Override
	public OPallShape bounds(BoundsConsumer<Bounds2D> processor) {
		processor.boundApply(bounds2D);
		return this;
	}

	@Override
	public OPallShape updateBounds() {
		bounds2D.generateVertexBuffer(getScreenWidth(), getScreenHeight());
		return this;
	}


	@Override
	public int getWidth() {
		return (int) getScreenWidth();
	}

	@Override
	public int getHeight() {
		return (int) getScreenHeight();
	}


}
