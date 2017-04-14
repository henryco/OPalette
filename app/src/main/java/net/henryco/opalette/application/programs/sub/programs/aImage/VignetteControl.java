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
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.Vignette;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.ConvolveTexture;
import net.henryco.opalette.api.utils.dialogs.OPallOptionListDialog;
import net.henryco.opalette.api.utils.lambda.functions.OPallFunction;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 03/04/17.
 */

public class VignetteControl extends AppAutoSubControl<AppMainProto> {

	public interface VignetteControlHolder {
		int VIGNETTE_IMAGE = 0;
		int VIGNETTE_SCREEN = 1;
		void setVignetteImage();
		void setVignetteScreen();
	}

 	private static final int img_button_res = R.drawable.ic_vignette_white_24dp;
	private static final int txt_button_res = R.string.control_vignette;

	private final Vignette vignette;
	private AppSubProgram.ProxyRenderData<ConvolveTexture> imgHolder;
	private final VignetteControlHolder vignetteHolder;

	public VignetteControl(final Vignette vignette,
						   final AppSubProgram.ProxyRenderData<ConvolveTexture> renderData,
						   final VignetteControlHolder vignetteHolder) {
		super(img_button_res, txt_button_res);
		this.vignette = vignette;
		this.imgHolder = renderData;
		this.vignetteHolder = vignetteHolder;
	}



	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		Runnable updateFunc = () -> {
			imgHolder.setStateUpdated();
			context.getRenderSurface().update();
		};

		InjectableSeekBar vigBar = new InjectableSeekBar(view, context.getActivityContext().getResources().getString(txt_button_res));
		vigBar.onBarCreate(bar -> bar.setProgress(vigBar.de_norm(vignette.getPower())));
		vigBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				vignette.setPower(vigBar.norm(progress));
				vignette.setActive(!(progress == 0));
				updateFunc.run();
			}
		}));


		InjectableSeekBar radBar = new InjectableSeekBar(view, context.getActivityContext().getResources().getString(R.string.radius));
		radBar.setTextValuerCorrector(f -> f / 100);
		radBar.onBarCreate(bar -> bar.setProgress(radBar.de_norm(vignette.getRadius())));
		radBar.setBarListener(new OPallSeekBarListener().onProgress((seekBar, progress, fromUser) -> {
			if (fromUser) {
				vignette.setRadius(radBar.norm(progress));
				updateFunc.run();
			}
		}));

		context.setTopControlButton(button -> button.setVisible(true).setEnabled(true).setTitle(R.string.options), () -> {

			OPallFunction<String, Integer> stringFunc = integer
					-> context.getActivityContext().getResources().getString(integer);

			String options = stringFunc.apply(R.string.vignette_mode_pick);
			String disable = stringFunc.apply(R.string.disable);
			String image = stringFunc.apply(R.string.only_image);
			String screen = stringFunc.apply(R.string.full_screen);

			Runnable imgFunc = () -> {
				vignetteHolder.setVignetteImage();
				updateFunc.run();
			};

			Runnable scrFunc = () -> {
				vignetteHolder.setVignetteScreen();
				updateFunc.run();
			};

			Runnable resetFunc = () -> {
				vigBar.setProgress(0);
				radBar.setProgress(radBar.de_norm(0));
				vignette.setPower(0).setActive(false);
				vignette.setRadius(0);
				updateFunc.run();
			};

			new OPallOptionListDialog()
					.setTittle(options)
					.setOptionsNames(image, screen, disable)
					.setOptionsActions(imgFunc, scrFunc, resetFunc)
			.show(context.getActivityContext().getSupportFragmentManager(), "Vignette options dialog");
		});

		OPallViewInjector.inject(context.getActivityContext(), radBar, vigBar);
	}
}
