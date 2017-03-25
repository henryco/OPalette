package net.henryco.opalette.application.programs.sub.programs.aImage;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceTouchListener;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.utils.RefreshableTimer;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 25/03/17.
 */

public class CanvasSizeControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_aspect_ratio_white_24dp;
	private static final int txt_button_res = R.string.control_canvas_size;
	private static final int MIN_SIZE = 100;
	private final float defScrW;
	private final float defScrH;
	private final OPallSeekBarListener stop;
	private final OPallRequester requester;
	private RefreshableTimer timer;
	private OPallSurfaceTouchListener touchEventListener;

	public CanvasSizeControl(final float defScrW, final float defScrH, final OPallRequester requester) {
		super(img_button_res, txt_button_res);
		this.defScrW = defScrW;
		this.defScrH = defScrH;
		this.requester = requester;

		this.stop = new OPallSeekBarListener().onStop(bar -> timer.startIfWaiting().refresh());
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		OPallSurfaceView surface = context.getRenderSurface();
		InjectableSeekBar hBar = new InjectableSeekBar(view, "Canvas height").setMax((int) defScrH);

		this.timer = new RefreshableTimer(250, () -> {
			requester.sendRequest(new Request(AppSubProtocol.set_filters_enable));
			surface.update();
		});



//		InjectableSeekBar wBar = new InjectableSeekBar(view, "Canvas width").setMax((int) defScrW);
//		wBar.onBarCreate(bar -> bar.setProgress(surface.getWidth()));
//		wBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
//			if (fromUser) {
//				requester.sendRequest(new Request(AppSubProtocol.set_filters_disable));
//				surface.setSize((int) clamp(progress, defScrW, MIN_SIZE), surface.getHeight()).update();
//			}
//		}).onStop(stop));
//		OPallViewInjector.inject(context.getActivityContext(), wBar);



		hBar.onBarCreate(bar -> bar.setProgress(surface.getHeight()));
		hBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				requester.sendRequest(new Request(AppSubProtocol.set_filters_disable));
				surface.setSize(surface.getWidth(), (int) clamp(progress, defScrH, MIN_SIZE)).update();
			}
		}).onStop(stop));
		OPallViewInjector.inject(context.getActivityContext(), hBar);



		touchEventListener = new OPallSurfaceTouchListener(context.getActivityContext());
		touchEventListener.setOnActionUp(() -> timer.startIfWaiting().refresh());
		surface.addOnTouchEventListener(touchEventListener.setOnActionMove((dx, dy) -> {

			requester.sendRequest(new Request(AppSubProtocol.set_filters_disable));
			int py = (int) clamp(surface.getHeight() + dy, defScrH, MIN_SIZE);
			surface.setSize(surface.getWidth(), py).update();
			hBar.setProgress(py);
		}));
	}


	@Override
	public void onFragmentDestroyed(Fragment fragment, AppMainProto context) {
		context.getRenderSurface().removeTouchEventListener(touchEventListener);
	}


}
