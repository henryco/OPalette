package net.henryco.opalette.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.R;
import net.henryco.opalette.glES.layouts.OPallSurfaceView;
import net.henryco.opalette.glES.render.graphics.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.textures.OPallTexture;
import net.henryco.opalette.glES.render.renderers.OPallRenderer;
import net.henryco.opalette.utils.Utils;

public class TestActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		OPallSurfaceView surfaceView = (OPallSurfaceView) findViewById(R.id.surface);
		Bitmap bitmap = Utils.loadAssetsBitmap(this, false, R.drawable.bait);
		OPallCamera2D camera2D = new OPallCamera2D(surfaceView.getWidth(), surfaceView.getHeight(), true);
		surfaceView.setRenderer(new OPallRenderer(this, camera2D, context ->
				new OPallTexture(bitmap, this).bounds(b -> b.setScale(0.5f))
						//.setBounds(0, 100, 100, 100)

		));

	}

}
