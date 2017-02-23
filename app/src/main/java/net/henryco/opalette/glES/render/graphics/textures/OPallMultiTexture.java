package net.henryco.opalette.glES.render.graphics.textures;

import android.content.Context;

import net.henryco.opalette.glES.render.camera.OPallCamera2D;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by HenryCo on 22/02/17.
 */

public class OPallMultiTexture extends OPallTexture {

	public OPallMultiTexture(Context context) {
		super(null, context);
	}

	public OPallMultiTexture(Context context, filter filter) {
		super(null, context, filter);
	}

	public OPallMultiTexture(Context context, filter filter, String shaderVert, String shaderFrag) {
		super(null, context, filter, shaderVert, shaderFrag);
	}

	public OPallMultiTexture(Context context, String shaderVert, String shaderFrag) {
		super(null, context, shaderVert, shaderFrag);
	}




	@Override
	protected void render(int glProgram, int positionHandle, FloatBuffer vertexBuffer, ShortBuffer orderBuffer, OPallCamera2D camera) {
		super.render(glProgram, positionHandle, vertexBuffer, orderBuffer, camera);
		//TODO
	}

	@Override
	protected void uponRender(int glProgram, int positionHandle, int textureDataHandle, int mTextureUniformHandle, int mTextureCoordinateHandle,
							  FloatBuffer vertexBuffer, ShortBuffer orderBuffer, FloatBuffer texelBuffer, OPallCamera2D camera) {
		super.uponRender(glProgram, positionHandle, textureDataHandle, mTextureUniformHandle,
				mTextureCoordinateHandle, vertexBuffer, orderBuffer, texelBuffer, camera);
		//TODO
	}
}
