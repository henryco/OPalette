package net.henryco.opalette.api.glES.render.graphics.units.palette;

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


	private static final String VERT_FILE = OPallTexture.DEFAULT_VERT_FILE;
	private static final String[] FRAG_FILE = {

			"precision mediump float;\n" +
					"\n" +
					"varying vec4 v_Position;\n" +
					"varying vec4 v_WorldPos;\n" +
					"varying vec2 v_TexCoordinate;\n" +
					"\n" +
					"uniform sampler2D u_Texture0;\n" +
					"\n" +
					"uniform vec2 u_dimension;\n" +
					"uniform float u_cellSize;\n" +
					"uniform float u_margin;\n" +
					"uniform float u_numb;\n" +
					"\n" +
					"void main() {\n" +
					"\n" +
					"    vec2 pos = gl_FragCoord.xy / u_dimension;\n" +
					"    float n = floor(gl_FragCoord.x / u_cellSize);\n" +
					"\n" +
					"    vec2 margin = vec2(\n" +
					"        (n == 0.) ? u_margin : u_margin * .5,\n" +
					"        (n == u_numb - 1.) ? u_margin : u_margin * .5\n" +
					"    );\n" +
					"\n" +
					"    vec2 st_ed = vec2(n, n + 1.) * u_cellSize;\n" +
					"    vec2 mrgnL = vec2(st_ed.x, st_ed.x + margin.x);\n" +
					"    vec2 mrgnR = vec2(st_ed.y - margin.y, st_ed.y);\n" +
					"\n" +
					"\n" +
					"    if ((gl_FragCoord.x >= mrgnL.x && gl_FragCoord.x <= mrgnL.y) ||\n" +
					"        (gl_FragCoord.x >= mrgnR.x && gl_FragCoord.x <= mrgnR.y))\n" +
					"            gl_FragColor = vec4(0.);\n" +
					"    else if (texture2D(u_Texture0, pos).a != 0.) {\n" +
					"        vec3 color = vec3(0.);\n" +
					"        for (float i = st_ed.x; i < st_ed.y; i += 1.) {\n" +
					"            vec2 point = vec2(i / u_dimension.x, pos.y);\n" +
					"            color += texture2D(u_Texture0, point).rgb;\n" +
					"        }\n" +
					"        gl_FragColor = vec4(color / u_cellSize, 1.);\n" +
					"    } else gl_FragColor = vec4(0.);\n" +
					"\n" +
					"}"
			,
			OPallTexture.DEFAULT_FRAG_FILE
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
	private int numb = 4;

	private FrameBuffer buffer;
	private Texture texture;


	public CellPaletter() {
		this(CellType.Horizontal);
	}

	public CellPaletter(CellType type) {
		this.buffer = OPallFBOCreator.FrameBuffer();
		texture = new Texture(VERT_FILE, FRAG_FILE[type.type]);
	}

	
	public void generate(Texture source, Camera2D camera2D) {


		float cellSize = width / numb;
		float margin = cellSize * margin_pct;

		texture.set(source);
		buffer.beginFBO(() -> texture.render(camera2D, program -> {
			GLESUtils.clear();
			GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_cellSize), cellSize);
			GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_margin), margin);
			GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), width, height);
			GLES20.glUniform1f(GLES20.glGetUniformLocation(program, u_numb), numb);
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
