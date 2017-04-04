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

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.injectables.InjectableColorButtons;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

import static net.henryco.opalette.application.injectables.InjectableColorButtons.runColorPicker;

/**
 * Created by HenryCo on 27/03/17.
 */
public class BackGroundControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_insert_photo_white_24dp;
	private static final int txt_button_res = R.string.control_background;

	private final GLESUtils.Color color;

	public BackGroundControl(GLESUtils.Color color) {
		super(img_button_res, txt_button_res);
		this.color = color;
	}



	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {


		InjectableColorButtons background = new InjectableColorButtons(view, "Background");
		int col = color.hex();
		background.setChecked(color.a != 0);
		background.setButtonColor(col);
		background.setSwitchListener((buttonView, isChecked) -> {
			if (isChecked) {
				background.setButtonColor(Color.WHITE);
				color.set(GLESUtils.Color.WHITE.hex());
			} else color.set(GLESUtils.Color.TRANSPARENT.hex());
			context.getRenderSurface().update();
		});

		background.setColorButtonListener(v -> runColorPicker(context.getActivityContext(), i -> {
			color.set(Color.red(i), Color.green(i), Color.blue(i), 255);
			background.setButtonColor(i);
			context.getRenderSurface().update();
		}));


		OPallViewInjector.inject(context.getActivityContext(), background);
	}


}
