package net.henryco.opalette.api.glES.glSurface.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import net.henryco.opalette.api.utils.lambda.functions.OPallFunction;

/**
 * Created by HenryCo on 25/03/17.
 */

public class OPallSurfaceTouchListener implements OPallSurfaceView.OnTouchEventListener {


	private ScaleGestureDetector scaleDetector;
	private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;
	private boolean scaleMatters = true;
	private final float[] last = {0,0};


	public interface OnActionMove {
		void onActionMove(final float dx, final float dy);
	}
	public interface OnActionDown {
		void onActionDown(final float x, final float u);
	}

	private OnActionDown onActionDown;
	private OnActionMove onActionMove;
	private Runnable onActionUp, onActionPointerUp;
	private OPallFunction<Boolean, ScaleGestureDetector> onScale;


	public OPallSurfaceTouchListener(Context context) {
		setScaleDetector(context);
	}


	private boolean scaled = false;
	private OPallSurfaceTouchListener setScaleDetector(Context context) {
		this.scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				if (onScale != null) {
					scaled = true;
					return onScale.apply(detector);
				}	return false;
			}
		});
		return this;
	}



	@Override
	public void onTouchEvent(MotionEvent ev) {
		// Let the ScaleGestureDetector inspect all events.
		scaled = false;
		if (scaleDetector != null) scaleDetector.onTouchEvent(ev);

		final int action = MotionEventCompat.getActionMasked(ev);

		if (!scaled || !scaleMatters) {
			switch (action) {
				case MotionEvent.ACTION_DOWN: {
					final int pointerIndex = MotionEventCompat.getActionIndex(ev);
					final float x = MotionEventCompat.getX(ev, pointerIndex);
					final float y = MotionEventCompat.getY(ev, pointerIndex);

					if (onActionDown != null) onActionDown.onActionDown(x, y);

					// Remember where we started (for dragging)
					last[0] = x;
					last[1] = y;
					// Save the ID of this pointer (for dragging)
					mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
					break;
				}

				case MotionEvent.ACTION_MOVE: {
					// Find the index of the active pointer and fetch its position
					final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);

					final float x = MotionEventCompat.getX(ev, pointerIndex);
					final float y = MotionEventCompat.getY(ev, pointerIndex);

					// Calculate the distance moved
					final float dx = x - last[0];
					final float dy = y - last[1];

					if (onActionMove != null) onActionMove.onActionMove(dx, dy);

					// Remember this touch position for the next move event
					last[0] = x;
					last[1] = y;

					break;
				}

				case MotionEvent.ACTION_UP: {
					mActivePointerId = MotionEvent.INVALID_POINTER_ID;
					if (onActionUp != null) onActionUp.run();
					break;
				}

				case MotionEvent.ACTION_CANCEL: {
					mActivePointerId = MotionEvent.INVALID_POINTER_ID;
					break;
				}

				case MotionEvent.ACTION_POINTER_UP: {

					final int pointerIndex = MotionEventCompat.getActionIndex(ev);
					final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

					if (pointerId == mActivePointerId) {
						// This was our active pointer going up. Choose a new
						// active pointer and adjust accordingly.
						final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
						last[0] = MotionEventCompat.getX(ev, newPointerIndex);
						last[1] = MotionEventCompat.getY(ev, newPointerIndex);
						mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
					}
					if (onActionPointerUp != null) onActionPointerUp.run();
					break;
				}
			}
		}
	}



	public OPallSurfaceTouchListener setConcurrentScaling(boolean concurrentScaling) {
		scaleMatters = !concurrentScaling;
		return this;
	}

	public OPallSurfaceTouchListener setOnScale(OPallFunction<Boolean, ScaleGestureDetector> onScale) {
		this.onScale = onScale;
		return this;
	}

	public OPallSurfaceTouchListener setOnActionMove(OnActionMove onActionMove) {
		this.onActionMove = onActionMove;
		return this;
	}

	public OPallSurfaceTouchListener setOnActionDown(OnActionDown onActionDown) {
		this.onActionDown = onActionDown;
		return this;
	}

	public OPallSurfaceTouchListener setOnActionUp(Runnable onActionUp) {
		this.onActionUp = onActionUp;
		return this;
	}

	public OPallSurfaceTouchListener setOnActionPointerUp(Runnable onActionPointerUp) {
		this.onActionPointerUp = onActionPointerUp;
		return this;
	}



}
