package net.henryco.opalette.application.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;



/**
 * Created by HenryCo on 14/03/17.
 */

public class PickImageDialog extends AppCompatDialogFragment {


	public interface PickImageDialogListener {
		void dialogSelectedCamera(PickImageDialog dialog);
		void dialogSelectedGallery(PickImageDialog dialog);
	}

	private PickImageDialogListener listener;

	@NonNull @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		dialogBuilder.setTitle("Select image source");
		dialogBuilder.setItems(new String[]{"Camera", "Gallery"}, (dialog, which) -> {
			if (which == 0) listener.dialogSelectedCamera(this);
			else if (which == 1) listener.dialogSelectedGallery(this);
		});
		return dialogBuilder.create();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listener = (PickImageDialogListener) context;
		} catch (ClassCastException e) {
			throw new RuntimeException(context.getClass().getName()+
					" have to implements PickImageDialogListener"
			);
		}
	}
}
