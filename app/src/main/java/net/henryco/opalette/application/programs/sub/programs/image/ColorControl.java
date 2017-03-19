package net.henryco.opalette.application.programs.sub.programs.image;

import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.listener.OPallListener;
import net.henryco.opalette.api.utils.observer.OPallUpdObserver;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.MainActivity;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.programs.AppSubControl;

import static net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener.deNormalize;
import static net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener.normalize;

/**
 * Created by HenryCo on 16/03/17.
 */
public class ColorControl extends AppSubControl<MainActivity, EdTexture> {

	private static final int BRIGHTNESS = R.string.control_brightness;
	private static final int BUTTON_IMAGE = R.drawable.ic_brightness_6_white_24dp;

	public ColorControl(OPallListener<EdTexture> listener, OPallUpdObserver updObserver) {
		super(listener, updObserver);
	}

	@Override
	protected void onInject(MainActivity context, View view) {

		loadImageOptionButton(view, BRIGHTNESS, BUTTON_IMAGE, context, v ->
				context.switchToFragmentOptions(loadControlFragment(loader))
		);
	}




	private AppControlFragmentLoader<MainActivity> loader = (view, context, savedInstanceState) -> {
		String brightness = context.getResources().getString(R.string.control_brightness);

		InjectableSeekBar brightnessBar = new InjectableSeekBar(view, InjectableSeekBar.TYPE_NORMAL, brightness);
		brightnessBar.setDefaultPoint(0, 50);
		brightnessBar.setOnBarCreate(bar -> getOPallListener().onOPallAction(edTexture -> {
			int br = deNormalize(edTexture.getBrightness(), 100);
			bar.setProgress(br);
		}));

		brightnessBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			getOPallListener().onOPallAction(etx -> etx.brightness(b -> normalize(progress, 100)));
			getUpdObserver().update();
		}));

		OPallViewInjector.inject(context, brightnessBar);
	};



}
