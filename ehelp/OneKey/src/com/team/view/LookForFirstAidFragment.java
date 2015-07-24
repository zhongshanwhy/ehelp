package com.team.view;

import com.teamwork.onekey.R;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LookForFirstAidFragment extends Fragment {
	private TextView countdownTextView;
	private int duration;
	private Handler uiHandler;
	private Button cancel;
	private Thread timer;
	private boolean threadFlag = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.look_for_first_aid_fragment,
				parent, false);
		
		
		duration = 5;
		countdownTextView = (TextView) view
				.findViewById(R.id.countdownTextView);
		countdownTextView.setText(""+duration);
		
        timer = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(--duration > -1){
					Message msg = new Message();
					msg.what = duration;
					uiHandler.sendMessage(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
      cancel = (Button)view.findViewById(R.id.cancel);
      cancel.setOnClickListener(new OnClickListener(){

	    @Override
	    public void onClick(View arg0) {
	    	threadFlag = false;
		  getFragmentManager().popBackStack();
		
	    }
	
       });

		uiHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what > 0){
					countdownTextView.setText(""+duration);
				} 
				
				if(msg.what == 0 && threadFlag) {
					getFragmentManager().popBackStack();
					getFragmentManager().beginTransaction().
					replace(R.id.mainhelp, new WaitingForHelpFragment()).
					addToBackStack(null).commit();
				}
			}

		};
		
		
		timer.start();
		return view;
	}
	
	
}
