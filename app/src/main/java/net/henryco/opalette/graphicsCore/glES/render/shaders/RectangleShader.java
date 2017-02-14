package net.henryco.opalette.graphicsCore.glES.render.shaders;

import android.content.Context;
import android.opengl.GLES20;
import net.henryco.opalette.graphicsCore.glES.render.camera.OPallCamera;
import net.henryco.opalette.graphicsCore.glES.render.shaders.pure.UniShader;
import net.henryco.opalette.utils.GLESUtils;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


public class RectangleShader extends UniShader {


    public RectangleShader(Context context, String VERT, String FRAG) {
        super(context, VERT, FRAG, 2);
    }

    @Override
    protected float[] getVertices() {
        return new float[] {
                -0.5f,  0.5f,   // top left
                -0.5f, -0.5f,   // bottom left
                0.5f, -0.5f,   // bottom right
                0.5f,  0.5f }; // top right;
    }

    @Override
    protected short[] getOrder() {
        return new short[]{ 0, 3, 2, 1, 0, 3 }; // triangle1 + triangle2 = square
    }

    @Override
    protected void draw(int glProgram, int positionHandle, FloatBuffer vertexBuffer, ShortBuffer orderBuffer, OPallCamera camera) {
		GLESUtils.clear(0.9f, 0.9f, 0.9f, 0.9f);
        camera.update();
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(glProgram, "u_Color"), 1, new float[]{0.9f, 0.1f, 0.9f, 1.0f}, 0);

    }

}
