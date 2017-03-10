package net.henryco.opalette.api.glES.render.graphics.fbo;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.OPallTexture;

import java.nio.IntBuffer;

/**
 * Created by HenryCo on 25/02/17.
 */

public interface OPallFBO extends OPallRenderable {



	OPallFBO createFBO(int w, int h, int screenW, int screenH, boolean d);
	OPallFBO beginFBO();
	OPallFBO beginFBO(Runnable runnable);
	OPallFBO endFBO();
	OPallFBO setFlip(boolean f);
	Bitmap getBitmap();
	OPallTexture getTexture();
	OPallFBO setTargetTexture(OPallTexture targetTexture);




	final class methods {


		public static void beginFBO(int frameBuffer, int w, int h,  int texHandle, Runnable runnable) {
			beginFBO(frameBuffer);
			runnable.run();
			endFBO(w, h, texHandle);
		}


		public static void beginFBO(int frameBuffer) {
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer);
		}


		public static void endFBO(int width, int height, int texHandle) {

			GLES20.glActiveTexture(GLES20.GL_TEXTURE_2D);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texHandle);

			GLES20.glCopyTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, 0, 0, width, height);
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		}

		public static int[] getPixelData(int frameBuffer, int w, int h, int texHandle) {

			beginFBO(frameBuffer);
			int[] pixels = new int[w * h];
			IntBuffer intBuffer = IntBuffer.wrap(pixels);
			intBuffer.position(0);
			GLES20.glReadPixels(0, 0, w, h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, intBuffer);
			endFBO(w, h, texHandle);
			for (int i = 0; i < pixels.length; i++)
				pixels[i] = (pixels[i] & (0xFF00FF00)) | ((pixels[i] >> 16) & 0x000000FF) | ((pixels[i] << 16) & 0x00FF0000);
			return pixels;
		}


		public static int[] genGeneralBuff() {
			int[] fbName = {0};
			GLES20.glGenFramebuffers(1, fbName, 0);
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbName[0]);
			return fbName;
		}


		public static int[] genTextureBuff(int width, int height) {
			int[] renderedTexture = {0};
			GLES20.glGenTextures(1, renderedTexture, 0);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, renderedTexture[0]);
			GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, renderedTexture[0], 0);
			return renderedTexture;
		}


		public static int[] genDepthBuff(int width, int height) {
			int[] depthRenderBuffer = {0};
			GLES20.glGenRenderbuffers(1, depthRenderBuffer, 0);
			GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depthRenderBuffer[0]);
			GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height);
			GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, depthRenderBuffer[0]);
			return depthRenderBuffer;
		}


		public static String finishAndCheckStat(boolean out, long id) {
			int status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
			String fboName = "FrameBuffer"+(id != -1 ? "["+id+"]" : "")+": ";
			String msg = fboName+"OK";
			if (status != GLES20.GL_FRAMEBUFFER_COMPLETE)
				msg = fboName+status+"\n"+GLES20.glGetError();
			if (out) System.out.println(msg);
			return msg;
		}
		public static String finishAndCheckStat(boolean out) {
			return finishAndCheckStat(out, -1);
		}

		public static void wipe(int[] frameBuffer, int[] textureBuffer, int[] depthRenderBuffer) {
			if(frameBuffer != null) {
				GLES20.glDeleteFramebuffers(1, frameBuffer, 0);
				frameBuffer = null;
			}
			if(textureBuffer != null) {
				GLES20.glDeleteTextures(1, textureBuffer, 0);
				textureBuffer = null;
			}
			if(depthRenderBuffer != null) {
				GLES20.glDeleteRenderbuffers(1, depthRenderBuffer, 0);
				depthRenderBuffer = null;
			}
		}
	}




}
