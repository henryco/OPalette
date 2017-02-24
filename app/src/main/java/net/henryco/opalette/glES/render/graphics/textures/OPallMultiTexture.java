package net.henryco.opalette.glES.render.graphics.textures;

import android.graphics.Bitmap;

/**
 * Created by HenryCo on 24/02/17.
 */

public interface OPallMultiTexture extends OPallTexture {

	OPallMultiTexture setBitmap(int n, Bitmap image, filter filterMin, filter filterMag);
	OPallMultiTexture setBitmap(int n, Bitmap image, filter filter);
	OPallMultiTexture setBitmap(int n, Bitmap image);

}
