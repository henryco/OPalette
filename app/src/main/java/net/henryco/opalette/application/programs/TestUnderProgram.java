/*
 *   /*
 *    * Copyright (C) Henryk Timur Domagalski
 *    *
 *    * Licensed under the Apache License, Version 2.0 (the "License");
 *    * you may not use this file except in compliance with the License.
 *    * You may obtain a copy of the License at
 *    *
 *    *      http://www.apache.org/licenses/LICENSE-2.0
 *    *
 *    * Unless required by applicable law or agreed to in writing, software
 *    * distributed under the License is distributed on an "AS IS" BASIS,
 *    * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    * See the License for the specific language governing permissions and
 *    * limitations under the License.
 *
 */

package net.henryco.opalette.application.programs;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.glSurface.renderers.universal.OPallUnderProgram;
import net.henryco.opalette.api.glES.render.graphics.fbo.FrameBuffer;
import net.henryco.opalette.api.glES.render.graphics.fbo.OPallFBOCreator;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.Texture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.lambda.consumers.OPallTriConsumer;
import net.henryco.opalette.api.utils.observer.OPallUpdObserver;
import net.henryco.opalette.api.utils.requester.Request;
import net.henryco.opalette.application.proto.AppMainProto;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by HenryCo on 06/04/17.
 */

public class TestUnderProgram implements OPallUnderProgram<AppMainProto> {

	private List<OPallTriConsumer<AppMainProto, Integer, Integer>> onDrawQueue;
	private Camera2D camera2D;

	private Texture startImage;
	private FrameBuffer frameBuffer;

	private boolean uCan = false;

	private OPallUpdObserver observator;

	@Override
	public void setObservator(OPallUpdObserver observator) {
		this.observator = observator;
	}

	@Override
	public void acceptRequest(Request request) {
		request.openRequest(ProgramPipeLine.AppProgramProtocol.send_bitmap_to_program, () -> {
			onDrawQueue.add((appMainProto, width, height) -> {
				Bitmap bitmap = request.getData();
				startImage = new Texture();
				startImage.setScreenDim(width, height);
				startImage.setBitmap(bitmap);
				bitmap.recycle();
				bitmap = null;
				float scrWidth = startImage.getScreenWidth();
				float schHeight = startImage.getScreenHeight();
				float w = startImage.getWidth();
				float h = startImage.getHeight();
				float scaleW = scrWidth / w;
				float scaleH = schHeight / h;
				startImage.bounds(b -> b.setScale(Math.max(scaleW, scaleH)));
			});
			uCan = true;
			observator.update();
		});
	}

	@Override
	public long getID() {
		return 11;
	}

	private static final int icon_size = 300;

	@Override
	public void create(@Nullable GL10 gl, int width, int height, AppMainProto context) {
		camera2D = new Camera2D(width, height, true);
		onDrawQueue = new ArrayList<>();
		frameBuffer = OPallFBOCreator.FrameBuffer(icon_size, icon_size, false);
	}

	@Override
	public void onDraw(@Nullable GL10 gl, AppMainProto context, int width, int height) {

		camera2D.setPosXY(0,0).update();
		GLESUtils.clear();

		for (int i = 0; i < onDrawQueue.size(); i++) {
			onDrawQueue.remove(i).consume(context, width, height);
		}

		if (uCan) {

			float s = startImage.bounds2D.getScale();
			float dx = width - startImage.getWidth()*s;
			float dy = height - startImage.getHeight()*s;

			camera2D.setPosXY(dx * 0.5f, dy * 0.5f);
			startImage.render(camera2D);

//			camera2D.backTranslate(() -> frameBuffer.beginFBO(() -> {
//				camera2D.setPosY(icon_size - height);
//				startImage.setSize(icon_size, icon_size);
//				startImage.render(camera2D);
//			}));
//
//			frameBuffer.render(camera2D);



		}

	}

	@Override
	public void onSurfaceChange(@Nullable GL10 gl, AppMainProto context, int width, int height) {

	}
}
