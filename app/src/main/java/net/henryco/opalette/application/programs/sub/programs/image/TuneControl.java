package net.henryco.opalette.application.programs.sub.programs.image;

import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.listener.OPallListener;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.programs.AppSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

import static net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener.deNormalize;
import static net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener.normalize;

/**
 * Created by HenryCo on 19/03/17.
 */

public class TuneControl extends AppSubControl<AppMainProto, EdTexture> {

	private static final int TUNE = R.string.control_tune;
	private static final int IMG = R.drawable.ic_tune_white_24dp;

	public TuneControl(OPallListener<EdTexture> listener) {
		super(listener);
	}


	@Override
	protected void onInject(AppMainProto context, View view) {

		loadImageOptionButton(view, TUNE, IMG, context.getActivityContext(), v ->
				context.switchToFragmentOptions(loadControlFragment(onFragmentCreate))
		);
	}



	private AppControlFragmentLoader<AppMainProto> onFragmentCreate = (view, context, savedInstanceState) -> {

		int type = InjectableSeekBar.TYPE_SMALL;
		InjectableSeekBar redBar = new InjectableSeekBar(view, type, "Red");
		InjectableSeekBar greenBar = new InjectableSeekBar(view, type, "Green");
		InjectableSeekBar blueBar = new InjectableSeekBar(view, type, "Blue");

		redBar.setDefaultPoint(0, 50);
		greenBar.setDefaultPoint(0, 50);
		blueBar.setDefaultPoint(0, 50);

		redBar.onBarCreate(bar -> getOPallListener().onOPallAction(edTexture -> bar.setProgress(deNormalize(edTexture.add.r, 100))));
		greenBar.onBarCreate(bar -> getOPallListener().onOPallAction(edTexture -> bar.setProgress(deNormalize(edTexture.add.g, 100))));
		blueBar.onBarCreate(bar -> getOPallListener().onOPallAction(edTexture -> bar.setProgress(deNormalize(edTexture.add.b, 100))));

		redBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			getOPallListener().onOPallAction(etx -> etx.add.r = normalize(progress, 100));
			context.getRenderSurface().update();
		}));
		greenBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			getOPallListener().onOPallAction(etx -> etx.add.g = normalize(progress, 100));
			context.getRenderSurface().update();
		}));
		blueBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			getOPallListener().onOPallAction(etx -> etx.add.b = normalize(progress, 100));
			context.getRenderSurface().update();
		}));

		OPallViewInjector.inject(context.getActivityContext(), blueBar, greenBar, redBar);
	};


}
