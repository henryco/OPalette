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
 * Created by HenryCo on 02/04/17.
 */

public class ContrastControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_timelapse_white_24dp;
	private static final int txt_button_res = R.string.control_contrast;

	private final EdTexture texture;

	public ContrastControl(EdTexture texture) {
		super(img_button_res, txt_button_res);
		this.texture = texture;
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		String contrast = context.getActivityContext().getResources().getString(R.string.control_contrast);
		InjectableSeekBar contBar = new InjectableSeekBar(view, contrast);
		contBar.setDefaultPoint(0, 50);
		contBar.onBarCreate(bar -> bar.setProgress(contBar.de_norm(texture.getContrast() - 1)));
		contBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			texture.setContrast(contBar.norm(progress) + 1);
			context.getRenderSurface().update();
		}));

		context.setTopControlButton(bar -> bar.setTitle(R.string.control_top_bar_button_reset).setEnabled(true).setVisible(true), () -> {
			contBar.setProgress(contBar.de_norm(0));
		});

		OPallViewInjector.inject(context.getActivityContext(), contBar);
	}
}
