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

package net.henryco.opalette.application.programs.sub.programs.bFilter;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 06/04/17.
 */

public class FilterPipeLiner<T extends Texture> implements OPallRenderable {

	private final T filterTexture;
	private final Texture texture;
	private final FrameBuffer buffer;

	private boolean active;
	private float w, h;

	public FilterPipeLiner(T filterTexture, int scrW, int scrH, boolean active) {
		this.buffer = OPallFBOCreator.FrameBuffer(scrW, scrH, false);
		this.filterTexture = filterTexture;
		this.texture = new Texture();
		this.texture.set(filterTexture);
		this.active = active;

		texture.setScreenDim(w, h);
		buffer.setScreenDim(w, h);
		this.w = scrW;
		this.h = scrH;
		setScreenDim(scrW, scrH);
	}
	public FilterPipeLiner(T filterTexture, int scrW, int scrH) {
		this(filterTexture, scrW, scrH, false);
	}

	public boolean isActive() {
		return active;
	}

	public FilterPipeLiner setActive(boolean active) {
		this.active = active;
		return this;
	}

	public T getFilterTexture() {
		return filterTexture;
	}

	public FilterPipeLiner setTextureDataHandle(int dataHandle) {
		this.filterTexture.setTextureDataHandle(dataHandle);
		this.texture.setTextureDataHandle(dataHandle);
		return this;
	}

	public Texture getResult() {
		if (active) return buffer.getTexture();
		return texture;
	}

	@Override
	public void render(Camera2D camera) {
		if (active) buffer.beginFBO(() -> filterTexture.render(camera, program -> GLESUtils.clear()));
	}

	@Override
	public void setScreenDim(float w, float h) {
		filterTexture.setScreenDim(w, h);
	}

	@Override
	public int getWidth() {
		return (int) w;
	}

	@Override
	public int getHeight() {
		return (int) h;
	}
}
