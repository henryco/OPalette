package net.henryco.opalette.application.programs.sub.programs.image;

import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.application.programs.sub.programs.AppSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 15/03/17.
 */

public class TranslationControl extends AppSubControl<AppMainProto> {


	private static final int MOVE = R.string.control_move;
	private static final int BUTTON_IMAGE = R.drawable.ic_transform_white_24dp;

	public TranslationControl() {
		super();
	}


	@Override
	protected void onInject(AppMainProto context, View view) {
		loadImageOptionButton(view, MOVE, BUTTON_IMAGE, context.getActivityContext(), v ->
				context.switchToFragmentOptions(loadControlFragment(onFragmentCreate))
		);
	}


	private AppControlFragmentLoader<AppMainProto> onFragmentCreate = (view, context, savedInstanceState) -> {
	//TODO
	};


}
