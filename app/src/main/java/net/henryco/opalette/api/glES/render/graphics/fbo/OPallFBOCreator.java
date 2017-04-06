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

package net.henryco.opalette.api.glES.render.graphics.fbo;

import android.content.Context;

import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;

/**
 * Created by HenryCo on 02/03/17.
 */

public class OPallFBOCreator {


	public static FrameBuffer FrameBuffer(Context context) {
		return new FrameBuffer().setTargetTexture(new Texture(context));
	}
	public static FrameBuffer FrameBuffer(Context context, int width, int height, boolean depth) {
		return new FrameBuffer(width, height, depth).setTargetTexture(new Texture(context));
	}
	public static FrameBuffer FrameBuffer(Context context, int width, int height, int scrW, int scrH, boolean depth) {
		return new FrameBuffer(width, height, scrW, scrH, depth).setTargetTexture(new Texture(context));
	}


	public static FrameBuffer FrameBuffer() {
		return new FrameBuffer().setTargetTexture(new Texture());
	}
	public static FrameBuffer FrameBuffer(int width, int height, boolean depth) {
		return new FrameBuffer(width, height, depth).setTargetTexture(new Texture());
	}
	public static FrameBuffer FrameBuffer(int width, int height, int scrW, int scrH, boolean depth) {
		return new FrameBuffer(width, height, scrW, scrH, depth).setTargetTexture(new Texture());
	}
}
