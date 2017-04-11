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

package net.henryco.opalette.application;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import net.henryco.opalette.R;
import net.henryco.opalette.api.utils.Utils;
import net.henryco.opalette.application.conf.GodConfig;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupActionBar();
		getFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content, new GeneralPreferenceFragment())
				.commit();
	}


	private void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.DARK)));
			actionBar.setDisplayHomeAsUpEnabled(true);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.DARK));
				actionBar.setElevation(10);
			}
		}
	}


	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) finish();
		return true;
	}


	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this);
	}



	protected boolean isValidFragment(String fragmentName) {
		return PreferenceFragment.class.getName().equals(fragmentName)
				|| GeneralPreferenceFragment.class.getName().equals(fragmentName);
	}


	public static class GeneralPreferenceFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference);
			setHasOptionsMenu(true);
			PreferenceManager.setDefaultValues(getActivity(), R.xml.preference, false);

			String VERSION = Utils.getSourceAssetsText(GodConfig.DEF_VERSION_FILE, getActivity());
			String MAILTO = Utils.getLineFromString(Utils.getSourceAssetsText(GodConfig.DEF_MAILTO_FILE, getActivity()), 0);
			findPreference(GodConfig.PREF_KEY_VERSION).setSummary(VERSION);
			findPreference(GodConfig.PREF_KEY_ADS_ENABLE).setOnPreferenceChangeListener((preference, newValue) -> {
				if (!(boolean) newValue)
					new AlertDialog.Builder(getActivity())
							.setTitle(R.string.dialog_are_u_sure)
							.setMessage(R.string.dialog_disable_ads_info)
							.setNegativeButton(R.string.dialog_keep_enabled, (dialog, which) -> {
								getPreferenceManager().getSharedPreferences().edit().putBoolean(GodConfig.PREF_KEY_ADS_ENABLE, true).apply();
								((SwitchPreference) preference).setChecked(true);
							}).setPositiveButton(R.string.dialog_disable_anyway, (dialog, which) -> {
								getPreferenceManager().getSharedPreferences().edit().putBoolean(GodConfig.PREF_KEY_ADS_ENABLE, false).apply();
								((SwitchPreference) preference).setChecked(false);
							}).create().show();
				return (boolean) newValue;
			});
			findPreference(GodConfig.PREF_KEY_CONTACT).setOnPreferenceClickListener(preference -> {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{MAILTO});
				i.putExtra(Intent.EXTRA_SUBJECT, "");
				i.putExtra(Intent.EXTRA_TEXT   , "");
				try {
					startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(getActivity(), R.string.pref_no_email_installed, Toast.LENGTH_SHORT).show();
				}
				return true;
			});
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			if (id == android.R.id.home) {
				startActivity(new Intent(getActivity(), SettingsActivity.class));
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
	}




}
