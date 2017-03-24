package net.henryco.opalette.application.programs.sub.programs.filters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.ConvolveTexture;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 24/03/17.
 */

public class RotationControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_crop_rotate_white_24dp;
	private static final int txt_button_res = R.string.control_rotation;
	private AppSubProgram.ProxyRenderData<ConvolveTexture> imgHolder;


	public RotationControl(final AppSubProgram.ProxyRenderData<ConvolveTexture> renderData) {
		super(img_button_res, txt_button_res);
		this.imgHolder = renderData;
	}

	private Runnable updateFunc = () -> imgHolder.setStateUpdated();
	private Runnable stopFunc = () -> {
		imgHolder.getRenderData().setFilterEnable(true);
		updateFunc.run();
	};
	private OPallSeekBarListener stop = new OPallSeekBarListener().onStop(bar -> stopFunc.run());


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		InjectableSeekBar angleBar = new InjectableSeekBar(view, "Angle").setDefaultPoint(0, 45).setMax(90);
		angleBar.onBarCreate(bar -> bar.setProgress(angleBar.de_norm(imgHolder.getRenderData().getRotation() / 90f)));

		updateFunc = () -> {
			imgHolder.setStateUpdated();
			context.getRenderSurface().update();
		};

		angleBar.setBarListener(new OPallSeekBarListener().onStop(stop).onProgress((bar, progress, fromUser) -> {
			imgHolder.getRenderData().setFilterEnable(false).setRotation(angleBar.norm(progress) * 90f);
			updateFunc.run();
		}));

		OPallViewInjector.inject(context.getActivityContext(), angleBar);
	}

}


