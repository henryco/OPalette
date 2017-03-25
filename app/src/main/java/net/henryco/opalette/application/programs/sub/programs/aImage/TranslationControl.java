package net.henryco.opalette.application.programs.sub.programs.aImage;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceTouchListener;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.ConvolveTexture;
import net.henryco.opalette.api.utils.RefreshableTimer;
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

	private static final float MAX_SCALE = 4;
	private static final float MIN_SCALE = 0.25f;

	private OPallSurfaceTouchListener touchEventListener;
	private AppSubProgram.ProxyRenderData<ConvolveTexture> imgHolder;
	private final float defaultScale; // TODO

	public TranslationControl(final AppSubProgram.ProxyRenderData<ConvolveTexture> renderData) {
		super(BUTTON_IMAGE, MOVE);
		this.imgHolder = renderData;
		this.defaultScale = imgHolder.getRenderData().bounds2D.getScale();
	}


	private Runnable updateFunc = () -> imgHolder.setStateUpdated();
	private Runnable stopFunc = () -> {
		imgHolder.getRenderData().setFilterEnable(true);
		updateFunc.run();
	};
	private RefreshableTimer timer = new RefreshableTimer(500, stopFunc);
	private OPallSeekBarListener stop = new OPallSeekBarListener().onStop(bar -> timer.startIfWaiting().refresh());





	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		OPallSurfaceView surface = context.getRenderSurface();
		ConvolveTexture image = imgHolder.getRenderData();
		int type = InjectableSeekBar.TYPE_SMALL;

		InjectableSeekBar horBar = new InjectableSeekBar(view, type, "Horizontal").setDefaultPoint(0, 50);
		InjectableSeekBar verBar = new InjectableSeekBar(view, type, "Vertical").setDefaultPoint(0, 50);
		InjectableSeekBar zoomBar = new InjectableSeekBar(view, type, "Scale").setMax(100);

		updateFunc = () -> {
			imgHolder.setStateUpdated();
			context.getRenderSurface().update();
		};




		horBar.onBarCreate(bar -> bar.setProgress((int)clamp(horBar.de_norm(image.bounds2D.getX() / image.getScreenWidth()), 100, -100)));
		horBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				image.setFilterEnable(false).bounds2D.setX(horBar.norm(progress) * image.getScreenWidth());
				updateFunc.run();
			}
		}).onStop(stop));




		verBar.onBarCreate(bar -> bar.setProgress((int)clamp(verBar.de_norm(image.bounds2D.getY() / image.getScreenHeight()), 100, -100)));
		verBar.setBarListener(new OPallSeekBarListener().onProgress((seekBar, progress, fromUser) -> {
			if (fromUser) {
				image.setFilterEnable(false).bounds2D.setY(verBar.norm(progress) * image.getScreenHeight());
				updateFunc.run();
			}
		}).onStop(stop));



		zoomBar.setTextValuerCorrector(f -> Math.max(f * 0.01f * MAX_SCALE, MIN_SCALE));
		zoomBar.onBarCreate(bar -> bar.setProgress(zoomBar.de_norm(image.bounds2D.getScale() / MAX_SCALE)));
		zoomBar.setBarListener(new OPallSeekBarListener().onStop(stop).onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				image.setFilterEnable(false).bounds2D.setScale(Math.max(zoomBar.norm(progress) * MAX_SCALE, MIN_SCALE));
				updateFunc.run();
			}
		}));



		touchEventListener = new OPallSurfaceTouchListener(context.getActivityContext());
		touchEventListener.setOnActionUp(() -> timer.startIfWaiting().refresh());
		touchEventListener.setOnActionMove((dx, dy) -> {
			float px = image.bounds2D.getX() + dx;
			float py = image.bounds2D.getY() + dy;
			horBar.setProgress((int) clamp(horBar.de_norm((px / image.getWidth())), 100, -100));
			verBar.setProgress((int) clamp(verBar.de_norm((py / image.getHeight())), 100, -100));
			image.setFilterEnable(false).bounds2D.setPosition(px, py);
			updateFunc.run();
		});

		touchEventListener.setOnScale(scaleGestureDetector -> {
			float scale = clamp(image.bounds2D.getScale() * scaleGestureDetector.getScaleFactor(), MAX_SCALE, MIN_SCALE);
			zoomBar.setProgress(zoomBar.de_norm(scale / MAX_SCALE));
			image.setFilterEnable(false).bounds2D.setScale(scale);
			updateFunc.run();
			return true;
		});


		surface.addOnTouchEventListener(touchEventListener);
		OPallViewInjector.inject(context.getActivityContext(), zoomBar, verBar, horBar);
	}



	@Override
	public void onFragmentDestroyed(Fragment fragment, AppMainProto context) {
		context.getRenderSurface().removeTouchEventListener(touchEventListener);
	}




}
