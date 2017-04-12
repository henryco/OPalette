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
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.BlurTexture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.FilterMatrices;
import net.henryco.opalette.api.utils.OPallAnimated;
import net.henryco.opalette.api.utils.dialogs.OPallAlertDialog;
import net.henryco.opalette.api.utils.dialogs.OPallOptionListDialog;
import net.henryco.opalette.api.utils.lambda.functions.OPallFunction;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
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
		int[] dim = proxyRenderData.getDimension();
		float h = blurTexture.getHeight();
		if (dim != null) {
			h = dim[1];
		}
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

				final TextView inf = (TextView) view.findViewById(R.id.palette_text_info);
				inf.setText(R.string.blur_region_multi_touch_info);

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
					blurTexture.getFilterTexture().setPointsVisible(true);
					reg.setTextColor(fcb);
					non.setTextColor(fca);
					if (!blurTexture.isActive()) context.getRenderSurface().addOnTouchEventListener(listener);
					blurTexture.setActive(true);
					updateFunc.run();
					new Handler().postDelayed(() -> {
						blurTexture.getFilterTexture().setPointsVisible(false);
						updateFunc.run();
					}, 300);
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


		context.setTopControlButton(button -> button.setVisible(true).setEnabled(true).setTitle(R.string.options), () -> {

			OPallFunction<String, Integer> stringFunc = integer
					-> context.getActivityContext().getResources().getString(integer);

			String options = stringFunc.apply(R.string.options);
			String types = stringFunc.apply(R.string.blur_type);
			String pwr = stringFunc.apply(R.string.transition_power);
			String apl = stringFunc.apply(R.string.apply);

			String sb1 = "m_diagShatter";
			String sb2 = "m_blur";
			String sb3 = "m_boxBlur";
			String sb4 = "m_gaussianBlur (recommended)";
			String sb5 = "m_horizontalMotionBlur";


			Runnable blur1 = () -> {
				blurTexture.getFilterTexture().setBlurMatrix(FilterMatrices.m_diagShatter());
				updateFunc.run();
			};
			Runnable blur2 = () -> {
				blurTexture.getFilterTexture().setBlurMatrix(FilterMatrices.m_blur());
				updateFunc.run();
			};
			Runnable blur3 = () -> {
				blurTexture.getFilterTexture().setBlurMatrix(FilterMatrices.m_boxBlur());
				updateFunc.run();
			};
			Runnable blur4 = () -> {
				blurTexture.getFilterTexture().setBlurMatrix(FilterMatrices.m_gaussianBlur());
				updateFunc.run();
			};
			Runnable blur5 = () -> {
				blurTexture.getFilterTexture().setBlurMatrix(FilterMatrices.m_horizontalMotionBlur());
				updateFunc.run();
			};

			Runnable power = () -> {
				LinearLayout layout = new LinearLayout(context.getActivityContext());
				InjectableSeekBar powerBar = new InjectableSeekBar(layout);
				powerBar.onBarCreate(bar -> bar.setProgress(powerBar.de_norm(blurTexture.getFilterTexture().getPower())));
				powerBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
					if (fromUser) blurTexture.getFilterTexture().setPower(powerBar.norm(progress));
				}));
				OPallViewInjector.inject(context.getActivityContext(), powerBar);
				new OPallAlertDialog()
						.content(layout)
						.title(pwr)
						.positive(apl, updateFunc)
						.negative(stringFunc.apply(R.string.cancel))
						.neutral(stringFunc.apply(R.string.control_top_bar_button_reset), () -> {
							blurTexture.getFilterTexture().resetPower();
							updateFunc.run();
						}).show(context.getActivityContext().getSupportFragmentManager(), "Blur transition power dialog");
			};


			Runnable type = () -> new OPallOptionListDialog()
					.setTittle(types)
					.setOptionsNames(sb1, sb2, sb3, sb4, sb5)
					.setOptionsActions(blur1, blur2, blur3, blur4, blur5)
					.show(context.getActivityContext().getSupportFragmentManager(), "Blur types dialog");


			new OPallOptionListDialog()
					.setTittle(options)
					.setOptionsNames(types, pwr)
					.setOptionsActions(type, power)
					.show(context.getActivityContext().getSupportFragmentManager(), "Blur options dialog");
		});


		OPallViewInjector.inject(context.getActivityContext(), controls);
	}



	@Override
	public void onFragmentDestroyed(Fragment fragment, AppMainProto context) {

		context.getRenderSurface().removeTouchEventListener(listener);
		context.getRenderSurface().addOnTouchEventListener(backListener);

	}
}
