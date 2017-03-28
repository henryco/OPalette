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

package net.henryco.opalette.application.injectables;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.views.OPallViewInjector;

/**
 * Created by HenryCo on 28/03/17.
 */

public class InjectableColorButtons extends OPallViewInjector<Activity> {

	private static int default_text_color = TEXT_COLOR_DEFAULT_DARK;
	public static void setDefaultTextColor(int color) {
		default_text_color = color == -1 ? TEXT_COLOR_DEFAULT_DARK : color;
	}


	private String textValue;
	private int text_color;
	private int button_color;
	private CompoundButton.OnCheckedChangeListener changeListener;
	private View.OnClickListener clickListener;
	private Button colorButton;



	public InjectableColorButtons(int container, String ... name) {
		super(container, R.layout.colors);
		reset(name);
	}

	public InjectableColorButtons(ViewGroup container, String ... name) {
		super(container, R.layout.colors);
		reset(name);
	}

	public InjectableColorButtons(View container, String ... name) {
		super(container, R.layout.colors);
		reset(name);
	}

	public void reset(String ... name) {
		String n = "";
		for (String nn : name) n += nn + " ";
		setTextValue(n)
				.setButtonColor(-1)
				.setTextColor(default_text_color)
				.setSwitchListener((buttonView, isChecked) -> {})
				.setColorButtonListener(v -> {});
	}

	@Override
	protected void onInject(Activity context, View view) {

		TextView text = (TextView) view.findViewById(R.id.color_text);
		if (text_color != -1) text.setTextColor(text_color);
		text.setText(textValue);

		colorButton = (Button) view.findViewById(R.id.color_button);
		colorButton.setEnabled(false);
		colorButton.setOnClickListener(v -> clickListener.onClick(v));
		colorButton.setBackgroundColor(0x00000000);
		if (button_color != -1) colorButton.setBackgroundColor(button_color);



		Switch swi = (Switch) view.findViewById(R.id.color_switch);
		swi.setChecked(false);
		swi.setOnCheckedChangeListener((button, isChecked) -> {
			colorButton.setEnabled(isChecked);
			if (!isChecked) colorButton.setBackgroundColor(0x00000000);
			changeListener.onCheckedChanged(button, isChecked);
		});
	}

	public InjectableColorButtons setTextValue(String textValue) {
		this.textValue = textValue;
		return this;
	}

	public InjectableColorButtons setTextColor(int text_color) {
		this.text_color = text_color;
		return this;
	}

	public InjectableColorButtons setSwitchListener(CompoundButton.OnCheckedChangeListener changeListener) {
		this.changeListener = changeListener;
		return this;
	}

	public InjectableColorButtons setColorButtonListener(View.OnClickListener clickListener) {
		this.clickListener = clickListener;
		return this;
	}

	public InjectableColorButtons setButtonColor(int color) {
		this.button_color = color;
		if (colorButton != null) colorButton.setBackgroundColor(color);
		return this;
	}
}
