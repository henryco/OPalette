package net.henryco.opalette.glES.render.graphics.shaders;

import android.opengl.GLES20;

import net.henryco.opalette.glES.render.OPallRenderable;

/**
 * Created by HenryCo on 23/02/17.
 */

public interface OPallShader extends OPallRenderable {


    /*  Requested in *.vert file:
	 *
     *      attribute vec4 a_Position;
     *      attribute vec2 a_TexCoordinate;	// Per-vertex texture coordinate information we will pass in.
     *
     *      varying vec4 v_Position;
     *      varying vec4 v_WorldPos;
     *      varying vec2 v_TexCoordinate;   // This will be passed into the fragment shader.
     *
     *      uniform mat4 u_MVPMatrix;
     *
     *
     *      void main() {
     *          ...
     *
     *          v_Position = a_Position;
     *			v_WorldPos = u_MVPMatrix * a_Position;
     *			v_TexCoordinate = a_TexCoordinate;
   	 *			gl_Position = v_WorldPos;
     *
     *      	...
     *      }
     */



	void setScreenDim(float w, float h);


	String a_Position = "a_Position";
	String u_MVPMatrix = "u_MVPMatrix";
	String u_Texture_n = "u_Texture";
	String a_TexCoordinate = "a_TexCoordinate";


	final class methods {
		public static String defTextureN(int numb) {
			return OPallShader.u_Texture_n + numb;
		}
		public static int loadShader(int type, String shaderCode) {
			int shader = GLES20.glCreateShader(type);
			GLES20.glShaderSource(shader, shaderCode);
			GLES20.glCompileShader(shader);
			return shader;
		}
		public static void applyCameraMatrix(int program, float[] mMVPMatrix) {
			GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, OPallShader.u_MVPMatrix), 1, false, mMVPMatrix, 0);
		}
	}
}

