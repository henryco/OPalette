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
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.henryco.opalette.R;

/**
 * Created by HenryCo on 15/03/17.
 */

public abstract class OPallViewInjector<T> {

	public static final int TEXT_COLOR_DEFAULT_DARK = R.color.common_google_signin_btn_text_light_default;
	public static final int TEXT_COLOR_BLACK_OVERLAY = R.color.black_overlay;
	public static final int TEXT_COLOR_DEFAULT_LIGHT = R.color.common_google_signin_btn_text_dark_default;
	public static final int TEXT_COLOR_LIGHT_OVERLAY = R.color.common_google_signin_btn_text_dark_disabled;
	public static final int TEXT_COLOR_LIGHT = R.attr.colorButtonNormal;


	@SuppressWarnings("unchecked")
	private static void inject(Activity context, OPallViewInjector injector, long delay) {
		
		context.runOnUiThread(() -> {
			View view = ((LayoutInflater) context.getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(injector.ids[1], null);

			final ViewGroup insertGroup;
			if (injector.containerGroup == null && injector.ids[0] != (-1))
				insertGroup = (ViewGroup) context.findViewById(injector.ids[0]);
			else if (injector.containerGroup != null && injector.ids[0] == (-1))
				insertGroup = injector.containerGroup;
			else throw new RuntimeException("VIEW GROUP == NULL");

			try {
				injector.onInject(context, view);
			} catch (ClassCastException e) {
				throw new RuntimeException(context.getClass().getName()+
						" wrong generic instance, must be compatible with: "+
						injector.getClass().getName()
				);
			}

			view.setVisibility(View.GONE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				view.setElevation(0);
			}
			new Handler().postDelayed(() -> context.runOnUiThread(() -> {
				insertGroup.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
				view.setVisibility(View.VISIBLE);
			}), delay);

		});
	}

	public static void inject(Activity context, OPallViewInjector ... injector) {
		inject(context, 0, injector);
	}
	public static void inject(Activity context, long delay, OPallViewInjector ... injector) {
		for (OPallViewInjector i : injector) inject(context, i, delay);
	}

	private final int[] ids;
	private final ViewGroup containerGroup;

	public OPallViewInjector(int container, int layer) {
		ids = new int[]{container, layer};
		containerGroup = null;
	}
	public OPallViewInjector(ViewGroup container, int layer) {
		ids = new int[]{-1, layer};
		containerGroup = container;
	}
	public OPallViewInjector(View container, int layer) {
		this((ViewGroup)container, layer);
	}

	protected abstract void onInject(T context, View view);

}
