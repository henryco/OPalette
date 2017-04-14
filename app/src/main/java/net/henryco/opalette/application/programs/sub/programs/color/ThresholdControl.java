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
import android.widget.Switch;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

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


		InjectableSeekBar bar = new InjectableSeekBar(view, context.getActivityContext().getResources().getString(txt_button_res));
		bar.onBarCreate(seekBar -> {
			seekBar.setProgress(bar.de_norm(texture.getThreshold()));
			seekBar.setEnabled(texture.isThresholdEnable());
		});
		bar.setBarListener(new OPallSeekBarListener().onProgress((seekBar, progress, fromUser) -> {
			if (fromUser) texture.setThreshold(bar.norm(progress));
			if (texture.isThresholdEnable()) context.getRenderSurface().update();
		}));

		OPallViewInjector<AppMainProto> control = new OPallViewInjector<AppMainProto>(view, R.layout.switcher) {
			@Override
			protected void onInject(AppMainProto context, View view) {
				TextView textView = (TextView) view.findViewById(R.id.switcherText);
				textView.setText(R.string.transparency);

				Switch switcher = (Switch) view.findViewById(R.id.switcherButton);
				switcher.setChecked(texture.isThresholdColored());
				switcher.setEnabled(texture.isThresholdEnable());
				switcher.setOnCheckedChangeListener((buttonView, isChecked) -> {
					texture.setThresholdColored(isChecked);
					context.getRenderSurface().update();
				});

				String dis = context.getActivityContext().getResources().getString(R.string.disable);
				String enb = context.getActivityContext().getResources().getString(R.string.enable);

				context.setTopControlButton(button -> button
						.setVisible(true).setEnabled(true)
						.setTitle(texture.isThresholdEnable() ? dis : enb), () -> {
							if (texture.isThresholdEnable()) {
								context.setTopControlButton(b -> b.setTitle(enb));
								texture.setThresholdEnable(false);
								bar.setEnable(false);
								switcher.setEnabled(false);
								context.getRenderSurface().update();
							} else {
								context.setTopControlButton(b -> b.setTitle(dis));
								texture.setThresholdEnable(true);
								bar.setEnable(true);
								switcher.setEnabled(true);
								context.getRenderSurface().update();
							}
						}
				);
			}
		};

		OPallViewInjector.inject(context.getActivityContext(), bar, control);
	}


}
