package com.team.view;

import com.teamwork.onekey.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class QuestionFragment extends Fragment{
    
    private Button submit;
    private Button cancel;
    
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.question_fragment, parent,false);
        
        submit = (Button)view.findViewById(R.id.submit);
        submit.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                
            }});
        
        cancel = (Button)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                
            }
            
        });
        
        return view;
    }

}
