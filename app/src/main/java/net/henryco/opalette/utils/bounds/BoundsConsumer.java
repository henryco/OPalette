package net.henryco.opalette.utils.bounds;

/**
 * Created by HenryCo on 23/02/17.
 */

@FunctionalInterface
public interface BoundsConsumer <T extends OPallBounds> {
	void boundApply(T b);
}
