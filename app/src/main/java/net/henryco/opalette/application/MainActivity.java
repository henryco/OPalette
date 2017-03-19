package net.henryco.opalette.application;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUniRenderer;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.UniRenderer;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.utils.dialogs.OPallAlertDialog;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.api.utils.requester.RequestSender;
import net.henryco.opalette.application.programs.ProgramPipeLine;
import net.henryco.opalette.application.proto.AppMainProto;

public class MainActivity extends AppCompatActivity
		implements AppMainProto, ProgramPipeLine.AppProgramProtocol {


	private final RequestSender stateRequester = new RequestSender();


	private Fragment actualFragment = null;
	boolean optionsSwitched = false;
	long renderID = 0;





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		if (getSupportActionBar() != null){
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}

		initialization();
		switchToScrollOptionsView();
	}



	@Override
	public void switchToFragmentOptions(Fragment fragment) {

		if (fragment != null) {
			FragmentTransaction fragmentTransaction = getFragmentManager()
					.beginTransaction().add(R.id.fragmentContainer, fragment);
			fragmentTransaction.commit();
		}

		findViewById(R.id.scrollOptionsView).setVisibility(View.GONE);
		findViewById(R.id.fragmentSuperContainer).setVisibility(View.VISIBLE);

		optionsSwitched = true;
		actualFragment = fragment;
	}

	@Override
	public void switchToScrollOptionsView() {

		if (actualFragment != null) {
			getFragmentManager().beginTransaction().remove(actualFragment).commit();
		}

		findViewById(R.id.scrollOptionsView).setVisibility(View.VISIBLE);
		findViewById(R.id.fragmentSuperContainer).setVisibility(View.GONE);

		optionsSwitched = false;
		actualFragment = null;
	}

	@Override
	public OPallSurfaceView getRenderSurface() {
		return (OPallSurfaceView) findViewById(R.id.opallView);
	}

	@Override
	public Activity getActivityContext() {
		return this;
	}




	private void initialization() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.DARK));
		}

		OPallUniRenderer renderer = new UniRenderer(this, new ProgramPipeLine());
		renderID = stateRequester.addRequestListener(renderer);

		OPallSurfaceView oPallSurfaceView = (OPallSurfaceView) findViewById(R.id.opallView);
		oPallSurfaceView.setDimProportions(OPallSurfaceView.DimensionProcessors.RELATIVE_SQUARE);
		oPallSurfaceView.setRenderer(renderer);

		oPallSurfaceView.addToGLContextQueue(gl ->
				stateRequester.sendNonSyncRequest(new Request(send_bitmap_to_program,
						StartUpActivity.BitmapPack::close, StartUpActivity.BitmapPack.get()))
		);

	}







	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
			startBackDialog();
		return super.onOptionsItemSelected(item);
	}




	@Override
	public void onBackPressed() {
		if (!optionsSwitched) startBackDialog();
		else switchToScrollOptionsView();
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
