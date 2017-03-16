package net.henryco.opalette.application.programs.controlls;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.activities.MainActivity;

/**
 * Created by HenryCo on 15/03/17.
 */

public class PPHTranslation extends OPallViewInjector<MainActivity> {




	public PPHTranslation() {
		super(R.id.scrollContainer, R.layout.image_option);


	}


	@Override
	protected void onInject(MainActivity context, View view) {

		TextView textView = (TextView) view.findViewById(R.id.iopTextView);
		textView.setText("translate");

		ImageButton imageButton = (ImageButton) view.findViewById(R.id.iopImageButton);
		imageButton.setImageResource(R.drawable.ic_translate_white_24dp);
		imageButton.setClickable(false);

	}


	@Override
	public void onPostInject(MainActivity context, View view) {

		view.setOnClickListener(v -> {
			synchronized (context) {
				context.switchToFragmentOptions();
			}
		});

	}



}
