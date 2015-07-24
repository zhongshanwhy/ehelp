package com.team.view;

import model.User;

import com.teamwork.onekey.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PersonInfoFragment extends Fragment{
	private User user;
	private TextView  namePanel;
	private TextView  jobPanel;
	private TextView  agePanel;
	private TextView  phonePanel;
	//private TextView  healthStatePanel;
	
    @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
      View view = inflater.inflate(R.layout.person_info_fragment, parent,false);
      namePanel = (TextView)view.findViewById(R.id.name_panel);
      namePanel.setText(user.getName());
  	  jobPanel= (TextView)view.findViewById(R.id.job_panel);
  	  jobPanel.setText(user.getCareer());
  	  agePanel= (TextView)view.findViewById(R.id.age_panel);
  	  agePanel.setText(""+user.getAge());
  	  phonePanel= (TextView)view.findViewById(R.id.phone_panel);
  	  phonePanel.setText(user.getPhoneNum());
  	  //healthStatePanel= (TextView)view.findViewById(R.id.healthstate);
  	  //healthStatePanel.setText(user.getHealthState());
      return view;
  }
    
    public static PersonInfoFragment getInstance(User user){
    	PersonInfoFragment instance = new PersonInfoFragment();
    	instance.user = user;
    	return instance;
    }
}
