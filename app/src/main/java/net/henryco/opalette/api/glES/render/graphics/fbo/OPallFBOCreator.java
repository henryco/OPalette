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
