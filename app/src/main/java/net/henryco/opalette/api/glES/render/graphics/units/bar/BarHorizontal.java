/*
 *   /*
 *    * Copyright (C) Henryk Timur Domagalski
 *    *
 *    * Licensed under the Apache License, Version 2.0 (the "License");
 *    * you may not use this file except in compliance with the License.
 *    * You may obtain a copy of the License at
 *    *
 *    *      http://www.apache.org/licenses/LICENSE-2.0
 *    *
 *    * Unless required by applicable law or agreed to in writing, software
 *    * distributed under the License is distributed on an "AS IS" BASIS,
 *    * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    * See the License for the specific language governing permissions and
 *    * limitations under the License.
 *
 */

package net.henryco.opalette.api.glES.render.graphics.units.bar;


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
	private float cameraTranslationStep = 4.f;
	private float height_pct = 0.201f;
	private float yPos_pct = 0.8f;
	private float cellHeight_pct = 0.8f;
	private FrameBuffer buffer;

	private float width = 0, height = 0;
	private float posX = 0, posY = 0;

	private int scrW, scrH;

	private FrameBuffer bottomBuffer;

	public BarHorizontal() {
		buffer = OPallFBOCreator.FrameBuffer();
		bottomBuffer = OPallFBOCreator.FrameBuffer();
	}

	public BarHorizontal(int scrWidth, int scrHeight) {
		this();
		createBar(scrWidth, scrHeight);
	}

	public void createBar(int scrWidth, int scrHeight, int height, GLESUtils.Color color) {
		buffer.createFBO(scrWidth, height, scrWidth, scrHeight, false);
		buffer.beginFBO(() -> GLESUtils.clear(color));
		this.width = scrWidth;
		this.height = height;
		this.posX = 0;
		this.posY = scrHeight * yPos_pct;

		scrW = scrWidth;
		scrH = scrHeight;


		float botSize = (height - (height * cellHeight_pct)) * 0.5f;
		bottomBuffer.createFBO(scrWidth, (int) Math.ceil(botSize), scrWidth, scrHeight, false);
		bottomBuffer.beginFBO(() -> GLESUtils.clear(color));
	}



	@Override
	public void createBar(int scrWidth, int scrHeight) {
		createBar(scrWidth, scrHeight, (int) (scrHeight * height_pct), color);
	}


	private void drawBar(OPallRenderable barLine, Camera2D camera2D,
						 int barHeight, int buffer_quantum, float cameraTranslationStep) {

		int iter = Math.round(((float)barHeight / (float)buffer_quantum));
		float d = buffer_quantum - cameraTranslationStep;
		float lost = iter * d;
		int extr = Math.round((lost + d) / cameraTranslationStep);

		for (int i = 0; i < iter + extr; i++) barLine.render(camera2D.translateY(-cameraTranslationStep).update());
	}


	@Override
	public void render(Camera2D camera, OPallRenderable renderable, int buffer_quantum) {

		camera.backTranslate(() -> {

			buffer.render(camera.setPosY_absolute(-2 * yPos_pct));
			float cellHeight = getHeight() * cellHeight_pct;
			float cellPtc = getHeight() - cellHeight;
			float margin = cellPtc * 0.5f;

			camera.translateY((int)-margin + 0.5f * buffer_quantum);
			drawBar(renderable, camera, (int) (cellHeight), buffer_quantum, cameraTranslationStep);

			camera.setPosY_absolute(-2 * yPos_pct);
			camera.translateY(bottomBuffer.getHeight() - buffer.getHeight());
			bottomBuffer.render(camera);
		});
	}


	public BarHorizontal setCameraTranslationStep(float cameraTranslationStep) {
		this.cameraTranslationStep = cameraTranslationStep;
		return this;
	}

	@Override
	public BarHorizontal setColor(GLESUtils.Color color) {
		this.color.set(color);
		buffer.beginFBO(() -> GLESUtils.clear(color));
		return this;
	}

	@Override
	public BarHorizontal setRelativeSize(float size_pct) {
		if (size_pct != height_pct && size_pct != 0) {
			this.height_pct = size_pct;
			createBar(scrW, scrH);
		}
		return this;
	}

	@Override
	public BarHorizontal setRelativePosition(float pos_pct) {
		this.yPos_pct = pos_pct;
		return this;
	}

	@Override
	public BarHorizontal setRelativeContentSize(float size_pct) {
		if (size_pct != 0) this.cellHeight_pct = size_pct;
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
	public float getPosX() {
		return posX;
	}

	@Override
	public float getPosY() {
		return posY;
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
