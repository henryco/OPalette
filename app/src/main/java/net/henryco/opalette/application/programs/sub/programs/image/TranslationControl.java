package net.henryco.opalette.application.programs.sub.programs.image;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 15/03/17.
 */

public class TranslationControl extends AppAutoSubControl<AppMainProto> {


	private static final int MOVE = R.string.control_move;
	private static final int BUTTON_IMAGE = R.drawable.ic_transform_white_24dp;

	public TranslationControl() {
		super(BUTTON_IMAGE, MOVE);
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		OPallSurfaceView surface = context.getRenderSurface();

	}


	@Override
	public void onFragmentDestroyed(Fragment fragment, AppMainProto context) {

	}
}
