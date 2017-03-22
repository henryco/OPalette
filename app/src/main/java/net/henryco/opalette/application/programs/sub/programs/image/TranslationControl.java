package net.henryco.opalette.application.programs.sub.programs.image;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.injectables.InjectableSeekBar;
import net.henryco.opalette.application.programs.sub.programs.AppAutoSubControl;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 15/03/17.
 */

public class TranslationControl extends AppAutoSubControl<AppMainProto> {


	private static final int MOVE = R.string.control_move;
	private static final int BUTTON_IMAGE = R.drawable.ic_transform_white_24dp;

	private OPallSurfaceView.OnTouchEventListener touchEventListener;
	private Texture image;

	public TranslationControl(Texture image) {
		super(BUTTON_IMAGE, MOVE);
		this.image = image;
	}


	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		InjectableSeekBar horBar = new InjectableSeekBar(view, "Horizontal");
		InjectableSeekBar verBar = new InjectableSeekBar(view, "Vertical");

		horBar.setDefaultPoint(0, 50);
		verBar.setDefaultPoint(0, 50);

		image.bounds2D.setX(100);

		horBar.onBarCreate(bar -> bar.setProgress(horBar.de_norm(image.bounds2D.getX() / image.getWidth())));
		verBar.onBarCreate(bar -> bar.setProgress(verBar.de_norm(image.bounds2D.getY() / image.getHeight())));

		horBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			image.bounds(b -> b.setX(horBar.norm(progress) * image.getWidth()));
			context.getRenderSurface().update();
		}));
		verBar.setBarListener(new OPallSeekBarListener().onProgress((seekBar, progress, fromUser) -> {
			image.bounds(b -> b.setY(verBar.norm(progress) * image.getHeight()));
			context.getRenderSurface().update();
		}));

		OPallSurfaceView surface = context.getRenderSurface();
		touchEventListener = event -> {
			
		};
		surface.addOnTouchEventListener(touchEventListener);


		OPallViewInjector.inject(context.getActivityContext(), verBar, horBar);
	}


	@Override
	public void onFragmentDestroyed(Fragment fragment, AppMainProto context) {
		context.getRenderSurface().removeTouchEventListener(touchEventListener);
	}
}
