package net.henryco.opalette.activity.grad;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.R;
import net.henryco.opalette.graphicsCore.glES.layouts.OPallSurfaceView;
import net.henryco.opalette.graphicsCore.glES.render.camera.OPallCamera;
import net.henryco.opalette.graphicsCore.glES.render.graphics.OPallTexture;
import net.henryco.opalette.graphicsCore.glES.render.renderers.OPallRenderer;
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

		int viewWidth = gradGLSurfaceView.getWidth();
		int viewHeight = gradGLSurfaceView.getHeight();
		OPallCamera camera = new OPallCamera(viewWidth, viewHeight, true);

		Bitmap image = Utils.loadAssetsBitmap(this, false, R.drawable.bait);

		//camera.zoom = 0.5f;

		gradGLSurfaceView.setRenderer(new OPallRenderer(this, camera, context ->
				new OPallTexture(image, context, OPallTexture.filter.LINEAR).setBounds(0,0, 300, 300))
		//		.setOnDrawAction((gl10, cam) -> GLESUtils.clear())
		);

        gradGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		gradGLSurfaceView.executeWhenReady(() -> Utils.loopStart(50, null, () -> gradGLSurfaceView.update(() -> camera.translateX(0))));

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
