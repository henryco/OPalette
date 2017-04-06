/*
 *   /*
 *    * Copyright (C) Henryk Timur Domagalski
 *    *
 *    * Licensed under the Apache License, Version 2.0 (the "License");
 *    * you may not use this file except in compliance with the License.
 *    * You may obtain a copy of the License at
 *    *
 *    *      http://www.apache.org/licenses/LICENSE-2.0
 *    *
 *    * Unless required by applicable law or agreed to in writing, software
 *    * distributed under the License is distributed on an "AS IS" BASIS,
 *    * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    * See the License for the specific language governing permissions and
 *    * limitations under the License.
 *
 */

package net.henryco.opalette.api.utils.views.widgets;

import android.widget.SeekBar;

/**
 * Created by HenryCo on 18/03/17.
 */

public class OPallSeekBarListener implements SeekBar.OnSeekBarChangeListener {



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

	public OPallSeekBarListener onProgress(progress p) {
		this.p = p;
		return this;
	}
	public OPallSeekBarListener
	onProgress(OPallSeekBarListener bar) {
		return onProgress(bar.p);
	}
	public OPallSeekBarListener onStart(start str) {
		this.str = str;
		return this;
	}
	public OPallSeekBarListener onStart(OPallSeekBarListener bar) {
		return onStart(bar.str);
	}
	public OPallSeekBarListener onStop(stop stp) {
		this.stp = stp;
		return this;
	}
	public OPallSeekBarListener onStop(OPallSeekBarListener bar) {
		return onStop(bar.stp);
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
