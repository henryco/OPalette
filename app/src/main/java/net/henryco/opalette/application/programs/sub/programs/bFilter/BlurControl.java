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

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.BlurTexture;
import net.henryco.opalette.api.utils.OPallAnimated;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 07/04/17.
 */

public class BlurControl  extends AppAutoSubControl<AppMainProto> {


	private static final int img_button_res = R.drawable.ic_blur_linear_white_24dp;
	private static final int txt_button_res = R.string.control_blur;

	private final AppSubProgram.ProxyRenderData proxyRenderData;
	private final FilterPipeLiner<BlurTexture> blurTexture;

	private OPallSurfaceView.OnTouchEventListener listener;
	private OPallSurfaceView.OnTouchEventListener backListener;

	public BlurControl(final AppSubProgram.ProxyRenderData proxyRenderData, final FilterPipeLiner<BlurTexture> blurTexture) {
		super(img_button_res, txt_button_res);
		this.proxyRenderData = proxyRenderData;
		this.blurTexture = blurTexture;
		resetPoints();

	}

	private void resetPoints() {
		float h = blurTexture.getHeight();
		float d = 0.3333f;
		blurTexture.getFilterTexture().setPoints(0, d * h, 0, d * h * 2f);
	}



	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		Runnable updateFunc = () -> {
			proxyRenderData.setStateUpdated();
			context.getRenderSurface().update();
		};

		listener = event -> {
			final int action = event.getAction();
			final int count = event.getPointerCount();
			switch (action & MotionEvent.ACTION_MASK) {

				case MotionEvent.ACTION_DOWN:
					blurTexture.getFilterTexture().setPointsVisible(true);
					updateFunc.run();

				case MotionEvent.ACTION_MOVE:
					if (count >= 2) {
						blurTexture.getFilterTexture().setPoints(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
						blurTexture.getFilterTexture().setPointsVisible(true);
						updateFunc.run();
					}
				break;

				case MotionEvent.ACTION_UP:
					blurTexture.getFilterTexture().setPointsVisible(false);
					updateFunc.run();
				break;
			}
		};


		OPallViewInjector<AppMainProto> controls = new OPallViewInjector<AppMainProto>(view, R.layout.palette_region_layout) {
			@Override
			protected void onInject(AppMainProto context, View view) {

				final TextView reg = (TextView) view.findViewById(R.id.paletteTextRegion);
				final TextView non = (TextView) view.findViewById(R.id.paletteTextNone);
				final Button regButton = (Button) view.findViewById(R.id.bwButtonOn);
				final Button nonButton = (Button) view.findViewById(R.id.bwButtonOff);

				final int fca = ContextCompat.getColor(context.getActivityContext(), TEXT_COLOR_BLACK_OVERLAY);
				final int fcb = 0xFF000000;

				reg.setTextColor(blurTexture.isActive() ? fcb : fca);
				non.setTextColor(blurTexture.isActive() ? fca : fcb);

				backListener = context.getRenderSurface().getLastTouchEventListener();
				context.getRenderSurface().removeTouchEventListener(backListener);

				if (blurTexture.isActive()) context.getRenderSurface().addOnTouchEventListener(listener);

				regButton.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {
					blurTexture.setActive(true);
					context.getRenderSurface().addOnTouchEventListener(listener);
					reg.setTextColor(fcb);
					non.setTextColor(fca);
					updateFunc.run();
				}));

				nonButton.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {
					context.getRenderSurface().removeTouchEventListener(listener);
					non.setTextColor(fcb);
					reg.setTextColor(fca);
					blurTexture.setActive(false);
					resetPoints();
					updateFunc.run();
				}));
			}
		};


		OPallViewInjector.inject(context.getActivityContext(), controls);

	}



	@Override
	public void onFragmentDestroyed(Fragment fragment, AppMainProto context) {

		context.getRenderSurface().removeTouchEventListener(listener);
		context.getRenderSurface().addOnTouchEventListener(backListener);

	}
}
