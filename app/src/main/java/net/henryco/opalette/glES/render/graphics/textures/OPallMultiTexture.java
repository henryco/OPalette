package net.henryco.opalette.glES.render.graphics.textures;

import android.graphics.Bitmap;

/**
 * Created by HenryCo on 24/02/17.
 */

public interface OPallMultiTexture extends OPallTexture {


	/*	Requested in *.frag file:
	 *
	 *		precision mediump float;
	 *
	 *		varying vec4 v_Position;
	 *		varying vec4 v_WorldPos;
	 *		varying vec2 v_TexCoordinate;
	 *
	 *		uniform sampler2D u_Texture0;
	 *		uniform sampler2D u_Texture1;
	 *		.
	 *		.
	 *		.
	 *		uniform sampler2D u_Texture(n); // {0, 1, ..., n}
	 *
	 *		void main() {
	 *			...
	 *			gl_FragColor = vec4(...);
	 *		}
	 */



	OPallMultiTexture setBitmap(int n, Bitmap image, filter filterMin, filter filterMag);
	OPallMultiTexture setBitmap(int n, Bitmap image, filter filter);
	OPallMultiTexture setBitmap(int n, Bitmap image);

}
