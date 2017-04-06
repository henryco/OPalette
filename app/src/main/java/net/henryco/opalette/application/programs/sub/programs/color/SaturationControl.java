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

package net.henryco.opalette.application.programs.sub.programs.color;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 06/04/17.
 */

public class SaturationControl extends AppAutoSubControl<AppMainProto> {


	private static final int img_button_res = R.drawable.ic_filter_vintage_white_24dp;
	private static final int txt_button_res = R.string.control_saturation;

	private final EdTexture texture;

	public SaturationControl(final EdTexture texture) {
		super(img_button_res, txt_button_res);
		this.texture = texture;
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		InjectableSeekBar satBar = new InjectableSeekBar(view, context.getActivityContext().getResources().getString(txt_button_res));
		satBar.setDefaultPoint(0, 50);
		satBar.onBarCreate(bar -> bar.setProgress(satBar.de_norm(texture.getSaturation())));
		satBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			texture.setAddSaturation(satBar.norm(progress));
			context.getRenderSurface().update();
		}));

		context.setTopControlButton(button -> button.setEnabled(true).setVisible(true).setTitle(R.string.control_top_bar_button_reset), () -> {
			satBar.setProgress(satBar.de_norm(0));
		});

		OPallViewInjector.inject(context.getActivityContext(), satBar);
	}
}
