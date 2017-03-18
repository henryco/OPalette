package net.henryco.opalette.application.programs.sub.programs.image;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.OPallUtils;
import net.henryco.opalette.api.utils.listener.OPallListener;
import net.henryco.opalette.api.utils.listener.OPallListenerHolder;
import net.henryco.opalette.api.utils.observer.OPallUpdObserved;
import net.henryco.opalette.api.utils.observer.OPallUpdObserver;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.activities.MainActivity;

/**
 * Created by HenryCo on 16/03/17.
 */

public class ColorControl extends OPallViewInjector<MainActivity>
		implements OPallListenerHolder<EdTexture>, OPallUpdObserved {


	private ImageButton imageButton;
	private OPallListener<EdTexture> listener;
	private OPallUpdObserver updObserver;

	public ColorControl(OPallListener<EdTexture> listener, OPallUpdObserver updObserver) {
		this();
		setOPallListener(listener);
		setObservator(updObserver);
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
						ControlFragment fragment = new ControlFragment();
						fragment.setOPallListener(listener);
						fragment.setObservator(updObserver);
						context.runOnUiThread(() -> context.switchToFragmentOptions(fragment));
					}
				})
		);

	}

	@Override
	public void setOPallListener(OPallListener<EdTexture> listener) {
		this.listener = listener;
	}

	@Override
	public void setObservator(OPallUpdObserver observator) {
		this.updObserver = observator;
	}

	public static class ControlFragment extends Fragment
			implements OPallListenerHolder<EdTexture>, OPallUpdObserved {

		//	TODO: MAYBE NEED OVERRIDE [onAttach(), onCreate()]
		private OPallListener<EdTexture> texListener;
		private OPallUpdObserver updObserver;

		private float b = 0;



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
			texListener = null;
			updObserver = null;
		}

		@Override
		public void setOPallListener(OPallListener<EdTexture> listener) {
			texListener = listener;
			texListener.onOPallAction(edTexture -> edTexture.brightness(b -> {
				this.b = (b * 50) + 50;
				return b;
			}));
		}

		@Override
		public void setObservator(OPallUpdObserver observator) {
			this.updObserver = observator;
		}

		@Override
		public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
			System.out.println("CR Br: "+b);
			seekBar.setMax(100);
			seekBar.setProgress((int) b);
//			seekBar.setOnSeekBarChangeListener();

			seekBar.setOnClickListener(v -> {
				float pos = seekBar.getProgress();
				System.out.println("Br: "+pos);
				texListener.onOPallAction(etx -> etx.brightness(b -> {
					float bb = (pos - 50f) / 50f;
					System.out.println("BBR: "+bb);
					return bb;
				}));
				updObserver.update();
			});
		}
	}

}
