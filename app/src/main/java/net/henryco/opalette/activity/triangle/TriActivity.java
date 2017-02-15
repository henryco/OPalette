package net.henryco.opalette.activity.triangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.graphicsCore.glES.render.camera.OPallCamera;
import net.henryco.opalette.graphicsCore.glES.render.renderers.OPallAbsRenderer;
import net.henryco.opalette.graphicsCore.glES.render.shaders.geom.TriangleShader;
import net.henryco.opalette.graphicsCore.glES.render.shaders.pure.OPallShader;


public class TriActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GLSurfaceView(this) {
            public GLSurfaceView init(Context context) {
                setEGLContextClientVersion (2);
                setRenderer(new OPallAbsRenderer(context, new OPallCamera(0, 0)) {
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


