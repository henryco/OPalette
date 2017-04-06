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

package net.henryco.opalette.api.utils;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;

import net.henryco.opalette.R;

/**
 * Created by HenryCo on 19/03/17.
 */

public class OPallAnimated {

	public static void pressButton75_225(Context c, View v, Runnable action) {
		v.startAnimation(AnimationUtils.loadAnimation(c, R.anim.press_75pct_225ms));
		new Handler().postDelayed(action, 180);
	}

}
