package net.henryco.opalette.application.programs.sub.programs.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.listener.OPallListener;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;


/**
 * Created by HenryCo on 19/03/17.
 */

public class MinColorControl extends AppAutoSubControl<AppMainProto, EdTexture> {


	private static final int img_button_res = R.drawable.ic_tonality_white_down_24dp;
	private static final int txt_button_res = R.string.control_minimum;

	public MinColorControl(OPallListener<EdTexture> listener) {
		super(listener, img_button_res, txt_button_res);
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		int type = InjectableSeekBar.TYPE_SMALL;
		InjectableSeekBar redBar = new InjectableSeekBar(view, type, "Red");
		InjectableSeekBar greenBar = new InjectableSeekBar(view, type, "Green");
		InjectableSeekBar blueBar = new InjectableSeekBar(view, type, "Blue");

		redBar.setMax(255);
		greenBar.setMax(255);
		blueBar.setMax(255);

		redBar.onBarCreate(bar -> getOPallListener().onOPallAction(edTexture -> bar.setProgress(redBar.de_norm(edTexture.min.r))));
		greenBar.onBarCreate(bar -> getOPallListener().onOPallAction(edTexture -> bar.setProgress(greenBar.de_norm(edTexture.min.g))));
		blueBar.onBarCreate(bar -> getOPallListener().onOPallAction(edTexture -> bar.setProgress(blueBar.de_norm(edTexture.min.b))));

		redBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			getOPallListener().onOPallAction(etx -> etx.min.r = redBar.norm(progress));
			context.getRenderSurface().update();
		}));
		greenBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			getOPallListener().onOPallAction(etx -> etx.min.g = greenBar.norm(progress));
			context.getRenderSurface().update();
		}));
		blueBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			getOPallListener().onOPallAction(etx -> etx.min.b = blueBar.norm(progress));
			context.getRenderSurface().update();
		}));

		OPallViewInjector.inject(context.getActivityContext(), blueBar, greenBar, redBar);
	}


}
