package controller;

import java.util.ArrayList;
import com.teamwork.onekey.R;
import model.User;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class UserListAdapter extends BaseAdapter{
   
    private Context context;
    private ArrayList<User> friendList;
    private ArrayList<User> tempList;
    private boolean flag;
    
    public UserListAdapter(Context context,ArrayList<User> friendList){
        this.context = context;
        this.friendList = friendList;
        tempList = new ArrayList<User>();
        /*
         * if flag == true: the original list is being used
         * else : a temp list is being used
         */
        flag = true;
    }
    
    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout rl = new RelativeLayout(context);
        
        ImageView avatar = new ImageView(context);
        avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
        avatar.setId(2);
        LayoutParams avatar_lp = new LayoutParams(60,60);
        avatar_lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        avatar_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        avatar.setLayoutParams(avatar_lp);
        rl.addView(avatar);
        
        TextView name = new TextView(context);
        name.setText(friendList.get(position).getName());
        name.setId(3);
        LayoutParams name_lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        //name_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        name_lp.addRule(RelativeLayout.RIGHT_OF,2);
        name.setLayoutParams(name_lp);
        rl.addView(name);
        
        TextView resume = new TextView(context);
        resume.setText(friendList.get(position).getCareer());
        resume.setId(4);
        LayoutParams resume_lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        resume_lp.addRule(RelativeLayout.BELOW,3);
        resume_lp.addRule(RelativeLayout.ALIGN_LEFT,3);
        resume.setLayoutParams(resume_lp);
        rl.addView(resume);   
        
        return rl;
    }
    
    public void addItem(User user){
        friendList.add(user);
        notifyDataSetChanged();
    }

	public void useTempList() {
		ArrayList<User> temp = friendList;
		friendList = tempList;
		tempList = temp;
		flag = false;
		notifyDataSetChanged();
	}
	
	public void useOriginalList() {
		friendList = tempList;
		tempList.clear();
		flag = true;
		notifyDataSetChanged();
	}
	
	public boolean isUsingTempList(){
		return flag;
	}

}
