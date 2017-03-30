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

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableColorButtons;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

import static net.henryco.opalette.application.injectables.InjectableColorButtons.runColorPicker;

/**
 * Created by HenryCo on 30/03/17.
 */

public class ThresholdControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_compare_white_24dp;
	private static final int txt_button_res = R.string.control_threshold;

	private final EdTexture texture;

	public ThresholdControl(EdTexture texture) {
		super(img_button_res, txt_button_res);
		this.texture = texture;
	}

	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		context.getRenderSurface().update();

		InjectableColorButtons threshold = new InjectableColorButtons(view, "Color");
		threshold.setChecked(texture.isThresholdEnable());
		threshold.setButtonColor(texture.isThresholdEnable() ? texture.thr.hex() : Color.TRANSPARENT);
		threshold.setChecked(texture.isThresholdEnable());
		threshold.setSwitchListener((button, isChecked) -> {
			if (isChecked) threshold.setButtonColor(texture.thr.hex());
			else threshold.setButtonColor(Color.TRANSPARENT);
			texture.setThresholdEnable(isChecked);
			context.getRenderSurface().update();
		});
		threshold.setColorButtonListener(v -> runColorPicker(context.getActivityContext(), i -> {
			texture.thr.set(i);
			threshold.setButtonColor(i);
			context.getRenderSurface().update();
		}));

		InjectableSeekBar bar = new InjectableSeekBar(view, "Level").setMax(100);
		bar.onBarCreate(seekBar -> seekBar.setProgress(bar.de_norm(texture.getThreshold())));
		bar.setBarListener(new OPallSeekBarListener().onProgress((seekBar, progress, fromUser) -> {
			if (fromUser) texture.setThreshold(bar.norm(progress));
			if (texture.isThresholdEnable()) context.getRenderSurface().update();
		}));

		context.setTopControlButton(button -> button.setEnabled(true).setVisible(true).setTitle(R.string.control_top_bar_button_reset), () -> {
			texture.setThreshold(0.5f);
			texture.thr.set(GLESUtils.Color.WHITE);
			bar.setProgress(bar.de_norm(texture.getThreshold()));
			threshold.setButtonColor(texture.isThresholdEnable() ? texture.thr.hex() : Color.TRANSPARENT);
			context.getRenderSurface().update();
		});

		OPallViewInjector.inject(context.getActivityContext(), bar, threshold);
	}


}
