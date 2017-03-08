package net.henryco.opalette.api.glES.render.graphics.units.palette;

import android.content.Context;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.OPallTexture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 08/03/17.
 */

public class CellPaletter implements OPallRenderable {


	public enum CellType {

		Horizontal(0),
		Vertical(1);

		public int type;
		CellType(int type) {
			this.type = type;
		}
	}

	public static final String VERT_FILE = OPallTexture.DEF_SHADER+".vert";
	public static final String[] FRAG_FILE = {
			OPallTexture.FRAG_DIR+"/CellPaletteHorizontal.frag",
			OPallTexture.FRAG_DIR+"/CellPaletteVertical.frag"
	};

	public float margin_pct = 0.1f;
	public float width = 0;
	public float height = 0;
	public int numb = 0;

	public FrameBuffer buffer;
	public Texture texture;


	public CellPaletter(Context context) {
		this(context, CellType.Horizontal);
	}

	public CellPaletter(Context context, CellType type) {
		this.buffer = OPallFBOCreator.FrameBuffer(context);
		texture = new Texture(context, VERT_FILE, FRAG_FILE[type.type]);
	}


	/**
	 * TODO
	 */
	public void generate(OPallTexture source, Camera2D camera2D) {


		buffer.beginFBO(() -> {
			GLESUtils.clear();
			source.render(camera2D);
		});

//		texture.setBitmap(source);
//		buffer.beginFBO(() -> texture.render(camera2D, program -> {
//			GLESUtils.clear();
//		}));
	}



	public CellPaletter setCellNumb(int n) {
		this.numb = n;
		return this;
	}

	public CellPaletter setMargin_pct(float m) {
		this.margin_pct = m;
		return this;
	}





	@Override
	public void render(Camera2D camera) {
		buffer.render(camera);
	}


	@Override
	public void setScreenDim(float w, float h) {
		this.width = w;
		this.height = h;
		buffer.createFBO(getWidth(), getHeight(), false);
		texture.setScreenDim(w, h);
	}

	@Override
	public int getWidth() {
		return (int) width;
	}

	@Override
	public int getHeight() {
		return (int) height;
	}
}
