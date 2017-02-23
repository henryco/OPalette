package net.henryco.opalette.utils.bounds.consumer;

import net.henryco.opalette.utils.bounds.OPallBounds;

/**
 * Created by HenryCo on 23/02/17.
 */

@FunctionalInterface
public interface BoundsConsumer <T extends OPallBounds> {
	void boundApply(T b);
}
