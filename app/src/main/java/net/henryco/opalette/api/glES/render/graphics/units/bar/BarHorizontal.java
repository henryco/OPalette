package net.henryco.opalette.api.glES.render.graphics.units.bar;

import android.content.Context;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 06/03/17.
 */

public class BarHorizontal implements OPallBar {

	public final GLESUtils.Color color = GLESUtils.Color.WHITE;
	public float cameraTranslationStep = 4.f;
	public float height_pct = 0.201f;
	public float yPos_pct = 0.8f;
	public float cellHeight_pct = 0.8f;
	private FrameBuffer buffer;

	private float width = 0, height = 0;

	public BarHorizontal(Context context) {
		buffer = OPallFBOCreator.FrameBuffer(context);
	}
	public BarHorizontal() {
		buffer = OPallFBOCreator.FrameBuffer();
	}


	public void createBar(int scrWidth, int scrHeight, int height, GLESUtils.Color color) {
		buffer.createFBO(scrWidth, height, scrWidth, scrHeight, false);
		buffer.beginFBO(() -> GLESUtils.clear(color));
		this.width = scrWidth;
		this.height = height;
	}

	private void drawBar(OPallRenderable barLine, Camera2D camera2D,
						 int barHeight, int buffer_quantum, float cameraTranslationStep) {

		int iter = (barHeight / buffer_quantum);
		float d = buffer_quantum - cameraTranslationStep;
		float lost = iter * d;
		int extr = (int) Math.floor((lost + d) / cameraTranslationStep);

		for (int i = 0; i < iter + extr; i++)
			barLine.render(camera2D.translateY(-cameraTranslationStep).update());
	}




	@Override
	public void createBar(int scrWidth, int scrHeight) {
		createBar(scrWidth, scrHeight, (int) (scrHeight * height_pct), color);
	}

	@Override
	public void render(Camera2D camera, OPallRenderable renderable, int buffer_quantum) {
		float[] camYPos = camera.getPosition();
		buffer.render(camera.setPosY_absolute(-2 * yPos_pct).update());
		float cellHeight = getHeight() * cellHeight_pct;
		float cellPtc = (getHeight() - cellHeight);
		float margin = cellPtc * 0.5f;

		camera.translateY((int)-margin + 0.5f * buffer_quantum).update();
		drawBar(renderable, camera, (int) (cellHeight), buffer_quantum, cameraTranslationStep);
		camera.setPosition(camYPos).update();

	}


	public BarHorizontal setCameraTranslationStep(float cameraTranslationStep) {
		this.cameraTranslationStep = cameraTranslationStep;
		return this;
	}

	@Override
	public BarHorizontal setColor(GLESUtils.Color color) {
		this.color.set(color);
		return this;
	}

	@Override
	public BarHorizontal setRelativeSize(float size_pct) {
		this.height_pct = size_pct;
		return this;
	}

	@Override
	public BarHorizontal setRelativePosition(float pos_pct) {
		this.yPos_pct = pos_pct;
		return this;
	}

	@Override
	public BarHorizontal setRelativeContentSize(float size_pct) {
		this.cellHeight_pct = size_pct;
		return this;
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return "BarHorizontal{" +
				"color=" + color +
				", height_pct=" + height_pct +
				", yPos_pct=" + yPos_pct +
				", cellHeight_pct=" + cellHeight_pct +
				'}';
	}
}
