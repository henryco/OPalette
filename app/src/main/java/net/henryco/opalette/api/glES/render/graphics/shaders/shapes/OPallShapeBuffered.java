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

package net.henryco.opalette.api.glES.render.graphics.shaders.shapes;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.bounds.Bounds2D;
import net.henryco.opalette.api.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

/**
 * Created by HenryCo on 28/03/17.
 */

public abstract class OPallShapeBuffered extends OPallShape {


	private final FrameBuffer imageBuffer;
	private final float[] defDim = {0,0};
	private boolean needUpDate = false;
	private boolean visible = true;

	public OPallShapeBuffered(String VERT_FILE, String FRAG_FILE, float w, float h) {
		this(VERT_FILE, FRAG_FILE);
		create(w, h);
	}
	public OPallShapeBuffered(String VERT_FILE, String FRAG_FILE) {
		super(VERT_FILE, FRAG_FILE, 2);
		imageBuffer = OPallFBOCreator.FrameBuffer();
	}

	public OPallShapeBuffered create(float scr_width, float scr_height) {
		super.setScreenDim(scr_width, scr_height);
		if (scr_width != 0 && scr_height != 0) {
			imageBuffer.createFBO(scr_width, scr_height, false);
			defDim[0] = scr_width;
			defDim[1] = scr_height;
			update();
		}	return this;
	}



	@Override
	protected void render(int glProgram, Camera2D camera, OPallConsumer<Integer> setter) {

		if (needUpDate) {
			imageBuffer.beginFBO(() -> {
				GLESUtils.clear(GLESUtils.Color.TRANSPARENT);
				super.render(glProgram, camera, setter);
			});
			needUpDate = false;
		}
		if (visible) {
			camera.backTranslate(() -> {
				camera.setPosXY_absolute(0,0);
				float tx = getScreenWidth() - defDim[0];
				float ty = getScreenHeight() - defDim[1];
				camera.translateXY(tx, ty); // position correction while canvas size changed
				imageBuffer.render(camera);
			});
		}
	}

	public FrameBuffer getShapeBuffer() {
		return imageBuffer;
	}

	public OPallShapeBuffered setVisible(boolean visible) {
		this.visible = visible;
		update();
		return this;
	}

	public boolean isVisible() {
		return visible;
	}

	@Override
	public OPallShapeBuffered bounds(BoundsConsumer<Bounds2D> processor) {
		super.bounds(processor);
		update();
		return this;
	}

	@Override
	public OPallShapeBuffered updateBounds() {
		super.updateBounds();
		update();
		return this;
	}

	protected void update() {
		needUpDate = true;
	}

}
