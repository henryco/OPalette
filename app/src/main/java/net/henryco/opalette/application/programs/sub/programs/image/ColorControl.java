package net.henryco.opalette.application.programs.sub.programs.image;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import net.henryco.opalette.R;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend.EdTexture;
import net.henryco.opalette.api.utils.OPallUtils;
import net.henryco.opalette.api.utils.listener.OPallListener;
import net.henryco.opalette.api.utils.listener.OPallListenerHolder;
import net.henryco.opalette.api.utils.views.OPallViewInjector;
import net.henryco.opalette.application.activities.MainActivity;

/**
 * Created by HenryCo on 16/03/17.
 */

public class ColorControl extends OPallViewInjector<MainActivity> implements OPallListenerHolder<EdTexture> {


	private ImageButton imageButton;
	private OPallListener<EdTexture> listener;

	public ColorControl(OPallListener<EdTexture> listener) {
		this();
		setOPallListener(listener);
	}
	public ColorControl() {
		super(R.id.scrollContainer, R.layout.image_option);
	}

	@Override
	protected void onInject(MainActivity context, View view) {

		TextView textView = (TextView) view.findViewById(R.id.iopTextView);
		textView.setText("color");

		imageButton = (ImageButton) view.findViewById(R.id.iopImageButton);
		imageButton.setImageResource(R.drawable.ic_translate_white_24dp);
		imageButton.setClickable(false);

	}

	@Override
	protected void onPostInject(MainActivity context, View view) {

		view.setOnClickListener(v ->
				OPallUtils.pressButton75_225(context, imageButton, () -> {
					synchronized (context) {
						context.runOnUiThread(() -> context.switchToFragmentOptions(new ControlFragment()));
					}
				})
		);

	}

	@Override
	public void setOPallListener(OPallListener<EdTexture> listener) {
		this.listener = listener;
	}





	public static class ControlFragment extends Fragment {

		// TODO: Rename parameter arguments, choose names that match
		// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
		private static final String ARG_PARAM1 = "param1";
		private static final String ARG_PARAM2 = "param2";

		// TODO: Rename and change types of parameters
		private String mParam1;
		private String mParam2;


		public ControlFragment() {
			// Required empty public constructor
		}

		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @param param1 Parameter 1.
		 * @param param2 Parameter 2.
		 * @return A new instance of fragment ProtoFragment.
		 */
		// TODO: Rename and change types and number of parameters
		public static ControlFragment newInstance(String param1, String param2) {
			ControlFragment fragment = new ControlFragment();
			Bundle args = new Bundle();
			args.putString(ARG_PARAM1, param1);
			args.putString(ARG_PARAM2, param2);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if (getArguments() != null) {
				mParam1 = getArguments().getString(ARG_PARAM1);
				mParam2 = getArguments().getString(ARG_PARAM2);
			}
		}


		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.control_image_color, container, false);
		}


		@Override
		public void onAttach(Context context) {
			super.onAttach(context);
		}

		@Override
		public void onDetach() {
			super.onDetach();
		}

	}

}
