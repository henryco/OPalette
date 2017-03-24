package net.henryco.opalette.api.utils.animation;

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
