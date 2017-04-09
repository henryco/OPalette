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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.glES.render.graphics.shaders.shapes.TouchLines;
import net.henryco.opalette.api.glES.render.graphics.units.OPalette;
import net.henryco.opalette.api.utils.OPallAnimated;
import net.henryco.opalette.api.utils.dialogs.OPallAlertDialog;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 26/03/17.
 */

public class PaletteRegionControl extends AppAutoSubControl<AppMainProto> {


	private static final int img_button_res = R.drawable.ic_texture_white_24dp;
	private static final int txt_button_res = R.string.control_palette_region;
	private static final int target_layer = R.id.paletteOptionsContainer;

	private TouchLines touchLines;
	private OPallSurfaceView.OnTouchEventListener listener;
	private final OPalette palette;

	public PaletteRegionControl(TouchLines touchLines, final OPalette palette) {
		super(target_layer, img_button_res, txt_button_res);
		this.touchLines = touchLines;
		this.palette = palette;
	}

	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {
		touchLines.setVisible(true);
		context.getRenderSurface().update();

		listener = event -> {
			final int action = event.getAction();
			final int count = event.getPointerCount();
			if (count >= 2) {
				switch (action & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_MOVE: {
						touchLines.setPoints(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
						context.getRenderSurface().update();
						break;
					}
				}
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

				reg.setTextColor(touchLines.isDefault() ? fca : fcb);
				non.setTextColor(touchLines.isDefault() ? fcb : fca);

				backListener = context.getRenderSurface().getLastTouchEventListener();
				context.getRenderSurface().removeTouchEventListener(backListener);

				if (!touchLines.isDefault()) context.getRenderSurface().addOnTouchEventListener(listener);

				regButton.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {
					if (touchLines.isDefault()) context.getRenderSurface().addOnTouchEventListener(listener);
					reg.setTextColor(fcb);
					non.setTextColor(fca);
				}));

				nonButton.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {
					context.getRenderSurface().removeTouchEventListener(listener);
					non.setTextColor(fcb);
					reg.setTextColor(fca);
					touchLines.reset();
					context.getRenderSurface().update();
				}));
			}
		};

		OPallViewInjector.inject(context.getActivityContext(), controls);

		if (palette.getOrientation() == OPalette.ORIENTATION_NONE) {
			new OPallAlertDialog().message(context.getActivityContext().getResources().getString(R.string.palette_pick_warn))
					.show(context.getActivityContext().getSupportFragmentManager(), "Palette alert");
		}
	}

	private OPallSurfaceView.OnTouchEventListener backListener;

	@Override
	public void onFragmentDestroyed(Fragment fragment, AppMainProto context) {
		context.getRenderSurface().removeTouchEventListener(listener);
		touchLines.setVisible(false);
		context.getRenderSurface().update();
		context.getRenderSurface().addOnTouchEventListener(backListener);
	}
}
