package com.ronnelrazo.fatteningfeedingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.novoda.merlin.Bindable;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;
import com.ronnelrazo.fatteningfeedingapp.localDB.MyDatabaseHelper;
import com.ronnelrazo.fatteningfeedingapp.localDB.PC_FAT_MAS_AUTHORIZE;
import com.ronnelrazo.fatteningfeedingapp.localDB.PC_FAT_MAS_USER;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class GlobalMethod {

    private static GlobalMethod application;
    private static Context cont;

    public Merlin merlin;
    public boolean isConnectedToInternet;


    public GlobalMethod(Context context){
        cont = context;
    }



    public static synchronized GlobalMethod getInstance(Context context){
        if(application == null){
            application = new GlobalMethod(context);
        }
        return application;
    }



    public MyDatabaseHelper databaseHelper(){
        return new MyDatabaseHelper(cont);
    }

    public SQLiteDatabase db(int type){
        if(type == 1){
            return  databaseHelper().getReadableDatabase();
        }
        else{
            return databaseHelper().getWritableDatabase();
        }
    }



    //Intent
    public void intent(Class<?> activity, Context context){
        Intent i = new Intent(context,activity);
        context.startActivity(i);
    }


    protected void status(TextView textView,String color,String msg){
        textView.setText("");
        int labelColor = Color.parseColor(color);
        textView.setTextColor(labelColor);
        textView.setText(msg);

    }


    public void Connection(Context context) {
        merlin = new Merlin.Builder().withAllCallbacks().build(context);
    }

    public void registerBind(TextView textView){
        merlin.registerBindable(new Bindable() {
            @Override
            public void onBind(NetworkStatus networkStatus) {
                boolean connection = networkStatus.isAvailable();
                isConnectedToInternet = connection;
                if(connection){
                    status(textView,"#2ecc71","Connected");


                }
                else{
                    status(textView,"#d35400","Disconnected");
               ;
                }
            }
        });
    }

    public void isConnected(TextView textView){
        merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {
                status(textView,"#2ecc71","Connected");

            }
        });
    }

    public void isDisconnected(TextView textView){
        merlin.registerDisconnectable(new Disconnectable() {
            @Override
            public void onDisconnect() {
                try{
                    status(textView,"#d35400","Disconnected");

                }catch (Exception e){
                    Log.d("razo",e.getMessage());
                    status(textView,"#d35400","Disconnected");

                }

            }
        });
    }


    //Toast
    public void toast(int raw,String body,int postion,int x ,int y){
        Toast toast = new Toast(cont);
        View vs = LayoutInflater.from(cont).inflate(R.layout.custom_toast, null);
        LottieAnimationView icon = vs.findViewById(R.id.icon);
        TextView msg = vs.findViewById(R.id.body);

        icon.setAnimation(raw);
        icon.loop(false);
        icon.playAnimation();
        msg.setText(body);
        toast.setGravity(postion, x, y);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(vs);
        toast.show();
    }


    public boolean lastIndex(int i, JSONArray result){
        if(i++ == result.length() - 1){
            return true;
        }
        else{
            return false;
        }
    }


    //textwatcher

    public void inputwatcher(TextInputEditText inputEditText, TextInputLayout layout, String msg){
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(i >= 1){
                    layout.setErrorEnabled(false);
                }
                else{
                    layout.setErrorEnabled(true);
                    layout.setErrorIconDrawable(null);
                    layout.setError(msg);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    //localDB
    /**  check user if already downloaded   **/
    public Cursor check_mas_user(String ad_user){
        String query = "SELECT *  FROM PC_FAT_MAS_USER where AD_USER = '"+ad_user+"'";
        Cursor cursor = null;
        if(db(1) != null){
            cursor = db(1).rawQuery(query, null);
        }
        return cursor;
    }

    /** download user in database **/
    public boolean save_mas_user(String ad,String password,String status){
        try {
            PC_FAT_MAS_USER column = new PC_FAT_MAS_USER();
            ContentValues cv = new ContentValues();
            cv.put(column.AD_USER, ad);
            cv.put(column.PASSWORD, password);
            cv.put(column.ACTIVE_FLAG, status);
            db(2).insert(column.PC_FAT_MAS_USER,null, cv);
            return true;
        }catch (SQLException e){
            Log.d("MAS_USER",e.getMessage());
            return false;
        }
    }


    public boolean save_mas_authorize(String ad,String org_code,String farm_code){
        try {
            PC_FAT_MAS_AUTHORIZE column = new PC_FAT_MAS_AUTHORIZE();
            ContentValues cv = new ContentValues();
            cv.put(column.AD_USER, ad);
            cv.put(column.ORG_CODE, org_code);
            cv.put(column.FARM_CODE, farm_code);
            db(2).insert(column.PC_FAT_MAS_AUTHORIZED,null, cv);
            return true;
        }catch (SQLException e){
            Log.d("MAS_USER",e.getMessage());
            return false;
        }
    }


    protected String decodePassword(String password){
        byte[] data = new byte[0];
        try {
            data = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }

    public boolean login(String username,String password){
        Cursor res = db(1).rawQuery("SELECT * FROM PC_FAT_MAS_USER WHERE AD_USER = '"+username+"' AND  PASSWORD = '"+password+"' AND ACTIVE_FLAG = 'Y'",null);
        if(res.getCount() > 0)

            while (res.moveToNext()){
                String getusername = res.getString(0);
                String getpassword = res.getString(1);
//                function.getInstance(context).setUSER_POS(ID,FAMRORG);
                return true;
            }
        else
            res.close();
        return false;
    }










}
