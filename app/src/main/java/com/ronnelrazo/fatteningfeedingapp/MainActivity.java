package com.ronnelrazo.fatteningfeedingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.ronnelrazo.fatteningfeedingapp.API.API;
import com.ronnelrazo.fatteningfeedingapp.sharedPref.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public GlobalMethod globalMethod;

    @BindView(R.id.status)
    TextView status;

    @BindView(R.id.download)
    MaterialButton download;

    SharedPref sharedPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        globalMethod = new GlobalMethod(this);
        sharedPref = new SharedPref(this);

        globalMethod.Connection(this);
        globalMethod.registerBind(status);
        globalMethod.isConnected(status);
        globalMethod.isDisconnected(status);

        globalMethod.merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {
                Download_Mas_User();
                Download_Mas_Authorize();
                download.setEnabled(true);
            }
        });

        globalMethod.merlin.registerDisconnectable(new Disconnectable() {
            @Override
            public void onDisconnect() {
                download.setEnabled(false);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Download_Mas_User();
                Download_Mas_Authorize();
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
                                globalMethod.toast(R.raw.checked,"Download user Data successfully", Gravity.TOP|Gravity.CENTER,0,50);
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
                                globalMethod.toast(R.raw.checked,"Download authorize Data successfully", Gravity.TOP|Gravity.CENTER,0,50);
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




    @BindViews({R.id.username,R.id.password})
    TextInputEditText[] input;
    @BindViews({R.id.username_layout,R.id.password_layout})
    TextInputLayout[] input_layout;
    @BindView(R.id.keepme)
    CheckBox keepme;
    public void login(View view) {
        String getusername = input[0].getText().toString();
        String getPassword = globalMethod.decodePassword(input[1].getText().toString()).trim();
        String keepmelogin =  keepme.isChecked() ? "true" : "false";

        if(getusername.isEmpty()){
            input_layout[0].setError("Invalid User ID");
            globalMethod.inputwatcher(input[0],input_layout[0],"Invalid User AD");
            input[0].requestFocus();
        }
        else if(getPassword.isEmpty()){
            input_layout[1].setError("Invalid Password");
            globalMethod.inputwatcher(input[1],input_layout[1],"Invalid Password");
            input[1].requestFocus();
        }
        else{
            Log.d("password_decode",getPassword);
            if(globalMethod.login(getusername,getPassword)){
                boolean save_shared = sharedPref.setAutoLogin(getusername, getPassword, keepmelogin);
                if(save_shared){
                    globalMethod.toast(R.raw.checked,"Login successfully", Gravity.BOTTOM|Gravity.CENTER,0,50);
                    globalMethod.intent(Home.class,view.getContext());
                    finish();
                }
            }
            else{
                globalMethod.toast(R.raw.error,"Invalid User AD or Password", Gravity.BOTTOM|Gravity.CENTER,0,50);
            }

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


}