package net.henryco.opalette.application.activities;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import net.henryco.opalette.R;
import net.henryco.opalette.StartUpActivity;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUniRenderer;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.UniRenderer;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.utils.dialogs.OPallAlertDialog;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.fragments.ImageOptionFragment;
import net.henryco.opalette.application.programs.PaletteProgramHorizontal;

public class MainActivity extends AppCompatActivity implements ImageOptionFragment.OnFragmentInteractionListener {



	private final OPallRequester stateRequester = new OPallRequester();




	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		if (getSupportActionBar() != null){
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

		initialization();
	}






	long renderID = 0;

	private void initialization() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.DARK));
		}

		OPallUniRenderer<MainActivity> renderer = new UniRenderer<>(this, new PaletteProgramHorizontal());
		renderID = stateRequester.addRequestListener(renderer);

		OPallSurfaceView oPallSurfaceView = (OPallSurfaceView) findViewById(R.id.opallView);
		oPallSurfaceView.setDimProportions(OPallSurfaceView.DimensionProcessors.RELATIVE_SQUARE);
		oPallSurfaceView.setRenderer(renderer);

		oPallSurfaceView.addToGLContextQueue(gl ->
				stateRequester.sendRequest(new Request("LoadImage",
						StartUpActivity.BitmapPack::close, StartUpActivity.BitmapPack.get()))
		);


	}



	@Override
	public void onFragmentInteraction(Uri uri) {

	}





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
			startBackDialog();
		return super.onOptionsItemSelected(item);
	}




	@Override
	public void onBackPressed() {
		startBackDialog();
	}


	private void closeActivity() {
		super.onBackPressed();
		finish();
	}


	private void startBackDialog() {
		new OPallAlertDialog()
				.title("U SURE?")
				.message("SURE?")
				.negative("cancel")
				.positive("accept", this::closeActivity)
		.show(getSupportFragmentManager(), "backDialog");
	}



}
