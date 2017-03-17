package net.henryco.opalette.application.programs.sub.programs.image;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.OPallUtils;
import net.henryco.opalette.api.utils.listener.OPallListener;
import net.henryco.opalette.api.utils.listener.OPallListenerHolder;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.activities.MainActivity;

/**
 * Created by HenryCo on 16/03/17.
 */

public class ColorControl extends OPallViewInjector<MainActivity> implements OPallListenerHolder<EdTexture> {


	private ImageButton imageButton;
	private OPallListener<EdTexture> listener;

	public ColorControl(OPallListener<EdTexture> listener) {
		this();
		setOPallListener(listener);
	}
	public ColorControl() {
		super(R.id.scrollContainer, R.layout.image_option);
	}

	@Override
	protected void onInject(MainActivity context, View view) {

		TextView textView = (TextView) view.findViewById(R.id.iopTextView);
		textView.setText("color");

		imageButton = (ImageButton) view.findViewById(R.id.iopImageButton);
		imageButton.setImageResource(R.drawable.ic_translate_white_24dp);
		imageButton.setClickable(false);

	}

	@Override
	protected void onPostInject(MainActivity context, View view) {

		view.setOnClickListener(v ->
				OPallUtils.pressButton75_225(context, imageButton, () -> {
					synchronized (context) {
						context.runOnUiThread(() -> context.switchToFragmentOptions(new ControlFragment()));
					}
				})
		);

	}

	@Override
	public void setOPallListener(OPallListener<EdTexture> listener) {
		this.listener = listener;
	}





	public static class ControlFragment extends Fragment {

		//	TODO: MAYBE NEED OVERRIDE [onAttach(), onCreate()]


		public ControlFragment() {
			// Required empty public constructor
		}


		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.control_image_color, container, false);
		}


		@Override
		public void onDetach() {
			super.onDetach();
		}

	}

}
