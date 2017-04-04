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

package net.henryco.opalette.api.utils.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;



/**
 * Created by HenryCo on 04/04/17.
 */

public class OPallOptionListDialog extends AppCompatDialogFragment {


	private String tittle = "";
	private String[] optionsNamesList;
	private Runnable[] optionsActionsList;

	public OPallOptionListDialog setOptionsNames(String ... names) {
		this.optionsNamesList = names;
		return this;
	}

	public OPallOptionListDialog setOptionsActions(Runnable ... actions) {
		this.optionsActionsList = actions;
		return this;
	}

	public OPallOptionListDialog set(String tittle, String[] names, Runnable ... actions) {
		return setTittle(tittle).setOptionsNames(names).setOptionsActions(actions);
	}

	public OPallOptionListDialog setTittle(String tittle) {
		this.tittle = tittle;
		return this;
	}

	@NonNull @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		dialogBuilder.setTitle(tittle);
		dialogBuilder.setItems(optionsNamesList, (dialog, which) -> {
			if (which < optionsActionsList.length) optionsActionsList[which].run();
		});
		return dialogBuilder.create();
	}
}
