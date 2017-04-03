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
 * Created by HenryCo on 03/04/17.
 */

public class GammaCorrControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_brush_white_24dp;
	private static final int txt_button_res = R.string.control_gamma_correction;

	private static final float range_max = 5;
	private static final float range_min = 1 / range_max;
	private static final float ratio = 20;

	private final EdTexture texture;
	private final float def_gcr;

	public GammaCorrControl(EdTexture texture) {
		super(img_button_res, txt_button_res);
		this.texture = texture;
		this.def_gcr = texture.getGammaCorrection();
	}

	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		InjectableSeekBar gammaBar = new InjectableSeekBar(view, context.getActivityContext().getResources().getString(txt_button_res));
		gammaBar.setMax((int) ((range_max - range_min) * ratio));
		gammaBar.setTextValuerCorrector(f -> (f / ratio) + range_min);
		gammaBar.onBarCreate(bar -> bar.setProgress((int)((texture.getGammaCorrection() - range_min) * ratio)));
		gammaBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			texture.setGammaCorrection((progress / ratio) + range_min);
			context.getRenderSurface().update();
		}));

		context.setTopControlButton(button -> button.setVisible(true).setEnabled(true).setTitle(R.string.control_top_bar_button_reset), () -> {
			texture.setGammaCorrection(def_gcr);
			gammaBar.setProgress((int)((def_gcr - range_min) * ratio));
		});

		OPallViewInjector.inject(context.getActivityContext(), gammaBar);

	}


}
