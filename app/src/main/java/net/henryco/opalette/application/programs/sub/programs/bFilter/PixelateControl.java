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
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.PixelatedTexture;
import net.henryco.opalette.api.utils.lambda.functions.OPallFunction;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.conf.GodConfig;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 06/04/17.
 */

public class PixelateControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_blur_on_white_24dp;
	private static final int txt_button_res = R.string.control_pixelization;

	private final float defTexPixels;
	private final float defTexQuantum;
	private final int defDitherType;

	private final AppSubProgram.ProxyRenderData proxyRenderData;
	private final FilterPipeLiner<PixelatedTexture> pixelatedTexture;
	private final FilterPipeLiner<DitherTexture> ditherTexture;

	public PixelateControl(final AppSubProgram.ProxyRenderData proxyRenderData,
						   final FilterPipeLiner<PixelatedTexture> pixelatedTexture,
						   final FilterPipeLiner<DitherTexture> ditherTexture) {

		super(img_button_res, txt_button_res);
		this.proxyRenderData = proxyRenderData;
		this.pixelatedTexture = pixelatedTexture;
		this.ditherTexture = ditherTexture;
		this.defTexPixels = pixelatedTexture.getFilterTexture().getPixelsNumb();
		this.defTexQuantum = pixelatedTexture.getFilterTexture().getPixelQuantum();
		this.defDitherType = ditherTexture.getFilterTexture().getFilterType();
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		final OPallFunction<String, Integer> getStringFunc = integer -> context.getActivityContext().getResources().getString(integer);
		final OPallFunction<Float, Float> valFunc = f -> defTexPixels * (1f - (f / GodConfig.NORM_RANGE));
		final OPallFunction<Integer, Float> prgFunc = f -> (int)((GodConfig.NORM_RANGE / defTexPixels) * (defTexPixels - f));

		final Runnable updateFunc = () -> {
			proxyRenderData.setStateUpdated();
			context.getRenderSurface().update();
		};

		final int typeSmall = InjectableSeekBar.TYPE_SMALL;
		final InjectableSeekBar pixBar = new InjectableSeekBar(view, typeSmall, getStringFunc.apply(R.string.texel_size));
		final InjectableSeekBar quantumBar = new InjectableSeekBar(view, typeSmall, getStringFunc.apply(R.string.pixelation_leveL)).setMax(20);
		final InjectableSeekBar ditherBar = new InjectableSeekBar(view, typeSmall, getStringFunc.apply(R.string.effect_scale_0_3));

		final String disable = getStringFunc.apply(R.string.disable);
		final String enable = getStringFunc.apply(R.string.enable);

		boolean[] stat = {pixelatedTexture.isActive()};
		context.setTopControlButton(button -> button.setEnabled(true).setVisible(true).setTitle(pixelatedTexture.isActive() ? disable : enable), () -> {

			if (!stat[0]){
				context.setTopControlButton(b -> b.setTitle(disable));
				ditherTexture.setActive(true);
			}
			else {
				context.setTopControlButton(b -> b.setTitle(enable));
				pixelatedTexture.getFilterTexture().setPixelQuantum(defTexQuantum).setPixelsNumb(defTexPixels);
				ditherTexture.getFilterTexture().setFilterType(defDitherType);
				ditherTexture.setActive(false);
				quantumBar.setProgress((int) defTexQuantum);
				ditherBar.setProgress(defDitherType + 1);
				pixBar.setProgress(0);
			}
			stat[0] = !stat[0];
			pixelatedTexture.setActive(stat[0]);
			quantumBar.setEnable(stat[0]);
			ditherBar.setEnable(stat[0]);
			pixBar.setEnable(stat[0]);
			updateFunc.run();
		});


		pixBar.setMax(GodConfig.NORM_RANGE).setEnable(pixelatedTexture.isActive());
		pixBar.onBarCreate(bar -> bar.setProgress(prgFunc.apply(pixelatedTexture.getFilterTexture().getPixelsNumb())));
		pixBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				pixelatedTexture.getFilterTexture().setPixelsNumb(valFunc.apply((float) Math.min(progress, GodConfig.NORM_RANGE - 1)));
				updateFunc.run();
			}
		}));

		quantumBar.setEnable(pixelatedTexture.isActive());
		quantumBar.onBarCreate(bar -> bar.setProgress((int) pixelatedTexture.getFilterTexture().getPixelQuantum()));
		quantumBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				pixelatedTexture.getFilterTexture().setPixelQuantum(Math.max(1, progress));
				updateFunc.run();
			}
		}));


		ditherBar.setMax(3).setDiscrete(true).setEnable(pixelatedTexture.isActive());
		ditherBar.onBarCreate(bar -> bar.setProgress(ditherTexture.getFilterTexture().getFilterType() + 1));
		ditherBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				ditherTexture.getFilterTexture().setFilterType(progress - 1);
				ditherTexture.setActive(progress != 0);
				updateFunc.run();
			}
		}));

		OPallViewInjector.inject(context.getActivityContext(), ditherBar, quantumBar, pixBar);

	}
}
