package net.henryco.opalette.activity.t1;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.R;
import net.henryco.opalette.glES.layouts.OPallSurfaceView;
import net.henryco.opalette.glES.render.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.textures.OPallTexture;
import net.henryco.opalette.glES.render.renderers.OPallRenderer;
import net.henryco.opalette.utils.Utils;

public class T1Activity extends AppCompatActivity {

	private static final int ACTIVITY_SELECT_IMAGE = 14662;

	private OPallSurfaceView contentSurface;
	private int width, height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_t1);

		loadSurface(new OPallRenderer(this));
		width = contentSurface.getWidth();
		height = contentSurface.getHeight();

		contentSurface.executeWhenReady(() -> {

		});

	}

	//TODO FIXME
	private void loadSurface(GLSurfaceView.Renderer renderer) {
		contentSurface = (OPallSurfaceView) findViewById(R.id.contentSurface);
		contentSurface.setDimProcessor(OPallSurfaceView.DimensionProcessors.RELATIVE_SQUARE);
		contentSurface.setOnClickListener(v -> loadImage());
		contentSurface.setRenderer(renderer);

	}


	private void loadImage() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == AppCompatActivity.RESULT_OK && requestCode == ACTIVITY_SELECT_IMAGE) {
			loadSurface(new OPallRenderer(this, new OPallCamera2D(width, height, true),
					context -> new OPallTexture(Utils.loadUriBitmap(this, data), context, OPallTexture.filter.LINEAR)));
			contentSurface.update();
		}
	}
}
