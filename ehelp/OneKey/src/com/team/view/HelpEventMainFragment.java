package com.team.view;

import com.teamwork.onekey.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class HelpEventMainFragment extends Fragment{
	private TabHost myTabHost;
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.msg_main_fragment, parent,false);
		myTabHost = (TabHost)view.findViewById(R.id.mytabhost);
		myTabHost.setup();
		myTabHost.addTab(myTabHost.newTabSpec("allmsg").setIndicator("全部信息").setContent(R.id.allmsg));
		myTabHost.addTab(myTabHost.newTabSpec("joinmsg").setIndicator("我参与的").setContent(R.id.joinmsg));
		myTabHost.addTab(myTabHost.newTabSpec("mymsg").setIndicator("我发出的").setContent(R.id.mymsg));
		myTabHost.addTab(myTabHost.newTabSpec("nearbymsg").setIndicator("附近消息")
		        .setContent(R.id.nearbymsg));
		getFragmentManager().beginTransaction().add(R.id.allmsg, new HelpEventListFragment()).commit();
		return view;
	}

}
