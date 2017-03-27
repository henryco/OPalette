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

package net.henryco.opalette.api.utils.views;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.application.injectables.InjectableSeekBar;

/**
 * Created by HenryCo on 27/03/17.
 */

public class ColorPickerView extends LinearLayout {

	private float layout_weight = 1f;
	private final GLESUtils.Color color = new GLESUtils.Color(GLESUtils.Color.WHITE);

	public ColorPickerView(Context context) {
		super(context);
		setOrientation(VERTICAL);
		setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
	}

	public ColorPickerView create() {
		return create((Activity) getContext());
	}

	public ColorPickerView create(Activity activity) {
		FrameLayout relativeLayout = new FrameLayout(getContext());
		relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		relativeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.SEA));

		LinearLayout linearLayout = new LinearLayout(getContext());
		linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		InjectableSeekBar red = new InjectableSeekBar(linearLayout, "Red").setMax(255).setValueVisible(false);
		InjectableSeekBar green = new InjectableSeekBar(linearLayout, "Green").setMax(255).setValueVisible(false);
		InjectableSeekBar blue = new InjectableSeekBar(linearLayout, "Blue").setMax(255).setValueVisible(false);

		red.onBarCreate(bar -> bar.setProgress(red.de_norm(color.r)));
		green.onBarCreate(bar -> bar.setProgress(green.de_norm(color.g)));
		blue.onBarCreate(bar -> bar.setProgress(blue.de_norm(color.b)));


		this.addView(relativeLayout);
//		this.addView(linearLayout);

//		OPallViewInjector.inject(activity, blue, green, red);

		return this;
	}

}
