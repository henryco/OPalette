package net.henryco.opalette.application.programs.sub.programs.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
		super(R.id.scrollContainer, R.layout.image_option_button, listener, updObserver);
		setOPallListener(listener);
	}


	@Override
	protected void onInject(MainActivity context, View view) {

		loadImageOptionButton(view, MOVE, BUTTON_IMAGE, context, v -> {
			synchronized (context) {
				context.runOnUiThread(() -> context.switchToFragmentOptions(new ControlFragment()));
			}
		});
	}


	@Override
	public void setOPallListener(OPallListener<Void> listener) {
	//TODO
	}


	public static final class ControlFragment extends AppControlFragment {
		@Override
		public void onFragmentCreated(View view, @Nullable Bundle savedInstanceState) {
			//TODO
		}
	}
}
