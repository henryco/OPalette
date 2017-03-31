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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.units.OPalette;
import net.henryco.opalette.api.utils.OPallAnimated;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
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
				final TextView horText = (TextView) view.findViewById(R.id.paletteTextHorizontal);
				final TextView verText = (TextView) view.findViewById(R.id.paletteTextVertical);
				final TextView nonText = (TextView) view.findViewById(R.id.paletteTextNone);


				horButton.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {
					horText.setTextColor(fcb);
					verText.setTextColor(fcg);
					nonText.setTextColor(fcg);
					palette.setOrientation(OPalette.ORIENTATION_HORIZONTAL);
					context.getRenderSurface().update();
				}));

				verButton.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {
					horText.setTextColor(fcg);
					verText.setTextColor(fcb);
					nonText.setTextColor(fcg);
					palette.setOrientation(OPalette.ORIENTATION_VERTICAL);
					context.getRenderSurface().update();
				}));

				nonButton.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {
					horText.setTextColor(fcg);
					verText.setTextColor(fcg);
					nonText.setTextColor(fcb);
					palette.setOrientation(OPalette.ORIENTATION_NONE);
					context.getRenderSurface().update();
				}));

			}
		};

		OPallViewInjector.inject(context.getActivityContext(), controls);
	}
}
