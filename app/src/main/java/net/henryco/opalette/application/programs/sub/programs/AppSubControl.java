package net.henryco.opalette.application.programs.sub.programs;

import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.api.utils.listener.OPallListener;
import net.henryco.opalette.api.utils.listener.OPallListenerHolder;
import net.henryco.opalette.api.utils.observer.OPallUpdObserved;
import net.henryco.opalette.api.utils.observer.OPallUpdObserver;
import net.henryco.opalette.api.utils.views.OPallViewInjector;

/**
 * Created by HenryCo on 18/03/17.
 */

public abstract class AppSubControl<T extends AppCompatActivity, U> extends OPallViewInjector<T>
		implements OPallListenerHolder<U>, OPallUpdObserved {

	private static OPallUpdObserver updObserver;

	public AppSubControl(int container, int layer, OPallListener<U> listener, OPallUpdObserver updObserver) {
		super(container, layer);
		setOPallListener(listener);
		setObservator(updObserver);
	}

	@Override
	public void setObservator(OPallUpdObserver observator) {
		if (observator != null) updObserver = observator;
	}

	public static OPallUpdObserver getUpdObserver() {
		return updObserver;
	}

}
