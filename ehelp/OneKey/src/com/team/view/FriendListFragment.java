package com.team.view;

import java.util.ArrayList;

import model.HttpMsg;
import model.User;
import com.teamwork.onekey.R;
import controller.UserListAdapter;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;

public class FriendListFragment extends ListFragment{
    private SearchView searchPanel;
    private UserListAdapter friendListManager;
    private Handler listener;
  
  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
      View view = inflater.inflate(R.layout.friend_list_fragment, parent,false);
      User user = new User("隔壁老王",40,"13800138000","心理医生","身强体壮，精力旺盛");
      ArrayList<User> friendlist = new ArrayList<User>();
      friendlist.add(user);
      friendlist.add(new User("隔壁老张",30,"8008208820","心理医生","身强体壮，精力旺盛"));
      friendListManager = new UserListAdapter(getActivity(),friendlist);
       setListAdapter(friendListManager);
      
      //debug for virtual machine which some bugs exist when using SearchView
      final EditText searchTV = (EditText)view.findViewById(R.id.search_et);
      Button searchBT = (Button)view.findViewById(R.id.search_bt);
      searchBT.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
				Intent intentToService = new Intent("MainService");
				HttpMsg getUser = new HttpMsg(HttpMsg.REQ_GET_PINFO,"");
				intentToService.putExtra("HttpMsg", getUser);
				getActivity().sendBroadcast(intentToService);
		}});
      
      
      //setting of searchView
      /*searchPanel = (SearchView)view.findViewById(R.id.search);
      searchPanel.setSubmitButtonEnabled(true);
      searchPanel.setOnQueryTextListener(new OnQueryTextListener(){

		@Override
		public boolean onQueryTextChange(String content) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onQueryTextSubmit(String content) {
			Intent intentToService = new Intent("MainService");
			HttpMsg getUser = new HttpMsg(HttpMsg.REQ_GET_PINFO,content);
			intentToService.putExtra("MyMessage", getUser);
			getActivity().sendBroadcast(intentToService);
			return true;
		}});
		*/
      
      listener = new Handler(){
    	  @Override
    	  public void handleMessage(Message msg){
    		  Log.d("friendlist handler","respond:"+msg.what);
    		  switch(msg.what) {
    		    case HttpMsg.RES_GET_PINFO_OK:
    			  friendListManager.useTempList();
    			  friendListManager.addItem((User)msg.obj);
    			  break;
    			default:break;
              }
    	  }};
    	  ((MainActivity)getActivity()).setNotifierForUserListFragment(listener);
      
      return view;
  }
  
  @Override
  public void onListItemClick(ListView lv,View view,int position,long id){
	  PersonInfoFragment hostInfoFragment = PersonInfoFragment.getInstance((User)friendListManager.getItem(position));
	  getFragmentManager().beginTransaction().replace(R.id.friendlist, hostInfoFragment).addToBackStack(null).commit();
  }
}
