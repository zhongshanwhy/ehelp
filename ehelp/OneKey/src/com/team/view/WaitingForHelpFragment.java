package com.team.view;

import com.teamwork.onekey.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WaitingForHelpFragment extends Fragment{
	
	private Button back;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.waiting_for_help_fragment,parent,false);
		return view;
	}

}
