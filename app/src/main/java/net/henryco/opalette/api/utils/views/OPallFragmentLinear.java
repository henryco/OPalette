package net.henryco.opalette.api.utils.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by HenryCo on 18/03/17.
 */

public abstract class OPallFragmentLinear extends Fragment {



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		return linearLayout;
	}


}
