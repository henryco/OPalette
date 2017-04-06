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

package net.henryco.opalette.api.utils.bounds;

import net.henryco.opalette.api.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.api.utils.bounds.observer.OPallBoundsHolder;

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
			return new short[]{0,1,2, 2,3,0};
		}
	}


}
