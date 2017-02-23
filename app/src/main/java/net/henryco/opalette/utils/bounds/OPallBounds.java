package net.henryco.opalette.utils.bounds;

import net.henryco.opalette.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.utils.bounds.observer.OPallBoundsHolder;

/**
 * Created by HenryCo on 23/02/17.
 */
public interface OPallBounds <T extends OPallBounds> {

	T apply(BoundsConsumer<T> p);
	T setHolder(OPallBoundsHolder<T> holder);

	final class vertices {
		public static float[] FLAT_SQUARE_2D(){
			return new float[]{-1,1, -1,-1, 1,-1, 1,1};
		}

	}

	final class order {
		public static short[] FLAT_SQUARE_2D(){
			return new short[]{0,1,2, 0,2,3};
		}
	}


}
