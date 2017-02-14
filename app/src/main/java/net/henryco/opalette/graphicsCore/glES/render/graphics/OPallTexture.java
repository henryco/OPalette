package net.henryco.opalette.graphicsCore.glES.render.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.graphicsCore.glES.render.camera.OPallCamera;
import net.henryco.opalette.graphicsCore.glES.render.shaders.pure.UniShader;
import net.henryco.opalette.utils.GLESUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by root on 14/02/17.
 */

public class OPallTexture extends UniShader {

	private static final String DEF_SHADER = "shaders/default/Default";

	public enum filter {
		LINEAR(GLES20.GL_LINEAR),
		NEAREST(GLES20.GL_NEAREST);

		private int type;
		filter(int type) {
			this.type = type;
		}
	}

	private final int textureGL_ID;

	public OPallTexture(Bitmap image, Context context) {
		this(image, context, filter.LINEAR);
	}

	public OPallTexture(Bitmap image, Context context, filter filter) {
		this(image, context, filter, DEF_SHADER + ".vert", DEF_SHADER + ".frag");
	}

	public OPallTexture(Bitmap image, Context context, filter filter, String shaderVert, String shaderFrag) {
		super(context, shaderVert, shaderFrag, 2);
		textureGL_ID = GLESUtils.loadTexture(image, filter.type, filter.type);
	}

	public OPallTexture(Bitmap image, Context context, String shaderVert, String shaderFrag) {
		this(image, context, filter.LINEAR, shaderVert, shaderFrag);
	}


	@Override
	protected void draw(int glProgram, int positionHandle, FloatBuffer vertexBuffer, ShortBuffer orderBuffer, OPallCamera camera) {

	}

	@Override
	protected float[] getVertices() {
		return new float[] {
				-1, 1,
				-1, -1,
				1, -1,
				1, 1
		};
	}

	@Override
	protected short[] getOrder() {
		return new short[]{
				0, 3, 2,
				1, 0, 3 }; // triangle1 + triangle2 = square
	}




}
