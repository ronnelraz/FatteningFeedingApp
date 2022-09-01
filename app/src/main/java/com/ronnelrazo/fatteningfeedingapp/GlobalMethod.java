package com.ronnelrazo.fatteningfeedingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.novoda.merlin.Bindable;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.NetworkStatus;
import com.ronnelrazo.fatteningfeedingapp.API.API_fr_mas_trn_feed;
import com.ronnelrazo.fatteningfeedingapp.localDB.FR_MS_TRN_FEED;
import com.ronnelrazo.fatteningfeedingapp.localDB.MyDatabaseHelper;
import com.ronnelrazo.fatteningfeedingapp.localDB.PC_FAT_MAS_AUTHORIZE;
import com.ronnelrazo.fatteningfeedingapp.localDB.PC_FAT_MAS_USER;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class GlobalMethod {

    private static GlobalMethod application;
    private static Context cont;

    public Merlin merlin;
    public boolean isConnectedToInternet;

    private String set_org_code,set_farm_code;



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

    public int randomQuotes(){
       return new Random().nextInt(6);
    }

    //select user authorize
    public void get_user_authorize(String user){
        Cursor res = db(1).rawQuery("SELECT DISTINCT GROUP_CONCAT(a.ORG_CODE) AS org_code," +
                        "CASE " +
                        "WHEN a.FARM_CODE == 'null' THEN ''" +
                        "WHEN a.FARM_CODE IS NULL THEN '' " +
                        "ELSE  GROUP_CONCAT(a.FARM_CODE)  END AS farm_code "+
                        "FROM pc_fat_mas_authorized a " +
                        "JOIN pc_fat_mas_user b " +
                        "ON b.AD_USER = a.AD_USER " +
                        "WHERE b.ACTIVE_FLAG = 'Y' "+
                        "AND b.AD_USER = 'ronnel.raz'",
                null);
        if(res.getCount() > 0){
            while (res.moveToNext()){
                set_org_code = res.getString(0);
               set_farm_code = res.getString(1);

            }
        }
        else{
            res.close();
        }

    }

    public String getOrg_code(){
        return set_org_code;
    }
    public String getFarm_code(){
        return set_farm_code;
    }



    /*******************trn feed**********************/

    public boolean save_fr_mas_trn_feed(List<String> data){
        try {
            FR_MS_TRN_FEED column = new FR_MS_TRN_FEED();
            ContentValues cv = new ContentValues();
            cv.put(column.ORG_CODE, data.get(0));
            cv.put(column.PERIOD, data.get(1));
            cv.put(column.PROJECT_CODE, data.get(2));
            cv.put(column.FARM_ORG, data.get(3));
            cv.put(column.TRANSACTION_DATE, data.get(4));
            cv.put(column.DOCUMENT_TYPE, data.get(5));
            cv.put((column.DOCUMENT_NO),data.get(6));
            cv.put((column.DOCUMENT_EXT),data.get(7));
            cv.put((column.PROJECT_STOCK_ORG),data.get(8));
            cv.put((column.STOCK_ORG),data.get(9));
            cv.put((column.PRODUCT_CODE),data.get(10));
            cv.put((column.PRODUCT_SPEC),data.get(11));
            cv.put((column.GRADE_CODE),data.get(12));
            cv.put((column.LOT_NO),data.get(13));
            cv.put((column.TRANSACTION_TYPE),data.get(14));
            cv.put((column.TRANSACTION_CODE),data.get(15));
            cv.put((column.QTY),data.get(16));
            cv.put((column.WGH),data.get(17));
            cv.put((column.VAL),data.get(18));
            cv.put((column.COST),data.get(19));
            cv.put((column.REF_DOCUMENT_TYPE),data.get(20));
            cv.put((column.REF_DOCUMENT_NO),data.get(21));
            cv.put((column.REF_DOCUMENT_EXT),data.get(22));
            cv.put((column.REF_ORG_CODE),data.get(23));
            cv.put((column.REF_PROJECT_CODE),data.get(24));
            cv.put((column.REF_PLANT_CODE),data.get(25));
            cv.put((column.REF_FARM_ORG),data.get(26));
            cv.put((column.USER_CREATE),data.get(27));
            cv.put((column.CREATE_DATE),data.get(28));
            cv.put((column.REF_DOCUMENT_TYPE_OR),data.get(29));
            cv.put((column.REF_DOCUMENT_NO_OR),data.get(30));
            cv.put((column.REF_DOCUMENT_DATE_OR),data.get(31));
            cv.put((column.CANCEL_FLAG),data.get(32));
            cv.put((column.CANCEL_DATE),data.get(33));
            cv.put((column.LAST_USER_ID),data.get(34));
            cv.put((column.LAST_UPDATE_DATE),data.get(35));
            cv.put((column.PRODUCTION_DATE),data.get(36));

            db(2).insert(column.FR_MS_TRN_FEED,null, cv);
            return true;
        }catch (SQLException e){
            Log.d("MAS_USER",e.getMessage());
            return false;
        }
    }


    public boolean save_fr_farm_org(List<String> data){
        try {
            ContentValues cv = new ContentValues();
            cv.put("ORG_CODE", data.get(0));
            cv.put("FARM_ORG", data.get(1));
            cv.put("NAME_LOC", data.get(2));
            cv.put("NAME_ENG", data.get(3));
            cv.put("FARM", data.get(4));
            cv.put("UNIT", data.get(5));
            cv.put("HOUSE",data.get(6));
            cv.put("LOCATION",data.get(7));
            cv.put("FLOCK",data.get(8));
            cv.put("GRP",data.get(9));
            cv.put("CANCEL_FLAG",data.get(10));
            cv.put("CANCEL_DATE",data.get(11));
            cv.put("USER_CREATE",data.get(12));
            cv.put("CREATE_DATE",data.get(13));
            cv.put("LAST_USER_ID",data.get(14));
            cv.put("LAST_UPDATE_DATE",data.get(15));
            cv.put("ACTIVE_FLG",data.get(16));
            cv.put("PROJECT",data.get(17));
            cv.put("PARENT_FARM_ORG",data.get(18));
            cv.put("MANAGEMENT_FLG",data.get(19));
            cv.put("REC_FD_FLG",data.get(20));
            cv.put("REC_MD_FLG",data.get(21));
            cv.put("REC_BD_FLG",data.get(22));
            cv.put("ISS_FD_FLG",data.get(23));
            cv.put("ISS_MD_FLG",data.get(24));
            cv.put("ISS_BD_FLG",data.get(25));
            cv.put("REC_FD_WAY_TYPE",data.get(26));
            cv.put("REC_MD_WAY_TYPE",data.get(27));
            cv.put("REC_BD_WAY_TYPE",data.get(28));
            cv.put("ISS_FD_WAY_TYPE",data.get(29));
            cv.put("ISS_MD_WAY_TYPE",data.get(30));
            cv.put("ISS_BD_WAY_TYPE",data.get(31));
            cv.put("PARENT_FARM_ORG_FD",data.get(32));
            cv.put("PARENT_FARM_ORG_MD",data.get(33));
            cv.put("REC_AI_FLG",data.get(34));
            cv.put("ISS_AI_FLG",data.get(35));
            cv.put("REC_AI_WAY_TYPE",data.get(36));
            cv.put("ISS_AI_WAY_TYPE",data.get(37));
            cv.put("PARENT_FARM_ORG_AI",data.get(38));
            cv.put("REC_MK_FLG",data.get(39));
            cv.put("ISS_MK_FLG",data.get(40));
            cv.put("REC_MK_WAY_TYPE",data.get(41));
            cv.put("ISS_MK_WAY_TYPE",data.get(42));
            cv.put("PARENT_FARM_ORG_MK",data.get(43));
            cv.put("REF_FARM",data.get(44));
            cv.put("CHAT_FARM_NAME",data.get(45));
            cv.put("FLOCK_IN",data.get(46));
            cv.put("CUSTOMER_STD_CODE",data.get(47));
            db(2).insert("FR_FARM_ORG",null, cv);
            return true;
        }catch (SQLException e){
            Log.d("MAS_USER",e.getMessage());
            return false;
        }
    }


    public boolean save_fr_mas_swine_material(List<String> data){
        try {
            ContentValues cv = new ContentValues();
            cv.put("ORG_CODE", data.get(0));
            cv.put("PERIOD", data.get(1));
            cv.put("PROJECT_CODE", data.get(2));
            cv.put("FARM_ORG", data.get(3));
            cv.put("STOCK_TYPE", data.get(4));
            cv.put("TRANSACTION_DATE", data.get(5));
            cv.put("DOCUMENT_TYPE",data.get(6));
            cv.put("DOCUMENT_NO",data.get(7));
            cv.put("DOCUMENT_EXT",data.get(8));
            cv.put("REF_DOCUMENT_NO",data.get(9));
            cv.put("PROJECT_STOCK_ORG",data.get(10));
            cv.put("STOCK_ORG",data.get(11));
            cv.put("PRODUCT_CODE",data.get(12));
            cv.put("PRODUCT_SPEC",data.get(13));
            cv.put("GRADE_CODE",data.get(14));
            cv.put("LOT_NO",data.get(15));
            cv.put("TRANSACTION_TYPE",data.get(16));
            cv.put("TRANSACTION_CODE",data.get(17));
            cv.put("QTY",data.get(18));
            cv.put("WGH",data.get(19));
            cv.put("VAL",data.get(20));
            cv.put("COST",data.get(21));
            cv.put("CANCEL_FLAG",data.get(22));
            cv.put("CANCEL_DATE",data.get(23));
            cv.put("USER_CREATE",data.get(24));
            cv.put("CREATE_DATE",data.get(25));
            cv.put("LAST_USER_ID",data.get(26));
            cv.put("LAST_UPDATE_DATE",data.get(27));
            cv.put("LAST_FUNCTION",data.get(28));
            cv.put("FARM_ORG_LOCATION",data.get(29));
            cv.put("DES_FARM_ORG",data.get(30));
            cv.put("UNIT",data.get(31));
            cv.put("ADJ",data.get(32));
            cv.put("LICENSE_NO",data.get(33));
            cv.put("REF_PRODUCT_CODE",data.get(34));
            cv.put("EXPIRE_DATE",data.get(35));
            cv.put("PRINT_NO",data.get(36));
            cv.put("PRODUCTION_DATE",data.get(37));
            db(2).insert("FR_MS_SWINE_MATERIAL",null, cv);
            return true;
        }catch (SQLException e){
            Log.d("MAS_USER",e.getMessage());
            return false;
        }
    }

    public boolean save_fr_mas_close_period(String period){
        try {
            ContentValues cv = new ContentValues();
            cv.put("ACTIVE_PERIOD", period);
            db(2).insert("FR_MAS_CLOSE_PERIOD",null, cv);
            return true;
        }catch (SQLException e){
            Log.d("MAS_USER",e.getMessage());
            return false;
        }
    }

    public boolean save_FDSTOCKBALANCE(List<String> data){
        try {
            ContentValues cv = new ContentValues();
            cv.put("ORG_CODE", data.get(0));
            cv.put("FARM", data.get(1));
            cv.put("FARM_ORG", data.get(2));
            cv.put("PRODUCT_CODE", data.get(3));
            cv.put("PRODUCT_NAME", data.get(4));
            cv.put("PRODUCT_SPEC", data.get(5));
            cv.put("LOT_NO",data.get(6));
            cv.put("USE_CONDITION",data.get(7));
            cv.put("BF_QTY",data.get(8));
            cv.put("RECEIVED_DATE",data.get(9));
            cv.put("ISSUED_QTY",data.get(10));
            cv.put("BALANCE_QTY",data.get(11));
            cv.put("BF_WGH",data.get(12));
            cv.put("RECEIVED_WGH",data.get(13));
            cv.put("ISSUED_WGH",data.get(14));
            cv.put("BALANCE_WGH",data.get(15));
            cv.put("PACKING_SIZE",data.get(16));
            db(2).insert("FEED_STOCK_BALANCE",null, cv);
            return true;
        }catch (SQLException e){
            Log.d("MAS_USER",e.getMessage());
            return false;
        }
    }

    public boolean save_FR_MAS_FARM_INFORMATION(List<String> data){
        try {
            ContentValues cv = new ContentValues();
            cv.put("ORG_CODE", data.get(0));
            cv.put("FARM", data.get(1));
            cv.put("NAME_LOC", data.get(2));
            cv.put("NAME_ENG", data.get(3));
            cv.put("PROJECT", data.get(4));
            cv.put("FARM_TYPE", data.get(5));
            cv.put("FARM_MANAGER",data.get(6));
            cv.put("AREA_FEEDING",data.get(7));
            cv.put("AREA_SALE",data.get(8));
            cv.put("HUSBANDRY",data.get(9));
            cv.put("OPERATION",data.get(10));
            cv.put("FEED_CONDITION",data.get(11));
            cv.put("MEDICINE_CONDITION",data.get(12));
            cv.put("BREEDER_CONDITION",data.get(13));
            cv.put("CANCEL_FLAG",data.get(14));
            cv.put("CANCEL_DATE",data.get(15));
            cv.put("USER_CREATE",data.get(16));
            cv.put("CREATE_DATE", data.get(17));
            cv.put("LAST_USER_ID", data.get(18));
            cv.put("LAST_UPDATE_DATE", data.get(19));
            cv.put("REF_FARM",data.get(20));
            cv.put("AUTO_TRACKING",data.get(21));
            cv.put("FARM_OLD",data.get(22));
            cv.put("BENEFIT_CONDITION",data.get(23));
            cv.put("TAX_CONDITION",data.get(24));
            cv.put("HATCHERY_CONDITION",data.get(25));
            cv.put("ITF_SMARTSOFT",data.get(26));
            cv.put("AIR_SYSTEM",data.get(27));
            cv.put("WATER_SYSTEM",data.get(28));
            cv.put("HOLDING_DAY",data.get(29));
            cv.put("PARENT_FARM",data.get(30));
            cv.put("BANK_ACCOUNT", data.get(31));
            cv.put("LICENSE_FARM", data.get(32));
            cv.put("TELEPHONE_NO", data.get(33));
            cv.put("NAME_LOC_LAB",data.get(34));
            cv.put("NAME_ENG_LAB",data.get(35));
            cv.put("CHAT_FARM_NAME",data.get(36));
            cv.put("CUSTOMER_STD_CODE",data.get(37));
            db(2).insert("FR_MAS_FARM_INFORMATION",null, cv);
            return true;
        }catch (SQLException e){
            Log.d("MAS_USER",e.getMessage());
            return false;
        }
    }

    public boolean save_device_log(String sd, String fd, String tn, int td, int dd, String ad){
        try {
            ContentValues cv = new ContentValues();
            cv.put("Download_start_date", sd);
            cv.put("Download_finish_date", fd);
            cv.put("Download_table_name", tn);
            cv.put("Total_data", String.valueOf(td));
            cv.put("Total_data_download", String.valueOf(dd));
            cv.put("AD_USER",ad);
            db(2).insert("DEVICE_LOG",null, cv);
            return true;
        }catch (SQLException e){
            Log.d("MAS_USER",e.getMessage());
            return false;
        }
    }

    public String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }



