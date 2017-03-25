package net.henryco.opalette.application.programs.sub.programs.aImage;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Switch;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceTouchListener;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.ConvolveTexture;
import net.henryco.opalette.api.utils.RefreshableTimer;
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
	private static final float touch_sensitivity = 0.4f;
	private static final float max_angle = 90f;
	private AppSubProgram.ProxyRenderData<ConvolveTexture> imgHolder;
	private OPallSurfaceTouchListener touchEventListener;

	public RotationControl(final AppSubProgram.ProxyRenderData<ConvolveTexture> renderData) {
		super(img_button_res, txt_button_res);
		this.imgHolder = renderData;
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

		updateFunc = () -> {
			imgHolder.setStateUpdated();
			context.getRenderSurface().update();
		};

		OPallViewInjector<AppMainProto> flipButtons = new OPallViewInjector<AppMainProto>
				(view, R.layout.flip_buttons_layout) {

			@Override
			protected void onInject(AppMainProto context, View view) {

				Texture image = imgHolder.getRenderData();
				int textColor = ContextCompat.getColor(context.getActivityContext(), InjectableSeekBar.TEXT_COLOR_DEFAULT_DARK);

				Switch horizontal = (Switch) view.findViewById(R.id.flipHorizontalButton);
				Switch vertical = (Switch) view.findViewById(R.id.flipVerticalButton);

				horizontal.setTextColor(textColor);
				horizontal.setChecked(image.getFlip()[0]);
				vertical.setTextColor(textColor);
				vertical.setChecked(image.getFlip()[1]);

				horizontal.setOnCheckedChangeListener((button, isChecked) -> {
					image.setFlip(isChecked, image.getFlip()[1]);
					updateFunc.run();
				});
				vertical.setOnCheckedChangeListener((button, isChecked) -> {
					image.setFlip(image.getFlip()[0], isChecked);
					updateFunc.run();
				});
			}
		};

		InjectableSeekBar angleBar = new InjectableSeekBar(view, "Angle").setDefaultPoint(0, (int) (max_angle * 0.5f)).setMax((int) max_angle);
		angleBar.onBarCreate(bar -> bar.setProgress(angleBar.de_norm(imgHolder.getRenderData().getRotation() / max_angle)));
		angleBar.setBarListener(new OPallSeekBarListener().onStop(stop).onProgress((bar, progress, fromUser) -> {
			if (fromUser) {
				imgHolder.getRenderData().setFilterEnable(false).setRotation(angleBar.norm(progress) * max_angle);
				updateFunc.run();
			}
		}));


		touchEventListener = new OPallSurfaceTouchListener(context.getActivityContext());
		touchEventListener.setOnActionUp(() -> timer.startIfWaiting().refresh());
		context.getRenderSurface().addOnTouchEventListener(touchEventListener.setOnActionMove((dx, dy) -> {
			float angle = clamp(imgHolder.getRenderData().getRotation() + dx * touch_sensitivity, max_angle, -max_angle);
			imgHolder.getRenderData().setFilterEnable(false).setRotation(angle);
			angleBar.setProgress(angleBar.de_norm(angle / max_angle));
			updateFunc.run();
		}));


		OPallViewInjector.inject(context.getActivityContext(), flipButtons, angleBar);
	}

	@Override
	public void onFragmentDestroyed(Fragment fragment, AppMainProto context) {
		context.getRenderSurface().removeTouchEventListener(touchEventListener);
	}
}


