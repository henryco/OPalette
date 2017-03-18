package net.henryco.opalette.api.utils.views.widgets;

import android.widget.SeekBar;

/**
 * Created by HenryCo on 18/03/17.
 */

public class OPallSeekBar implements SeekBar.OnSeekBarChangeListener {

	public interface progress {
		void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
	}
	public interface start {
		void onStartTrackingTouch(SeekBar seekBar);
	}
	public interface stop {
		void onStopTrackingTouch(SeekBar seekBar);
	}
	private progress p = null;
	private start str = null;
	private stop stp = null;

	public OPallSeekBar onProgress(progress p) {
		this.p = p;
		return this;
	}
	public OPallSeekBar onStart(start str) {
		this.str = str;
		return this;
	}
	public OPallSeekBar onStop(stop stp) {
		this.stp = stp;
		return this;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (p != null) p.onProgressChanged(seekBar, progress, fromUser);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		if (str != null) str.onStartTrackingTouch(seekBar);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (stp != null) stp.onStopTrackingTouch(seekBar);
	}
}
