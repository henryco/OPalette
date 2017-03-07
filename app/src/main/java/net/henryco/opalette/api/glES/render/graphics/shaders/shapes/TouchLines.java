package net.henryco.opalette.api.glES.render.graphics.shaders.shapes;


import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.utils.geom.OPallGeometry;

/**
 * Created by HenryCo on 07/03/17.
 */

public class TouchLines extends OPallShape {


	private static final String VERT_PROGRAM = "";
	private static final String FRAG_PROGRAM = "";


	private float[] linesCoefficients;

	public TouchLines() {
		this(0,0);
	}
	public TouchLines(int w, int h) {
		super(VERT_PROGRAM, FRAG_PROGRAM, 2);
		setScreenDim(w, h);
		reset();
	}


	@Override
	public void render(int program, Camera2D camera) {

	}

	/**
	 * Calculate region bounded by 2 perpendicular lines between 2 fingers position.<br>
	 * (finger1) <----------> (finger2) <br>
	 * @param p1 point where is first finger
	 * @param p2 point where is second finger
	 */
	public TouchLines setPoints(float[] p1, float[] p2) {

		float x1 = p1[0] == p2[0] ? p1[0] + 0.1f : p1[0];
		float y1 = p1[1] == p2[1] ? p1[1] + 0.1f : p1[1];
		float x2 = p2[0];
		float y2 = p2[1];

		float[] c1 = OPallGeometry.lineABC_cmnPerp(x1, y1, x2, y2);
		float[] c2 = OPallGeometry.lineABC_cmnPerp(x2, y2, x1, y1);

		linesCoefficients = new float[] {
				c1[0], c1[1], c1[2], c2[0], c2[1], c2[2]
		};
		return this;
	}

	public float[] getCoefficients() {
		return linesCoefficients;
	}

	public TouchLines reset(float width, float height) {
		return setPoints(
				new float[]{0, 0}, new float[]{0, width}
		);
	}

	public TouchLines reset() {
		return reset(getWidth(), getHeight());
	}


}
