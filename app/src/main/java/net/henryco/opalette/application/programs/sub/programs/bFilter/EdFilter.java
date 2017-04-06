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

package net.henryco.opalette.application.programs.sub.programs.bFilter;

import android.graphics.Color;

import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.lambda.functions.OPallFunction;
import net.henryco.opalette.application.conf.GodConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by HenryCo on 05/04/17.
 */

public class EdFilter {


	private static final EdFilter DEFAULT_FILTER;
	static {
		DEFAULT_FILTER = new EdFilter("Default", Color.TRANSPARENT, 1f, 1f, 0f, 0f, 0f, false);
		DEFAULT_FILTER.setAdd(0,0,0);
		DEFAULT_FILTER.setMin(0,0,0);
		DEFAULT_FILTER.setMax(1,1,1);
		DEFAULT_FILTER.setAlpha(1);
	}
	public static EdFilter getDefaultFilter() {
		return DEFAULT_FILTER.copy();
	}

	private static List<EdFilter> loadFilterListFromJSON() {

		/*

		{ 	"type": "t255" | "t01" ,
			"filters": [{
				"name": "filter1",
				"color": "#FFFFFFFF",
				"contrast": "0",
				"gamma": "1",
				"hue": "0",
				"saturation": "0",
				"lightness": "0",
				"bw": "false",
				"min": ["0", "0", "0"],
				"max": ["255", "255", "255"],
				"add": ["0", "0", "0"]
			}]
		}
		*/

		List<EdFilter> filterList = new ArrayList<>();
		filterList.add(getDefaultFilter());
		String file = GodConfig.TEXTURE_FILTERS_DATA_FILE;
		InputStream in = EdFilter.class.getClassLoader().getResourceAsStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder builder = new StringBuilder();
		try {
			String line;
			while ((line = reader.readLine()) != null)
				builder.append(line).append('\n');
			in.close();

			JSONObject data = new JSONObject(builder.toString());
			String type = "t01";
			try {
				type = data.getString("type");
			} catch (Exception ignored){}

			final OPallFunction<Float, Float> corrector;
			final boolean type255 = type.equalsIgnoreCase("t255");
			if (type255) corrector = f -> f / 255f;
			else corrector = Float::floatValue;

			JSONArray filters = data.getJSONArray("filters");
			for (int i = 0; i < filters.length(); i++) {
				JSONObject filter = filters.getJSONObject(i);
				String f_name = filter.getString("name");
				boolean bw = false;
				float contrast = 1f;
				float gamma = 1f;
				int col = 0xFFFFFFFF;
				float light = 0f;
				float sat = 0f;
				float hue = 0f;

				try {
					sat = (float) filter.getDouble("saturation");
					if (type255) sat /= GodConfig.NORM_RANGE;
				} catch (Exception ignored){}
				try {
					hue = (float) filter.getDouble("hue");
					if (type255) hue /= GodConfig.HUE_CLAMP_RANGE;
				} catch (Exception ignored){}
				try {
					light = (float) filter.getDouble("lightness");
					if (type255) light /= GodConfig.NORM_RANGE;
				} catch (Exception ignored){}
				try {
					col = Color.parseColor(filter.getString("color"));
				} catch (Exception ignored){}
				try {
					contrast = (float) filter.getDouble("contrast");
					if (type255) contrast /= GodConfig.NORM_RANGE;
					contrast += 1f;
				} catch (Exception ignored){}
				try {
					gamma = (float) filter.getDouble("gamma");
				} catch (Exception ignored){}
				try {
					bw = filter.getBoolean("bw");
				} catch (Exception ignored){}

				OPallFunction<float[], String> colorFunc = s -> {
					try {
						JSONArray color = filter.getJSONArray(s);
						float r = corrector.apply((float) color.getDouble(0));
						float g = corrector.apply((float) color.getDouble(1));
						float b = corrector.apply((float) color.getDouble(2));
						return new float[]{r, g, b};
					} catch (JSONException e) {
						return null;
					}
				};

				float[] add = colorFunc.apply("add");
				float[] min = colorFunc.apply("min");
				float[] max = colorFunc.apply("max");

				EdFilter extFilter = new EdFilter(f_name, col, gamma, contrast, hue, sat, light, bw);
				if (add != null) extFilter.setAdd(add[0], add[1], add[2]);
				if (min != null) extFilter.setMin(min[0], min[1], min[2]);
				if (max != null) extFilter.setMax(max[0], max[1], max[2]);

				filterList.add(extFilter);
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		Collections.reverse(filterList);
		return filterList;
	}

	private static final class Vec {
		private final float r, g, b, a;
		private Vec(final float r, final float g, final float b, final float a) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		}
		private void applyColor(GLESUtils.Color color) {
			color.set(r, g, b, a);
		}

		private Vec copy() {
			return new Vec(this.r, this.g, this.b, this.a);
		}

		@Override
		public String toString() {
			return "Vec{" +
					"r=" + r +
					", g=" + g +
					", b=" + b +
					", a=" + a +
					'}';
		}
	}

	public final String name;
	public final int color;

	private Vec add;
	private Vec min;
	private Vec max;
	private final boolean bw_enable;
	private final float contrast;
	private final float gammaCorrection;
	private final float lightness;
	private final float saturation;
	private final float hue;

	private float alpha;

	private static final List<EdFilter> filterList = loadFilterListFromJSON();
	public static List<EdFilter> getFilterList() {
		return filterList;
	}


	private EdFilter(final String name,
					 final int tag_color,
					 final float gammaCorrection,
					 final float contrast,
					 final float hue,
					 final float sat,
					 final float lig,
					 final boolean bw_enable) {
		this.gammaCorrection = gammaCorrection;
		this.bw_enable = bw_enable;
		this.contrast = contrast;
		this.color = tag_color;
		this.name = name;
		this.lightness = lig;
		this.saturation = sat;
		this.hue = hue;
		this.alpha = 1f;
	}


	private EdFilter setAdd(final Vec add) {
		return setAdd(add.r, add.g, add.b);
	}
	private EdFilter setAdd(float r, float g, float b) {
		if (add == null) this.add = new Vec(r, g, b, 0f);
		return this;
	}

	private EdFilter setMin(final Vec min) {
		return setMin(min.r, min.g, min.b);
	}
	private EdFilter setMin(float r, float g, float b) {
		if (min == null) this.min = new Vec(r, g, b, 0f);
		return this;
	}

	private EdFilter setMax(final Vec max) {
		return setMax(max.r, max.g, max.b);
	}
	private EdFilter setMax(float r, float g, float b) {
		if (max == null) this.max = new Vec(r, g, b, 1f);
		return this;
	}

	public EdFilter setAlpha(float a) {
		this.alpha = a;
		return this;
	}
	public float getAlpha() {
		return alpha;
	}

	public EdTexture applyFilter(EdTexture texture) {
		texture.setGammaCorrection(gammaCorrection);
		texture.setBwEnable(bw_enable);
		texture.setContrast(contrast);
		texture.setAddHue(hue);
		texture.setAddSaturation(saturation);
		texture.setAddLightness(lightness);

		if (add != null) add.applyColor(texture.add);
		else new Vec(0,0,0,0).applyColor(texture.add);

		if (max != null) max.applyColor(texture.max);
		else new Vec(1,1,1,1).applyColor(texture.max);

		if (min != null) min.applyColor(texture.min);
		else new Vec(0,0,0,0).applyColor(texture.min);

		texture.setAlpha(alpha);
		return texture;
	}

	public EdFilter copy() {
		EdFilter copy = new EdFilter(name, color, gammaCorrection, contrast, hue, saturation, lightness, bw_enable);
		if (add != null) copy.setAdd(this.add.copy());
		if (min != null) copy.setMin(this.min.copy());
		if (max != null) copy.setMax(this.max.copy());
		return copy;
	}

	@Override
	public String toString() {
		return "EdFilter{" +
				"name='" + name + '\'' +
				", add=" + add +
				", min=" + min +
				", max=" + max +
				", bw_enable=" + bw_enable +
				", contrast=" + contrast +
				", gammaCorrection=" + gammaCorrection +
				'}';
	}
}

