package net.henryco.opalette.api.glES.render.graphics.fbo;

import android.graphics.Bitmap;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.OPallTexture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;

/**
 * Created by HenryCo on 25/02/17.
 */

public class FrameBuffer implements OPallFBO {

	public static boolean debug = false;

	private int[] frameBHandle = null;
	private int[] texBHandle = null;
	private int[] depthBHandle = null;

	private int width = 0, height = 0;
	private int scrW = 0, scrH = 0;
	private Texture texture = null;
	private boolean flip = true;

	private final long buffer_id;

	public FrameBuffer(){
		buffer_id = hashCode();
	}
	public FrameBuffer(int width, int height, boolean depth) {
		this(width, height, width, height, depth);
	}
	public FrameBuffer(int width, int height, int screenW, int screenH, boolean depth) {
		this();
		createFBO(width, height, screenW, screenH, depth);
	}


	public FrameBuffer createFBO(int w, int h, boolean depth) {
		return createFBO(w, h, w, h, depth);
	}

	@Override
	public FrameBuffer createFBO(int w, int h, int screenW, int screenH, boolean depth) {
		OPallFBO.methods.wipe(frameBHandle, texBHandle, depthBHandle);
		frameBHandle = OPallFBO.methods.genGeneralBuff();
		texBHandle = OPallFBO.methods.genTextureBuff(w, h);
		if (depth) depthBHandle = OPallFBO.methods.genDepthBuff(w, h);
		OPallFBO.methods.finishAndCheckStat(debug, buffer_id);
		setScreenDim(screenW, screenH);
		width = w;
		height = h;
		return this;
	}

	@Override
	public void render(Camera2D camera){
		if (texture != null) texture.render(camera);
	}

	@Override
	public FrameBuffer beginFBO() {
		OPallFBO.methods.beginFBO(frameBHandle[0]);
		return this;
	}

	@Override
	public FrameBuffer beginFBO(Runnable runnable) {
		beginFBO();
		runnable.run();
		endFBO();
		return this;
	}

	@Override
	public FrameBuffer endFBO() {

		OPallFBO.methods.endFBO(width, height);
		if (texture != null) {
			texture.setScreenDim(scrW, scrH);
			texture.setTextureDataHandle(getTextureBufferHandle());
			texture.setSize(width, height);
			setFlip(flip);
		}
		return this;
	}

	@Override
	public OPallFBO setFlip(boolean f) {
		this.flip = f;
		if (texture != null) texture.setFlip(false, f);
		return this;
	}

	@Override
	public FrameBuffer setTargetTexture(OPallTexture targetTexture) {
		if (!(targetTexture instanceof Texture))
			throw new RuntimeException("<targetTexture> must be the <Texture> instance!");
		this.texture = (Texture) targetTexture;
		setFlip(flip);
		return this;
	}


	@Override
	public void setScreenDim(float w, float h) {
		this.scrW = (int) w;
		this.scrH = (int) h;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Bitmap getBitmap() {
		return Bitmap.createBitmap(OPallFBO.methods.getPixelData(frameBHandle[0], width, height),
				width, height, Bitmap.Config.ARGB_8888);
	}

	@Override
	public Texture getTexture() {
		return texture;
	}

	public int getScreenHeight() {
		return scrH;
	}

	public int getScreenWidth() {
		return scrW;
	}

	public int getFrameBufferHandle() {
		return frameBHandle[0];
	}

	public int getTextureBufferHandle() {
		return texBHandle[0];
	}

	public int getDepthBufferHandle() {
		return depthBHandle[0];
	}


}
