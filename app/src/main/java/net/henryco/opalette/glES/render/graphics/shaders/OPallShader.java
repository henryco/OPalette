package net.henryco.opalette.glES.render.graphics.shaders;

/**
 * Created by root on 13/02/17.
 */

import android.content.Context;
import android.opengl.GLES20;

import net.henryco.opalette.glES.render.camera.OPallCamera2D;
import net.henryco.opalette.utils.GLESUtils;
import net.henryco.opalette.utils.Utils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public abstract class OPallShader {

    public final int program;
    public final ShortBuffer orderBuffer;

    public final int COORDS_PER_VERTEX;
    public final int vertexCount;
    public final int vertexStride;

	private FloatBuffer vertexBuffer;
	private float scrW = 0, scrH = 0;

    /*  Requested in *.vert file:
	 *
     *      attribute vec4 a_Position;
     *      attribute vec2 a_TexCoordinate;
     *      varying vec4 v_Position;
     *      varying vec4 v_WorldPos;
     *      varying vec2 v_TexCoordinate;
     *      uniform mat4 u_MVPMatrix;
     *
     *      void main() {
     *          ...
     *      }
     */

    public OPallShader(Context context, String VERT, String FRAG) {
        this(context, VERT, FRAG, 3);
    }

    public OPallShader(Context context, String VERT, String FRAG, int coordsPerVertex) {

        String vertex = Utils.getSourceAssetsText(VERT, context);
        String fragment = Utils.getSourceAssetsText(FRAG, context);
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, GLESUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertex));
        GLES20.glAttachShader(program, GLESUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragment));
        GLES20.glLinkProgram(program);
        GLES20.glUseProgram(program);
		generateVertexBuffer(getVertices());
        orderBuffer = GLESUtils.createShortBuffer(getOrder());
        COORDS_PER_VERTEX = coordsPerVertex;
        vertexCount = getOrder().length;
        vertexStride = COORDS_PER_VERTEX * 4; //coz float = 4 byte
		outErrorLog();
	}



	protected abstract float[] getVertices();
	protected abstract short[] getOrder();
    protected abstract void render(final int glProgram, final int positionHandle, final FloatBuffer vertexBuffer, final ShortBuffer orderBuffer, OPallCamera2D camera);





	protected final void generateVertexBuffer(float[] vertices) {
		vertexBuffer = GLESUtils.createFloatBuffer(vertices);
	}




    public void render(OPallCamera2D camera) {

        GLES20.glUseProgram(program);
        render(program, GLES20.glGetAttribLocation(program, GLESUtils.a_Position), vertexBuffer, orderBuffer, camera.setProgram(program));
        GLES20.glUseProgram(-1);
    }


	public void setScrDim(float w,float h) {
		this.scrW = w;
		this.scrH = h;
	}
	public float[] getScrDim() {
		return new float[]{scrW, scrH};
	}


    public void outErrorLog() {
		System.out.println(getErrorLog());
    }
	public String getErrorLog() {
		return GLES20.glGetShaderInfoLog(program);
	}

}
