package controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import model.HttpMsg;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class MainService extends Service implements Runnable{
    
    private class MsgReceiverForService extends BroadcastReceiver{
        @Override
        public void onReceive(Context context,Intent intent){
        	    Log.d("service","receive a request");
            	sendToServer((HttpMsg)intent.getSerializableExtra("HttpMsg"));
        }
    }
    
    private HttpClient client = new DefaultHttpClient();
	private HttpPost postMethod = new HttpPost("http://10.0.2.2:9334");
    //private HttpPost postMethod = new HttpPost("http://192.168.191.1:9334");
    private MsgReceiverForService MsgReceiver = new MsgReceiverForService(); 
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void onCreate() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("MainService");
        registerReceiver(MsgReceiver,filter);
        Log.d("service","service receiver regist");
        new Thread(MainService.this).start();
        Log.d("service","run");
    }
    
    @Override
    public void run(){
    	HttpClient cometClient = new DefaultHttpClient();
        HttpPost cometPost = new HttpPost("http://10.0.2.2:9334");
        HttpMsg cometMsg = HttpMsg.getCometRequest();
        List<NameValuePair> params = cometMsg.convertToHttpParams();
        Log.d("service","name:"+params.get(0).getName()+" value:"+params.get(0).getValue());
        try {
        	cometPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
			HttpResponse cometResponse;
	        while(true){
	        	Log.d("service","server comet");
	        	cometResponse = cometClient.execute(cometPost);
	        	if(cometResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
	        		String respondStr = EntityUtils.toString(cometResponse.getEntity());
	        		Intent msgToActivity = new Intent("MainActivity");
					msgToActivity.putExtra("respond", respondStr);
					sendBroadcast(msgToActivity);
	        	}
	        }
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    public void sendToServer(final HttpMsg request){
    	final List<NameValuePair> params = request.convertToHttpParams();
		new Thread(new Runnable(){
			public void run(){
				HttpResponse response;
					try {
						postMethod.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
						response = client.execute(postMethod);
						if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
							String respondStr = EntityUtils.toString(response.getEntity());
							Log.d("http respond","response:"+respondStr);
							Intent msgToActivity = new Intent("MainActivity");
							msgToActivity.putExtra("respond", respondStr);
							sendBroadcast(msgToActivity);
						}else{
							System.out.println("");
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
			}
		}).start();
	}
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	unregisterReceiver(MsgReceiver);
    	Log.d("Service","onDestroy()");
    }
}
