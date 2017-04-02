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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.proto.AppMainProto;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 02/04/17.
 */

public class FilterProgram implements AppSubProgram<AppMainProto>, AppSubProtocol {


	private long id = methods.genID(FilterProgram.class);
	private ProxyRenderData<Texture> proxyRenderData = new ProxyRenderData<>();


	private OPallRequester feedBackListener;
	private AppSubProgramHolder holder;


	@Override public void setProgramHolder(AppSubProgramHolder holder) {
		this.holder = holder;
	}
	@Override public void setFeedBackListener(OPallRequester feedBackListener) {
		this.feedBackListener = feedBackListener;
	}

	@Override public void setID(long id) {
		this.id = id;
	}
	@Override public long getID() {
		return id;
	}



	@Override
	public void acceptRequest(Request request) {

	}



	@Override
	public void create(@Nullable GL10 gl, int width, int height, AppMainProto context) {

	}

	@Override
	public void onSurfaceChange(@Nullable GL10 gl, AppMainProto context, int width, int height) {

	}

	@Override
	public void render(@Nullable GL10 gl10, AppMainProto context, Camera2D camera, int w, int h) {

	}




	@Override
	public void setRenderData(OPallRenderable data) {
		if (data != null) proxyRenderData.setRenderData((Texture) data);
	}


	@NonNull @Override
	public OPallRenderable getRenderData() {
		return proxyRenderData.getRenderData();
	}

	@Nullable @Override
	public OPallRenderable getFinalRenderData() {
		return null;
	}
}
