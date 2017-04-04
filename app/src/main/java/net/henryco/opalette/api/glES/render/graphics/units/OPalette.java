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

package net.henryco.opalette.api.glES.render.graphics.units;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.PaletteTexture;
import net.henryco.opalette.api.glES.render.graphics.units.bar.BarHorizontal;
import net.henryco.opalette.api.glES.render.graphics.units.bar.BarVertical;
import net.henryco.opalette.api.glES.render.graphics.units.bar.OPallBar;
import net.henryco.opalette.api.glES.render.graphics.units.palette.CellPaletter;
import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 31/03/17.
 */

public class OPalette implements OPallRenderable {

	public static final int ORIENTATION_HORIZONTAL = 2;
	public static final int ORIENTATION_VERTICAL = 1;
	public static final int ORIENTATION_NONE = 0;

	private final GLESUtils.Color color = new GLESUtils.Color();

	private FrameBuffer barGradientBuffer;
	private FrameBuffer barSrcBufferW;
	private FrameBuffer barScrBufferH;

	private OPallBar backBarW;
	private OPallBar backBarH;

	private CellPaletter cellPaletterW;
	private CellPaletter cellPaletterH;

	private PaletteTexture paletteTextureW;
	private PaletteTexture paletteTextureH;

	private int orientation;
	private int buffer_quantum;
	private float scrW, scrH;
	private float[] lineCoeffs;
	private boolean discrete;
	private boolean color_update;
	private boolean size_update;

	private float size_pct;
	private float pos_pct;
	private float content_pct;
	private float margin_pct;
	private int cell_numb;

	private Texture renderData;

	private FrameBuffer adapter;
	private final float dimX, dimY;


	public OPalette(int w, int h) {
		this(ORIENTATION_NONE, w, h);
	}
	public OPalette(final int orientation, int w, int h) {
		setRangeLineCoeffs(new float[]{});
		setColor(GLESUtils.Color.WHITE);
		setOrientation(orientation);
		setRelativeContentSize(.8f);
		setRelativePosition(.8f);
		setRelativeSize(.201f);
		setBufferQuantum(5);
		setMargin_pct(.1f);
		setDiscrete(true);
		setCellNumb(4);
		create(w, h);
		dimX = w;
		dimY = h;
	}

	// FIXME: 01/04/17 // TODO: 01/04/17 when canvas size changes, vertical palette shows with error
	public OPalette create(int w, int h) {

		adapter = OPallFBOCreator.FrameBuffer(w, h, false);
		barGradientBuffer = OPallFBOCreator.FrameBuffer(w, h, false);

		barSrcBufferW = OPallFBOCreator.FrameBuffer()
				.createFBO(w, buffer_quantum, w, h, false).beginFBO(GLESUtils::clear);
		barScrBufferH = OPallFBOCreator.FrameBuffer()
				.createFBO(buffer_quantum, h, w, h, false).beginFBO(GLESUtils::clear);

		paletteTextureW = new PaletteTexture(PaletteTexture.TYPE_HORIZONTAL, w, h);
		paletteTextureH = new PaletteTexture(PaletteTexture.TYPE_VERTICAL, w, h);

		cellPaletterW = new CellPaletter(CellPaletter.CellType.Horizontal, w, h);
		cellPaletterH = new CellPaletter(CellPaletter.CellType.Vertical, w, h);

		backBarW = new BarHorizontal(w, h);
		backBarH = new BarVertical(w, h);

		setScreenDim(w, h);
		return this;
	}

