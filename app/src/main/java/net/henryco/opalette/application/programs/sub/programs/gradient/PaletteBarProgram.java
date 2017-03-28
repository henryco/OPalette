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

package net.henryco.opalette.application.programs.sub.programs.gradient;

import android.support.annotation.Nullable;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.glES.render.graphics.units.bar.BarHorizontal;
import net.henryco.opalette.api.glES.render.graphics.units.bar.OPallBar;
import net.henryco.opalette.api.glES.render.graphics.units.palette.CellPaletter;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.proto.AppMainProto;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public class PaletteBarProgram implements AppSubProgram<AppMainProto>, AppSubProtocol {

	private long id = methods.genID(PaletteBarProgram.class);
	private ProxyRenderData<Texture> proxyRenderData = new ProxyRenderData<>();


	private CellPaletter cellPaletter;
	private OPallBar backBar;
	private int buffer_quantum = 5;
	private OPallRequester feedBackListener;
	private AppSubProgramHolder holder;

	@Override
	public void setProgramHolder(AppSubProgramHolder holder) {
		this.holder = holder;
	}

	@Override
	public void setFeedBackListener(OPallRequester feedBackListener) {
		this.feedBackListener = feedBackListener;
	}

	@Override
	public void acceptRequest(Request request) {
		request.openRequest(set_buffer_quantum, () -> this.buffer_quantum = request.getData());
		request.openRequest(update_proxy_render_state, () -> proxyRenderData.setStateUpdated());
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public void create(@Nullable GL10 gl, int width, int height, AppMainProto context) {

		cellPaletter = new CellPaletter();
		cellPaletter.setScreenDim(width, height);
		backBar = new BarHorizontal();
		backBar.createBar(width, height);
		sendBarHeightInfo();
	}

	@Override
	public void onSurfaceChange(@Nullable GL10 gl, AppMainProto context, int width, int height) {

		proxyRenderData.setStateUpdated();
		sendBarHeightInfo();
	}

	@Override
	public void render(@Nullable GL10 gl10, AppMainProto context, Camera2D camera, int w, int h) {

		cellPaletter.generate(getRenderData(), camera);
		backBar.render(camera, cellPaletter, buffer_quantum);
	}


	private OPallRenderable finalRenderData = new OPallRenderable() {

		@Override public void render(Camera2D camera) {
			backBar.render(camera, cellPaletter, buffer_quantum);
		}
		@Override public void setScreenDim(float w, float h) {

		}
		@Override public int getWidth() {
			return 0;
		}
		@Override public int getHeight() {
			return 0;
		}
	};



	@Override
	public void setRenderData(OPallRenderable data) {
		proxyRenderData.setRenderData((Texture) data);
	}

	@Override
	public Texture getRenderData() {
		return proxyRenderData.getRenderData();
	}

	@Nullable @Override
	public OPallRenderable getFinalRenderData() {
		return finalRenderData;
	}

	private void sendBarHeightInfo() {
		feedBackListener.sendRequest(new Request(send_back_bar_start, backBar.getPosY()).destination(d -> d.except(id)));
		feedBackListener.sendRequest(new Request(send_back_bar_end, backBar.getPosY() + backBar.getHeight()).destination(d -> d.except(id)));
	}
}
