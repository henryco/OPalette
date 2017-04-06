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

package net.henryco.opalette.api.glES.render.graphics.units.bar;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.OPallRenderable;
import net.henryco.opalette.api.utils.GLESUtils;

/**
 * Created by HenryCo on 06/03/17.
 */

public interface OPallBar {

	void createBar(int scrWidth, int scrHeight);
	void render(Camera2D camera, OPallRenderable renderable, int buffer_quantum);

	float getWidth();
	float getHeight();

	OPallBar setColor(GLESUtils.Color color);
	OPallBar setRelativeSize(float size_pct);
	OPallBar setRelativePosition(float pos_pct);
	OPallBar setRelativeContentSize(float size_pct);

	float getPosX();
	float getPosY();
}
