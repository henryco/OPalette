package net.henryco.opalette.application.programs.sub.programs;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.OPallAnimated;
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

	private OPallUpdObserver updObserver;
	private OPallListener<U> listener;

	public AppSubControl(OPallListener<U> listener, OPallUpdObserver updObserver) {
		super(R.id.scrollContainer, R.layout.image_option_button);
		setOPallListener(listener);
		setObservator(updObserver);
	}

	@Override
	public void setObservator(OPallUpdObserver observator) {
		updObserver = observator;
	}

	@Override
	public void setOPallListener(OPallListener<U> listener) {
		this.listener = listener;
	}

	public OPallUpdObserver getUpdObserver() {
		return updObserver;
	}
	public OPallListener<U> getOPallListener() {
		return listener;
	}






	public static <V extends AppCompatActivity> ControlFragment<V> loadControlFragment(AppControlFragmentLoader<V> loader) {
		return new ControlFragment<V>().onFragmentCreated(loader);
	}

	public static void loadImageOptionButton(View view, int textRes, int imgRes, AppCompatActivity context, View.OnClickListener clickListener) {

		TextView textView = (TextView) view.findViewById(R.id.iopTextView);
		textView.setText(textRes);

		ImageButton imageButton = (ImageButton) view.findViewById(R.id.iopImageButton);
		imageButton.setImageResource(imgRes);
		imageButton.setClickable(false);

		view.setOnClickListener(v ->
				OPallAnimated.pressButton75_225(context, imageButton, () -> {
					synchronized (context) {
						context.runOnUiThread(() -> clickListener.onClick(v));
					}
				})
		);

	}






	public interface AppControlFragmentLoader<U extends AppCompatActivity> {
		void onFragmentCreated(View view, U context,  @Nullable Bundle savedInstanceState);
	}


	public static abstract class AppControlFragment<T extends AppCompatActivity> extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			linearLayout.setGravity(Gravity.CENTER);
			return linearLayout;
		}

		@SuppressWarnings("unchecked")
		public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			T context;
			try {
				context = (T) getActivity();
			} catch (ClassCastException e) {
				throw new RuntimeException(getClass().getName()+
						" generic <T extends AppCompatActivity> " +
						"must be compatible with super Activity instance");
			}
			onFragmentCreated(view, context, savedInstanceState);
		}

		protected abstract void onFragmentCreated(View view, T context, @Nullable Bundle savedInstanceState);
	}


	public static final class ControlFragment<T extends AppCompatActivity> extends AppControlFragment<T> {

		private AppControlFragmentLoader<T> loader;

		public ControlFragment<T> onFragmentCreated(AppControlFragmentLoader<T> loader) {
			this.loader = loader;
			return this;
		}

		@Override
		protected void onFragmentCreated(View view, T context, @Nullable Bundle savedInstanceState) {
			if (loader != null) loader.onFragmentCreated(view, context, savedInstanceState);
		}
	}

}
