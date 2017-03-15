package net.henryco.opalette.api.utils.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

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


	@NonNull @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		if (dialogTitle != null) dialogBuilder.setTitle(dialogTitle);
		if (dialogMessage != null) dialogBuilder.setMessage(dialogMessage);

		dialogBuilder.setPositiveButton(positive, (dialog, which) -> onPositive.run());
		if (onNegative != null && negative != null)
			dialogBuilder.setNegativeButton(negative, (dialog, which) -> onNegative.run());
		if (onNeutral != null && neutral != null)
			dialogBuilder.setNeutralButton(neutral, (dialog, which) -> onNeutral.run());
		return  dialogBuilder.create();
	}


	public OPallAlertDialog message(String dialogMessage) {
		this.dialogMessage = dialogMessage;
		return this;
	}

	public OPallAlertDialog title(String dialogTitle) {
		this.dialogTitle = dialogTitle;
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

}
