package net.henryco.opalette.application.test;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.solo.SoloRenderer;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.glES.render.graphics.textures.MultiTexture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.Utils;

public class TestActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		OPallSurfaceView surfaceView = (OPallSurfaceView) findViewById(R.id.surface);
		Camera2D camera2D = new Camera2D(surfaceView.getWidth(), surfaceView.getHeight(), true);

		Bitmap edgy = Utils.loadAssetsBitmap(this, false, R.drawable.edgy);
		Bitmap bait = Utils.loadAssetsBitmap(this, false, R.drawable.bait);



		surfaceView.setRenderer(new SoloRenderer(this, camera2D, context ->
				new MultiTexture(this, 2)
						//.bounds(1, b -> b.setScale(0.75f))
						.setFocusOn(1)
						.setCameraForceUpdate(true)
		).setOnDrawAction((gl10, camera) -> GLESUtils.clear(GLESUtils.Color.PINKY)));



		surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		surfaceView.update(() -> surfaceView.addToGLContextQueue(gl10 -> {
			SoloRenderer renderer = surfaceView.getRenderer();
			MultiTexture texture = renderer.getShader();

			texture.setBitmap(0, bait);
			texture.setBitmap(1, edgy);

			camera2D.update();
		}));

	}

}
