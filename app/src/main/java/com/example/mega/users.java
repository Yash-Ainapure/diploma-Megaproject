package com.example.mega;

public class users {
    public String username;
    public String userpass;
    public users(){

    }
    public users(String username,String userpass){
        this.username=username;
        this.userpass=userpass;
    }

    public String getUserName() {
        return username;
    }

    public String getUserPass() {
        return userpass;
    }

    public void setUserName(String username) {
        this.username = username;
    }



    public void setUserPass(String userpass) {
        this.userpass = userpass;
    }
}
