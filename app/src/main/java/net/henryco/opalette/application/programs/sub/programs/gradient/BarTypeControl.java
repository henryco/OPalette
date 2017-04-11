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

package net.henryco.opalette.application.programs.sub.programs.gradient;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.units.OPalette;
import net.henryco.opalette.api.utils.OPallAnimated;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.conf.GodConfig;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 31/03/17.
 */

public class BarTypeControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_gradient_white_24dp;
	private static final int txt_button_res = R.string.control_palette_type;
	private static final int target_layer = R.id.paletteOptionsContainer;

	private final OPalette palette;

	public BarTypeControl(OPalette palette) {
		super(target_layer, img_button_res, txt_button_res);
		this.palette = palette;
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		final int fcg = ContextCompat.getColor(context.getActivityContext(), TEXT_COLOR_BLACK_OVERLAY);
		final int fcb = 0xFF000000;

		OPallViewInjector<AppMainProto> controls = new OPallViewInjector<AppMainProto>(view, R.layout.palette_type_layout) {

			@Override
			protected void onInject(AppMainProto context, View view) {

				final Button horButton = (Button) view.findViewById(R.id.paletteButtonHorizontal);
				final Button verButton = (Button) view.findViewById(R.id.paletteButtonVertical);
				final Button nonButton = (Button) view.findViewById(R.id.paletteButtonNone);
				final Button gradHorButton = (Button) view.findViewById(R.id.paletteButtonGradHorizontal);
				final Button gradVerButton = (Button) view.findViewById(R.id.paletteButtonGradVertical);
				final TextView horText = (TextView) view.findViewById(R.id.paletteTextHorizontal);
				final TextView verText = (TextView) view.findViewById(R.id.paletteTextVertical);
				final TextView nonText = (TextView) view.findViewById(R.id.paletteTextNone);
				final TextView gradHorText = (TextView) view.findViewById(R.id.paletteTextGradHorizontal);
				final TextView gradVerText = (TextView) view.findViewById(R.id.paletteTextGradVertical);

				final TextView[] textViews = {horText, verText, nonText, gradHorText, gradVerText};
				OPallConsumer<TextView> clickF = textView -> {
					for (TextView t: textViews) {
						if (t == textView) t.setTextColor(fcb);
						else t.setTextColor(fcg);
					}
				};

				int o = palette.getOrientation();
				if (o == OPalette.ORIENTATION_HORIZONTAL) {
					if (palette.isDiscrete()) horText.setTextColor(fcb);
					else gradHorText.setTextColor(fcb);
				} else if (o == OPalette.ORIENTATION_VERTICAL) {
					if (palette.isDiscrete()) verText.setTextColor(fcb);
					else gradVerText.setTextColor(fcb);
				} else nonText.setTextColor(fcb);

				horButton.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {

					clickF.consume(horText);
					palette.setOrientation(OPalette.ORIENTATION_HORIZONTAL);
					palette.setDiscrete(true);
					context.getRenderSurface().update();
				}));

				verButton.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {

					clickF.consume(verText);
					palette.setOrientation(OPalette.ORIENTATION_VERTICAL);
					palette.setDiscrete(true);
					context.getRenderSurface().update();
				}));

				gradHorButton.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {

					clickF.consume(gradHorText);
					palette.setOrientation(OPalette.ORIENTATION_HORIZONTAL);
					palette.setDiscrete(false);
					context.getRenderSurface().update();
				}));

				gradVerButton.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {

					clickF.consume(gradVerText);
					palette.setOrientation(OPalette.ORIENTATION_VERTICAL);
					palette.setDiscrete(false);
					context.getRenderSurface().update();
				}));

				nonButton.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {

					clickF.consume(nonText);
					palette.setOrientation(OPalette.ORIENTATION_NONE);
					context.getRenderSurface().update();
				}));

			}
		};

		OPallViewInjector.inject(context.getActivityContext(), controls);
	}


	@Override
	public void onFragmentDestroyed(Fragment fragment, AppMainProto context) {

		if (context.getFireBase() != null) {
			Bundle bundle = new Bundle();
			bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, palette.getOrientation());
			bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, GodConfig.Analytics.TYPE_PALETTE_BAR_ORIENTATION);
			context.getFireBase().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
		}
	}
}
