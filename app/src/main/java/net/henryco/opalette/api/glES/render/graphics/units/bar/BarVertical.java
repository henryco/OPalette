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
public class BarVertical implements OPallBar {

	public final GLESUtils.Color color = GLESUtils.Color.WHITE;
	private FrameBuffer buffer;

	private float cameraTranslationStep = 4.f;
	private float width_pct = 0.201f;
	private float xPos_pct = 0.8f;
	private float cellWidth_pct = 0.8f;

	private float width = 0, height = 0;
	private float posX = 0, posY = 0;

	private int scrW, scrH;

	private FrameBuffer endBuffer;

	private float active_w;

	public BarVertical() {
		buffer = OPallFBOCreator.FrameBuffer();
		endBuffer = OPallFBOCreator.FrameBuffer();
	}
	public BarVertical(int scrWidth, int scrHeight) {
		this();
		active_w = scrWidth;
		createBar(scrWidth, scrHeight);
	}


	public void createBar(int scrWidth, int scrHeight, int width, GLESUtils.Color color) {

		buffer.createFBO(width, scrHeight, scrWidth, scrHeight, false);
		buffer.beginFBO(() -> GLESUtils.clear(color));

		float endSize = (width - (width * cellWidth_pct)) * 0.5f;
		endBuffer.createFBO((int) Math.ceil(endSize), scrHeight, scrWidth, scrHeight, false);
		endBuffer.beginFBO(() -> GLESUtils.clear(color));

		this.width = width;
		this.height = scrHeight;
		this.posX = active_w * xPos_pct;
		this.posY = 0;

		scrW = scrWidth;
		scrH = scrHeight;

	}

	@Override
	public void createBar(int scrWidth, int scrHeight) {
		createBar(scrWidth, scrHeight, (int) (active_w * width_pct), color);
	}


	private void drawBar(OPallRenderable barLine, Camera2D camera2D,
						 int barWidth, int buffer_quantum, float cameraTranslationStep) {

		int iter = Math.round((float)barWidth / (float)buffer_quantum);
		float d = buffer_quantum - cameraTranslationStep;
		float lost = iter * d;
		int extr = Math.round((lost + d) / cameraTranslationStep);

		for (int i = 0; i < iter + extr; i++) barLine.render(camera2D.translateX(-cameraTranslationStep).update());
	}

	@Override
	public void render(Camera2D camera, OPallRenderable renderable, int buffer_quantum) {

		camera.backTranslate(() -> {

			posX = active_w * xPos_pct;
			buffer.render(camera.setPosX(-posX));

			float cellWidth = getWidth() * cellWidth_pct;
			float cellPtc = getWidth() - cellWidth;
			float margin = cellPtc * 0.5f;

			camera.translateX((int)-margin + 0.5f * buffer_quantum);
			drawBar(renderable, camera, (int) (cellWidth), buffer_quantum, cameraTranslationStep);

			endBuffer.render(camera.setPosX(-posX));
			camera.translateX(endBuffer.getWidth() - buffer.getWidth());
			endBuffer.render(camera);
		});
	}

	@Override
	public OPallBar setActiveSize(float w, float h) {
		active_w = w;
		createBar(scrW, scrH);
		return this;
	}

	@Override
	public OPallBar setColor(GLESUtils.Color color) {
		this.color.set(color);
		buffer.beginFBO(() -> GLESUtils.clear(color));
		endBuffer.beginFBO(() -> GLESUtils.clear(color));
		return this;
	}

	@Override
	public OPallBar setRelativeSize(float size_pct) {
		if (size_pct != width_pct && size_pct != 0) {
			this.width_pct = size_pct;
			createBar(scrW, scrH);
		}
		return this;
	}

	@Override
	public OPallBar setRelativePosition(float pos_pct) {
		this.xPos_pct = pos_pct;
		return this;
	}

	@Override
	public OPallBar setRelativeContentSize(float size_pct) {
		if (size_pct != cellWidth_pct && size_pct != 0) {
			this.cellWidth_pct = size_pct;
			createBar(scrW, scrH);
		}
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
		return "BarVertical{" +
				"width=" + width +
				", height=" + height +
				", posX=" + posX +
				", posY=" + posY +
				'}';
	}
}
