package net.henryco.opalette.api.utils.views;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HenryCo on 15/03/17.
 */

public abstract class OPallViewInjector<T extends Context> {


	@SuppressWarnings("unchecked")
	public static void inject(Activity context, OPallViewInjector injector, long delay) {

		context.runOnUiThread(() -> {
			View view = ((LayoutInflater) context.getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(injector.ids[1], null);

			final ViewGroup insertGroup;
			if (injector.containerGroup == null && injector.ids[0] != (-1))
				insertGroup = (ViewGroup) context.findViewById(injector.ids[0]);
			else if (injector.containerGroup != null && injector.ids[0] == (-1))
				insertGroup = injector.containerGroup;
			else throw new RuntimeException("VIEW GROUP == NULL");

			try {
				injector.onInject(context, view);
			} catch (ClassCastException e) {
				throw new RuntimeException(context.getClass().getName()+
						" wrong generic instance, must be compatible with: "+
						injector.getClass().getName()
				);
			}

			view.setVisibility(View.GONE);
			new Handler().postDelayed(() -> context.runOnUiThread(() -> {
				insertGroup.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
				view.setVisibility(View.VISIBLE);
			}), delay);

		});
	}

	public static void inject(Activity context, OPallViewInjector injector) {
		inject(context, injector, 0);
	}

	private final int[] ids;
	private final ViewGroup containerGroup;

	public OPallViewInjector(int container, int layer) {
		ids = new int[]{container, layer};
		containerGroup = null;
	}
	public OPallViewInjector(ViewGroup container, int layer) {
		ids = new int[]{-1, layer};
		containerGroup = container;
	}
	public OPallViewInjector(View container, int layer) {
		this((ViewGroup)container, layer);
	}

	protected abstract void onInject(T context, View view);

}
