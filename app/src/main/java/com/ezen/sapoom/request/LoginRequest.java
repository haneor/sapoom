package com.ezen.sapoom.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String URL = "http://eor0601.dothome.co.kr/usersLogin.php";
    private Map<String, String> parameters;

    public LoginRequest(String db_id, String db_pw, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("db_id", db_id);
        parameters.put("db_pw", db_pw);

    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
