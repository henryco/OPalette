package net.henryco.opalette.api.utils.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HenryCo on 15/03/17.
 */

public abstract class OPallSmartLayerInjector <T extends AppCompatActivity> {


	@SuppressWarnings("unchecked")
	public static void inject(AppCompatActivity context, OPallSmartLayerInjector injector) {
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
			insertGroup.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		});
	}


	protected abstract void onInject(T context, View view);
	private final int[] ids;

	public OPallSmartLayerInjector(int container, int layer) {
		ids = new int[]{container, layer};
	}



}
