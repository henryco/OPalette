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

	public synchronized RefreshableTimer startIfWaiting(){
		return startIfWaiting(100);
	}

	public synchronized RefreshableTimer startIfWaiting(long thr_sleep_time) {

		if (wait.get()) {
			wait.set(false);
			new Thread(() -> {
				refresh();
				while (time_remaining_ms - timeLeft() > 0)
					try {
						Thread.sleep(thr_sleep_time);
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
