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
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.Borders;
import net.henryco.opalette.api.glES.render.graphics.units.OPalette;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableColorButtons;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;

/**
 * Created by HenryCo on 28/03/17.
 */

public class BordersControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_crop_din_white_24dp;
	private static final int txt_button_res = R.string.control_borders;
	private static final int target_layer = R.id.paletteOptionsContainer;

	private final Borders borders;
	private final OPalette palette;
	private final float defSize;

	public BordersControl(final Borders borders, final OPalette palette) {
		super(target_layer, img_button_res, txt_button_res);
		this.palette = palette;
		this.borders = borders;
		this.defSize = borders.getSize();
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		InjectableColorButtons border = new InjectableColorButtons(view, "Borders");
		border.setChecked(borders.isVisible());
		if (!borders.isVisible()) borders.color.set(new GLESUtils.Color(0x00000000));
		int col = borders.color.hex();
		border.setButtonColor(col);
		border.setSwitchListener((buttonView, isChecked) -> {
			if (isChecked) {
				border.setButtonColor(Color.WHITE);
				borders.setColor(new GLESUtils.Color(Color.WHITE));
			} else borders.setColor(new GLESUtils.Color(0x00000000));
			System.out.println("SET");
			palette.setColor(new GLESUtils.Color(Color.WHITE));
			borders.setVisible(isChecked);
			context.getRenderSurface().update();
		});

		border.setColorButtonListener(v -> runColorPicker(context.getActivityContext(), i -> {
			borders.setColor(new GLESUtils.Color(i));
			palette.setColor(new GLESUtils.Color(i));
			border.setButtonColor(i);
			context.getRenderSurface().update();
		}));

		InjectableSeekBar sizeBar = new InjectableSeekBar(view, "Size");
		sizeBar.onBarCreate(bar -> bar.setProgress(sizeBar.de_norm(borders.getSize())));
		sizeBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				borders.setSize(sizeBar.norm(progress));
				context.getRenderSurface().update();
			}
		}));

		context.setTopControlButton(button ->
				button.setEnabled(true).setVisible(true).setTitle(R.string.control_top_bar_button_reset), () -> {
			borders.setSize(defSize);
			if (borders.isVisible()) border.setButtonColor(Color.WHITE);
			borders.setColor(new GLESUtils.Color(Color.WHITE));
			palette.setColor(new GLESUtils.Color(Color.WHITE));
			sizeBar.setProgress(sizeBar.de_norm(defSize));
			context.getRenderSurface().update();
		});

		OPallViewInjector.inject(context.getActivityContext(), sizeBar, border);
	}


	private static void runColorPicker(AppCompatActivity context, ColorSelectListener listener) {
		new ChromaDialog.Builder()
				.initialColor(Color.WHITE)
				.colorMode(ColorMode.ARGB) // There's also ARGB and HSV
				.onColorSelected(listener)
				.create().show(context.getSupportFragmentManager(), "ColorPicker");
	}
}
