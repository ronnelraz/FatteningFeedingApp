package com.ronnelrazo.fatteningfeedingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.ronnelrazo.fatteningfeedingapp.API.API;
import com.ronnelrazo.fatteningfeedingapp.API.APIJSON;
import com.ronnelrazo.fatteningfeedingapp.Adapter.Adapter_menu_list;
import com.ronnelrazo.fatteningfeedingapp.model.model_menu;
import com.ronnelrazo.fatteningfeedingapp.sharedPref.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements ItemClickListener,Menu_enable {

    private GlobalMethod globalMethod;
    private SharedPref sharedPref;
    @BindView(R.id.hamburgerIcon) ImageView Logout;


    String[] Menu_title = new String[]{
            "Title 1", "Title 2", "Title 3", "Title 4",
            "Title 5", "Title 6", "Title 7", "Download Resources"
    };

    int[] Menu_icon = new int[]{
            R.drawable.ic_icons8_image,R.drawable.ic_icons8_image,R.drawable.ic_icons8_image,R.drawable.ic_icons8_image,
            R.drawable.ic_icons8_image,R.drawable.ic_icons8_image,R.drawable.ic_icons8_image,R.drawable.icons8_download_from_the_cloud
    };

    @BindView(R.id.menulist)
    RecyclerView recyclerView;

    List<model_menu> menulist = new ArrayList<>();
    RecyclerView.Adapter adapter;

    private int downloadpercent = 0;

    @BindView(R.id.download_container)
    RelativeLayout download_container;
    @BindView(R.id.download_bar) ProgressBar download_bar;
    @BindView(R.id.download_progress) TextView download_progress;
    @BindView(R.id.download_table) TextView download_table;

    @BindView(R.id.status)
    TextView status;

    @BindView(R.id.user) TextView user;
    @BindView(R.id.org_code) TextView org_code;

    boolean disableMenu = false;
    private String setStartDate = null;

    String[] listjoke = {
            "“We cannot solve problems with the kind of thinking we employed when we came up with them.” — Albert Einstein",
            "“Learn as if you will live forever, live like you will die tomorrow.” — Mahatma Gandhi",
            "“Stay away from those people who try to disparage your ambitions. Small minds will always do that, but great minds will give you a feeling that you can become great too.” — Mark Twain",
            "“Success usually comes to those who are too busy looking for it.” — Henry David Thoreau",
            "Success is not final; failure is not fatal: It is the courage to continue that counts. — Winston S. Churchill",
            "“You learn more from failure than from success. Don’t let it stop you. Failure builds character.” — Unknown ",
            "“Try not to become a man of success, but rather become a man of value.” – Albert Einstein"
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        globalMethod = new GlobalMethod(this);
        sharedPref = new SharedPref(this);

        user.setText("User Login : "+sharedPref.getUserLogin());
        globalMethod.get_user_authorize(sharedPref.getUserLogin());
        org_code.setText("Access Org code : " + globalMethod.getOrg_code());
//        Toast.makeText(this, globalMethod.getOrg_code(), Toast.LENGTH_SHORT).show();
//
//        if(globalMethod.getFarm_code() == null || globalMethod.getFarm_code().isEmpty()){
//            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(this, "not null", Toast.LENGTH_SHORT).show();
//        }

        Menu_initialized();

        globalMethod.Connection(this);
        globalMethod.registerBind(status);
        globalMethod.isConnected(status);
        globalMethod.isDisconnected(status);


        adapter = new Adapter_menu_list(menulist,getApplicationContext(),this,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);
        recyclerView.setAdapter(adapter);

            Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "out", Toast.LENGTH_SHORT).show();
            }
        });







    }

    protected void Download_Mas_User(){
        API.getClient().PC_FAT_USER().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {

                    JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray result = jsonResponse.getJSONArray("data");

                    if(success){

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            boolean save = globalMethod.save_mas_user(
                                    object.getString("AD_USER"),
                                    object.getString("PASSWORD"),
                                    object.getString("ACTIVE_FLAG")
                            );
                            boolean lastIndex = globalMethod.lastIndex(i,result);
                            if(lastIndex){
//                                globalMethod.toast(R.raw.checked,"Download user Data successfully", Gravity.TOP|Gravity.CENTER,0,50);
                                Log.d("Download","user");
                            }
                            else{

                            }


                            if(save){
                                Log.d("MAS_USER","Save : " + object.getString("AD_USER") );
                            }
                            else{
                                Log.d("MAS_USER","Error Save : " + object.getString("AD_USER") );
                            }



                        }

                    }
                    else{

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("swine",e.getMessage() + " Error");


                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });

    }
    protected void Download_Mas_Authorize(){
        API.getClient().PC_FAT_USER_AUTHORIZE().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if(response.isSuccessful()){
                    Log.d("MAS_USER","success");
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        JSONArray result = jsonResponse.getJSONArray("data");

                        if(success){

                            for (int i = 0; i < result.length(); i++) {
                                JSONObject object = result.getJSONObject(i);
                                boolean save = globalMethod.save_mas_authorize(
                                        object.getString("AD_USER"),
                                        object.getString("ORG_CODE"),
                                        object.getString("FARM_CODE")
                                );

                                boolean lastIndex = globalMethod.lastIndex(i,result);
                                if(lastIndex){
//                                    globalMethod.toast(R.raw.checked,"Download authorize Data successfully", Gravity.TOP|Gravity.CENTER,0,50);
                                    Log.d("authorize","download");
                                }
                                else{

                                }

                                if(save){
                                    Log.d("MAS_USER","Save authorize : " + object.getString("AD_USER") );
                                }
                                else{
                                    Log.d("MAS_USER","Error Save authorize: " + object.getString("AD_USER") );
                                }



                            }

                        }
                        else{
                            Log.d("MAS_USER","error connecetion");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");


                    }
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });


    }







    protected void Download_FR_FARM_ORG(View v,String org_code,String farm,MaterialButton button){
        API.getClient().FR_FARM_ORG(org_code).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if(response.isSuccessful()){
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        String id = jsonResponse.getString("id");
                        if(success){
                            get_fr_farm_org(v,org_code,farm,button,id);
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");
                    }
                }
                else{
                    Toast.makeText(Home.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });
    }
    protected void get_fr_farm_org(View v,String org_code,String farm,MaterialButton button,String id){
        download_container.setVisibility(View.VISIBLE);
        download_bar.setIndeterminate(false);
        download_progress.setText("Preparing FR_FARM_ORG data...");
        download_table.setText("Downloading FR_FARM_ORG data...");
        setStartDate = globalMethod.getDate();
        APIJSON.getClient().get_fr_farm_org(id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if(response.isSuccessful()){
                    download_bar.setProgress(0);
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        JSONArray result = jsonResponse.getJSONArray("data");
                        download_bar.setMax(result.length());
                        if(success){
                            Handler handler1 = new Handler();
                            for (int i = 0; i < result.length(); i++) {
                                int finalI = i;
                                handler1.postDelayed(new Runnable() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void run() {
                                        JSONObject object;

                                        try {
                                            object = result.getJSONObject(finalI);


                                            if(finalI == 0){
                                                Iterator<String> stringIterator = object.keys();
                                                while (stringIterator.hasNext()) {
                                                    Log.d("body_razo", stringIterator.next());
                                                }
                                            }


                                            boolean save = globalMethod.save_fr_farm_org(
                                                    Arrays.asList(
                                                            object.getString("ORG_CODE"),
                                                            object.getString("FARM_ORG"),
                                                            object.getString("NAME_LOC"),
                                                            object.getString("NAME_ENG"),
                                                            object.getString("FARM"),
                                                            object.getString("UNIT"),
                                                            object.getString("HOUSE"),
                                                            object.getString("LOCATION"),
                                                            object.getString("FLOCK"),
                                                            object.getString("GRP"),
                                                            object.getString("CANCEL_FLAG"),
                                                            object.getString("CANCEL_DATE"),
                                                            object.getString("USER_CREATE"),
                                                            object.getString("CREATE_DATE"),
                                                            object.getString("LAST_USER_ID"),
                                                            object.getString("LAST_UPDATE_DATE"),
                                                            object.getString("ACTIVE_FLG"),
                                                            object.getString("PROJECT"),
                                                            object.getString("PARENT_FARM_ORG"),
                                                            object.getString("MANAGEMENT_FLG"),
                                                            object.getString("REC_FD_FLG"),
                                                            object.getString("REC_MD_FLG"),
                                                            object.getString("REC_BD_FLG"),
                                                            object.getString("ISS_FD_FLG"),
                                                            object.getString("ISS_MD_FLG"),
                                                            object.getString("ISS_BD_FLG"),
                                                            object.getString("REC_FD_WAY_TYPE"),
                                                            object.getString("REC_MD_WAY_TYPE"),
                                                            object.getString("REC_BD_WAY_TYPE"),
                                                            object.getString("ISS_FD_WAY_TYPE"),
                                                            object.getString("ISS_MD_WAY_TYPE"),
                                                            object.getString("ISS_BD_WAY_TYPE"),
                                                            object.getString("PARENT_FARM_ORG_FD"),
                                                            object.getString("PARENT_FARM_ORG_MD"),
                                                            object.getString("REC_AI_FLG"),
                                                            object.getString("ISS_AI_FLG"),
                                                            object.getString("REC_AI_WAY_TYPE"),
                                                            object.getString("ISS_AI_WAY_TYPE"),
                                                            object.getString("PARENT_FARM_ORG_AI"),
                                                            object.getString("REC_MK_FLG"),
                                                            object.getString("ISS_MK_FLG"),
                                                            object.getString("REC_MK_WAY_TYPE"),
                                                            object.getString("ISS_MK_WAY_TYPE"),
                                                            object.getString("PARENT_FARM_ORG_MK"),
                                                            object.getString("REF_FARM"),
                                                            object.getString("CHAT_FARM_NAME"),
                                                            object.getString("FLOCK_IN"),
                                                            object.getString("CUSTOMER_STD_CODE")

                                                    )
                                            );
                                            if(save){
                                                downloadpercent++;
                                                download_bar.setProgress(downloadpercent,true);
                                                download_progress.setText(String.format("FR_FARM_ORG (%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
                                            }
                                            else{
                                                downloadpercent++;
                                                download_bar.setProgress(downloadpercent,true);
                                                download_progress.setText(String.format("FR_FARM_ORG (%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
                                                Log.d("MAS_USER","Error Save : already exist");
                                            }

                                            boolean lastIndex = globalMethod.lastIndex(finalI,result);
                                            if(lastIndex){
                                                download_progress.setText(String.format("FR_FARM_ORG (%d/%d) %d%%", result.length(), finalI+1, (downloadpercent * 100) / result.length()));
                                                downloadpercent = 0;
                                                download_bar.setProgress(0,true);
//                                                globalMethod.toast(R.raw.checked,"Downlaod Completed", Gravity.BOTTOM|Gravity.CENTER,0,50);
                                                globalMethod.save_device_log(
                                                        setStartDate,
                                                        globalMethod.getDate(),
                                                        "FR_FARM_ORG",
                                                        result.length(),
                                                        finalI+1,
                                                        sharedPref.getUserLogin());
                                                Download_FR_MS_SWINE_MATERIAL(v, org_code,farm,button);
                                            }
                                            else{

                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, 20 * i);

                            }

                        }
                        else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");
                        globalMethod.toast(R.raw.error,e.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }
                }
                else{
                    Toast.makeText(Home.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });

    }

    protected void Download_FR_MS_SWINE_MATERIAL(View v,String org_code,String farm,MaterialButton button){
        API.getClient().FR_MS_SWINE_MATERIAL(org_code,farm).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if(response.isSuccessful()){
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        String id = jsonResponse.getString("id");
                        if(success){
                            get_FR_MS_SWINE_MATERIAL(v,org_code,farm,button,id);
                        }
                        else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");
                    }
                }
                else{
                    Toast.makeText(Home.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });
    }
    protected void get_FR_MS_SWINE_MATERIAL(View v,String org_code,String farm,MaterialButton button,String id){
        download_container.setVisibility(View.VISIBLE);
        download_bar.setIndeterminate(false);
        download_progress.setText("Preparing FR_MS_SWINE_MATERIAL data...");
        download_table.setText("Downloading FR_MS_SWINE_MATERIAL data...");
        setStartDate = globalMethod.getDate();
        APIJSON.getClient().get_fr_mas_swine_material(id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if(response.isSuccessful()){
                    download_bar.setProgress(0);
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        JSONArray result = jsonResponse.getJSONArray("data");
                        download_bar.setMax(result.length());
                        if(success){
                            Handler handler1 = new Handler();
                            for (int i = 0; i < result.length(); i++) {
                                int finalI = i;
                                handler1.postDelayed(new Runnable() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void run() {
                                        JSONObject object;

                                        try {
                                            object = result.getJSONObject(finalI);


                                            if(finalI == 0){
                                                Iterator<String> stringIterator = object.keys();
                                                while (stringIterator.hasNext()) {
                                                    Log.d("body_razo", stringIterator.next());
                                                }
                                            }


                                            boolean save = globalMethod.save_fr_mas_swine_material(
                                                    Arrays.asList(
                                                            object.getString("ORG_CODE"),
                                                            object.getString("PERIOD"),
                                                            object.getString("PROJECT_CODE"),
                                                            object.getString("FARM_ORG"),
                                                            object.getString("STOCK_TYPE"),
                                                            object.getString("TRANSACTION_DATE"),
                                                            object.getString("DOCUMENT_TYPE"),
                                                            object.getString("DOCUMENT_NO"),
                                                            object.getString("DOCUMENT_EXT"),
                                                            object.getString("REF_DOCUMENT_NO"),
                                                            object.getString("PROJECT_STOCK_ORG"),
                                                            object.getString("STOCK_ORG"),
                                                            object.getString("PRODUCT_CODE"),
                                                            object.getString("PRODUCT_SPEC"),
                                                            object.getString("GRADE_CODE"),
                                                            object.getString("LOT_NO"),
                                                            object.getString("TRANSACTION_TYPE"),
                                                            object.getString("TRANSACTION_CODE"),
                                                            object.getString("QTY"),
                                                            object.getString("WGH"),
                                                            object.getString("VAL"),
                                                            object.getString("COST"),
                                                            object.getString("CANCEL_FLAG"),
                                                            object.getString("CANCEL_DATE"),
                                                            object.getString("USER_CREATE"),
                                                            object.getString("CREATE_DATE"),
                                                            object.getString("LAST_USER_ID"),
                                                            object.getString("LAST_UPDATE_DATE"),
                                                            object.getString("LAST_FUNCTION"),
                                                            object.getString("FARM_ORG_LOCATION"),
                                                            object.getString("DES_FARM_ORG"),
                                                            object.getString("UNIT"),
                                                            object.getString("ADJ"),
                                                            object.getString("LICENSE_NO"),
                                                            object.getString("REF_PRODUCT_CODE"),
                                                            object.getString("EXPIRE_DATE"),
                                                            object.getString("PRINT_NO"),
                                                            object.getString("PRODUCTION_DATE")
                                                    )
                                            );
                                            if(save){
                                                downloadpercent++;
                                                download_bar.setProgress(downloadpercent,true);
                                                download_progress.setText(String.format("FR_MS_SWINE_MATERIAL (%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
                                            }
                                            else{
                                                downloadpercent++;
                                                download_bar.setProgress(downloadpercent,true);
                                                download_progress.setText(String.format("FR_MS_SWINE_MATERIAL (%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
                                                Log.d("MAS_USER","Error Save : already exist");
                                            }

                                            boolean lastIndex = globalMethod.lastIndex(finalI,result);
                                            if(lastIndex){
                                                download_progress.setText(String.format("FR_MS_SWINE_MATERIAL (%d/%d) %d%%", result.length(), finalI+1, (downloadpercent * 100) / result.length()));
                                                downloadpercent = 0;
                                                download_bar.setProgress(0,true);
//                                                globalMethod.toast(R.raw.checked,"Downlaod Completed", Gravity.BOTTOM|Gravity.CENTER,0,50);
                                                globalMethod.save_device_log(
                                                        setStartDate,
                                                        globalMethod.getDate(),
                                                        "FR_MS_SWINE_MATERIAL",
                                                        result.length(),
                                                        finalI+1,
                                                        sharedPref.getUserLogin());
                                                Download_FR_MAS_CLOSE_PERIOD(v,org_code,farm,button);

                                            }
                                            else{

                                            }




                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, 20 * i);

                            }

                        }
                        else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");
                        globalMethod.toast(R.raw.error,e.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }
                }
                else{
                    Toast.makeText(Home.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });

    }

    protected void Download_FR_MAS_CLOSE_PERIOD(View v,String org_code,String farm,MaterialButton button){
        API.getClient().FR_MAS_CLOSE_PERIOD(org_code,farm).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if(response.isSuccessful()){
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        String id = jsonResponse.getString("id");
                        if(success){
                            get_FR_MAS_CLOSE_PERIOD(v,org_code,farm,button,id);
                        }
                        else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");
                        globalMethod.toast(R.raw.error,e.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }
                }
                else{
                    Toast.makeText(Home.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });

    }
    protected void get_FR_MAS_CLOSE_PERIOD(View v,String org_code,String farm,MaterialButton button,String id){
        download_container.setVisibility(View.VISIBLE);
        download_bar.setIndeterminate(false);
        download_progress.setText("Preparing FR_MAS_CLOSE_PERIOD data...");
        download_table.setText("Downloading FR_MAS_CLOSE_PERIOD data...");
        setStartDate = globalMethod.getDate();
        APIJSON.getClient().get_fr_mas_close_period(id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if(response.isSuccessful()){
                    download_bar.setProgress(0);
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        JSONArray result = jsonResponse.getJSONArray("data");
                        download_bar.setMax(result.length());
                        if(success){
                            Handler handler1 = new Handler();
                            for (int i = 0; i < result.length(); i++) {
                                int finalI = i;
                                handler1.postDelayed(new Runnable() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void run() {
                                        JSONObject object;

                                        try {
                                            object = result.getJSONObject(finalI);


                                            if(finalI == 0){
                                                Iterator<String> stringIterator = object.keys();
                                                while (stringIterator.hasNext()) {
                                                    Log.d("body_razo", stringIterator.next());
                                                }
                                            }


                                            boolean save = globalMethod.save_fr_mas_close_period( object.getString("ACTIVE_PERIOD"));
                                            if(save){
                                                downloadpercent++;
                                                download_bar.setProgress(downloadpercent,true);
                                                download_progress.setText(String.format("FR_MAS_CLOSE_PERIOD (%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
                                            }
                                            else{
                                                downloadpercent++;
                                                download_bar.setProgress(downloadpercent,true);
                                                download_progress.setText(String.format("FR_MAS_CLOSE_PERIOD (%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
                                                Log.d("MAS_USER","Error Save : already exist");
                                            }

                                            boolean lastIndex = globalMethod.lastIndex(finalI,result);
                                            if(lastIndex){
                                                download_progress.setText(String.format("FR_MAS_CLOSE_PERIOD (%d/%d) %d%%", result.length(), finalI+1, (downloadpercent * 100) / result.length()));
                                                downloadpercent = 0;
                                                download_bar.setProgress(0,true);
//                                                globalMethod.toast(R.raw.checked,"Downlaod Completed", Gravity.BOTTOM|Gravity.CENTER,0,50);
                                                globalMethod.save_device_log(
                                                        setStartDate,
                                                        globalMethod.getDate(),
                                                        "FR_MAS_CLOSE_PERIOD",
                                                        result.length(),
                                                        finalI+1,
                                                        sharedPref.getUserLogin());
                                                Download_Feed_STOCK_BALANCE(v,org_code,farm,button);

                                            }
                                            else{

                                            }




                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, 1000 * i);

                            }

                        }
                        else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");
                        globalMethod.toast(R.raw.error,e.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }
                }
                else{
                    Toast.makeText(Home.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });

    }

    protected void Download_Feed_STOCK_BALANCE(View v,String org_code,String farm,MaterialButton button){
        API.getClient().FDSTOCKBALANCE(org_code).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if(response.isSuccessful()){
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        String id = jsonResponse.getString("id");
                        if(success){
                            get_Feed_STOCK_BALANCE(v,org_code,farm,button,id);
                        }
                        else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");
                        globalMethod.toast(R.raw.error,e.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }
                }
                else{
                    Toast.makeText(Home.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });

    }
    protected void get_Feed_STOCK_BALANCE(View v,String org_code,String farm,MaterialButton button,String id){
        download_container.setVisibility(View.VISIBLE);
        download_bar.setIndeterminate(false);
        download_progress.setText("Preparing FR_MAS_CLOSE_PERIOD data...");
        download_table.setText("Downloading FR_MAS_CLOSE_PERIOD data...");
        setStartDate = globalMethod.getDate();
        APIJSON.getClient().get_feed_stock_balance(id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if(response.isSuccessful()){
                    download_bar.setProgress(0);
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        JSONArray result = jsonResponse.getJSONArray("data");
                        download_bar.setMax(result.length());
                        if(success){
                            Handler handler1 = new Handler();
                            for (int i = 0; i < result.length(); i++) {
                                int finalI = i;
                                handler1.postDelayed(new Runnable() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void run() {
                                        JSONObject object;

                                        try {
                                            object = result.getJSONObject(finalI);


                                            if(finalI == 0){
                                                Iterator<String> stringIterator = object.keys();
                                                while (stringIterator.hasNext()) {
                                                    Log.d("body_razo", stringIterator.next());
                                                }
                                            }


                                            boolean save = globalMethod.save_FDSTOCKBALANCE(
                                                    Arrays.asList(
                                                            object.getString("ORG_CODE"),
                                                            object.getString("FARM"),
                                                            object.getString("FARM_ORG"),
                                                            object.getString("PRODUCT_CODE"),
                                                            object.getString("PRODUCT_NAME"),
                                                            object.getString("PRODUCT_SPEC"),
                                                            object.getString("LOT_NO"),
                                                            object.getString("USE_CONDITION"),
                                                            object.getString("BF_QTY"),
                                                            object.getString("RECEIVED_DATE"),
                                                            object.getString("ISSUED_QTY"),
                                                            object.getString("BALANCE_QTY"),
                                                            object.getString("BF_WGH"),
                                                            object.getString("RECEIVED_WGH"),
                                                            object.getString("ISSUED_WGH"),
                                                            object.getString("BALANCE_WGH"),
                                                            object.getString("PACKING_SIZE")
                                                    )
                                            );
                                            if(save){
                                                downloadpercent++;
                                                download_bar.setProgress(downloadpercent,true);
                                                download_progress.setText(String.format("Feed_STOCK_BALANCE (%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
                                            }
                                            else{
                                                downloadpercent++;
                                                download_bar.setProgress(downloadpercent,true);
                                                download_progress.setText(String.format("Feed_STOCK_BALANCE (%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
                                                Log.d("MAS_USER","Error Save : already exist");
                                            }

                                            boolean lastIndex = globalMethod.lastIndex(finalI,result);
                                            if(lastIndex){
                                                download_progress.setText(String.format("Feed_STOCK_BALANCE (%d/%d) %d%%", result.length(), finalI+1, (downloadpercent * 100) / result.length()));
                                                downloadpercent = 0;
                                                download_bar.setProgress(0,true);
//                                                globalMethod.toast(R.raw.checked,"Downlaod Completed", Gravity.BOTTOM|Gravity.CENTER,0,50);
                                                globalMethod.save_device_log(
                                                        setStartDate,
                                                        globalMethod.getDate(),
                                                        "Feed_STOCK_BALANCE",
                                                        result.length(),
                                                        finalI+1,
                                                        sharedPref.getUserLogin());
                                                Download_FR_MAS_FARM_INFORMATION(v,org_code,farm,button);

                                            }
                                            else{

                                            }




                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, 30 * i);

                            }

                        }
                        else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");
                        globalMethod.toast(R.raw.error,e.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }
                }
                else{
                    Toast.makeText(Home.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });

    }

    protected void Download_FR_MAS_FARM_INFORMATION(View v,String org_code,String farm,MaterialButton button){
        API.getClient().FR_MAS_FARM_INFORMATION(org_code).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if(response.isSuccessful()){
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        String id = jsonResponse.getString("id");
                        if(success){
                            get_FR_MAS_FARM_INFORMATION(v,org_code,farm,button,id);
                        }
                        else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");
                        globalMethod.toast(R.raw.error,e.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }
                }
                else{
                    Toast.makeText(Home.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });

    }
    protected void get_FR_MAS_FARM_INFORMATION(View v,String org_code,String farm,MaterialButton button,String id){
        download_container.setVisibility(View.VISIBLE);
        download_bar.setIndeterminate(false);
        download_progress.setText("Preparing FR_MAS_FARM_INFORMATION data...");
        download_table.setText("Downloading FR_MAS_FARM_INFORMATION data...");
        setStartDate = globalMethod.getDate();
        APIJSON.getClient().get_fr_mas_farm_information(id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if(response.isSuccessful()){
                    download_bar.setProgress(0);
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        JSONArray result = jsonResponse.getJSONArray("data");
                        download_bar.setMax(result.length());
                        if(success){
                            Handler handler1 = new Handler();
                            for (int i = 0; i < result.length(); i++) {
                                int finalI = i;
                                handler1.postDelayed(new Runnable() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void run() {
                                        JSONObject object;

                                        try {
                                            object = result.getJSONObject(finalI);


                                            if(finalI == 0){
                                                Iterator<String> stringIterator = object.keys();
                                                while (stringIterator.hasNext()) {
                                                    Log.d("body_razo", stringIterator.next());
                                                }
                                            }


                                            boolean save = globalMethod.save_FR_MAS_FARM_INFORMATION(
                                                    Arrays.asList(
                                                            object.getString("ORG_CODE"),
                                                            object.getString("FARM"),
                                                            object.getString("NAME_LOC"),
                                                            object.getString("NAME_ENG"),
                                                            object.getString("PROJECT"),
                                                            object.getString("FARM_TYPE"),
                                                            object.getString("FARM_MANAGER"),
                                                            object.getString("AREA_FEEDING"),
                                                            object.getString("AREA_SALE"),
                                                            object.getString("HUSBANDRY"),
                                                            object.getString("OPERATION"),
                                                            object.getString("FEED_CONDITION"),
                                                            object.getString("MEDICINE_CONDITION"),
                                                            object.getString("BREEDER_CONDITION"),
                                                            object.getString("CANCEL_FLAG"),
                                                            object.getString("CANCEL_DATE"),
                                                            object.getString("USER_CREATE"),
                                                            object.getString("CREATE_DATE"),
                                                            object.getString("LAST_USER_ID"),
                                                            object.getString("LAST_UPDATE_DATE"),
                                                            object.getString("REF_FARM"),
                                                            object.getString("AUTO_TRACKING"),
                                                            object.getString("FARM_OLD"),
                                                            object.getString("BENEFIT_CONDITION"),
                                                            object.getString("TAX_CONDITION"),
                                                            object.getString("HATCHERY_CONDITION"),
                                                            object.getString("ITF_SMARTSOFT"),
                                                            object.getString("AIR_SYSTEM"),
                                                            object.getString("WATER_SYSTEM"),
                                                            object.getString("HOLDING_DAY"),
                                                            object.getString("PARENT_FARM"),
                                                            object.getString("BANK_ACCOUNT"),
                                                            object.getString("LICENSE_FARM"),
                                                            object.getString("TELEPHONE_NO"),
                                                            object.getString("NAME_LOC_LAB"),
                                                            object.getString("NAME_ENG_LAB"),
                                                            object.getString("CHAT_FARM_NAME"),
                                                            object.getString("CUSTOMER_STD_CODE")
                                                    )
                                            );
                                            if(save){
                                                downloadpercent++;
                                                download_bar.setProgress(downloadpercent,true);
                                                download_progress.setText(String.format("FR_MAS_FARM_INFORMATION (%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
                                            }
                                            else{
                                                downloadpercent++;
                                                download_bar.setProgress(downloadpercent,true);
                                                download_progress.setText(String.format("FR_MAS_FARM_INFORMATION (%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
                                                Log.d("MAS_USER","Error Save : already exist");
                                            }

                                            boolean lastIndex = globalMethod.lastIndex(finalI,result);
                                            if(lastIndex){
                                                download_progress.setText(String.format("FR_MAS_FARM_INFORMATION (%d/%d) %d%%", result.length(), finalI+1, (downloadpercent * 100) / result.length()));
                                                downloadpercent = 0;
                                                download_bar.setProgress(0,true);
//                                                globalMethod.toast(R.raw.checked,"Downlaod Completed", Gravity.BOTTOM|Gravity.CENTER,0,50);
                                                globalMethod.save_device_log(
                                                        setStartDate,
                                                        globalMethod.getDate(),
                                                        "FR_MAS_FARM_INFORMATION",
                                                        result.length(),
                                                        finalI+1,
                                                        sharedPref.getUserLogin());
                                                Download_fr_mas_trn_feed(v,org_code,farm,button);

                                            }
                                            else{

                                            }




                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, 1000 * i);

                            }

                        }
                        else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");
                        globalMethod.toast(R.raw.error,e.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }
                }
                else{
                    Toast.makeText(Home.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });

    }

    protected void Download_fr_mas_trn_feed(View v,String org_code,String farm,MaterialButton button){
        API.getClient().FR_MS_TRN_FEED(org_code,farm).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if(response.isSuccessful()){
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        String id = jsonResponse.getString("id");

                        if(success){
                            get_fr_mas_trn_feed(v,org_code,farm,button,id);
                        }
                        else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");
                        globalMethod.toast(R.raw.error,e.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }
                }
                else{
                    Toast.makeText(Home.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });

    }
    protected void get_fr_mas_trn_feed(View v,String org_code,String farm,MaterialButton button,String id){
//        2xm1lrc
        download_container.setVisibility(View.VISIBLE);
        download_bar.setIndeterminate(false);
        download_progress.setText("Preparing fr_mas_trn_feed data...");
        download_table.setText("Downloading fr_mas_trn_feed data...");
        setStartDate = globalMethod.getDate();
        APIJSON.getClient().get_fr_mas_trn_feed(id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                if(response.isSuccessful()){
                    download_bar.setProgress(0);
                    try {

                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        boolean success = jsonResponse.getBoolean("success");
                        JSONArray result = jsonResponse.getJSONArray("data");
                        download_bar.setMax(result.length());
                        if(success){
                            Handler handler1 = new Handler();
                            for (int i = 0; i < result.length(); i++) {
                                int finalI = i;

                                handler1.postDelayed(new Runnable() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void run() {
                                        JSONObject object;
                                        try {
                                            object = result.getJSONObject(finalI);
//                                                downloadpercent++;
//                                                download_bar.setProgress(downloadpercent,true);
//                                                download_progress.setText(String.format("(%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
//                                                Log.d("response_object",object.getString("VAL"));

                                            boolean save = globalMethod.save_fr_mas_trn_feed(
                                                    Arrays.asList(
                                                            object.getString("ORG_CODE"),
                                                            object.getString("PERIOD"),
                                                            object.getString("PROJECT_CODE"),
                                                            object.getString("FARM_ORG"),
                                                            object.getString("TRANSACTION_DATE"),
                                                            object.getString("DOCUMENT_TYPE"),
                                                            object.getString("DOCUMENT_NO"),
                                                            object.getString("DOCUMENT_EXT"),
                                                            object.getString("PROJECT_STOCK_ORG"),
                                                            object.getString("STOCK_ORG"),
                                                            object.getString("PRODUCT_CODE"),
                                                            object.getString("PRODUCT_SPEC"),
                                                            object.getString("GRADE_CODE"),
                                                            object.getString("LOT_NO"),
                                                            object.getString("TRANSACTION_TYPE"),
                                                            object.getString("TRANSACTION_CODE"),
                                                            object.getString("QTY"),
                                                            object.getString("WGH"),
                                                            object.getString("VAL"),
                                                            object.getString("COST"),
                                                            object.getString("REF_DOCUMENT_TYPE"),
                                                            object.getString("REF_DOCUMENT_NO"),
                                                            object.getString("REF_DOCUMENT_EXT"),
                                                            object.getString("REF_ORG_CODE"),
                                                            object.getString("REF_PROJECT_CODE"),
                                                            object.getString("REF_PLANT_CODE"),
                                                            object.getString("REF_FARM_ORG"),
                                                            object.getString("USER_CREATE"),
                                                            object.getString("CREATE_DATE"),
                                                            object.getString("REF_DOCUMENT_TYPE_OR"),
                                                            object.getString("REF_DOCUMENT_NO_OR"),
                                                            object.getString("REF_DOCUMENT_DATE_OR"),
                                                            object.getString("CANCEL_FLAG"),
                                                            object.getString("CANCEL_DATE"),
                                                            object.getString("LAST_USER_ID"),
                                                            object.getString("LAST_UPDATE_DATE"),
                                                            object.getString("PRODUCTION_DATE")

                                                    )
                                            );
                                            if(save){
                                                downloadpercent++;
                                                download_bar.setProgress(downloadpercent,true);
                                                download_progress.setText(String.format("FR_MS_TRN_FEED (%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
                                            }
                                            else{
                                                downloadpercent++;
                                                download_bar.setProgress(downloadpercent,true);
                                                download_progress.setText(String.format("FR_MS_TRN_FEED (%d/%d) %d%%", result.length(), finalI, (downloadpercent * 100) / result.length()));
                                                Log.d("MAS_USER","Error Save : already exist");
                                            }

                                            boolean lastIndex = globalMethod.lastIndex(finalI,result);
                                            if(lastIndex){
                                                download_progress.setText(String.format("FR_MS_TRN_FEED (%d/%d) %d%%", result.length(), finalI+1, (downloadpercent * 100) / result.length()));
                                                downloadpercent = 0;
                                                download_bar.setProgress(0,true);
                                                download_container.setVisibility(View.GONE);
                                                disableMenu = false;
                                                button.setEnabled(true);
                                                boolean fr_ms_trn_feed = globalMethod.save_device_log(
                                                        setStartDate,
                                                        globalMethod.getDate(),
                                                        "FR_MS_TRN_FEED",
                                                        result.length(),
                                                        finalI + 1,
                                                        sharedPref.getUserLogin());
                                                try {
                                                    GlobalMethod.getInstance(v.getContext()).toast(R.raw.checked,"Download Completed", Gravity.BOTTOM|Gravity.CENTER,0,50);
//                                                Toast.makeText(Home.this, "Download Complete", Toast.LENGTH_SHORT).show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Log.d("happy_toast",e.getMessage());
                                                }



                                            }
                                            else{

                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, 20 * i);






                            }

                        }
                        else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("swine",e.getMessage() + " Error");
                        globalMethod.toast(R.raw.error,e.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }
                }
                else{
                    Toast.makeText(Home.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (t instanceof IOException) {
                    globalMethod.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);

                }
            }
        });

    }

    protected void Menu_initialized(){
        for(int i = 0; i < Menu_title.length; i++){
            model_menu list = new model_menu(
                    i,
                    Menu_icon[i],
                    Menu_title[i]
            );
            menulist.add(list);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        globalMethod.merlin.bind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        globalMethod.merlin.unbind();
    }

    @Override
    public void onClick(View v,int position,MaterialButton button) {

        if(position == 7){
            if(globalMethod.isConnectingToInternet()){
                //disableMenu = true;
                Download_Mas_Authorize();
                Download_Mas_User();
//                downloading_container.setVisibility(View.VISIBLE);
//                joke.setText(listjoke[globalMethod.randomQuotes()]);
                button.setEnabled(false);
//                Download_fr_mas_trn_feed(v,globalMethod.getOrg_code(),globalMethod.getFarm_code(),button);
//                Download_FR_MAS_FARM_INFORMATION(v,globalMethod.getOrg_code(),globalMethod.getFarm_code(),button);

                Download_FR_FARM_ORG(v, globalMethod.getOrg_code(),globalMethod.getFarm_code(),button);
            }
            else{
                globalMethod.toast(R.raw.nointernet,"No Internet Connection", Gravity.BOTTOM|Gravity.CENTER,0,50);
            }

        }
        else{
            Toast.makeText(this, "under maintenance", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void postion(int pos, MaterialButton button, model_menu getData) {
//        disableMenu = true;
//        if (disableMenu) {
//            button.setEnabled(false);
//        } else {
//            button.setEnabled(false);
//        }
        if(pos == 7){
            if(globalMethod.isConnectingToInternet()){
                button.setIconResource(R.drawable.icons8_download_from_the_cloud);
                Download_Mas_Authorize();
                Download_Mas_User();
                button.setEnabled(true);
            }
            else{
                button.setIconResource(R.drawable.icons8_cloud_cross);
                button.setEnabled(false);
            }
        }

        globalMethod.merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {
                if(pos == 7){
                    Download_Mas_Authorize();
                    Download_Mas_User();
                    button.setIconResource(R.drawable.icons8_download_from_the_cloud);
                    button.setEnabled(true);
                }
            }
        });

        globalMethod.merlin.registerDisconnectable(new Disconnectable() {
            @Override
            public void onDisconnect() {
                if(pos == 7){
                   try {
                       button.setIconResource(R.drawable.icons8_cloud_cross);
                       button.setEnabled(false);
                   } catch (Exception e) {
                       e.printStackTrace();
                       button.setIconResource(R.drawable.icons8_cloud_cross);
                       button.setEnabled(false);
                   }
                }
            }
        });
    }



}