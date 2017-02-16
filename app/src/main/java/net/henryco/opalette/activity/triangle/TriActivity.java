package net.henryco.opalette.activity.triangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.glES.render.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.shaders.OPallShader;
import net.henryco.opalette.glES.render.graphics.shaders.universal.geometry.TriangleShader;
import net.henryco.opalette.glES.render.renderers.OPallAbsRenderer;


public class TriActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GLSurfaceView(this) {
            public GLSurfaceView init(Context context) {
                setEGLContextClientVersion (2);
                setRenderer(new OPallAbsRenderer(context, new OPallCamera2D(0, 0)) {
                    @Override
                    protected OPallShader createShader(Context context) {
                        return new TriangleShader(context, "shaders/triangle/Tri.vert", "shaders/triangle/Tri.frag");
                    }
                });
                return this;
            }
        }.init(this));

    }
}


