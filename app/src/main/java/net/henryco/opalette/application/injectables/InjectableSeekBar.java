package net.henryco.opalette.application.injectables;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

public class InjectableSeekBar extends OPallViewInjector<Activity> {

	public static final int TEXT_COLOR_DEFAULT_DARK = R.color.common_google_signin_btn_text_light_default;
	public static final int TEXT_COLOR_BLACK_OVERLAY = R.color.black_overlay;
	public static final int TEXT_COLOR_DEFAULT_LIGHT = R.color.common_google_signin_btn_text_dark_default;
	public static final int TEXT_COLOR_LIGHT_OVERLAY = R.color.common_google_signin_btn_text_dark_disabled;
	public static final int TEXT_COLOR_LIGHT = R.attr.colorButtonNormal;


	public static final int TYPE_NORMAL = R.layout.bar_control_layout;
	public static final int TYPE_SMALL = R.layout.bar_small_control_layout;


	private OnBarCreate onBarCreator;
	public interface OnBarCreate {
		void onSeekBarCreate(SeekBar seekBar);
	}


	private static int default_text_color = TEXT_COLOR_DEFAULT_DARK;
	public static void setDefaultTextColor(int color) {
		default_text_color = color == -1 ? TEXT_COLOR_DEFAULT_DARK : color;
	}

	private static int default_bar_color = Color.DKGRAY;
	public static void  setDefaultBarColor(int color) {
		default_bar_color = color == -1 ? Color.DKGRAY : color;
	}

	private OPallSeekBarListener barListener;
	private String barName;
	private int valueCorrection;
	private int max;
	private int text_color;
	private int value_color;
	private int progress_color;
	private int thumb_color;


	public InjectableSeekBar(View container, int type, String ... name) {
		super(container, type);
		reset(name);
	}
	public InjectableSeekBar(ViewGroup container, int type, String ... name) {
		super(container, type);
		reset(name);
	}
	public InjectableSeekBar(int container, int type, String ... name) {
		super(container, type);
		reset(name);
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


	public void reset(String ... nm) {
		String name = "";
		for (String n : nm) name += n + " ";
		setDefaultPoint(0, 0)
				.setMax(-1).setBarName(name)
				.onBarCreate(seekBar -> {})
				.setTextColor(default_text_color)
				.setBarColor(default_bar_color)
				.setBarListener(new OPallSeekBarListener());
	}


	@Override
	protected void onInject(Activity context, View view) {

		SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		seekBar.setMax(max);
		seekBar.getProgressDrawable().setColorFilter(progress_color, PorterDuff.Mode.SRC_IN);
		seekBar.getThumb().setColorFilter(thumb_color, PorterDuff.Mode.SRC_IN);
		onBarCreator.onSeekBarCreate(seekBar);

		TextView textBar = (TextView) view.findViewById(R.id.barName);
		if (text_color != -1) textBar.setTextColor(text_color);
		textBar.setText(barName);

		TextView valBar = (TextView) view.findViewById(R.id.barValue);
		if (value_color != -1) valBar.setTextColor(value_color);
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

	public int de_norm(float v) {
		return (int) (v * (max + valueCorrection) - valueCorrection);
	}

	public float norm(float v) {
		return (v + valueCorrection) / (max + valueCorrection);
	}


	public InjectableSeekBar setBarColor(int color) {
		return setProgressColor(color).setThumbColor(color);
	}

	public InjectableSeekBar setProgressColor(int color) {
		this.progress_color = color;
		return this;
	}

	public InjectableSeekBar setThumbColor(int color) {
		this.thumb_color = color;
		return this;
	}

	public InjectableSeekBar setTextColor(int color) {
		return setNameColor(color).setValueColor(color);
	}

	public InjectableSeekBar setNameColor(int color) {
		this.text_color = color;
		return this;
	}

	public InjectableSeekBar setValueColor(int color) {
		this.value_color = color;
		return this;
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

	public InjectableSeekBar onBarCreate(OnBarCreate onBarCreator) {
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
