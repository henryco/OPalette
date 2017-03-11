package net.henryco.opalette.api.glES.render.graphics.shaders;

/**
 * Created by root on 13/02/17.
 */

import android.content.Context;
import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.OPallUtils;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

public abstract class Shader implements OPallShader {


	public final int program;
    public final int COORDS_PER_VERTEX;
    public final int vertexStride;

	private float screenWidth = 0, screenHeight = 0;
	protected boolean cameraForceUpdate;



    public Shader(Context context, String VERT, String FRAG) {
        this(context, VERT, FRAG, 3);
    }
    public Shader(Context context, String VERT, String FRAG, int coordsPerVertex) {
		this(OPallUtils.getSourceAssetsText(VERT, context), OPallUtils.getSourceAssetsText(FRAG, context), coordsPerVertex);
	}
	public Shader(String vertex, String fragment) {
		this(vertex, fragment, 3);
	}
	public Shader(String vertex, String fragment, int coordsPerVertex) {

		program = GLES20.glCreateProgram();
		GLES20.glAttachShader(program, OPallShader.methods.loadShader(GLES20.GL_VERTEX_SHADER, vertex));
		GLES20.glAttachShader(program, OPallShader.methods.loadShader(GLES20.GL_FRAGMENT_SHADER, fragment));
		GLES20.glLinkProgram(program);
		GLES20.glUseProgram(program);
		COORDS_PER_VERTEX = coordsPerVertex;
		vertexStride = COORDS_PER_VERTEX * 4; //coz float = 4 byte
		setCameraForceUpdate(false);
		outErrorLog();
	}


    protected abstract void render(final int glProgram, Camera2D camera, OPallConsumer<Integer> setter);


	@Override
	public void setScreenDim(float w, float h) {
		screenWidth = w;
		screenHeight = h;
	}


	@Override
	public void render(Camera2D camera2D, OPallConsumer<Integer> setter) {


		GLESUtils.glUseProgram(program, () -> {
			if (cameraForceUpdate) camera2D.update();
			OPallShader.methods.applyCameraMatrix(program, camera2D.getMVPMatrix());
			render(program, camera2D, setter);
		});
	}

	@Override
    public void render(Camera2D camera) {
		render(camera, integer -> {});
    }




    public void outErrorLog() {
		System.out.println("GLSL SHADER: "+getErrorLog());
    }
	public String getErrorLog() {
		return GLES20.glGetShaderInfoLog(program);
	}


	protected int getPositionHandle() {
		return GLES20.glGetAttribLocation(program, OPallShader.a_Position);
	}
	protected int getTextureUniformHandle(int n) {
		return GLES20.glGetUniformLocation(program, OPallShader.methods.defTextureN(n));
	}
	protected int getTextureCoordinateHandle() {
		return GLES20.glGetAttribLocation(program, OPallShader.a_TexCoordinate);
	}


	protected float getScreenWidth() {
		return screenWidth;
	}
	protected float getScreenHeight() {
		return screenHeight;
	}

	public Shader setCameraForceUpdate(boolean b) {
		cameraForceUpdate = b;
		return this;
	}
}
