package net.henryco.opalette.activity.grad;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.R;
import net.henryco.opalette.graphicsCore.glES.layouts.OPallSurfaceView;
import net.henryco.opalette.graphicsCore.glES.render.camera.OPallCamera;
import net.henryco.opalette.graphicsCore.glES.render.renderers.OPallRenderer;
import net.henryco.opalette.graphicsCore.glES.render.shaders.RectangleShader;
import net.henryco.opalette.graphicsCore.glES.render.shaders.pure.OPallShader;
import net.henryco.opalette.utils.Utils;

public class GradientActivity extends AppCompatActivity {


    private OPallSurfaceView gradGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        initGLES();

    }


    public void initGLES() {

        gradGLSurfaceView = (OPallSurfaceView) findViewById(R.id.gradSurfaceView);
		OPallCamera camera = new OPallCamera();


        gradGLSurfaceView.setRenderer(new OPallRenderer(this, camera) {
            @Override
            protected OPallShader createShader(Context context) {
                return new RectangleShader(context, "shaders/default/Default.vert", "shaders/default/Default.frag").outErrorLog();
				//return new RectangleShader(context, "shaders/triangle/Tri.vert", "shaders/triangle/Tri.frag").outErrorLog();
			}
        });
        gradGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		/*
		Utils.loopStart(25, new Utils.Stopper().start(), () -> {
			camera.translateX(0.001f);
			camera.zoom += 0.01f;
			gradGLSurfaceView.update();
		});
		//*/
	}

    @Override
    public void onResume() {
        super.onResume();
        gradGLSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        gradGLSurfaceView.onPause();
    }



}
