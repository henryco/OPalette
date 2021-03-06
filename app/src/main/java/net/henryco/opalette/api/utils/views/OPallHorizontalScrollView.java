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

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 15/03/17.
 */
@Deprecated
public class OPallHorizontalScrollView extends HorizontalScrollView {

	public OPallHorizontalScrollView(Context context) {
		super(context);
	}

	public OPallHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OPallHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private int fadeColor = 0xFFFFFFFF; //AARRGGBB

	public void setFadeColor(GLESUtils.Color color) {
		int a = Math.min((int) (color.a * 255), 255);
		int r = Math.min((int) (color.r * 255), 255);
		int g = Math.min((int) (color.g * 255), 255);
		int b = Math.min((int) (color.b * 255), 255);
		setFadeColor(Color.argb(a, r, g, b));
	}

	public void setFadeColor(int fadeColor) {
		this.fadeColor = fadeColor;
	}

	@Override
	public int getSolidColor() {
		return fadeColor;
	}
}
