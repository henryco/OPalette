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
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.OPallAnimated;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 30/03/17.
 */

public class BlackWhiteControl extends AppAutoSubControl<AppMainProto> {


	private static final int img_button_res = R.drawable.ic_filter_b_and_w_white_24dp;
	private static final int txt_button_res = R.string.control_black_white;

	private final EdTexture texture;

	public BlackWhiteControl(EdTexture texture) {
		super(img_button_res, txt_button_res);
		this.texture = texture;
	}

	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		OPallViewInjector<AppMainProto> controls = new OPallViewInjector<AppMainProto>(view, R.layout.black_white_layout) {

			@Override
			protected void onInject(AppMainProto context, View view) {

				final int fca = ContextCompat.getColor(context.getActivityContext(), TEXT_COLOR_BLACK_OVERLAY);
				final int fcb = 0xFF000000;

				Button buttonOn = (Button) view.findViewById(R.id.bwButtonOn);
				Button buttonOff = (Button) view.findViewById(R.id.bwButtonOff);
				TextView textOn = (TextView) view.findViewById(R.id.bwTextOn);
				TextView textOff = (TextView) view.findViewById(R.id.bwTextOff);

				textOn.setTextColor(texture.isBwEnable() ? fcb : fca);
				textOff.setTextColor(texture.isBwEnable() ? fca : fcb);

				buttonOn.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {
					textOn.setTextColor(fcb);
					textOff.setTextColor(fca);
					texture.setBwEnable(true);
					context.getRenderSurface().update();
				}));

				buttonOff.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {
					textOff.setTextColor(fcb);
					textOn.setTextColor(fca);
					texture.setBwEnable(false);
					context.getRenderSurface().update();
				}));
			}
		};

		OPallViewInjector.inject(context.getActivityContext(), controls);
	}
}
