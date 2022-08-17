package com.ronnelrazo.fatteningfeedingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private Method method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        method = new Method(this);
    }


    @BindViews({R.id.username,R.id.password})
    TextInputEditText[] input;
    @BindViews({R.id.username_layout,R.id.password_layout})
    TextInputLayout[] input_layout;
    @BindView(R.id.keepme)
    CheckBox keepme;
    public void login(View view) {
        String getusername = input[0].getText().toString();
        String getPassword = input[1].getText().toString();
        String keepmelogin =  keepme.isChecked() ? "true" : "false";
        method.intent(Home.class,view.getContext());
        finish();




//        if(getusername.isEmpty()){
//            input_layout[0].setError("Invalid User ID");
//            data.inputwatcher(input[0],input_layout[0],"Invalid User ID");
//            input[0].requestFocus();
//        }
//        else if(getPassword.isEmpty()){
//            input_layout[1].setError("Invalid Password");
//            data.inputwatcher(input[1],input_layout[1],"Invalid Password");
//            input[1].requestFocus();
//        }
//        else{
//            data.Preloader(this,"Please wait...");
//            API.getClient().loginAPI(getusername.toLowerCase(),getPassword).enqueue(new Callback<Object>() {
//                @Override
//                public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
//                    try {
//                        JSONObject jsonResponse = new JSONObject(new Gson().toJson(response.body()));
//                        boolean SUCCESS = jsonResponse.getBoolean("success");
//                        String MESSAGE = jsonResponse.getString("message");
//                        String TOKEN = jsonResponse.getString("token");
//
//
//                        if(SUCCESS){
//                            Log.d("swine",TOKEN);
//                            Log.d("swine","Keep me signed in : " + keepmelogin);
//                            Log.d("swine",MESSAGE);
//                            data.decodeToken(TOKEN,sharedPref);
//                            sharedPref.set_login_auth(TOKEN,keepmelogin);
//                            data.toast(R.raw.checked,MESSAGE, Gravity.TOP|Gravity.CENTER,0,50); //50
//                            data.loaddialog.dismiss();
//                            data.intent(inv_form.class,  Login.this);
//                            finish();
//
////                            try {
////                                DecodedJWT jwt = JWT.decode(TOKEN);
////                                String getpayload = jwt.getPayload();
////
////
////                            } catch (JWTDecodeException exception){
////                                //Invalid token
////                                data.toast(R.raw.checked,"Invalid Token", Gravity.TOP|Gravity.CENTER,0,50); //50
////                            }
//
//                        }
//                        else{
//                            data.toast(R.raw.error,MESSAGE, Gravity.TOP|Gravity.CENTER,0,50); //50
//                            data.loaddialog.dismiss();
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Log.d("swine",e.getMessage());
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Object> call, Throwable t) {
//                    if (t instanceof IOException) {
//                        data.toast(R.raw.error,t.getMessage(), Gravity.TOP|Gravity.CENTER,0,50);
//                        data.loaddialog.dismiss();
//                    }
//                }
//            });
//
//        }
    }


}