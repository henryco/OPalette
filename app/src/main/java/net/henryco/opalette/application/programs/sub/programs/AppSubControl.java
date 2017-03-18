package net.henryco.opalette.application.programs.sub.programs;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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





	public static class AppControlFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			linearLayout.setGravity(Gravity.CENTER);
			return linearLayout;
		}

	}

}
