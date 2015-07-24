package com.team.view;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.HttpMsg;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import com.teamwork.onekey.R;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SignFragment extends Fragment{
    
	private static final int GET_CHECK_CODE = 0;
	private static final int  CHECK_CODE_SUBMIT = 1;
	private static final int CHECK_CODE_ERROR= 2;
	
    private EditText phoneInput;
    private EditText nameInput;
    private EditText passwordInput;
    private EditText passwordAgain;
    private EditText checkcodeInput;
    private Button checkcodeButton;
    private Button sendCheckcode;
    private String phoneNum;
    private String name;
    private String password;
    private boolean isPhoneOK = false;
    private boolean isNameOK = false;
    private boolean isPasswordOK = false;
    private boolean isPasswordAgainOK = false;
    
    // 填写从短信SDK应用后台注册得到的APPKEY
 	private static String APPKEY = "8f22c61e85e0";

 	// 填写从短信SDK应用后台注册得到的APPSECRET
 	private static String APPSECRET = "abf038e7dbd01679a6c51ff3d74723bc";
 	
    public static SignFragment instance;
    private EventHandler eventHandler;
    private Handler signFragmentHandler;
    

  
  public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
      View view = inflater.inflate(R.layout.sign_fragment, parent,false);
      final LinearLayout ll  = (LinearLayout)view.findViewById(R.id.ll);
      
      signFragmentHandler = new Handler(){
          @Override
          public void handleMessage(Message msg){
              if(msg.what==GET_CHECK_CODE){
            	  checkcodeInput.setVisibility(View.VISIBLE);
				  sendCheckcode.setVisibility(View.VISIBLE);
                  Toast.makeText(getActivity(), "验证码已发送", Toast.LENGTH_LONG).show();
              } else if(msg.what ==  CHECK_CODE_SUBMIT){
            	  checkcodeButton.setVisibility(View.GONE);
				  checkcodeInput.setVisibility(View.GONE);
				  sendCheckcode.setVisibility(View.GONE);
				  nameInput.setVisibility(View.VISIBLE);
				  passwordInput.setVisibility(View.VISIBLE);
				  passwordAgain.setVisibility(View.VISIBLE);
				  Toast.makeText(getActivity(), "验证码提交成功", Toast.LENGTH_LONG).show();
              } else if(msg.what == CHECK_CODE_ERROR){
            	  Toast.makeText(getActivity(), "验证码错误", Toast.LENGTH_LONG).show();
              }
          }
      };
      //((MainActivity)getActivity()).setSignFragmentHandler(signFragmentHandler);
      
      ll.setOnTouchListener(new OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ll.setFocusable(true);
            ll.setFocusableInTouchMode(true);
            ll.requestFocus();
            InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
        }});
      
      
      
      Button back = (Button)view.findViewById(R.id.back);
      back.setOnClickListener(new OnClickListener(){
          public void onClick(View v){
              getFragmentManager().popBackStack();
          }
      });
      
      phoneInput = (EditText)view.findViewById(R.id.phone_input);
      phoneInput.setOnFocusChangeListener(new OnFocusChangeListener(){

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus)
            isPhoneOK = isMobileNum(phoneInput.getText().toString().trim()); 
        }});
      
      
      
      //短信验证
      initSDK();
      checkcodeButton = (Button)view.findViewById(R.id.checkcode_button);
      checkcodeButton.setOnClickListener(new OnClickListener(){

        @Override
        public void onClick(View v) {
            if(!isPhoneOK)Toast.makeText(getActivity(), "请输入有效的手机号码", Toast.LENGTH_LONG).show();
            else {
            	phoneNum = phoneInput.getText().toString().trim();
                Log.d("phone","+86"+phoneInput.getText().toString().trim());
            	SMSSDK.getVerificationCode("86",phoneNum);
 				Log.d("sign","sendMessage");
            	//registerPage.show(getActivity());
            }
            
        }});
      
      checkcodeInput = (EditText)view.findViewById(R.id.checkcode_input);
      sendCheckcode  = (Button)view.findViewById(R.id.send_checkcode);
      sendCheckcode.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			 SMSSDK.submitVerificationCode("86", phoneNum, checkcodeInput.getText().toString().trim());
		}
    	  
      });
      
      nameInput = (EditText)view.findViewById(R.id.name_input);
      nameInput.setOnFocusChangeListener(new OnFocusChangeListener(){

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            isNameOK = false;
            if(!hasFocus){
                if(nameInput.getText().toString().trim().length()<2){
                    Toast.makeText(getActivity(), "用户名长度不可小于2", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    /*Intent intent = new Intent("Service");
                    intent.putExtra("requestType",RequestType.CHECK_USERNAME);
                    intent.putExtra("name", nameInput.getText().toString());
                    getActivity().sendBroadcast(intent);*/
                	name = nameInput.getText().toString().trim();
                	isNameOK = true;
                } 
            }
        }
          
      });
      
      passwordInput = (EditText)view.findViewById(R.id.password_input);
      passwordInput.setOnFocusChangeListener(new OnFocusChangeListener(){

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
               if(passwordInput.getText().length()<4){
                  Toast.makeText(getActivity(), "密码长度不可小于4", Toast.LENGTH_LONG).show();
                   isPasswordOK = false;
               } else {
                   //passwordHint.setText("OK");
                   //passwordHint.setTextColor(Color.GREEN);
            	   password = passwordInput.getText().toString().trim();
                   isPasswordOK = true;
               }
            }
        }});
      
      passwordAgain = (EditText)view.findViewById(R.id.password_again);
      passwordAgain.setOnFocusChangeListener(new OnFocusChangeListener(){

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                if(passwordAgain.getText().toString().trim().equals(passwordInput.getText().toString().trim())){
                    //passwordAgainHint.setText("OK");
                    //passwordAgainHint.setTextColor(Color.GREEN);
                    isPasswordAgainOK = true;
                } else {
                   Toast.makeText(getActivity(), "密码不匹配", Toast.LENGTH_LONG).show();
                    isPasswordAgainOK = false;
                }
            } 
        }});
      
      
      Button submit  = (Button)view.findViewById(R.id.submit);
      submit.setOnClickListener(new OnClickListener(){
          public void onClick(View v){
              if(isNameOK&&isPasswordOK&&isPasswordAgainOK){
                  Intent intent = new Intent("MainService");
                  HttpMsg signRequest = new HttpMsg(HttpMsg.REQ_SIGN,phoneNum,name,password);
                  intent.putExtra("HttpMsg", signRequest);
                  getActivity().sendBroadcast(intent);
              }
          }
      });
      
      return view;
  }
  
  
  private void initSDK() {
		 SMSSDK.initSDK(getActivity(), APPKEY, APPSECRET);
			eventHandler = new EventHandler(){
				@Override
				public void afterEvent(int event, int result, Object data) {
				   if (result == SMSSDK.RESULT_ERROR){
					   Message.obtain(signFragmentHandler, CHECK_CODE_ERROR).sendToTarget();
					   return;
		             }
				   if (result == SMSSDK.RESULT_COMPLETE) {
					//回调完成
					   Log.d("SMSSDK","回调完成");
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
	                //提交验证码成功
						Message.obtain(signFragmentHandler, CHECK_CODE_SUBMIT).sendToTarget();
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
				    //获取验证码成功
						Message.obtain(signFragmentHandler, GET_CHECK_CODE).sendToTarget();
						Log.d("SMSSDK","获取验证码成功");
					} else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
	                //返回支持发送验证码的国家列表
	                } 
	              } else {                                                                 
	                 ((Throwable)data).printStackTrace(); 
	          }
	      } 
	   }; 
	   SMSSDK.registerEventHandler(eventHandler); 
	}
  
  public  boolean isMobileNum(String num){
      Pattern p = Pattern.compile("^(1[3,4,5,7,8][0-9])\\d{8}$"); 
      Matcher m = p.matcher(num); 
      return m.matches();
  } 
  
  @Override
  public void onDestroy(){
	  super.onDestroy();
	  SMSSDK.unregisterEventHandler(eventHandler);
	  Log.d("signfragment","onDestory()");
  }

public static SignFragment newInstance(){
    if(instance==null){
        instance = new SignFragment();
    }
    return instance;
}
}

