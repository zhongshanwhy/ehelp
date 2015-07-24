package com.team.view;

import com.teamwork.onekey.R;
import model.User;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MyDialogFragment extends DialogFragment{
  
   public static final int EXIT_DIALOG = 0;
   public static final int CHECKCODE_DIALOG = 1;
   private User host; 
   private int type;
   private String phoneNum;
   private EditText checkcodeInput;
   private Handler callback;
   
   public static MyDialogFragment getExitDialog(User user){
	   MyDialogFragment instance = new MyDialogFragment();
	   instance.setType(EXIT_DIALOG);
       instance.setHost(user);
       return instance;
   }
   
   public static MyDialogFragment getCheckCodeDialog(String phoneNum){
	   MyDialogFragment instance = new MyDialogFragment();
	   instance.setType(CHECKCODE_DIALOG);
	   instance.setPhoneNum(phoneNum);
       return instance;
   }
   
   public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
       if(EXIT_DIALOG == type) {
	      View view = inflater.inflate(R.layout.exit_dialog, parent,false);
          getDialog().setTitle("是否退出？");
          Button submit = (Button)view.findViewById(R.id.submit);
          submit.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
              /*Intent intent = new Intent("Service");
              intent.putExtra("loginFlag", true);
              intent.putExtra("name", host.getName());
              intent.putExtra("requestType", RequestType.EXIT);
              getActivity().sendBroadcast(intent);*/
              getActivity().finish();
            }});
          
          Button cancel = (Button)view.findViewById(R.id.cancel);
          cancel.setOnClickListener(new OnClickListener(){
             @Override
            public void onClick(View v) {
              MyDialogFragment.this.dismiss();  
            }});
          return view;
          
       } else if(CHECKCODE_DIALOG == type){
    	   View view = inflater.inflate(R.layout.check_code_dialog, parent,false);
    	   checkcodeInput = (EditText)view.findViewById(R.id.checkcode_input);
           Button submit = (Button)view.findViewById(R.id.submit);
           submit.setOnClickListener(new OnClickListener(){
             @Override
             public void onClick(View v) {
            	// Message.obtain(callback, HandlerMsgType.SIGN_CHECK_CODE, checkcodeInput.getText().toString().trim()).sendToTarget();
             }});
           
           Button cancel = (Button)view.findViewById(R.id.cancel);
           cancel.setOnClickListener(new OnClickListener(){
              @Override
             public void onClick(View v) {
               MyDialogFragment.this.dismiss();  
             }});
           return view;
       }
       
       return null;
   }
   
   public void setType(int type){
	   this.type = type;
   }
   
   public void setHost(User user){
	   this.host = user;
   }
   
   public void setPhoneNum(String phoneNum){
	   this.phoneNum = phoneNum;
   }
   
   public void setCallBackHandler(Handler handler){
	   this.callback = handler;
   }
}
