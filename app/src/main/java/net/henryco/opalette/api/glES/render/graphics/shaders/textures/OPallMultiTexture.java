package net.henryco.opalette.api.glES.render.graphics.shaders.textures;

import android.graphics.Bitmap;
import android.opengl.GLES20;

/**
 * Created by HenryCo on 24/02/17.
 */

public interface OPallMultiTexture extends OPallTexture {

	String DEFAULT_VERT_FILE =

			"attribute vec4  a_Position;\n" +
			"attribute vec2  a_TexCoordinate;\n" +
			"\n" +
			"varying vec4    v_Position;\n" +
			"varying vec4    v_WorldPos;\n" +
			"varying vec2    v_TexCoordinate[5];\n" +
			"\n" +
			"uniform mat4    u_MVPMatrix;\n" +
			"uniform int     u_texNumb;\n" +
			"uniform vec2    u_Flip[5];\n" +
			"\n" +
			"vec2 flip(vec2 f, vec2 tex) {\n" +
			"\n" +
			"    float x = min(1., f.x + 1.) - f.x * tex.x;\n" +
			"    float y = min(1., f.y + 1.) - f.y * tex.y;\n" +
			"    return vec2(x, y);\n" +
			"}\n" +
			"\n" +
			"void main() {\n" +
			"\n" +
			"    v_Position = a_Position;\n" +
			"    v_WorldPos = u_MVPMatrix * a_Position;\n" +
			"    for (int i = 0; i < u_texNumb; i++)\n" +
			"        v_TexCoordinate[i] = flip(vec2(u_Flip[i].x, u_Flip[i].y), a_TexCoordinate);\n" +
			"    gl_Position = v_WorldPos;\n" +
			"}";


	String DEFAULT_FRAG_FILE =

			"varying vec4 v_Position;\n" +
			"varying vec4 v_WorldPos;\n" +
			"varying vec2 v_TexCoordinate[5];\n" +
			"\n" +
			"uniform sampler2D u_Texture0;\n" +
			"uniform sampler2D u_Texture1;\n" +
			"uniform sampler2D u_Texture2;\n" +
			"uniform sampler2D u_Texture3;\n" +
			"uniform sampler2D u_Texture4;\n" +
			"uniform int u_texNumb;\n" +
			"\n" +
			"\n" +
			"void main() {\n" +
			"    gl_FragColor = texture2D(u_Texture0, v_TexCoordinate[0]).rgba;\n" +
			"}";





	String DEF_SHADER = methods.createDefaultShader();
	String FRAG_DIR = SHADERS_DIR+"/multiTexture/frags";
	String u_texNumb = "u_texNumb";




	OPallMultiTexture setBitmap(int n, Bitmap image, Filter filterMin, Filter filterMag);
	OPallMultiTexture setBitmap(int n, Bitmap image, Filter filter);
	OPallMultiTexture setBitmap(int n, Bitmap image);
	OPallMultiTexture setFlip(int n, boolean x, boolean y);
	OPallMultiTexture setSize(int n, int w, int h);
	Bitmap getBitmap(int n);


	final class methods {


		private static String createDefaultShader() {
			// TODO
			return SHADERS_DIR+"/multiTexture/MultiTexture";
		}


		public static void applyTexNumb(int program, int texNumb) {
			GLES20.glUniform1i(GLES20.glGetUniformLocation(program, u_texNumb), texNumb);
		}


		public static void applyFlip(int program, boolean[][] xy) {
			float[] flip = {0,0, 0,0, 0,0, 0,0, 0,0};
			for (int i = 0; i < xy.length; i++) {
				flip[2*i] = OPallTexture.methods.flipValue(xy[i][0]);
				flip[2*i+1] = OPallTexture.methods.flipValue(xy[i][1]);
			}
			GLES20.glUniform2fv(GLES20.glGetUniformLocation(program, u_Flip), 5, flip, 0);
		}


		public static void bindTextures(int texNumb, int[] textureDataHandle, int[] mTextureUniformHandle) {
			for (int i = 0; i < texNumb; i++)
				GLES20.glUniform1i(mTextureUniformHandle[i], i);
			for (int i = 0; i < texNumb; i++) {
				GLES20.glActiveTexture(ACTIVE_TEXTURES[i]);
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle[i]);
			}
		}


		public static void bindTextures(int[] textureDataHandle, int[] mTextureUniformHandle) {
			if (textureDataHandle.length != mTextureUniformHandle.length)
				throw new IllegalArgumentException("Arrays length must be the same!");
			bindTextures(textureDataHandle.length, textureDataHandle, mTextureUniformHandle);
		}


	}
}
