package com.ronnelrazo.fatteningfeedingapp.API;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class API_fr_mas_trn_feed extends StringRequest {

    public static final String con = config.URL + "FR_MS_TRN_FEED";
    private Map<String,String> params;


    public API_fr_mas_trn_feed(String org_code, String farm_code, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("org_code",org_code);
        params.put("farm",farm_code);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
