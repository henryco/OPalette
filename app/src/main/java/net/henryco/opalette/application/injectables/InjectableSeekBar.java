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


	public static final int TYPE_NORMAL = R.layout.bar_control_layout;
	public static final int TYPE_SMALL = R.layout.bar_small_control_layout;


	private OnBarCreate onBarCreator;
	public interface OnBarCreate {
		void onSeekBarCreate(SeekBar seekBar);
	}


	private OPallSeekBarListener barListener;
	private String barName;
	private int valueCorrection;
	private int max;


	public InjectableSeekBar(View container, int type, String ... name) {
		super(container, type);
		init(name);
	}
	public InjectableSeekBar(ViewGroup container, int type, String ... name) {
		super(container, type);
		init(name);
	}
	public InjectableSeekBar(int container, int type, String ... name) {
		super(container, type);
		init(name);
	}
	public InjectableSeekBar(View container, String ... name) {
		this(container, TYPE_NORMAL, name);
	}
	public InjectableSeekBar(ViewGroup container, String ... name) {
		this(container, TYPE_NORMAL, name);
	}
	public InjectableSeekBar(int container, String ... name) {
		this(container, TYPE_NORMAL, name);
	}


	private void init(String ... nm) {
		String name = "";
		for (String n : nm) name += n + " ";
		setDefaultPoint(0, 0)
				.setMax(-1).setBarName(name)
				.setOnBarCreate(seekBar -> {})
				.setBarListener(new OPallSeekBarListener());
	}


	@Override
	protected void onInject(Context context, View view) {

		SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		seekBar.setMax(max);
		onBarCreator.onSeekBarCreate(seekBar);

		TextView textBar = (TextView) view.findViewById(R.id.barName);
		textBar.setText(barName);

		TextView valBar = (TextView) view.findViewById(R.id.barValue);
		valBar.setText(calcBarValue(seekBar.getMax(), valueCorrection, seekBar.getProgress()));

		seekBar.setOnSeekBarChangeListener(new OPallSeekBarListener()
				.onStart(barListener)
				.onStop(barListener)
				.onProgress((sBar, progress, fromUser) -> {
					barListener.onProgressChanged(sBar, progress, fromUser);
					valBar.setText(calcBarValue(seekBar.getMax(), valueCorrection, progress));
				})
		);

	}



	public InjectableSeekBar setMax(int max) {
		this.max = max == -1 ? 100 : max;
		return this;
	}

	public InjectableSeekBar setBarListener(OPallSeekBarListener listener) {
		this.barListener = listener;
		return this;
	}

	public InjectableSeekBar setDefaultPoint(int startValue, int startBarPoint) {
		this.valueCorrection = startValue - startBarPoint;
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

	private static String calcBarValue(float barMax, int corr, int value) {
		return Integer.toString((int)((barMax / (barMax - Math.abs((float) corr))) * (value + corr)));
	}
}
