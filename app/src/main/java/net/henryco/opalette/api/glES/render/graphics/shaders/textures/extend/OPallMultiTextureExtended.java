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

package net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend;

import android.content.Context;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.MultiTexture;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

/**
 * Created by HenryCo on 31/03/17.
 */

public abstract class OPallMultiTextureExtended extends MultiTexture {


	public OPallMultiTextureExtended(Context context) {
		super(context);
	}
	public OPallMultiTextureExtended(Context context, int texNumb) {
		super(context, texNumb);
	}
	public OPallMultiTextureExtended(Context context, String VERT, String FRAG) {
		super(context, VERT, FRAG);
	}
	public OPallMultiTextureExtended(Context context, String VERT, String FRAG, int texNumb) {
		super(context, VERT, FRAG, texNumb);
	}
	public OPallMultiTextureExtended(String VERT, String FRAG, int texNumb) {
		super(VERT, FRAG, texNumb);
	}
	public OPallMultiTextureExtended(String VERT, String FRAG) {
		super(VERT, FRAG);
	}
	public OPallMultiTextureExtended(int texNumb) {
		super(texNumb);
	}
	public OPallMultiTextureExtended() {
		super();
	}


	protected abstract void render(int program, Camera2D camera);


	@Override
	public synchronized void render(Camera2D camera2D, OPallConsumer<Integer> setter) {
		super.render(camera2D, program -> {
			setter.consume(program);
			render(program, camera2D);
		});
	}
}
