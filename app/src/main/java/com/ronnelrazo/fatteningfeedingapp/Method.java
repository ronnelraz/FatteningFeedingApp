package com.ronnelrazo.fatteningfeedingapp;

import android.content.Context;
import android.content.Intent;

public class Method {

    private static Method application;
    private static Context cont;

    public Method(Context context){
        cont = context;
    }

    public static synchronized Method getInstance(Context context){
        if(application == null){
            application = new Method(context);
        }
        return application;
    }


    //Intent
    public void intent(Class<?> activity, Context context){
        Intent i = new Intent(context,activity);
        context.startActivity(i);
    }

}
