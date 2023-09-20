package com.example.mega;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME="session";
    String SESSION_KEY="session-user";
    public SessionManagement(Context context){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public void saveSession(String username){
        editor.putString(SESSION_KEY,username).commit();
    }
    public String  getSession(){
        //return user whose session is saved
       return sharedPreferences.getString(SESSION_KEY,"false");
    }

    public void removeSession(){
        editor.putString(SESSION_KEY,"false").commit();
    }
}
