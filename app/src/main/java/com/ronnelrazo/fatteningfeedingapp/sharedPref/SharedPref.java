package com.ronnelrazo.fatteningfeedingapp.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPref {

    public static final String TAG = "Swine";
    private static SharedPref application;
    private static Context cont;


    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String SHARED_DATA = "SHARED_DATA";
    private static final String SHARED_KEEP_SIGNED_IN = "false";
    private static final String AD_USER = "AD_USER";
    private static final String PASSWORD = "PASSWORD";



    public SharedPref(Context context){
        cont = context;
    }

    public static synchronized SharedPref getInstance(Context context){
        if(application == null){
            application = new SharedPref(context);
        }
        return application;
    }


    public boolean setAutoLogin(String username, String password, String login){
        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(AD_USER,username);
        editor.putString(PASSWORD,password);
        editor.putString(SHARED_KEEP_SIGNED_IN,login);
        editor.apply();
        return true;
    }



//    public String getAuth_token(){
//        sharedPreferences = cont.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
//        return sharedPreferences.getString(SHARED_TOKEN,null);
//    }



}
