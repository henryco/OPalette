package net.henryco.opalette.utils.bounds;

import net.henryco.opalette.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.utils.bounds.observer.OPallBoundsHolder;

/**
 * Created by HenryCo on 23/02/17.
 */
public class Bounds3D implements OPallBounds <Bounds3D> {

	// TODO
	@Override
	public Bounds3D apply(BoundsConsumer<Bounds3D> p) {
		return this;
	}

	@Override
	public Bounds3D setHolder(OPallBoundsHolder<Bounds3D> holder) {
		return this;
	}
}
