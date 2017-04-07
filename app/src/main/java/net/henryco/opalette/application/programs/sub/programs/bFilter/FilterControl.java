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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.OPallAnimated;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.programs.AppSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 05/04/17.
 */

public class FilterControl extends AppSubControl<AppMainProto> {

	public interface EdFilterHolder {
		void setFilter(EdFilter filter, TextView textView);
		EdFilter getFilter();
		TextView getTextView();
		void stateUpDate();
	}
	private final EdFilterHolder filterHolder;

	private final Bitmap filterIcon;
	private final EdFilter filter;

	public FilterControl(final Bitmap filterPreviewImage, final EdFilter filter, final EdFilterHolder filterHolder) {
		super(R.id.filterOptionsContainer, R.layout.image_option_toggle);
		this.filterIcon = filterPreviewImage;
		this.filterHolder = filterHolder;
		this.filter = filter;
	}


	@Override
	protected void onInject(AppMainProto context, View view) {

		view.findViewById(R.id.iopColorBar).setBackground(new ColorDrawable(filter.color));

		TextView textView = (TextView) view.findViewById(R.id.iopTextView);
		textView.setText(filter.name);
		textView.setTextColor(ContextCompat.getColor(context.getActivityContext(), R.color.disableColor));
		if (filter.name.equalsIgnoreCase(EdFilter.getDefaultFilter().name)) {
			textView.setTextColor(ContextCompat.getColor(context.getActivityContext(), R.color.activeColor));
			filterHolder.setFilter(filter, textView);
			filterHolder.stateUpDate();
		}


		ImageButton imageButton = (ImageButton) view.findViewById(R.id.iopImageButton);
		imageButton.setBackground(new BitmapDrawable(context.getActivityContext().getResources(), filterIcon));
		imageButton.setClickable(false);

		view.setOnClickListener(v -> OPallAnimated.pressButton75_225(context.getActivityContext(), v, () -> {
			if (filterHolder.getFilter().name.equalsIgnoreCase(filter.name)) {
				if (!filter.name.equalsIgnoreCase(EdFilter.getDefaultFilter().name))
					context.switchToFragmentOptions(loadControlFragment(this::onFragmentCreate, this::onFragmentDestroyed));
			} else {
				TextView lastTextView = filterHolder.getTextView();
				if (lastTextView != null)
					lastTextView.setTextColor(ContextCompat.getColor(context.getActivityContext(), R.color.disableColor));
				textView.setTextColor(ContextCompat.getColor(context.getActivityContext(), R.color.activeColor));
				filterHolder.setFilter(filter.copy(), textView);
				context.getRenderSurface().update();
			}
		}));
	}



	private void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		InjectableSeekBar effectBar = new InjectableSeekBar(view, "Effect scale");
		effectBar.onBarCreate(bar -> bar.setProgress(effectBar.de_norm(filterHolder.getFilter().getAlpha())));
		effectBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				filterHolder.getFilter().setAlpha(effectBar.norm(progress));
				filterHolder.stateUpDate();
				context.getRenderSurface().update();
			}
		}));

		context.setTopControlButton(button -> button.setVisible(true).setEnabled(true).setTitle(R.string.control_top_bar_button_reset), () -> {
			filterHolder.getFilter().setAlpha(1f);
			filterHolder.stateUpDate();
			context.getRenderSurface().update();
		});

		OPallViewInjector.inject(context.getActivityContext(), effectBar);
	}


	private void onFragmentDestroyed(Fragment fragment, AppMainProto context) {

	}
}
