package model;

public class User {
  
  //three types of user
  public static final int SELF = 0;
  public static final int FRIEND = 1;
  public static final int STRINGER = 2;
	
  private String name;
  private int age;
  private String phoneNum;
  private String career;
  private String healthState;
  
  //flag is used to identify the type of user
  private int flag;
  
  public User(String name,int age,String phoneNum,String career,String healthState){
      this.name =  name;
      this.age =  age;
      this.phoneNum =  phoneNum;
      this.career =  career;
      this.healthState =  healthState;
      this.flag = STRINGER;
  }
  
  public User(String name,int age,String phoneNum,String career,String healthState,int flag){
      this.name =  name;
      this.age =  age;
      this.phoneNum =  phoneNum;
      this.career =  career;
      this.healthState =  healthState;
      this.flag = STRINGER;
  }
  
  public void setName(String name){
     this.name = name;
  }
  
  public void setAge(int age){
      this.age = age;
  }
  
  public void setPhoneNum(String phoneNum){
      this.phoneNum = phoneNum;
  }
  
  public void setCareer(String career){
      this.career = career;
  }
  
  public void setHealthState(String healthState){
      this.healthState = healthState;
  }
  
  public String getName(){
      return name;
  }
  
  public int getAge(){
      return age;
  }
  
  public String getPhoneNum(){
      return phoneNum;
  }
  
  public String getCareer(){
      return career;
  }
  
  public String getHealthState(){
      return healthState;
  }
  

}
