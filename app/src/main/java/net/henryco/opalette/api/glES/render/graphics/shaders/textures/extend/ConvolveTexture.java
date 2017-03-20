package net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend;

import android.content.Context;
import android.graphics.Bitmap;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.OPallTexture;

/**
 * Created by HenryCo on 20/03/17.
 */

public class ConvolveTexture extends OPallTextureExtended {


	private float[] original_filter_matrix = {};
	private float[] work_filter_matrix = {};
	private float center_scale = 1;
	private int matrix_size = 0;

	private boolean enable = false;

	public ConvolveTexture(Context context) {
		this(context, Filter.LINEAR);
	}
	public ConvolveTexture(Context context, Filter filter) {
		super(context, filter, OPallTexture.DEF_SHADER+".vert", FRAG_DIR);
	}
	public ConvolveTexture(Bitmap image, Context context) {
		this(image, context, Filter.LINEAR);
	}
	public ConvolveTexture(Bitmap image, Context context, Filter filter) {
		super(image, context, filter, OPallTexture.DEF_SHADER+".vert", FRAG_DIR);
	}
	public ConvolveTexture() {
		super(OPallTexture.DEFAULT_VERT_FILE, FRAG_FILE);
	}
	public ConvolveTexture(Bitmap image) {
		this(image, Filter.LINEAR);
	}
	public ConvolveTexture(Bitmap image, Filter filter) {
		super(image, filter, OPallTexture.DEFAULT_VERT_FILE, FRAG_FILE);
	}


	@Override
	protected void render(int program, Camera2D camera) {
		if (isEnable()) {
//			TODO
		} else {
//			TODO
		}
	}




	public ConvolveTexture setFilterMatrix(float ... matrix) {

		if (matrix.length == 0 || (matrix.length == 1 && matrix[0] == -1))
			setEnable(false);
		if (Math.sqrt(matrix.length) % 2 == 0)
			throw new RuntimeException(getClass().getName()
					+ ": Filter matrix dimension must be 3x3, 5x5, 7x7, 9x9 ...");
		original_filter_matrix = matrix;
		matrix_size = original_filter_matrix.length;
		return setCenterEffect(center_scale);
	}

	public ConvolveTexture setCenterEffect(float s) {

		center_scale = s;
		float[] matrix = new float[matrix_size];
		int dim = (int) Math.sqrt(matrix_size);
		int center = (int) (0.5f * (dim - 1f));
		System.arraycopy(original_filter_matrix, 0, matrix, 0, matrix_size);
		matrix[(center * dim) + center] *= center_scale;
		work_filter_matrix = normalize(matrix);
		return this;
	}

	private static float[] normalize(float[] matrix) {

		float sum = 0;
		for (float v : matrix) sum += v;
		float n = sum / matrix.length;
		if (n == 0) return matrix;
		for (int i = 0; i < matrix.length; i++)
			matrix[i] /= n;
		return matrix;
	}


	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	private static final String FRAG_DIR = OPallTexture.FRAG_DIR+"/EditableDefault.frag";
	private static final String FRAG_FILE = "";

}
