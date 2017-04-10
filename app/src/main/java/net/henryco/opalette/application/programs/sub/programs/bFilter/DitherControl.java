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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.DitherTexture;
import net.henryco.opalette.api.utils.lambda.functions.OPallFunction;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 10/04/17.
 */

public class DitherControl extends AppAutoSubControl<AppMainProto> {


	private static final int img_button_res = R.drawable.ic_dither_white_24dp;
	private static final int txt_button_res = R.string.control_dither;

	private final FilterPipeLiner<DitherTexture> ditherTexture;
	private final AppSubProgram.ProxyRenderData proxyRenderData;

	private final int defFilterType;

	public DitherControl(final AppSubProgram.ProxyRenderData proxyRenderData,
						 final FilterPipeLiner<DitherTexture> ditherTexture) {
		super(img_button_res, txt_button_res);
		this.ditherTexture = ditherTexture;
		this.proxyRenderData = proxyRenderData;
		this.defFilterType = ditherTexture.getFilterTexture().getFilterType();
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {
		OPallFunction<String, Integer> getStringFunc = integer -> context.getActivityContext().getResources().getString(integer);
		Runnable updateFunc = () -> {
			proxyRenderData.setStateUpdated();
			context.getRenderSurface().update();
		};

		InjectableSeekBar ditherBar = new InjectableSeekBar(view, getStringFunc.apply(R.string.effect_scale_0_3));
		ditherBar.setMax(3).setDiscrete(true);
		ditherBar.onBarCreate(bar -> bar.setProgress(ditherTexture.getFilterTexture().getFilterType() + 1));
		ditherBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				ditherTexture.getFilterTexture().setFilterType(progress - 1);
				ditherTexture.setActive(progress != 0);
				updateFunc.run();
			}
		}));

		context.setTopControlButton(b -> b.setEnabled(true).setVisible(true).setTitle(getStringFunc.apply(R.string.control_top_bar_button_reset)),() -> {
			ditherTexture.setActive(false);
			ditherTexture.getFilterTexture().setFilterType(defFilterType);
			ditherBar.setProgress(defFilterType + 1);
			updateFunc.run();
		});

		OPallViewInjector.inject(context.getActivityContext(), ditherBar);
	}
}
