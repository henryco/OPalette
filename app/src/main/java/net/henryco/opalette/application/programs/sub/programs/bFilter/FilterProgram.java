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

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.BlurTexture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.BubbleTexture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.PixelatedTexture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.conf.GodConfig;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.proto.AppMainProto;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 02/04/17.
 */

public class FilterProgram implements AppSubProgram<AppMainProto>, AppSubProtocol, FilterControl.EdFilterHolder {


	private long id = methods.genID(FilterProgram.class);
	private ProxyRenderData<Texture> proxyRenderData = new ProxyRenderData<>();

	private OPallRequester feedBackListener;
	private AppSubProgramHolder holder;

	private TextView actualTextView;

	private boolean firstTime;
	private EdTexture filterTexture;
	private FrameBuffer filterBuffer;
	private FrameBuffer filterPreviewBuffer;
	private EdFilter actualFilter;

	private List<FilterPipeLiner> filterPipeLine;

	private boolean pipeLineStatus;


	private static final int FILTER_PREV_SIZE = GodConfig.DEFAULT_FILTER_PREVIEW_ICON_SIZE;

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
		request.openRequest(update_proxy_render_state, () -> proxyRenderData.setStateUpdated());
		request.openRequest(set_pipe_line_enable, () -> pipeLineStatus = true);
		request.openRequest(set_pipe_line_disable, () -> pipeLineStatus = false);
	}


	@Override
	public void stateUpDate() {
		proxyRenderData.setStateUpdated();
	}

	@Override
	public void setFilter(EdFilter filter, TextView textView) {
		this.actualTextView = textView;
		this.actualFilter = filter;
		proxyRenderData.setStateUpdated();
	}

	@Override
	public EdFilter getFilter() {
		return actualFilter;
	}


	@Override
	public TextView getTextView() {
		return actualTextView;
	}

	// TODO: 05/04/17 ADD FILTERS PIPE-LINE
	@Override // TODO: 05/04/17 ADD GAUSSIAN BLUR
	public void create(@Nullable GL10 gl, int width, int height, AppMainProto context) {

		firstTime = true;
		pipeLineStatus = true;
		filterPipeLine = new ArrayList<>();
		filterBuffer = OPallFBOCreator.FrameBuffer(width, height, false);
		filterTexture = new EdTexture();
		filterTexture.setScreenDim(width, height);
		actualFilter = EdFilter.getDefaultFilter();
		filterPreviewBuffer = OPallFBOCreator.FrameBuffer(FILTER_PREV_SIZE, FILTER_PREV_SIZE, false);


		FilterPipeLiner<PixelatedTexture> pixelator = new FilterPipeLiner<>(new PixelatedTexture(), width, height);
		FilterPipeLiner<BubbleTexture> bubble = new FilterPipeLiner<>(new BubbleTexture(), width, height);
		FilterPipeLiner<BlurTexture> blur = new FilterPipeLiner<>(new BlurTexture(), width, height);

		filterPipeLine.add(pixelator);
		filterPipeLine.add(bubble);
		filterPipeLine.add(blur);

		OPallViewInjector.inject(context.getActivityContext(), new BubbleControl(proxyRenderData, bubble));
		OPallViewInjector.inject(context.getActivityContext(), new PixelateControl(proxyRenderData, pixelator));
		OPallViewInjector.inject(context.getActivityContext(), new BlurControl(proxyRenderData, blur));
	}

	@Override
	public void onSurfaceChange(@Nullable GL10 gl, AppMainProto context, int width, int height) {
		for (FilterPipeLiner p: filterPipeLine) p.setScreenDim(width, height);
	}

	@Override
	public void render(@Nullable GL10 gl10, AppMainProto context, Camera2D camera, int w, int h) {

		if (firstTime) {	// CREATE FILTERS AND THEIR PREVIEW ICONS

			filterTexture.setSize(FILTER_PREV_SIZE, FILTER_PREV_SIZE);
			filterTexture.setFlip(false, false);

			camera.backTranslate(() -> {
				camera.setPosY(FILTER_PREV_SIZE - h);
				for (EdFilter filter: EdFilter.getFilterList()) {
					filter.applyFilter(filterTexture);
					filterPreviewBuffer.beginFBO(() -> filterTexture.render(camera, program -> GLESUtils.clear()));
					Bitmap prev = filterPreviewBuffer.getBitmap();
					OPallViewInjector.inject(context.getActivityContext(), new FilterControl(prev, filter, this));
				}
			});

			filterTexture.setSize(w, h);
			filterTexture.setFlip(false, true);
			firstTime = false;
		}

		if (pipeLineStatus) {
			if (!firstTime && proxyRenderData.stateUpdated()) {	// HERE ACTIVE FILTERS PIPE-LINE

				int texData = proxyRenderData.getRenderData().getTextureDataHandle();
				Texture filter = proxyRenderData.getRenderData();
				for (FilterPipeLiner fp: filterPipeLine) {
					fp.setTextureDataHandle(texData);
					fp.render(camera);
					filter = fp.getResult();
					texData = filter.getTextureDataHandle();
				}
				final Texture lastFilter = filter;
				filterTexture.setTextureDataHandle(texData);
				filterBuffer.beginFBO(() -> {
					GLESUtils.clear();
					lastFilter.render(camera);
					actualFilter.applyFilter(filterTexture).render(camera);
				});
			}
		}


	}




	@Override
	public void setRenderData(OPallRenderable data) {
		if (data != null) {
			proxyRenderData.setRenderData((Texture) data);
			if (filterTexture != null) {
				if (firstTime) filterTexture.set((Texture) data); //WE EXPECT FULLSCREEN FRAMEBUFFER
				filterTexture.setTextureDataHandle(((Texture) data).getTextureDataHandle());
			}
		}
	}


	@NonNull @Override
	public OPallRenderable getRenderData() {
		return filterBuffer.getTexture();
	}

	@Nullable @Override
	public OPallRenderable getFinalRenderData() {
		return null;
	}


}
