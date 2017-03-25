package net.henryco.opalette.application.programs.sub;

import android.support.annotation.Nullable;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallSubProgram;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.utils.requester.OPallRequestFeedBack;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 16/03/17.
 */

public interface AppSubProgram<T> extends OPallSubProgram<T>, OPallRequestFeedBack {

	void render(@Nullable GL10 gl10, T context, Camera2D camera, int w, int h);
	void setID(long id);

	void setRenderData(OPallRenderable data);
	void setProgramHolder(AppSubProgramHolder holder);
	OPallRenderable getRenderData();


	interface AppSubProgramHolder {
		void addSubProgram(AppSubProgram p);
		void setSubProgram(AppSubProgram p, int i);
		boolean removeSubProgram(AppSubProgram p);
		boolean removeSubProgram(int i);
	}

	final class ProxyRenderData <U extends OPallRenderable> {

		private U data = null;
		private boolean state = false;

		public void setRenderData(U data) {
			this.data = data;
		}
		public U getRenderData() {
			if (data == null) return null;
			return data;
		}
		public boolean stateUpdated() {
			boolean r = state;
			state = false;
			return r;
		}
		public ProxyRenderData<U> setStateUpdated() {
			this.state = true;
			return this;
		}
	}
}
