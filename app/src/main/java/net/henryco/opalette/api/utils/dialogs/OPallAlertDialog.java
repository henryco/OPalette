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

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HenryCo on 15/03/17.
 */

public class OPallAlertDialog extends AppCompatDialogFragment {


	private String dialogMessage = null;
	private String dialogTitle = "";
	private String positive = "accept";
	private String negative = null;
	private String neutral = null;

	private Runnable onPositive = () -> {};
	private Runnable onNegative = null;
	private Runnable onNeutral = null;

	private View dialogContent = null;

	private float size = 0;

	@NonNull @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		if (dialogTitle != null) dialogBuilder.setTitle(dialogTitle);
		if (dialogMessage != null) dialogBuilder.setMessage(dialogMessage);
		if (dialogContent != null) dialogBuilder.setView(dialogContent);

		dialogBuilder.setPositiveButton(positive, (dialog, which) -> onPositive.run());
		if (onNegative != null && negative != null)
			dialogBuilder.setNegativeButton(negative, (dialog, which) -> onNegative.run());
		if (onNeutral != null && neutral != null)
			dialogBuilder.setNeutralButton(neutral, (dialog, which) -> onNeutral.run());
		return  dialogBuilder.create();
	}

	public OPallAlertDialog content(View content) {
		this.dialogContent = content;
		return this;
	}

	public OPallAlertDialog message(String dialogMessage) {
		this.dialogMessage = dialogMessage;
		return this;
	}

	public OPallAlertDialog title(String dialogTitle) {
		this.dialogTitle = dialogTitle;
		return this;
	}

	public OPallAlertDialog size(float pct) {
		this.size = pct;
		return this;
	}

	public OPallAlertDialog positive(String positive) {
		return positive(positive, () -> {});
	}
	public OPallAlertDialog positive(String positive, Runnable action) {
		this.positive = positive;
		this.onPositive = action;
		return this;
	}
	public OPallAlertDialog negative(String negative) {
		return negative(negative, () -> {});
	}
	public OPallAlertDialog negative(String negative, Runnable action) {
		this.negative = negative;
		this.onNegative = action;
		return this;
	}
	public OPallAlertDialog neutral(String neutral) {
		return neutral(neutral, () -> {});
	}
	public OPallAlertDialog neutral(String neutral, Runnable action) {
		this.neutral = neutral;
		this.onNeutral = action;
		return this;
	}

	@Override
	public void onStart() {
		super.onStart();
		Dialog dialog = getDialog();
		if (dialog != null && size != 0)
			dialog.getWindow().setLayout((int) (getScreenWidth(getActivity()) * size), ViewGroup.LayoutParams.MATCH_PARENT);
	}

	private static int getScreenWidth(Activity activity) {
		Point size = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(size);
		return size.x;
	}
}
