package net.henryco.opalette.application.programs.sub.programs.gradient;

import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.MultiTexture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.OPallMultiTexture;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.MainActivity;
import net.henryco.opalette.application.programs.sub.AppSubProgram;
import net.henryco.opalette.application.programs.sub.AppSubProtocol;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public class GradientBarProgram implements AppSubProgram<MainActivity>, AppSubProtocol {

	private long id = methods.genID(GradientBarProgram.class);
	private ProxyRenderData<Texture> proxyRenderData = new ProxyRenderData<>();


	private MultiTexture multiTexture;
	private FrameBuffer barGradientBuffer;
	private FrameBuffer barSrcBuffer;


	private float[] externalLineCoeffs = {};
	private float externalBarHeight = 0;
	private int buffer_quantum = 5;


	private OPallRequester feedBackListener;

	@Override
	public void setFeedBackListener(OPallRequester feedBackListener) {
		this.feedBackListener = feedBackListener;
	}

	@Override
	public void acceptRequest(Request request) {
		request.openRequest(set_buffer_quantum, () -> buffer_quantum = request.getData());
		request.openRequest(send_back_bar_height, () -> externalBarHeight = request.getData());
		request.openRequest(send_line_coeffs, () -> externalLineCoeffs = request.getData());
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public void create(GL10 gl, int width, int height, MainActivity context) {

		barGradientBuffer = OPallFBOCreator.FrameBuffer();
		barSrcBuffer = OPallFBOCreator.FrameBuffer();
		multiTexture = new MultiTexture(context, VERT_FILE, FRAG_FILE, 2);
	}

	@Override
	public void onSurfaceChange(GL10 gl, MainActivity context, int width, int height) {

		barGradientBuffer.createFBO(width, height, false);
		barSrcBuffer.createFBO(width, buffer_quantum, width, height, false).beginFBO(GLESUtils::clear);
		multiTexture.setScreenDim(width, height);
	}

	@Override
	public void render(GL10 gl10, MainActivity context, Camera2D camera, int w, int h) {

		multiTexture.set(0, getRenderData());
		multiTexture.set(1, barSrcBuffer.getTexture());
		multiTexture.setFocusOn(1);

		//CREATE GRADIENT BAR
		barGradientBuffer.beginFBO(() -> multiTexture.render(camera, program -> {
			GLESUtils.clear();
			GLES20.glUniform2f(GLES20.glGetUniformLocation(program, u_dimension), w, h - externalBarHeight);
			GLES20.glUniform3fv(GLES20.glGetUniformLocation(program, u_line), 2, externalLineCoeffs, 0);
		}));
		setRenderData(barGradientBuffer.getTexture());
	}


	@Override
	public void setRenderData(OPallRenderable data) {
		proxyRenderData.setRenderData((Texture) data);
	}

	@Override
	public Texture getRenderData() {
		return proxyRenderData.getRenderData();
	}


	public static final String u_dimension = "u_dimension";
	public static final String u_line = "u_line";
	public static final String VERT_FILE = OPallMultiTexture.DEF_SHADER+".vert";
	public static final String FRAG_FILE = OPallMultiTexture.FRAG_DIR+"/StdPaletteHorizontal.frag";
	public static final String FRAG_PROGRAM =
			"#version 100\n" +
					"precision mediump float;\n" +
					"\n" +
					"varying vec4 v_Position;\n" +
					"varying vec4 v_WorldPos;\n" +
					"varying vec2 v_TexCoordinate[5];\n" +
					"\n" +
					"uniform sampler2D u_Texture0;\n" +
					"uniform sampler2D u_Texture1;\n" +
					"uniform sampler2D u_Texture2;\n" +
					"uniform sampler2D u_Texture3;\n" +
					"uniform sampler2D u_Texture4;\n" +
					"\n" +
					"uniform vec2 u_dimension;\n" +
					"uniform int u_texNumb;\n" +
					"uniform vec3 u_line[2]; // Ax + By + C = 0\n" +
					"\n" +
					"void main() {\n" +
					"\n" +
					"    float trueHeight = 0.0;\n" +
					"    vec3 p_col = vec3(0.0);\n" +
					"\n" +
					"    for (float y = 0.0; y < u_dimension.y; y += 1.0) {\n" +
					"\n" +
					"        vec2 pointNormed = vec2(v_TexCoordinate[1].s, y / u_dimension.y);\n" +
					"        vec2 point = vec2(pointNormed.x * u_dimension.x, u_dimension.y * (1. - pointNormed.y));\n" +
					"        vec4 pointColor = texture2D(u_Texture0, pointNormed).rgba;\n" +
					"\n" +
					"        if (pointColor.a != 0.0) {\n" +
					"\n" +
					"            float py1 = (-1.) * ((u_line[0].x * point.x) + u_line[0].z) / u_line[0].y;\n" +
					"            float py2 = (-1.) * ((u_line[1].x * point.x) + u_line[1].z) / u_line[1].y;\n" +
					"\n" +
					"            if ((point.y > py1 && point.y < py2) || (point.y > py2 && point.y < py1)) {\n" +
					"                p_col += pointColor.rgb;\n" +
					"                trueHeight += 1.0;\n" +
					"            }\n" +
					"        }\n" +
					"    }\n" +
					"\n" +
					"    vec3 color = p_col / max(trueHeight, 1.0);\n" +
					"    gl_FragColor = vec4(color, 1.0);\n" +
					"}";

}
