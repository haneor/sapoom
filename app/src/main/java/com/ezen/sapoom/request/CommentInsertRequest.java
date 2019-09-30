package com.ezen.sapoom.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CommentInsertRequest extends StringRequest {

    final static private String URL = "http://eor0601.dothome.co.kr/comment_insert.php";
    private Map<String, String> parameters;

    public CommentInsertRequest(String index, String image_id, String comment_sub, String user_id, String good, String created , Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("index", index);
        parameters.put("image_id", image_id);
        parameters.put("comment_sub", comment_sub);
        parameters.put("user_id", user_id);
        parameters.put("good", good);
        parameters.put("created", created);

    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
