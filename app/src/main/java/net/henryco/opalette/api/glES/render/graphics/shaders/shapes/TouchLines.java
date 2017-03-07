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

	public TouchLines setPoints(float[] p1, float[] p2) {

		float[] point11 = {p1[0], p1[1]};
		float[] point12 = {p1[2], p1[3]};
		float[] point21 = {p2[0], p2[1]};
		float[] point22 = {p2[2], p2[3]};

		float a1 = OPallGeometry.lineAx(point11, point12);
		float b1 = OPallGeometry.lineBy(point11, point12);
		float c1 = OPallGeometry.lineC(point11, point12);

		float a2 = OPallGeometry.lineAx(point21, point22);
		float b2 = OPallGeometry.lineBy(point21, point22);
		float c2 = OPallGeometry.lineC(point21, point22);

		linesCoefficients = new float[]{a1, b1, c1, a2, b2, c2};
		return this;
	}

	public float[] getCoefficients() {
		return linesCoefficients;
	}

	public TouchLines reset(float width, float height) {
		return setPoints(
				new float[]{0, 0, width, 0},
				new float[]{0, height, width, height}
		);
	}

	public TouchLines reset() {
		return reset(getWidth(), getHeight());
	}


}
