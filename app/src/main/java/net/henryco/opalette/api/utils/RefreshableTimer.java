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

package net.henryco.opalette.api.utils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by HenryCo on 24/03/17.
 */

public class RefreshableTimer {

	private Runnable timeOutAction;
	private long timer_ms;
	private long time_remaining_ms;
	private long time_left_ms;
	private AtomicBoolean wait;

	public RefreshableTimer(long timer_ms, Runnable timeOutAction) {
		this.timer_ms = timer_ms;
		this.timeOutAction = timeOutAction;
		wait = new AtomicBoolean(true);
	}



	public synchronized RefreshableTimer startIfWaiting() {

		if (wait.get()) {
			wait.set(false);
			new Thread(() -> {
				refresh();
				while (time_remaining_ms - timeLeft() > 0)
					try {
						Thread.sleep(time_remaining_ms - timeLeft());
					} catch (InterruptedException ignored) {}
				timeOutAction.run();
				forceStop();
			}).start();
		}
		return this;
	}

	private synchronized long timeLeft() {
		return System.currentTimeMillis() - time_left_ms;
	}

	public synchronized RefreshableTimer refresh() {
		time_remaining_ms = timer_ms;
		time_left_ms = System.currentTimeMillis();
		return this;
	}

	public synchronized RefreshableTimer forceStop() {
		time_remaining_ms = -1;
		wait.set(true);
		return this;
	}
}
