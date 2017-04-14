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

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.units.OPalette;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.dialogs.OPallAlertDialog;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.injectables.InjectableColorButtons;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

import static net.henryco.opalette.application.injectables.InjectableColorButtons.runColorPicker;

/**
 * Created by HenryCo on 04/04/17.
 */

public class PaletteColorControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_opacity_white_24dp;
	private static final int txt_button_res = R.string.control_color;

	private final OPalette palette;

	public PaletteColorControl(final OPalette palette) {
		super(R.id.paletteOptionsContainer, img_button_res, txt_button_res);
		this.palette = palette;
	}

	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		InjectableColorButtons background = new InjectableColorButtons(view, context.getActivityContext().getResources().getString(R.string.palette_background));
		int col = palette.getColor().hex();
		background.setChecked(true);
		background.setButtonColor(col);
		background.applySwitch(aSwitch -> aSwitch.setVisibility(View.INVISIBLE));

		background.setColorButtonListener(v -> runColorPicker(context.getActivityContext(), i -> {
			if (palette.getOrientation() != OPalette.ORIENTATION_NONE) {
				palette.setColor(new GLESUtils.Color(i));
				background.setButtonColor(i);
				context.getRenderSurface().update();
			}
		}));

		context.setTopControlButton(button -> button.setVisible(true).setEnabled(true).setTitle(R.string.control_top_bar_button_reset), () -> {
			if (palette.getOrientation() != OPalette.ORIENTATION_NONE) {
				palette.setColor(new GLESUtils.Color(Color.WHITE));
				background.setButtonColor(Color.WHITE);
				context.getRenderSurface().update();
			}
		});

		OPallViewInjector.inject(context.getActivityContext(), background);

		if (palette.getOrientation() == OPalette.ORIENTATION_NONE) {
			new OPallAlertDialog().message(context.getActivityContext().getResources().getString(R.string.palette_pick_warn))
					.show(context.getActivityContext().getSupportFragmentManager(), "Palette alert");
		}
	}
}
