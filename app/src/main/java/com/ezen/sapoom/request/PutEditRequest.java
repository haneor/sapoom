package com.ezen.sapoom.request;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PutEditRequest extends StringRequest {

    final static private String URL = "http://eor0601.dothome.co.kr/puteditimage.php";
    private Map<String, String> parameters;

    public PutEditRequest(String db_id, String db_substance, String created, String image_id,int comment_code, String comment_sub, int good, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("db_id", db_id);
        parameters.put("db_substance", db_substance);
        parameters.put("created", created);
        parameters.put("image_id", image_id);
        parameters.put("comment_code", String.valueOf(comment_code));
        parameters.put("comment_sub", comment_sub);
        parameters.put("good", String.valueOf(good));

    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
