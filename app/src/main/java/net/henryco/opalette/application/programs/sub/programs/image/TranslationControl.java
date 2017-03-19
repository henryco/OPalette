package net.henryco.opalette.application.programs.sub.programs.image;

import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.listener.OPallListener;
import net.henryco.opalette.api.utils.observer.OPallUpdObserver;
import net.henryco.opalette.application.MainActivity;
import net.henryco.opalette.application.programs.sub.programs.AppSubControl;

/**
 * Created by HenryCo on 15/03/17.
 */

public class TranslationControl extends AppSubControl<MainActivity, Void> {


	private static final int MOVE = R.string.control_move;
	private static final int BUTTON_IMAGE = R.drawable.ic_transform_white_24dp;

	public TranslationControl(OPallListener<Void> listener, OPallUpdObserver updObserver) {
		super(listener, updObserver);
	}


	@Override
	protected void onInject(MainActivity context, View view) {
		loadImageOptionButton(view, MOVE, BUTTON_IMAGE, context, v ->
				context.switchToFragmentOptions(loadControlFragment(onFragmentCreate))
		);
	}


	private AppControlFragmentLoader<MainActivity> onFragmentCreate = (view, context, savedInstanceState) -> {
	//TODO
	};


}
