package com.team.view;

import model.User;

import com.teamwork.onekey.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class MainFragment extends Fragment{
    private TabHost myTabHost;
    FragmentManager fragmentManager; 
    FragmentTransaction ft;
    HelpEventMainFragment msgFragment = new HelpEventMainFragment();
     MainHelpFragment mainHelpFragment = new MainHelpFragment();
    FriendListFragment friendListFragment = new FriendListFragment();
    PersonInfoFragment hostInfoFragment;
    Handler handler;
    private User host;
    
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.main_fragment, parent,false);
        
        handler = new Handler(){};
        myTabHost = (TabHost)view.findViewById(R.id.mytabhost);
        myTabHost.setup();
        myTabHost.addTab(myTabHost.newTabSpec("tab1").setIndicator("地图").setContent(R.id.tab1));
        myTabHost.addTab(myTabHost.newTabSpec("messagelist").setIndicator("信息").setContent(R.id.messagelist));
        myTabHost.addTab(myTabHost.newTabSpec("mainhelp").setIndicator("主页").setContent(R.id.mainhelp));
        myTabHost.addTab(myTabHost.newTabSpec("friendlist").setIndicator("通信录").setContent(R.id.friendlist));
        myTabHost.addTab(myTabHost.newTabSpec("individual").setIndicator("我").setContent(R.id.individual));
        myTabHost.setCurrentTab(2);
        
        hostInfoFragment = PersonInfoFragment.getInstance(host);
        
        fragmentManager = getFragmentManager();
        ft = fragmentManager.beginTransaction();
        ft.add(R.id.messagelist,msgFragment);
        ft.add(R.id.mainhelp,mainHelpFragment);
        ft.add(R.id.individual, hostInfoFragment);
        ft.add(R.id.friendlist, friendListFragment);
        ft.commit();


        return view;
    }
    
    public static MainFragment getInstance(User user){
    	MainFragment instance = new MainFragment();
    	instance.setHost(user);
    	return instance;
    }
    
    public void setHost(User user){
    	host = user;
    }
}
