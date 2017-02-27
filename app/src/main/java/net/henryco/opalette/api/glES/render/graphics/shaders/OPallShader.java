package net.henryco.opalette.api.glES.render.graphics.shaders;

import android.opengl.GLES20;

import net.henryco.opalette.api.glES.render.OPallRenderable;

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
     *		uniform vec2 u_Flip;            // 1 || -1
     *
     *
     *		vec2 flip(vec2 f, vec2 texCoord) {
     *			float fvtx = min(1 + f.x, 1) - (f.x * texCoord.x);
     *			float fvty = min(1 + f.y, 1) - (f.y * texCoord.y);
     *			return vec2(fvtx, fvty);
     *		}
     *
     *
     *      void main() {
     *          ...
     *
     *          v_Position = a_Position;
     *			v_WorldPos = u_MVPMatrix * a_Position;
     *			v_TexCoordinate = vec2(flip(u_Flip, a_TexCoordinate));
   	 *			gl_Position = v_WorldPos;
     *
     *      	...
     *      }
     */


	void setScreenDim(float w, float h);


	String a_Position = "a_Position";
	String u_MVPMatrix = "u_MVPMatrix";
	String a_TexCoordinate = "a_TexCoordinate";


	String[] detTextureN = {
			"u_Texture0", "u_Texture1",
			"u_Texture2", "u_Texture3",
			"u_Texture4", "u_Texture5",
			"u_Texture6", "u_Texture7",
			"u_Texture8", "u_Texture9",
	};



	final class methods {

		public static String defTextureN(int numb) {
			return OPallShader.detTextureN[numb];
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

