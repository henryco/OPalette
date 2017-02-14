package net.henryco.opalette.graphicsCore.glES.render.shaders;

import android.content.Context;
import android.opengl.GLES20;

import net.henryco.opalette.graphicsCore.glES.render.camera.OPallCamera;
import net.henryco.opalette.graphicsCore.glES.render.shaders.pure.UniShader;
import net.henryco.opalette.utils.GLESUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by root on 13/02/17.
 */

public class TriangleShader extends UniShader {

    private float color[] = {0.9f, 0.1f, 0.9f, 1.0f};

    public TriangleShader(Context context, String VERT, String FRAG) {
        super(context, VERT, FRAG);
    }

    @Override
    protected float[] getVertices() {
        return new float[] {
                0.0f, 0.9f, 0.0f,   // top
                -0.5f, 0.1f, 0.0f,  // bottom left
                0.5f, 0.1f, 0.0f    // bottom right
        };
    }

    @Override
    protected short[] getOrder() {
        return new short[]{ 0, 1, 2 };  // top -> bot left -> bot right -> top
    }



    @Override
    protected void draw(int glProgram, int positionHandle, FloatBuffer vertexBuffer, ShortBuffer orderBuffer, OPallCamera camera) {
        GLESUtils.clear(0.9f, 0.9f, 0.9f, 0.9f);
        camera.update();
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(glProgram, "u_Color"), 1, color, 0);
    }
}
