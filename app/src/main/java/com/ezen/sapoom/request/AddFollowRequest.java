package com.ezen.sapoom.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddFollowRequest extends StringRequest {

    final static private String URL = "http://eor0601.dothome.co.kr/addfollow.php";
    private Map<String, String> parameters;

    public AddFollowRequest(String follow_index, String my_id, String follow_id, String following_id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("follow_index", follow_index);
        parameters.put("my_id", my_id);
        parameters.put("follow_id", follow_id);
        parameters.put("following_id", following_id);

    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
