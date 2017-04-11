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

package net.henryco.opalette.application.conf;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HenryCo on 05/04/17.
 */

public class GodConfig {

	public static final String TEXTURE_FILTERS_DATA_FILE = "assets/opall_filters.json";
	public static final int DEFAULT_FILTER_PREVIEW_ICON_SIZE = 32;
	public static final int HUE_CLAMP_RANGE = 180;
	public static final int NORM_RANGE = 100;
	public static final float GAMMA_MAX_RANGE = 5;

	public static final String defBitmapName = "OPalette";

	public static String genDefaultImgFileName() {
		return defBitmapName+"_"+ new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date(System.currentTimeMillis()));
	}

	public static final String DEF_VERSION_FILE = "VERSION";
	public static final String DEF_ABOUT_FILE = "ABOUT.txt";
	public static final String DEF_MAILTO_FILE = "MAILTO";

	public static final String PREF_KEY_SAVE_AFTER = "saveAfterShareSwitch";
	public static final String PREF_KEY_ADS_ENABLE = "addsEnableSwitch";
	public static final String PREF_KEY_VERSION = "pref_version";
	public static final String PREF_KEY_CONTACT = "pref_contact";
}
