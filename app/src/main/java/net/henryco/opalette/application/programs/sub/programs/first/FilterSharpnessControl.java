package net.henryco.opalette.application.programs.sub.programs.first;

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
 * Created by HenryCo on 21/03/17.
 */

public class FilterSharpnessControl extends AppAutoSubControl<AppMainProto> {

	private static final int img_button_res = R.drawable.ic_details_white_24dp;
	private static final int txt_button_res = R.string.control_sharpness;

	private AppSubProgram.ProxyRenderData<ConvolveTexture> filterHolder;

	public FilterSharpnessControl(AppSubProgram.ProxyRenderData<ConvolveTexture> data) {
		super(img_button_res, txt_button_res);
		filterHolder = data;
	}



	@Override
	protected void onFragmentCreate(View view, AppMainProto context, @Nullable Bundle savedInstanceState) {

		InjectableSeekBar seekBar = new InjectableSeekBar(view);
		seekBar.setBarName(context.getActivityContext().getResources().getString(txt_button_res));
		seekBar.onBarCreate(bar -> bar.setProgress(seekBar.de_norm(filterHolder.getRenderData().getEffectScale())));
		seekBar.setBarListener(new OPallSeekBarListener().onProgress((bar, progress, fromUser) -> {
			filterHolder.setStateUpdated().getRenderData().setEffectScale(seekBar.norm(progress));
			context.getRenderSurface().update();
		}));
		OPallViewInjector.inject(context.getActivityContext(), seekBar);
	}
}
