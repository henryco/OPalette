package net.henryco.opalette.application.programs.sub.programs.image;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.OPallUtils;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.activities.MainActivity;

/**
 * Created by HenryCo on 16/03/17.
 */

public class ColorControl extends OPallViewInjector<MainActivity> {


	private ImageButton imageButton;

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
	public void onPostInject(MainActivity context, View view) {

		view.setOnClickListener(v ->
				OPallUtils.pressButton75_225(context, imageButton, () -> {
					synchronized (context) {
						context.runOnUiThread(context::switchToFragmentOptions);
					}
				})
		);

	}



}
