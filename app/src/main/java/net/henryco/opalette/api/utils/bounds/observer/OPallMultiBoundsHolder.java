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

package net.henryco.opalette.api.utils.bounds.observer;

import net.henryco.opalette.api.utils.bounds.OPallBounds;
import net.henryco.opalette.api.utils.bounds.consumer.BoundsConsumer;

/**
 * Created by HenryCo on 23/02/17.
 */
public interface OPallMultiBoundsHolder <T extends OPallBounds> extends OPallBoundsHolder <T> {

	OPallMultiBoundsHolder bounds(int n, BoundsConsumer<T> processor);
	OPallMultiBoundsHolder updateBounds(int n);

}
