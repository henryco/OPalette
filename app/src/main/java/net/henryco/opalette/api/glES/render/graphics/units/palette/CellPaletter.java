package net.henryco.opalette.api.glES.render.graphics.units.palette;

import android.content.Context;
import android.opengl.GLES20;

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


	private static final String VERT_FILE = OPallTexture.DEF_SHADER+".vert";
	private static final String[] FRAG_FILE = {
			OPallTexture.FRAG_DIR+"/CellPaletteHorizontal.frag",
			OPallTexture.FRAG_DIR+"/CellPaletteVertical.frag"
	};

	private static final String u_cellSize = "u_cellSize";
	private static final String u_margin = "u_margin";
	private static final String u_dimension = "u_dimension";
	private static final String u_numb = "u_numb";


	public enum CellType {

		Horizontal(0),
		Vertical(1);

		public int type;
		CellType(int type) {
			this.type = type;
		}
	}



	private float margin_pct = 0.1f;
	private float width = 0;
	private float height = 0;
	private int numb = 0;

	private FrameBuffer buffer;
	private Texture texture;


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

		float margin = width * margin_pct;
		float cellSize = (width - (margin * (numb + 1))) / numb;

		texture.setBitmap(source);
		buffer.beginFBO(() -> texture.render(camera2D, program -> {
			GLESUtils.clear();
			GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_cellSize), cellSize);
			GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_margin), margin);
			GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), width, height);
			GLES20.glUniform1i(GLES20.glGetUniformLocation(program, u_numb), numb);
		}));
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
