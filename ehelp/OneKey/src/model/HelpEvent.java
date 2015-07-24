package model;


public class HelpEvent {
	   private String content;
	   private String time;
	   private User sender;
	   
       public HelpEvent(String content,User user) {
    	   this.content = content;
    	   this.sender = user;
       }	   
	   
	   public void setContent(String content){
	       this.content = content;
	   }
	   
	   public void setTime(String time){
	       this.time = time;
	   }
	   
	   public void setSender(User user){
	       this.sender = user;
	   }
	   
	   public String getContent(){
		      return content;   
       }
		   
	   public String getTime(){
	       return time;
	   }

	   public User getSender(){
	       return sender;
	   }
}
