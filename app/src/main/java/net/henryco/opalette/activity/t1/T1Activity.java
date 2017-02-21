package net.henryco.opalette.activity.t1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.R;
import net.henryco.opalette.glES.layouts.OPallSurfaceView;

public class T1Activity extends AppCompatActivity {

	private static final int ACTIVITY_SELECT_IMAGE = 1;

	private OPallSurfaceView contentSurface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_t1);

		contentSurface = (OPallSurfaceView) findViewById(R.id.contentSurface);
		contentSurface.setDimProcessor(OPallSurfaceView.DimensionProcessors.RELATIVE_SQUARE);


		contentSurface.executeWhenReady(this::loadImage);

	}

	public void loadImage() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
	}
}
