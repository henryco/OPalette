package net.henryco.opalette.glES.render.graphics.shaders.universal;

import android.content.Context;
import android.opengl.GLES20;

import net.henryco.opalette.glES.render.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.shaders.OPallShader;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by root on 13/02/17.
 */

public abstract class UniShader extends OPallShader {


    public UniShader(Context context, String VERT, String FRAG) {
        super(context, VERT, FRAG);
    }

    public UniShader(Context context, String VERT, String FRAG, int coordsPerVertex) {
        super(context, VERT, FRAG, coordsPerVertex);
    }


    @Override
    protected void render(int glProgram, int positionHandle, FloatBuffer vertexBuffer, ShortBuffer orderBuffer, OPallCamera2D camera) {

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        draw(glProgram, positionHandle, vertexBuffer, orderBuffer, camera);

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, vertexCount, GLES20.GL_UNSIGNED_SHORT, orderBuffer);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }


    protected abstract void draw(final int glProgram, final int positionHandle,
                                 final FloatBuffer vertexBuffer, final ShortBuffer orderBuffer,
                                 final OPallCamera2D camera);

}
