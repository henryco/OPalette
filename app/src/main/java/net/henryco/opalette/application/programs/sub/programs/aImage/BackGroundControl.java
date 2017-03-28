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
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.injectables.InjectableColorButtons;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;

/**
 * Created by HenryCo on 27/03/17.
 */
public class BackGroundControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_crop_din_white_24dp;
	private static final int txt_button_res = R.string.control_background;

	private final GLESUtils.Color color;
	private final AppSubProgram.ProxyRenderData proxyUpdater;

	public BackGroundControl(GLESUtils.Color color, AppSubProgram.ProxyRenderData proxyUpdater) {
		super(img_button_res, txt_button_res);
		this.color = color;
		this.proxyUpdater = proxyUpdater;
	}



	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {
		//TODO

		Runnable update = () -> {
			proxyUpdater.setStateUpdated();
			context.getRenderSurface().update();
		};

		InjectableColorButtons background = new InjectableColorButtons(view, "Background");
		background.setSwitchListener((buttonView, isChecked) -> {
			if (isChecked) {
				background.setButtonColor(Color.WHITE);
				this.color.set(GLESUtils.Color.WHITE);
				update.run();
			}
			else {
				this.color.set(GLESUtils.Color.TRANSPARENT);
				update.run();
			}
		});

		background.setColorButtonListener(v -> runColorPicker(context.getActivityContext(), i -> {
			this.color.set(Color.red(i), Color.green(i), Color.blue(i), 1);
			background.setButtonColor(i);
			update.run();
		}));


		OPallViewInjector.inject(context.getActivityContext(), background);
	}

	private static void runColorPicker(AppCompatActivity context, ColorSelectListener listener) {
		new ChromaDialog.Builder()
				.initialColor(Color.WHITE)
				.colorMode(ColorMode.RGB) // There's also ARGB and HSV
				.onColorSelected(listener)
				.create().show(context.getSupportFragmentManager(), "ColorPicker");
	}
}
