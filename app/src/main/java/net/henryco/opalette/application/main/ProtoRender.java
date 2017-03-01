package net.henryco.opalette.application.main;

import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUniRenderer;
import net.henryco.opalette.api.utils.requester.Request;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 27/02/17.
 */

public class ProtoRender extends OPallUniRenderer<ProtoActivity> {



	public ProtoRender(ProtoActivity context) {
		super(context);
	}
	public ProtoRender(ProtoActivity context, int id) {
		super(context, id);
	}


	@Override
	protected void onSurfaceCreated(GL10 gl, EGLConfig config, int width, int height, ProtoActivity context) {

	}





	@Override
	protected void onSurfaceChanged(GL10 gl, int width, int height, ProtoActivity context) {


	}




	@Override
	protected void onDrawFrame(GL10 gl, ProtoActivity context) {



	}





	@Override
	protected void acceptRequest(Request request, ProtoActivity context) {


	}


}
