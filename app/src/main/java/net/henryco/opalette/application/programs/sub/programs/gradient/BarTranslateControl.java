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
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceTouchListener;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.glES.render.graphics.units.OPalette;
import net.henryco.opalette.api.utils.dialogs.OPallAlertDialog;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 01/04/17.
 */

public class BarTranslateControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_transform_white_24dp;
	private static final int txt_button_res = R.string.control_move_bar;
	private static final int target_layer = R.id.paletteOptionsContainer;

	private final OPalette palette;
	private final float DEF_W, DEF_H, DEF_P;
	private final float def_size;

	private OPallSurfaceTouchListener touchEventListener;


	public BarTranslateControl(OPalette palette, float defW, float defH) {
		super(target_layer, img_button_res, txt_button_res);
		this.palette = palette;
		this.DEF_W = defW;
		this.DEF_H = defH;
		this.DEF_P = palette.getPos_pct();
		this.def_size = palette.getSize_pct();
	}

	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		InjectableSeekBar moveBar = new InjectableSeekBar(view, "Position");
		moveBar.onBarCreate(bar -> {
			bar.setProgress(moveBar.de_norm(palette.getPos_pct()));
			bar.setEnabled(palette.getOrientation() != OPalette.ORIENTATION_NONE);
		});
		moveBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser && palette.getOrientation() != OPalette.ORIENTATION_NONE) {
				palette.setRelativePosition(moveBar.norm(progress));
				context.getRenderSurface().update();
			}
		}));

		InjectableSeekBar sizeBar = new InjectableSeekBar(view, "Size");
		sizeBar.onBarCreate(bar -> {
			bar.setProgress(sizeBar.de_norm(palette.getSize_pct()));
			bar.setEnabled(palette.getOrientation() != OPalette.ORIENTATION_NONE);
		});
		sizeBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser && palette.getOrientation() != OPalette.ORIENTATION_NONE) {
				palette.setRelativeSize(sizeBar.norm(progress));
				context.getRenderSurface().update();
			}
		}));



		touchEventListener = new OPallSurfaceTouchListener(context.getActivityContext());
		touchEventListener.setOnActionMove((dx, dy, event) -> {
			final float x = event.getX() / DEF_W;
			final float y = event.getY() / DEF_H;
			final int o = palette.getOrientation();
			final float p;
			if (o == OPalette.ORIENTATION_HORIZONTAL) p = Math.max(0f, y - palette.getSize_pct() * 0.5f);
			else if (o == OPalette.ORIENTATION_VERTICAL) p = Math.max(0f, x - palette.getSize_pct() * 0.5f);
			else return;
			palette.setRelativePosition(p);
			moveBar.setProgress(moveBar.de_norm(p));
			context.getRenderSurface().update();
		});

		context.setTopControlButton(button -> button.setVisible(true).setEnabled(true).setTitle(R.string.control_top_bar_button_reset), () -> {
			if (palette.getOrientation() != OPalette.ORIENTATION_NONE) {
				palette.setRelativePosition(DEF_P);
				palette.setRelativeSize(def_size);
				moveBar.setProgress(moveBar.de_norm(DEF_P));
				sizeBar.setProgress(sizeBar.de_norm(def_size));
				context.getRenderSurface().update();
			}
		});

		if (palette.getOrientation() != OPalette.ORIENTATION_NONE) {
			backListener = context.getRenderSurface().getLastTouchEventListener();
			context.getRenderSurface().removeTouchEventListener(backListener);
			context.getRenderSurface().addOnTouchEventListener(touchEventListener);
		}
		OPallViewInjector.inject(context.getActivityContext(), sizeBar, moveBar);


		if (palette.getOrientation() == OPalette.ORIENTATION_NONE) {
			new OPallAlertDialog().message(context.getActivityContext().getResources().getString(R.string.palette_pick_warn))
					.show(context.getActivityContext().getSupportFragmentManager(), "Palette alert");
		}
	}

	private OPallSurfaceView.OnTouchEventListener backListener;

	@Override
	public void onFragmentDestroyed(Fragment fragment, AppMainProto context) {
		context.getRenderSurface().removeTouchEventListener(touchEventListener);
		context.getRenderSurface().addOnTouchEventListener(backListener);
	}
}
