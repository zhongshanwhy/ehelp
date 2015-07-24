package com.team.view;

import model.HttpMsg;
import model.User;
import org.json.JSONException;
import org.json.JSONObject;
import com.teamwork.onekey.R;
import controller.MainService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {
    LoginFragment loginFragment = new LoginFragment();
    LoadingFragment loadingFragment = new LoadingFragment();
    MainFragment mainFragment;
    MyDialogFragment exitDialog = MyDialogFragment.getExitDialog(null);
    
    private Handler notifierForUserListFragment;
    
    private User host;
    
    private class MsgReceiverForMainActivity extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            
            try {
                JSONObject respond = new JSONObject(intent.getStringExtra("respond"));
                switch(respond.getInt("respondType")){
                  
                  case HttpMsg.RES_LOGIN_OK:
                      host = new User(respond.getString("name"),22,respond.getString("phone"),
                                "","",User.SELF);
                      mainFragment =  MainFragment.getInstance(host);
                    getFragmentManager().beginTransaction().
                    replace(R.id.maincontent, mainFragment).commit();  
                    //getFragmentManager().beginTransaction().
                      //replace(R.id.maincontent, loadingFragment).commit();
                    break;
                  
                  case HttpMsg.RES_LOGIN_FAIL:
                    Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                    break;
                    
                  case HttpMsg.RES_SIGN_OK:
                      getFragmentManager().popBackStack();
                      Toast.makeText(MainActivity.this, "注册成功.请登录", Toast.LENGTH_LONG).show();
                      break;
                      
                  case HttpMsg.RES_GET_PINFO_OK:
                      User user = new User(respond.getString("name"),22,respond.getString("phone"),"","");
                      Message.obtain(notifierForUserListFragment, HttpMsg.RES_GET_PINFO_OK, user).sendToTarget();
                      break;
                      
                  case HttpMsg.RES_USER_NOT_EXIT:
                      Toast.makeText(MainActivity.this, "用户"+respond.getString("name")+"不存在", Toast.LENGTH_LONG).show();
                      break;
                  default:break;
                }
                
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }
    
    private MsgReceiverForMainActivity MsgReceiver = new MsgReceiverForMainActivity();
    
    
    private ServiceConnection conn  = new ServiceConnection(){
          public void onServiceConnected(ComponentName name, IBinder service) {
              // TODO Auto-generated method stub
              //countService = ((CountService.ServiceBinder) service).getService();

          }

          /** 无法获取到服务对象时的操作 */
          public void onServiceDisconnected(ComponentName name) {
              Toast.makeText(MainActivity.this, "应用服务开启失败，请重启应用", Toast.LENGTH_LONG).show();
          }
    };
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        
        //login page
        getFragmentManager().beginTransaction().add(R.id.maincontent, loginFragment).commit();

        //mainFragment = MainFragment.getInstance(
                 // new User("隔壁老王",40,"13800138000","心理医生","身强体壮，精力旺盛"));
       // getFragmentManager().beginTransaction().add(R.id.maincontent, mainFragment).commit();

        IntentFilter filter = new IntentFilter();
        filter.addAction("MainActivity");
        registerReceiver(MsgReceiver,filter);
        Intent intent = new Intent(MainActivity.this,MainService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }
    
    public void setNotifierForUserListFragment(Handler notifier) {
    	notifierForUserListFragment = notifier;
    }
    
    
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(MsgReceiver);
        unbindService(conn);
        Log.d("MainActivity","onDestroy()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed(){
    if(mainFragment!=null){
        if(mainFragment.getFragmentManager().getBackStackEntryCount()==0){
            exitDialog.show(getFragmentManager(), "exitDialog");
            return;
        } else {
            super.onBackPressed();
            return;
        }
    } else if(loginFragment!=null){
            exitDialog.show(getFragmentManager(), "exitDialog");
            return;
        }
     super.onBackPressed();
        
        
    }
}
