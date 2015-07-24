package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.text.format.Time;

public class HttpMsg implements Serializable{
  
   /**
	 * 
	 */
	private static final long serialVersionUID = 6170604588172474613L;
public final static int KEEP_ALIVE = -1;
   public final static int REQ_LOGIN = 0;
   public final static int REQ_SIGN = 1;
   public final static int REQ_FRIENDING = 2;
   public final static int REQ_DEL_FRIEND = 3;
   public final static int REQ_GET_PINFO = 4;
   public final static int REQ_GET_FRIENDLIST = 5;
   public final static int REQ_FIRST_AID = 6;
   public final static int REQ_FOR_HELP = 7;
   public final static int REQ_RAISE_QUESTION = 8;
   public final static int REQ_GET_QUESTIONLIST = 9;
   public final static int REQ_APPRAISE = 10;

   public final static int RES_LOGIN_OK = 0;
   public final static int RES_LOGIN_FAIL = 1;
   public final static int RES_SIGN_OK = 2;
   public final static int RES_GET_PINFO_OK = 3;
   public final static int RES_USER_NOT_EXIT = 4;

   private int type;
   private String arg1;
   private String arg2;
   private String arg3;
   
   public HttpMsg(){
	   this.type = KEEP_ALIVE;
   }
   
   public HttpMsg(int type,String arg1){
	   this.type = type;
	   this.arg1 = arg1;
   }
   
   public HttpMsg(int type,String arg1,String arg2){
	   this.type = type;
	   this.arg1 = arg1;
	   this.arg2 = arg2;
   }
   
   public HttpMsg(int type,String arg1,String arg2,String arg3){
	   this.type = type;
	   this.arg1 = arg1;
	   this.arg2 = arg2;
	   this.arg3 = arg3;
   }
   
   public static HttpMsg getCometRequest(){
         return  new HttpMsg();
   }
   
   
   public List<NameValuePair> convertToHttpParams() {
	   List<NameValuePair> params = new ArrayList<NameValuePair>();
	   params.add(new BasicNameValuePair("requestType",String.valueOf(type)));
	   switch(type) {
	   
	     case REQ_LOGIN :
	    	 params.add(new BasicNameValuePair("phone",arg1));
	    	 params.add(new BasicNameValuePair("password",arg2));
	    	 break;
	     case REQ_SIGN :
	    	 params.add(new BasicNameValuePair("phone",arg1));
	    	 params.add(new BasicNameValuePair("name",arg2));
	    	 params.add(new BasicNameValuePair("password",arg3));
	    	 break;
	     case REQ_FRIENDING :
	    	 break;
	     case REQ_DEL_FRIEND :
	    	 break;
	     case REQ_GET_PINFO :
             params.add(new BasicNameValuePair("name",arg1));
	    	 break;
	     case REQ_GET_FRIENDLIST :
	    	 break;
	     case REQ_FIRST_AID :
	    	 break;
	     case REQ_FOR_HELP :
	    	 break;
	     case REQ_RAISE_QUESTION :
	    	 break;
	     case REQ_GET_QUESTIONLIST :
	    	 break;
	     case REQ_APPRAISE :
	    	 break;
	     default:break;
	   }
	   
	   return params;
   }
   
   public void setType(int type){
       this.type = type;
   }
   
   public int getType(){
       return type;
   }
   
}
