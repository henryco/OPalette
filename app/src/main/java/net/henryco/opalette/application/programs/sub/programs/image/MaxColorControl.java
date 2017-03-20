package net.henryco.opalette.application.programs.sub.programs.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;


/**
 * Created by HenryCo on 19/03/17.
 */

public class MaxColorControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_tonality_white_up_24dp;
	private static final int txt_button_res = R.string.control_maximum;

	private EdTexture edTexture;

	public MaxColorControl(EdTexture edTexture) {
		super(img_button_res, txt_button_res);
		this.edTexture = edTexture;
	}

	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		int type = InjectableSeekBar.TYPE_SMALL;
		InjectableSeekBar redBar = new InjectableSeekBar(view, type, "Red max");
		InjectableSeekBar greenBar = new InjectableSeekBar(view, type, "Green max");
		InjectableSeekBar blueBar = new InjectableSeekBar(view, type, "Blue max");

		redBar.setMax(255);
		greenBar.setMax(255);
		blueBar.setMax(255);

		redBar.onBarCreate(bar -> bar.setProgress(redBar.de_norm(edTexture.max.r)));
		greenBar.onBarCreate(bar -> bar.setProgress(greenBar.de_norm(edTexture.max.g)));
		blueBar.onBarCreate(bar -> bar.setProgress(blueBar.de_norm(edTexture.max.b)));

		redBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			edTexture.max.r = redBar.norm(progress);
			context.getRenderSurface().update();
		}));
		greenBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			edTexture.max.g = greenBar.norm(progress);
			context.getRenderSurface().update();
		}));
		blueBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			edTexture.max.b = blueBar.norm(progress);
			context.getRenderSurface().update();
		}));

		OPallViewInjector.inject(context.getActivityContext(), blueBar, greenBar, redBar);
	}



}
