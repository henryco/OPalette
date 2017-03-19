package net.henryco.opalette.application.programs.sub.programs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import net.henryco.opalette.api.utils.listener.OPallListener;
import net.henryco.opalette.application.proto.AppMainProto;

/**
 * Created by HenryCo on 19/03/17.
 */

public abstract class AppAutoSubControl<T extends AppMainProto, U> extends AppSubControl<T, U> {

	private final int img_button_res;
	private final int txt_button_res;

	public AppAutoSubControl(OPallListener<U> listener, int img_button_res, int txt_button_res) {
		super(listener);
		this.img_button_res = img_button_res;
		this.txt_button_res = txt_button_res;
	}

	@Override
	protected void onInject(T context, View view) {

		loadImageOptionButton(view, txt_button_res, img_button_res, context.getActivityContext(),
				v -> context.switchToFragmentOptions(loadControlFragment(this::onFragmentCreate)));
	}

	protected abstract void onFragmentCreate(View view, T context, @Nullable Bundle savedInstanceState);

}
