package net.henryco.opalette.application.injectables;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.api.utils.views.widgets.OPallSeekBarListener;

/**
 * Created by HenryCo on 18/03/17.
 */

public class InjectableSeekBar extends OPallViewInjector {

	private OnBarCreate onBarCreator;
	public interface OnBarCreate {
		void onSeekBarCreate(SeekBar seekBar);
	}


	private OPallSeekBarListener barListener;
	private String barName;
	private int startValue;


	public InjectableSeekBar(View container) {
		super(container, R.layout.bar_control_layout);
		init();
	}
	public InjectableSeekBar(ViewGroup container) {
		super(container, R.layout.bar_control_layout);
		init();
	}
	public InjectableSeekBar(int container) {
		super(container, R.layout.bar_control_layout);
		init();
	}

	private void init() {
		setStartValue(0)
				.setBarName("")
				.setOnBarCreate(seekBar -> {})
				.setBarListener(new OPallSeekBarListener());
	}


	@Override
	protected void onInject(Context context, View view) {

		TextView valBar = (TextView) view.findViewById(R.id.barValue);
		TextView textBar = (TextView) view.findViewById(R.id.barName);
		textBar.setText(barName);

		SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		onBarCreator.onSeekBarCreate(seekBar);

		int valueCorrection = startValue - seekBar.getProgress();

		seekBar.setOnSeekBarChangeListener(new OPallSeekBarListener()
				.onStart(barListener)
				.onStop(barListener)
				.onProgress((sBar, progress, fromUser) -> {
					barListener.onProgressChanged(sBar, progress, fromUser);
					String barValue = Integer.toString(2 * (progress + valueCorrection));
					valBar.setText(barValue);
				})
		);


	}


	public InjectableSeekBar setBarListener(OPallSeekBarListener listener) {
		this.barListener = listener;
		return this;
	}

	public InjectableSeekBar setStartValue(int startValue) {
		this.startValue = startValue;
		return this;
	}

	public InjectableSeekBar setOnBarCreate(OnBarCreate onBarCreator) {
		this.onBarCreator = onBarCreator;
		return this;
	}

	public InjectableSeekBar setBarName(String barName) {
		this.barName = barName;
		return this;
	}
}
