package net.henryco.opalette.glES.render.graphics.shaders;

/**
 * Created by root on 13/02/17.
 */

import android.content.Context;
import android.opengl.GLES20;

import net.henryco.opalette.glES.render.graphics.camera.OPallCamera2D;
import net.henryco.opalette.utils.Utils;

public abstract class Shader implements OPallShader {


	public final int program;
    public final int COORDS_PER_VERTEX;
    public final int vertexStride;

	private float screenWidth = 0, screenHeight = 0;



    public Shader(Context context, String VERT, String FRAG) {
        this(context, VERT, FRAG, 3);
    }
    public Shader(Context context, String VERT, String FRAG, int coordsPerVertex) {

        String vertex = Utils.getSourceAssetsText(VERT, context);
        String fragment = Utils.getSourceAssetsText(FRAG, context);
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, OPallShader.methods.loadShader(GLES20.GL_VERTEX_SHADER, vertex));
        GLES20.glAttachShader(program, OPallShader.methods.loadShader(GLES20.GL_FRAGMENT_SHADER, fragment));
        GLES20.glLinkProgram(program);
        GLES20.glUseProgram(program);
        COORDS_PER_VERTEX = coordsPerVertex;
        vertexStride = COORDS_PER_VERTEX * 4; //coz float = 4 byte
		outErrorLog();
	}



    protected abstract void render(final int glProgram, OPallCamera2D camera);
	public void setScreenDim(float w, float h) {
		screenWidth = w;
		screenHeight = h;
	};



	@Override
    public void render(OPallCamera2D camera) {

        GLES20.glUseProgram(program);
		OPallShader.methods.applyCameraMatrix(program, camera.getMVPMatrix());
        render(program, camera);
        GLES20.glUseProgram(-1);
    }




    public void outErrorLog() {
		System.out.println(getErrorLog());
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
}
