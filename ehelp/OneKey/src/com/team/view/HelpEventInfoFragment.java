package com.team.view;

import com.teamwork.onekey.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HelpEventInfoFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.msg_info_fragment, parent,false);
        return view;
    }
}
