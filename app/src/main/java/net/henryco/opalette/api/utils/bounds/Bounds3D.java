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
