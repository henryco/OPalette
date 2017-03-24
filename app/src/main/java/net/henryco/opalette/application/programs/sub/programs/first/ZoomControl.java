package net.henryco.opalette.application.programs.sub.programs.first;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
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

public class ZoomControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_tonality_white_up_24dp;
	private static final int txt_button_res = R.string.control_zoom;
	private AppSubProgram.ProxyRenderData<ConvolveTexture> imgHolder;
	private OPallSurfaceView.OnTouchEventListener touchEventListener;

	private final float defaultScale; // TODO

	public ZoomControl(final AppSubProgram.ProxyRenderData<ConvolveTexture> renderData) {
		super(img_button_res, txt_button_res);
		this.imgHolder = renderData;
		this.defaultScale = imgHolder.getRenderData().bounds2D.getScale();
	}


	private Runnable updateFunc = () -> imgHolder.setStateUpdated();
	private Runnable stopFunc = () -> {
		imgHolder.getRenderData().setFilterEnable(true);
		updateFunc.run();
	};
	private OPallSeekBarListener stop = new OPallSeekBarListener().onStop(bar -> stopFunc.run());



	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		updateFunc = () -> {
			imgHolder.setStateUpdated();
			context.getRenderSurface().update();
		};

		ConvolveTexture image = imgHolder.getRenderData();
		OPallSurfaceView surface = context.getRenderSurface();

		InjectableSeekBar zoomBar = new InjectableSeekBar(view, "Scale").setMax(100);
		zoomBar.setTextValuerCorrector(f -> f * 0.02f); // 0.01f * 2f
		zoomBar.onBarCreate(bar -> bar.setProgress(zoomBar.de_norm(image.bounds2D.getScale() / 2f)));
		zoomBar.setBarListener(new OPallSeekBarListener().onStop(stop).onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				image.setFilterEnable(false).bounds2D.setScale(zoomBar.norm(progress) * 2f);
				updateFunc.run();
			}
		}));

		surface.addOnTouchEventListener(touchEventListener = event -> {

			//TODO
			switch (event.getAction()) {

			}

		});

		OPallViewInjector.inject(context.getActivityContext(), zoomBar);
	}


	@Override
	public void onFragmentDestroyed(Fragment fragment, AppMainProto context) {
		if (touchEventListener != null)
			context.getRenderSurface().removeTouchEventListener(touchEventListener);
	}
}
