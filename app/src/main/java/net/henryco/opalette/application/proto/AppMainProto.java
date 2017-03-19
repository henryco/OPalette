package net.henryco.opalette.application.proto;

import android.app.Fragment;

import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceActivity;

/**
 * Created by HenryCo on 19/03/17.
 */

public interface AppMainProto extends OPallSurfaceActivity {

	void switchToFragmentOptions(Fragment fragment);
	void switchToScrollOptionsView();

}
