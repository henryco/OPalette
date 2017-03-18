package net.henryco.opalette.application.programs.sub.programs.image;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.listener.OPallListener;
import net.henryco.opalette.api.utils.observer.OPallUpdObserver;
import net.henryco.opalette.application.activities.MainActivity;
import net.henryco.opalette.application.programs.sub.programs.AppSubControl;

/**
 * Created by HenryCo on 15/03/17.
 */

public class TranslationControl extends AppSubControl<MainActivity, Void> {



	public TranslationControl(OPallListener<Void> listener, OPallUpdObserver updObserver) {
		super(R.id.scrollContainer, R.layout.image_option_button, listener, updObserver);


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
	public void setOPallListener(OPallListener<Void> listener) {

	}
}
