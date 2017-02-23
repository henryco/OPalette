package net.henryco.opalette.utils.bounds.observer;

import net.henryco.opalette.utils.bounds.OPallBounds;
import net.henryco.opalette.utils.bounds.consumer.BoundsConsumer;

/**
 * Created by HenryCo on 23/02/17.
 */
public interface OPallMultiBoundsHolder <T extends OPallBounds> extends OPallBoundsHolder <T> {

	OPallMultiBoundsHolder bounds(int n, BoundsConsumer<T> processor);
	OPallMultiBoundsHolder updateBounds(int n);

}
