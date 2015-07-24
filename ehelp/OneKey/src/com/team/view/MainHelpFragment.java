package com.team.view;


import com.teamwork.onekey.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainHelpFragment extends Fragment{
	private Button FirstAidButton;
	private Button SeekHelpButton;
	private Button QuestionButton;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.main_help_fragment,parent,false);
        
        FirstAidButton = (Button) view.findViewById(R.id.first_aid_button);
        FirstAidButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LookForFirstAidFragment aidfragment = new LookForFirstAidFragment();
				getFragmentManager().beginTransaction().
				                     replace(R.id.mainhelp, aidfragment).addToBackStack(null).
				                     commit();
			}
        	
        });
        
        SeekHelpButton = (Button)view.findViewById(R.id.seek_help_button);
        SeekHelpButton.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                .replace(R.id.mainhelp, new LookForHelpFragment())
                .addToBackStack("").commit();
                
            }});
        
        QuestionButton  =(Button)view.findViewById(R.id.question_button);
        QuestionButton.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                .replace(R.id.mainhelp, new QuestionFragment())
                .addToBackStack("").commit();
                
            }});
        
        return view;
        
       
    }

}
