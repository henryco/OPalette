package net.henryco.opalette.glES.render.graphics.shaders;

import android.opengl.GLES20;

import net.henryco.opalette.glES.render.graphics.camera.OPallCamera2D;

/**
 * Created by HenryCo on 23/02/17.
 */

public interface OPallShader {


    /*  Requested in *.vert file:
	 *
     *      attribute vec4 a_Position;
     *      attribute vec2 a_TexCoordinate;
     *      varying vec4 v_Position;
     *      varying vec4 v_WorldPos;
     *      varying vec2 v_TexCoordinate;
     *      uniform mat4 u_MVPMatrix;
     *
     *      void main() {
     *          ...
     *      }
     */



	void render(OPallCamera2D camera);



	String a_Position = "a_Position";
	String u_MVPMatrix = "u_MVPMatrix";
	String u_Texture_n = "u_Texture";
	String a_TexCoordinate = "a_TexCoordinate";


	final class methods {
		static String defTextureN(int numb) {
			return OPallShader.u_Texture_n + numb;
		}
		static int loadShader(int type, String shaderCode) {
			int shader = GLES20.glCreateShader(type);
			GLES20.glShaderSource(shader, shaderCode);
			GLES20.glCompileShader(shader);
			return shader;
		}
		static void applyCameraMatrix(int program, float[] mMVPMatrix) {
			GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, OPallShader.u_MVPMatrix), 1, false, mMVPMatrix, 0);
		}
	}
}

