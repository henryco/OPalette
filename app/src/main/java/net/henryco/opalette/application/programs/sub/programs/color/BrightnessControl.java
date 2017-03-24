package net.henryco.opalette.application.programs.sub.programs.color;

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
 * Created by HenryCo on 16/03/17.
 */
public class BrightnessControl extends AppAutoSubControl<AppMainProto> {


	private static final int BUTTON_IMAGE = R.drawable.ic_brightness_6_white_24dp;
	private static final int BRIGHTNESS = R.string.control_brightness;

	private EdTexture edTexture;

	public BrightnessControl(EdTexture edTexture) {
		super(BUTTON_IMAGE, BRIGHTNESS);
		this.edTexture = edTexture;
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {
		String brightness = context.getActivityContext().getResources().getString(R.string.control_brightness);

		InjectableSeekBar brightnessBar = new InjectableSeekBar(view, brightness);
		brightnessBar.setDefaultPoint(0, 50);

		brightnessBar.onBarCreate(bar ->
				bar.setProgress(brightnessBar.de_norm(edTexture.getBrightness()))
		);

		brightnessBar.setBarListener(new OPallSeekBarListener().onProgress((sBar, progress, fromUser) -> {
			edTexture.brightness(b -> brightnessBar.norm(progress));
			context.getRenderSurface().update();
		}));

		OPallViewInjector.inject(context.getActivityContext(), brightnessBar);
	}




}
