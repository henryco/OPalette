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

package net.henryco.opalette.application.programs.sub.programs.aImage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.ConvolveTexture;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 03/04/17.
 */

public class NoiseControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_grain_white_24dp;
	private static final int txt_button_res = R.string.control_noise;

	private final AppSubProgram.ProxyRenderData<ConvolveTexture> filterHolder;

	public NoiseControl(final AppSubProgram.ProxyRenderData<ConvolveTexture> data) {
		super(img_button_res, txt_button_res);
		this.filterHolder = data;
	}

	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		InjectableSeekBar noiseBar = new InjectableSeekBar(view, context.getActivityContext().getResources().getString(R.string.noise_level));
		noiseBar.onBarCreate(bar -> bar.setProgress(noiseBar.de_norm(filterHolder.getRenderData().getNoiseLevel())));
		noiseBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				filterHolder.setStateUpdated().getRenderData().setNoiseLevel(noiseBar.norm(progress));
				context.getRenderSurface().update();
			}
		}));

		context.setTopControlButton(button
				-> button.setTitle(R.string.disable).setEnabled(true).setVisible(true), () -> {
			noiseBar.setProgress(noiseBar.de_norm(0));
			filterHolder.setStateUpdated().getRenderData().setNoiseLevel(0);
			context.getRenderSurface().update();
		});

		OPallViewInjector.inject(context.getActivityContext(), noiseBar);
	}
}
