package com.telecomexperience.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public final class ImageFragment extends Fragment {
	private static final String KEY_CONTENT = "TestFragment:Content";
	
	public static ImageFragment newInstance(int content) {
		ImageFragment fragment = new ImageFragment();
		fragment.mContent = content;
		return fragment;
	}
	
	private int mContent = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getInt(KEY_CONTENT);
		}
		ImageView image = new ImageView(getActivity());
		image.setPadding(20, 20, 20, 20);
		image.setImageResource(mContent);
		LinearLayout layout = new LinearLayout(getActivity());
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layout.setGravity(Gravity.CENTER);
		layout.addView(image);
		return layout;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_CONTENT, mContent);
	}
}
