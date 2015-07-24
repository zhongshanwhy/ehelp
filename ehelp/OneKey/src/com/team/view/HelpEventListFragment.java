package com.team.view;

import java.util.ArrayList;

import model.HelpEvent;
import model.User;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.teamwork.onekey.R;
import controller.HelpEventListAdapter;
import controller.UserListAdapter;

public class HelpEventListFragment extends ListFragment{
    private HelpEventListAdapter msgListManager;
    @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
      View view = inflater.inflate(R.layout.msg_list_fragment, parent,false);
      ArrayList<HelpEvent> temp = new ArrayList<HelpEvent>();
      temp.add(new HelpEvent("gg",new User("隔壁老王",40,"13800138000","心理医生","身强体壮，精力旺盛")));
      msgListManager = new HelpEventListAdapter(getActivity(),temp);
      setListAdapter(msgListManager);
      return view;
  }
    
    @Override
    public void onListItemClick(ListView list,View view,int postion,long id){
    	getFragmentManager().beginTransaction().replace(R.id.messagelist, new HelpEventInfoFragment()).addToBackStack("").commit();
    }
}
