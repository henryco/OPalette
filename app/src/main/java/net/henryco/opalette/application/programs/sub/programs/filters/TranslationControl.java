package net.henryco.opalette.application.programs.sub.programs.filters;

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
 * Created by HenryCo on 15/03/17.
 */

public class TranslationControl extends AppAutoSubControl<AppMainProto> {


	private static final int MOVE = R.string.control_move;
	private static final int BUTTON_IMAGE = R.drawable.ic_transform_white_24dp;

	private OPallSurfaceView.OnTouchEventListener touchEventListener;
	private AppSubProgram.ProxyRenderData<ConvolveTexture> imgHolder;

	public TranslationControl(AppSubProgram.ProxyRenderData<ConvolveTexture> renderData) {
		super(BUTTON_IMAGE, MOVE);
		imgHolder = renderData;
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		InjectableSeekBar horBar = new InjectableSeekBar(view, "Horizontal");
		InjectableSeekBar verBar = new InjectableSeekBar(view, "Vertical");

		horBar.setDefaultPoint(0, 50);
		verBar.setDefaultPoint(0, 50);

		ConvolveTexture image = imgHolder.getRenderData();

		horBar.onBarCreate(bar -> bar.setProgress(horBar.de_norm(image.bounds2D.getX() / image.getWidth())));
		verBar.onBarCreate(bar -> bar.setProgress(verBar.de_norm((-1) * image.bounds2D.getY() / image.getHeight())));

		Runnable updateFunc = () -> {
			imgHolder.setStateUpdated();
			context.getRenderSurface().update();
		};

		OPallSeekBarListener stop = new OPallSeekBarListener().onStop(bar -> {
			image.setFilterEnable(true);
			updateFunc.run();
		});

		horBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			image.setFilterEnable(false).bounds(b -> b.setX(horBar.norm(progress) * image.getWidth()));
			updateFunc.run();
		}).onStop(stop));

		verBar.setBarListener(new OPallSeekBarListener().onProgress((seekBar, progress, fromUser) -> {
			image.setFilterEnable(false).bounds(b -> b.setY((-1) * verBar.norm(progress) * image.getHeight()));
			updateFunc.run();
		}).onStop(stop));



		OPallSurfaceView surface = context.getRenderSurface();
		touchEventListener = event -> {

			float x = event.getX();
			float y = event.getY();



		};
		surface.addOnTouchEventListener(touchEventListener);


		OPallViewInjector.inject(context.getActivityContext(), verBar, horBar);
	}


	@Override
	public void onFragmentDestroyed(Fragment fragment, AppMainProto context) {
		context.getRenderSurface().removeTouchEventListener(touchEventListener);
	}
}
