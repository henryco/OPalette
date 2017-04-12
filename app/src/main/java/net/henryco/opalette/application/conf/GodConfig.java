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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	public static final String DEF_ABOUT_FILE = "ABOUT.txt";
	public static final String PREF_KEY_SAVE_AFTER = "saveAfterShareSwitch";
	public static final String PREF_KEY_ADS_ENABLE = "addsEnableSwitch";
	public static final String PREF_KEY_VERSION = "pref_version";
	public static final String PREF_KEY_CONTACT = "pref_contact";
	public static final String PREF_KEY_ANALYTICS_ENABLE = "sendAnalytics";
	public static final String PREF_KEY_DONATE = "pref_donate";
	public static final String PREF_KEY_GITHUB = "pref_github";



	public static final class Analytics {

		public static final String TYPE_COLOR_FILTER = "color_filter";
		public static final String TYPE_CANVAS_DIMENSION = "canvas_dim";
		public static final String TYPE_PALETTE_BAR_ORIENTATION = "palette_bar_orientation";
		public static final String TYPE_CUSTOM_COLOR_PREFS = "color_filter_custom_prefs";
		public static final String TYPE_SCREEN_SIZE = "screen_size";

	}





	public static final class JSON_CONF {

		private static final String JSON_CONF_FILE_NAME = "assets/CONF.json";
		private static final String JSON_CONF_FILE = loadJSONCONF(JSON_CONF_FILE_NAME);

		private static final String json_root_app = "app";
		private static final String json_app_name = "name";
		private static final String json_root_version = "version";
		private static final String json_version_numb = "numb";
		private static final String json_version_tag = "tag";
		private static final String json_donate = "donate";
		private static final String json_root_mail = "mail_to";
		private static final String json_mail_dev = "dev";
		private static final String json_mail_other = "other";
		private static final String json_github = "github";


		public static final String APP_NAME = getAppName(JSON_CONF_FILE);
		public static final String APP_VERSION_NUMB = getAppVersionNumb(JSON_CONF_FILE);
		public static final String APP_VERSION_TAG = getAppVersionTag(JSON_CONF_FILE);
		public static final String APP_DONATE_URL = getDonate(JSON_CONF_FILE);
		public static final String APP_GITHUB_URL = getGitHub(JSON_CONF_FILE);
		public static final String[] APP_DEV_MAILS = getDevMails(JSON_CONF_FILE);
		public static final String[] APP_OTHER_MAILS = getOtherMails(JSON_CONF_FILE);



		private static String loadJSONCONF(String jsonFileName) {

			InputStream in = GodConfig.class.getClassLoader().getResourceAsStream(jsonFileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder builder = new StringBuilder();
			String line;
			try {
				while ((line = reader.readLine()) != null)
					builder.append(line).append('\n');
				in.close();
				return builder.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		private static String getAppName(String jsonFile) {
			try {
				JSONObject root = new JSONObject(jsonFile);
				JSONObject app = root.getJSONObject(json_root_app);
				return app.getString(json_app_name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		private static String getAppVersionNumb(String jsonFile) {
			try {
				JSONObject root = new JSONObject(jsonFile);
				JSONObject app = root.getJSONObject(json_root_app);
				JSONObject ver = app.getJSONObject(json_root_version);
				return ver.getString(json_version_numb);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		private static String getAppVersionTag(String jsonFile) {
			try {
				JSONObject root = new JSONObject(jsonFile);
				JSONObject app = root.getJSONObject(json_root_app);
				JSONObject ver = app.getJSONObject(json_root_version);
				return ver.getString(json_version_tag);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		private static String getDonate(String jsonFile) {
			try {
				JSONObject root = new JSONObject(jsonFile);
				return root.getString(json_donate);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		private static String getGitHub(String jsonFile) {
			try {
				JSONObject root = new JSONObject(jsonFile);
				return root.getString(json_github);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		private static String[] getDevMails(String jsonFile) {
			try {
				JSONObject root = new JSONObject(jsonFile);
				JSONObject mail = root.getJSONObject(json_root_mail);
				JSONArray dev = mail.getJSONArray(json_mail_dev);

				String[] arr = new String[dev.length()];
				for (int i = 0; i < arr.length; i++)
					arr[i] = dev.getString(i);
				return arr;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		private static String[] getOtherMails(String jsonFile) {
			try {
				JSONObject root = new JSONObject(jsonFile);
				JSONObject mail = root.getJSONObject(json_root_mail);
				JSONArray other = mail.getJSONArray(json_mail_other);

				String[] arr = new String[other.length()];
				for (int i = 0; i < arr.length; i++)
					arr[i] = other.getString(i);
				return arr;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}


}
