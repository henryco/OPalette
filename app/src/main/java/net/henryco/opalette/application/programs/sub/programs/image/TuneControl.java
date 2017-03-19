package net.henryco.opalette.application.programs.sub.programs.image;

import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.listener.OPallListener;
import net.henryco.opalette.api.utils.observer.OPallUpdObserver;
import net.henryco.opalette.application.MainActivity;
import net.henryco.opalette.application.programs.sub.programs.AppSubControl;

/**
 * Created by HenryCo on 19/03/17.
 */

public class TuneControl extends AppSubControl<MainActivity, EdTexture> {

	private static final int TUNE = R.string.control_tune;
	private static final int IMG = R.drawable.ic_tune_white_24dp;

	public TuneControl(OPallListener<EdTexture> listener, OPallUpdObserver updObserver) {
		super(listener, updObserver);
	}



	@Override
	protected void onInject(MainActivity context, View view) {

		loadImageOptionButton(view, TUNE, IMG, context, v ->
				context.switchToFragmentOptions(loadControlFragment(loader))
		);
	}


	private AppControlFragmentLoader<MainActivity> loader = (view, context, savedInstanceState) -> {

	};


}
