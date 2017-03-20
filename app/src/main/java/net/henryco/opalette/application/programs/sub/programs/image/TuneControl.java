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

public class TuneControl extends AppAutoSubControl<AppMainProto> {

	private static final int IMG = R.drawable.ic_tune_white_24dp;
	private static final int TUNE = R.string.control_tune;

	private EdTexture edTexture;

	public TuneControl(EdTexture edTexture) {
		super(IMG, TUNE);
		this.edTexture = edTexture;
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		int type = InjectableSeekBar.TYPE_SMALL;
		InjectableSeekBar redBar = new InjectableSeekBar(view, type, "Red");
		InjectableSeekBar greenBar = new InjectableSeekBar(view, type, "Green");
		InjectableSeekBar blueBar = new InjectableSeekBar(view, type, "Blue");

		redBar.setDefaultPoint(0, 50);
		greenBar.setDefaultPoint(0, 50);
		blueBar.setDefaultPoint(0, 50);

		redBar.onBarCreate(bar -> bar.setProgress(redBar.de_norm(edTexture.add.r)));
		greenBar.onBarCreate(bar -> bar.setProgress(greenBar.de_norm(edTexture.add.g)));
		blueBar.onBarCreate(bar -> bar.setProgress(blueBar.de_norm(edTexture.add.b)));

		redBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			edTexture.add.r = redBar.norm(progress);
			context.getRenderSurface().update();
		}));
		greenBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			edTexture.add.g = greenBar.norm(progress);
			context.getRenderSurface().update();
		}));
		blueBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			edTexture.add.b = blueBar.norm(progress);
			context.getRenderSurface().update();
		}));

		OPallViewInjector.inject(context.getActivityContext(), blueBar, greenBar, redBar);
	}



}
