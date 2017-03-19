package net.henryco.opalette.application.programs.sub.programs.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.listener.OPallListener;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 19/03/17.
 */

public class MaxColorControl extends AppAutoSubControl<AppMainProto, EdTexture> {

	private static final int img_button_res = R.drawable.ic_tonality_white_down_24dp;
	private static final int txt_button_res = R.string.control_maximum;

	public MaxColorControl(OPallListener<EdTexture> listener) {
		super(listener, img_button_res, txt_button_res);
	}

	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {
//		TODO
	}
}