	@Override
	public void render(Camera2D camera) {

		if (colorUpDate()) {
			backBarW.setColor(color);
			backBarH.setColor(color);
		}

		if (orientation == ORIENTATION_NONE) return;
		if (orientation == ORIENTATION_HORIZONTAL) {

			paletteTextureW.set(0, renderData);
			paletteTextureW.set(1, barSrcBufferW.getTexture());
			paletteTextureW.setFocusOn(1);

			barGradientBuffer.beginFBO(() -> {
				GLESUtils.clear();
				paletteTextureW.setDimension(scrW, scrH);
				paletteTextureW.setRangeLineCoeffs(lineCoeffs);
				paletteTextureW.setStart(scrH - backBarW.getPosY() + backBarW.getHeight());
				paletteTextureW.setEnd(scrH - backBarW.getPosY());
				paletteTextureW.render(camera);
			});

			backBarW.setRelativeSize(size_pct);
			backBarW.setRelativePosition(pos_pct);
			backBarW.setRelativeContentSize(content_pct);
			cellPaletterW.setMargin_pct(margin_pct);
			cellPaletterW.setCellNumb(cell_numb);

			if (isDiscrete()) {
				cellPaletterW.generate(barGradientBuffer.getTexture(), camera);
				backBarW.render(camera, cellPaletterW, buffer_quantum);
			} else backBarW.render(camera, barGradientBuffer, buffer_quantum);

		} else if (orientation == ORIENTATION_VERTICAL) {

			paletteTextureH.set(0, renderData);
			paletteTextureH.set(1, barScrBufferH.getTexture());
			paletteTextureH.setFocusOn(1);


			barGradientBuffer.beginFBO(() -> {
				GLESUtils.clear();
				paletteTextureH.setDimension(scrW, scrH);
				paletteTextureH.setRangeLineCoeffs(lineCoeffs);
				paletteTextureH.setStart(backBarH.getPosX());
				paletteTextureH.setEnd(backBarH.getWidth() + backBarH.getPosX());
				paletteTextureH.render(camera);
			});

			backBarH.setRelativeSize(size_pct);
			backBarH.setRelativePosition(pos_pct);
			backBarH.setRelativeContentSize(content_pct);
			cellPaletterH.setMargin_pct(margin_pct);
			cellPaletterH.setCellNumb(cell_numb);


			if (isDiscrete()) {
				cellPaletterH.generate(barGradientBuffer.getTexture(), camera);
				backBarH.render(camera, cellPaletterH, buffer_quantum);
			} else backBarH.render(camera, barGradientBuffer.getTexture(), buffer_quantum);


//			 FIXME: 02/04/17 // TODO: 02/04/17
//			if (isDiscrete()) {
//				cellPaletterH.generate(barGradientBuffer.getTexture(), camera);
//				adapter.beginFBO(() -> {
//					GLESUtils.clear();
//					backBarH.render(camera, cellPaletterH, buffer_quantum);
//				});
//			} else adapter.beginFBO(() -> {
//				GLESUtils.clear();
//				backBarH.render(camera, barGradientBuffer.getTexture(), buffer_quantum);
//			});
//			adapter.render(camera);


		}
	}


	public OPalette setRelativeSize(float size_pct) {
		this.size_pct = size_pct;
		return this;
	}

	public OPalette setRelativePosition(float pos_pct) {
		this.pos_pct = pos_pct;
		return this;
	}

	public OPalette setRelativeContentSize(float size_pct) {
		this.content_pct = size_pct;
		return this;
	}

	public OPalette setCellNumb(int n) {
		this.cell_numb = n;
		return this;
	}

	public OPalette setMargin_pct(float m) {
		this.margin_pct = m;
		return this;
	}

	public OPalette setBufferQuantum(int bufferQuantum) {
		this.buffer_quantum = bufferQuantum;
		return this;
	}

	public OPalette setRangeLineCoeffs(float[] coeffs) {
		this.lineCoeffs = coeffs;
		return this;
	}

	public OPalette setRenderData(Texture renderData) {
		this.renderData = renderData;
		return this;
	}

	public OPalette setOrientation(int orientation) {
		this.orientation = orientation;
		return this;
	}

	public OPalette setColor(GLESUtils.Color color) {
		this.color.set(color);
		color_update = true;
		return this;
	}

	public OPalette setDiscrete(boolean discrete) {
		this.discrete = discrete;
		return this;
	}

	private boolean colorUpDate() {
		if (color_update) {
			color_update = false;
			return true;
		}
		return false;
	}

	private boolean sizeUpDate() {
		if (size_update) {
			size_update = false;
			return true;
		}
		return false;
	}

	public GLESUtils.Color getColor() {
		return color;
	}

	public float getSize_pct() {
		return size_pct;
	}

	public float getPos_pct() {
		return pos_pct;
	}

	public float getContentSize_pct() {
		return content_pct;
	}

	public float getMargin_pct() {
		return margin_pct;
	}

	public int getCellNumb() {
		return cell_numb;
	}

	public boolean isDiscrete() {
		return discrete;
	}

	public int getOrientation() {
		return orientation;
	}

	@Override
	public void setScreenDim(float w, float h) {
		scrW = w;
		scrH = h;

		size_update = true;
	}

	@Override
	public int getWidth() {
		return (int) scrW;
	}

	@Override
	public int getHeight() {
		return (int) scrH;
	}
}