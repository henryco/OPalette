package net.henryco.opalette.api.utils.views;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HenryCo on 15/03/17.
 */

public abstract class OPallViewInjector<T extends AppCompatActivity> {




	@SuppressWarnings("unchecked")
	public static void inject(AppCompatActivity context, OPallViewInjector injector, long delay) {
		context.runOnUiThread(() -> {
			View view = ((LayoutInflater) context.getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(injector.ids[1], null);
			ViewGroup insertGroup = (ViewGroup) context.findViewById(injector.ids[0]);
			try {
				injector.onInject(context, view);
			} catch (ClassCastException e) {
				throw new RuntimeException(context.getClass().getName()+
						" wrong generic instance, must be compatible with: "+
						injector.getClass().getName()
				);
			}
			view.setVisibility(View.GONE);
			insertGroup.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

			new Handler().postDelayed(() -> context.runOnUiThread(() -> {
				view.setVisibility(View.VISIBLE);
				injector.onPostInject(context, view);
			}), delay);

		});
	}
	public static void inject(AppCompatActivity context, OPallViewInjector injector) {
		inject(context, injector, 0);
	}


	protected abstract void onInject(T context, View view);
	private final int[] ids;

	public OPallViewInjector(int container, int layer) {
		ids = new int[]{container, layer};
	}

	public void onPostInject(T context, View view) {}

}
