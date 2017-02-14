package net.henryco.opalette.activity.triangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.henryco.opalette.graphicsCore.glES.render.camera.OPallCamera;
import net.henryco.opalette.graphicsCore.glES.render.renderers.OPallRenderer;
import net.henryco.opalette.graphicsCore.glES.render.shaders.pure.OPallShader;
import net.henryco.opalette.graphicsCore.glES.render.shaders.TriangleShader;


public class TriActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GLSurfaceView(this) {
            public GLSurfaceView init(Context context) {
                setEGLContextClientVersion (2);
                setRenderer(new OPallRenderer(context, new OPallCamera(0,0)) {
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