//
//    Response.Listener<String> response = new Response.Listener<String>() {
//        @Override
//        public void onResponse(String response1) {
//            try {
//                JSONObject jsonResponse = new JSONObject(response1);
//                boolean success = jsonResponse.getBoolean("success");
//                JSONArray array = jsonResponse.getJSONArray("data");
//
//                if (success) {
//
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject object = array.getJSONObject(i);
//                        Toast.makeText(Home.this, object.getString("ORG_CODE"), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//                else{
//                    globalMethod.toast(R.raw.error,"No Data found", Gravity.BOTTOM|Gravity.CENTER,0,50);
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    };
//    Response.ErrorListener errorListener = new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            String result = globalMethod.Errorvolley(error);
//            globalMethod.toast(R.raw.error,result, Gravity.BOTTOM|Gravity.CENTER,0,50);
//        }
//    };
//    API_fr_mas_trn_feed get = new API_fr_mas_trn_feed(org_code,farm,response,errorListener);
//        get.setRetryPolicy(new DefaultRetryPolicy(
//               5000,
//                       5,
//               DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(get);



    //network checker
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    public String Errorvolley(VolleyError volleyError){
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        return message;
    }


    public String ErrorRetrofit(Throwable t){
        String message = null;
        if (t instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (t instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (t instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (t instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (t instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (t instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        else if (t instanceof IOException) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        else if (t instanceof ConnectException) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        else if (t instanceof SocketTimeoutException) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        return message;
    }


}
