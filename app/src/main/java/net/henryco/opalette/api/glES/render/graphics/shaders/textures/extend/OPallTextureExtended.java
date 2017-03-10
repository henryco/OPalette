package net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend;

import android.content.Context;
import android.graphics.Bitmap;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

/**
 * Created by HenryCo on 10/03/17.
 */

public abstract class OPallTextureExtended extends Texture {


	public OPallTextureExtended(Filter filter, String vert, String frag) {
		super(vert, frag, filter);
	}
	public OPallTextureExtended(String vert, String frag) {
		this(Filter.LINEAR, vert, frag);
	}
	public OPallTextureExtended(Context context, String vert, String frag) {
		this(context, Filter.LINEAR, vert, frag);
	}
	public OPallTextureExtended(Context context, Filter filter, String vert, String frag) {
		super(context, filter, vert, frag);
	}
	public OPallTextureExtended(Bitmap image, Context context, String vert, String frag) {
		this(image, context, Filter.LINEAR, vert, frag);
	}
	public OPallTextureExtended(Bitmap image, Context context, Filter filter, String vert, String frag) {
		super(image, context, filter, vert, frag);
	}
	public OPallTextureExtended(Bitmap image, String vert, String frag) {
		this(image, Filter.LINEAR, vert, frag);
	}
	public OPallTextureExtended(Bitmap image, Filter filter, String vert, String frag) {
		super(image, vert, frag, filter);
	}



	protected abstract void render(int program, Camera2D camera);


	@Override
	public void render(Camera2D camera2D, OPallConsumer<Integer> setter) {
		super.render(camera2D, program -> {
			setter.consume(program);
			render(program, camera2D);
		});
	}
}
