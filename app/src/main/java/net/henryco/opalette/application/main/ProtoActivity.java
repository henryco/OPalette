package net.henryco.opalette.application.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUniRenderer;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.UniRenderer;
import net.henryco.opalette.api.glES.glSurface.view.OPallSurfaceView;
import net.henryco.opalette.api.utils.Utils;
import net.henryco.opalette.api.utils.requester.OPallRequester;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.main.program.PaletteProgramHorizontal;

public class ProtoActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {



	private final OPallRequester stateRequester = new OPallRequester();



	// ================================================ AUTO-GENERATED SKELETON ==================================================== //


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// ----------- AUTO GENERATED PART ---------- //

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proto);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show());

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);



		// -------- CUSTOM PART -------- //
		getWindow().setStatusBarColor(getResources().getColor(R.color.DARK));

		initialization();
	}







	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}







	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.proto, menu);
		return true;
	}





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}







	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}



	// ============================================== END OF AUTO-GENERATED SKELETON ========================================== //









	long renderID = 0;

	private void initialization() {

		OPallUniRenderer<ProtoActivity> renderer = new UniRenderer<>(this, new PaletteProgramHorizontal());

		renderID = stateRequester.addRequestListener(renderer);

		OPallSurfaceView oPallSurfaceView = (OPallSurfaceView) findViewById(R.id.opallView);
		oPallSurfaceView.setDimProportions(OPallSurfaceView.DimensionProcessors.RELATIVE_SQUARE);
		oPallSurfaceView.setOnClickListener(this::imageClickAction);
		oPallSurfaceView.setRenderer(renderer);


	}







	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == FragmentActivity.RESULT_OK)
			if (requestCode == Utils.activity.REQUEST_PICK_IMAGE)
				stateRequester.sendRequest(new Request("LoadImage", Utils.loadIntentBitmap(this, data)));
	}









	private void imageClickAction(View view) {
		//TODO another actions: (share, pick, save)
		Utils.loadImageActivity(this);
		setImageFilter();

	}







	private void setImageFilter() {
		//TODO
	}



}
