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

package net.henryco.opalette.api.glES.glSurface.renderers.universal;


import android.support.annotation.Nullable;

import net.henryco.opalette.api.utils.requester.OPallRequestListener;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public interface OPallSubProgram<T> extends OPallRequestListener {

	void create(@Nullable GL10 gl, int width, int height, T context);
	void onSurfaceChange(@Nullable GL10 gl, T context, int width, int height);

	final class methods  {
		public static long genID() {
			return (long) (System.nanoTime() * 0.5 - 1);
		}
		public static long genID(Class inst) {
			return inst.getName().hashCode();
		}
	}
}
