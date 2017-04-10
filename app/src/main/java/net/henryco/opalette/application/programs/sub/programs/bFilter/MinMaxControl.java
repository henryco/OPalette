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
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.MinMaxTexture;
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

public class MinMaxControl extends AppAutoSubControl<AppMainProto> {


	private final int defKernelSize;
	private final float defEffectScale;

	private final FilterPipeLiner<MinMaxTexture> minMaxTexture;
	private final AppSubProgram.ProxyRenderData proxyRenderData;

	public MinMaxControl(final AppSubProgram.ProxyRenderData proxyRenderData,
						 final FilterPipeLiner<MinMaxTexture> minMaxTexture) {
		super(getResImgByType(
				minMaxTexture.getFilterTexture().getType()),
				getResStringByType(minMaxTexture.getFilterTexture().getType()));
		this.minMaxTexture = minMaxTexture;
		this.proxyRenderData = proxyRenderData;
		this.defEffectScale = minMaxTexture.getFilterTexture().getEffectScale();
		this.defKernelSize = minMaxTexture.getFilterTexture().getKernelSize();
	}

	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		OPallFunction<String, Integer> getStringFunc = integer -> context.getActivityContext().getResources().getString(integer);

		InjectableSeekBar scaleBar = new InjectableSeekBar(view, getStringFunc.apply(R.string.effect_scale));
//		InjectableSeekBar kernelBar = new InjectableSeekBar(view, getStringFunc.apply(R.string.kernel_size));

		Runnable updateFunc = () -> {
			proxyRenderData.setStateUpdated();
			context.getRenderSurface().update();
		};

//		String enable = getStringFunc.apply(R.string.enable);
//		String disable = getStringFunc.apply(R.string.disable);

		context.setTopControlButton(b -> b.setEnabled(true).setVisible(true).setTitle(getStringFunc.apply(R.string.control_top_bar_button_reset)), () -> {
			scaleBar.setProgress(scaleBar.de_norm(defEffectScale));
			minMaxTexture.getFilterTexture().setEffectScale(defEffectScale);
			minMaxTexture.setActive(false);
			updateFunc.run();
		});

//		boolean[] stat = {minMaxTexture.isActive()};
//		context.setTopControlButton(b -> b.setVisible(true).setEnabled(true).setTitle(minMaxTexture.isActive() ? disable : enable), () -> {
//
//			if (!stat[0]) context.setTopControlButton(b -> b.setTitle(disable));
//			else {
//				scaleBar.setProgress(scaleBar.de_norm(defEffectScale));
//				kernelBar.setProgress(defKernelSize);
//				minMaxTexture.getFilterTexture().setEffectScale(defEffectScale);
//				minMaxTexture.getFilterTexture().setKernelSize(defKernelSize);
//				context.setTopControlButton(b -> b.setTitle(enable));
//			}
//			stat[0] = !stat[0];
//			scaleBar.setEnable(stat[0]);
//			kernelBar.setEnable(stat[0]);
//			minMaxTexture.setActive(stat[0]);
//			updateFunc.run();
//		});

//		scaleBar.setEnable(minMaxTexture.isActive());
//		kernelBar.setEnable(minMaxTexture.isActive());

		scaleBar.onBarCreate(bar -> bar.setProgress(scaleBar.de_norm(minMaxTexture.getFilterTexture().getEffectScale())));
		scaleBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				minMaxTexture.getFilterTexture().setEffectScale(scaleBar.norm(progress));
				minMaxTexture.setActive(progress != 0);
				updateFunc.run();
			}
		}));

//		kernelBar.setMax(9).setDiscrete(true, 3, 5, 9);
//		kernelBar.onBarCreate(bar -> bar.setProgress(minMaxTexture.getFilterTexture().getKernelSize()));
//		kernelBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
//			if (fromUser) {
//				minMaxTexture.getFilterTexture().setKernelSize(progress);
//				updateFunc.run();
//			}
//		}));

		OPallViewInjector.inject(context.getActivityContext(), scaleBar);

	}


	private static int getResImgByType(int type) {
		return type == MinMaxTexture.TYPE_EROSION ? R.drawable.ic_leak_add_white_24dp : R.drawable.ic_all_out_white_24dp;
	}
	private static int getResStringByType(int type) {
		return type == MinMaxTexture.TYPE_EROSION ? R.string.control_erosion : R.string.control_dilation;
	}


}
