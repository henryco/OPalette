package net.henryco.opalette.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
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
import net.henryco.opalette.glES.layouts.OPallSurfaceView;
import net.henryco.opalette.glES.render.graphics.camera.OPallCamera2D;
import net.henryco.opalette.glES.render.graphics.textures.Texture;
import net.henryco.opalette.glES.render.renderers.OPallRenderer;
import net.henryco.opalette.utils.Utils;

public class ProtoActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {



	private OPallSurfaceView oPallSurfaceView;
	private OPallCamera2D camera;
	private Bitmap image;






	// ================================================ AUTO-GENERATED SKELETON ==================================================== //


	@Override
	protected void onCreate(Bundle savedInstanceState) {
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






		oPallSurfaceView = (OPallSurfaceView) findViewById(R.id.opallView);
		oPallSurfaceView.setDimProportions(OPallSurfaceView.DimensionProcessors.RELATIVE_SQUARE);
		oPallSurfaceView.setRenderer(new OPallRenderer(this));
		oPallSurfaceView.setOnClickListener(this::imageClickAction);
		oPallSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		camera = new OPallCamera2D(oPallSurfaceView.getWidth(), oPallSurfaceView.getHeight(), true);

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












	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == FragmentActivity.RESULT_OK) {
			if (requestCode == Utils.activity.REQUEST_PICK_IMAGE)
				image = Utils.loadIntentBitmap(this, data);
				oPallSurfaceView.update(() -> oPallSurfaceView.runInGLContext(gl ->
						((OPallRenderer) oPallSurfaceView.getRenderer()).setCamera(camera)
						.setShader(new Texture(image, this).setCameraForceUpdate(true))));
		}
	}









	public void imageClickAction(View view) {
		//TODO another actions: (share, pick, save)
		Utils.loadImageActivity(this);
		setImageFilter();
	}







	public void setImageFilter() {
		oPallSurfaceView.runInGLContext(gl -> {
			OPallRenderer renderer = oPallSurfaceView.getRenderer();
			renderer.setShader(renderer.getShader());
			// TODO (actually doing nothing)
		}).update();
	}



}
