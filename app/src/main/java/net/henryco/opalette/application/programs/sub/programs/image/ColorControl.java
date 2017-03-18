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
import net.henryco.opalette.api.utils.observer.OPallUpdObserver;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;
import net.henryco.opalette.application.activities.MainActivity;
import net.henryco.opalette.application.programs.sub.programs.AppSubControl;

import static net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener.deNormalize;
import static net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener.normalize;

/**
 * Created by HenryCo on 16/03/17.
 */
public class ColorControl extends AppSubControl<MainActivity, EdTexture> {

	private static OPallListener<EdTexture> texListener;
	private ImageButton imageButton;



	

	public ColorControl(OPallListener<EdTexture> listener, OPallUpdObserver updObserver) {
		super(R.id.scrollContainer, R.layout.image_option, listener, updObserver);
		setOPallListener(listener);
		setObservator(updObserver);
	}

	@Override
	protected void onInject(MainActivity context, View view) {

		TextView textView = (TextView) view.findViewById(R.id.iopTextView);
		textView.setText("color");

		imageButton = (ImageButton) view.findViewById(R.id.iopImageButton);
		imageButton.setImageResource(R.drawable.ic_translate_white_24dp);
		imageButton.setClickable(false);

		view.setOnClickListener(v ->
				OPallUtils.pressButton75_225(context, imageButton, () -> {
					synchronized (context) {
						ControlFragment fragment = new ControlFragment();
						context.runOnUiThread(() -> context.switchToFragmentOptions(fragment));
					}
				})
		);
	}


	@Override
	public void setOPallListener(OPallListener<EdTexture> listener) {
		ColorControl.texListener = listener;
	}








//	TODO: MAYBE NEED CONSTRUCTOR and OVERRIDES [onAttach(), onCreate()]
	public static final class ControlFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.control_image_color, container, false);
		}

		@Override
		public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);

			SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
			texListener.onOPallAction(edTexture ->
					seekBar.setProgress(deNormalize(edTexture.getBrightness())));

			seekBar.setOnSeekBarChangeListener(new OPallSeekBarListener().onProgress
					((sBar, progress, fromUser) -> {
				texListener.onOPallAction(etx -> etx.brightness(b -> normalize(progress)));
				getUpdObserver().update();
			}));
		}

	}

}
