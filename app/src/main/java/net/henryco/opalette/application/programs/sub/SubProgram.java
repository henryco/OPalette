package net.henryco.opalette.application.programs.sub;

import android.support.v7.app.AppCompatActivity;

import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallSubProgram;
import net.henryco.opalette.api.glES.render.OPallRenderable;

/**
 * Created by HenryCo on 16/03/17.
 */

public interface SubProgram <T extends AppCompatActivity, U extends OPallRenderable>
		extends OPallSubProgram <T> {

	void setRenderSource(U source);
	U getRenderSource();

}
