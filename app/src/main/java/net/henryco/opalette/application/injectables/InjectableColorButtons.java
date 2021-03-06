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
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;
import net.henryco.opalette.api.utils.views.OPallViewInjector;

import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;

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
	private Switch swi;
	private boolean checked;
	private boolean enable;
	private OPallConsumer<Switch> switchConsumer;

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
				.setButtonColor(Integer.MAX_VALUE).setChecked(false)
				.setTextColor(default_text_color)
				.setSwitchListener((buttonView, isChecked) -> {})
				.setColorButtonListener(v -> {}).setSwitchEnable(true)
				.applySwitch(aSwitch -> {});
	}

	@Override
	protected void onInject(Activity context, View view) {

		TextView text = (TextView) view.findViewById(R.id.color_text);
		if (text_color != -1) text.setTextColor(text_color);
		text.setText(textValue);

		colorButton = (Button) view.findViewById(R.id.color_button);
		colorButton.setEnabled(checked);
		colorButton.setOnClickListener(v -> clickListener.onClick(v));
		colorButton.setBackgroundColor(0x00000000);
		if (button_color != Integer.MAX_VALUE) colorButton.setBackgroundColor(button_color);


		swi = (Switch) view.findViewById(R.id.color_switch);
		swi.setChecked(checked);
		swi.setEnabled(enable);
		switchConsumer.consume(swi);
		swi.setOnCheckedChangeListener((button, isChecked) -> {
			colorButton.setEnabled(isChecked);
			if (!isChecked) colorButton.setBackgroundColor(0x00000000);
			changeListener.onCheckedChanged(button, isChecked);
		});
	}

	public InjectableColorButtons applySwitch(OPallConsumer<Switch> consumer) {
		this.switchConsumer = consumer;
		if (swi != null) consumer.consume(swi);
		return this;
	}

	public InjectableColorButtons setSwitchEnable(boolean enable) {
		this.enable = enable;
		if (swi != null) swi.setEnabled(enable);
		return this;
	}

	public InjectableColorButtons setChecked(boolean checked) {
		this.checked = checked;
		if (swi != null) swi.setChecked(checked);
		return this;
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

	public static void runColorPicker(AppCompatActivity context, ColorSelectListener listener) {
		new ChromaDialog.Builder()
				.initialColor(Color.WHITE)
				.colorMode(ColorMode.RGB) // There's also ARGB and HSV
				.onColorSelected(listener)
				.create().show(context.getSupportFragmentManager(), "ColorPicker");
	}
}
