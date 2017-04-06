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

package net.henryco.opalette.api.glES.glSurface.renderers.solo;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.Shader2D;

/**
 * Created by root on 15/02/17.
 */

public class SoloRenderer extends OPallSoloRenderer {

	@FunctionalInterface
	public interface ShaderMaker {
		Shader2D createShader(Context context);
	}

	private ShaderMaker shaderMaker;

	public SoloRenderer(Context context, Camera2D camera, ShaderMaker shaderMaker) {
		super(context, camera);
		this.shaderMaker = shaderMaker;
	}
	public SoloRenderer(Context context, Camera2D camera) {
		this(context, camera, context1 -> null);
	}
	public SoloRenderer(Context context, ShaderMaker shaderMaker) {
		this(context, null, shaderMaker);
	}
	public SoloRenderer(Context context) {
		this(context, context1 -> null);
	}
	public SoloRenderer(GLSurfaceView.Renderer renderer) {
		super(renderer);
		if (renderer instanceof SoloRenderer) {
			this.shaderMaker = ((SoloRenderer) renderer).shaderMaker;
		}
	}

	@Override
	protected Shader2D createShader(Context context) {
		return shaderMaker.createShader(context);
	}

}
