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
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.BubbleTexture;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 08/04/17.
 */

public class BubbleControl  extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_bubble_chart_white_24dp;
	private static final int txt_button_res = R.string.control_bubble;

	private final AppSubProgram.ProxyRenderData proxyRenderData;
	private final FilterPipeLiner<BubbleTexture> bubbleTexture;

	private final float defSquareSize;
	private final float defRadius;

	public BubbleControl(final AppSubProgram.ProxyRenderData proxyRenderData,
						 final FilterPipeLiner<BubbleTexture> bubbleTexture) {
		super(img_button_res, txt_button_res);
		this.proxyRenderData = proxyRenderData;
		this.bubbleTexture = bubbleTexture;
		this.defRadius = bubbleTexture.getFilterTexture().getRadius();
		this.defSquareSize = bubbleTexture.getFilterTexture().getSquare();
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		InjectableSeekBar squareBar = new InjectableSeekBar(view, "Bubble size");
		InjectableSeekBar radBar = new InjectableSeekBar(view, "Radius");

		final Runnable updateFunc = () -> {
			proxyRenderData.setStateUpdated();
			context.getRenderSurface().update();
		};

		final String disable = context.getActivityContext().getResources().getString(R.string.disable);
		final String enable = context.getActivityContext().getResources().getString(R.string.enable);

		boolean[] stat = {bubbleTexture.isActive()};
		context.setTopControlButton(button -> button.setEnabled(true).setVisible(true).setTitle(bubbleTexture.isActive() ? disable : enable), () -> {

			if (!stat[0]) context.setTopControlButton(b -> b.setTitle(disable));
			else {
				context.setTopControlButton(b -> b.setTitle(enable));
				squareBar.setProgress((int) defSquareSize);
				radBar.setProgress(radBar.de_norm(defRadius));
				bubbleTexture.getFilterTexture().setRadius(defRadius);
				bubbleTexture.getFilterTexture().setSquare(defSquareSize);
			}
			stat[0] = !stat[0];
			bubbleTexture.setActive(stat[0]);
			squareBar.setEnable(stat[0]);
			radBar.setEnable(stat[0]);
			updateFunc.run();
		});


		radBar.setEnable(bubbleTexture.isActive());
		squareBar.setEnable(bubbleTexture.isActive());

		radBar.onBarCreate(bar -> bar.setProgress(radBar.de_norm(bubbleTexture.getFilterTexture().getRadius())));
		radBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				bubbleTexture.getFilterTexture().setRadius(radBar.norm(progress));
				updateFunc.run();
			}
		}));
		squareBar.setMax(Math.max(bubbleTexture.getWidth(), bubbleTexture.getHeight()) / 2);
		squareBar.onBarCreate(bar -> bar.setProgress((int) bubbleTexture.getFilterTexture().getSquare()));
		squareBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				bubbleTexture.getFilterTexture().setSquare(progress);
				updateFunc.run();
			}
		}));

		OPallViewInjector.inject(context.getActivityContext(), radBar, squareBar);
	}
}
