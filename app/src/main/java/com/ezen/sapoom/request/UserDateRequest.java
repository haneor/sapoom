package com.ezen.sapoom.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// 서버로 아이디와 비밀번호를 저장하는 메소드
public class UserDateRequest extends StringRequest {

    final static private String URL = "http://eor0601.dothome.co.kr/saveprofile.php";
    private Map<String, String> parameters;

    public UserDateRequest(String id, String nickname, String profile_sub, String e_mail, String profile_image, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("nickname", nickname);
        parameters.put("profile_sub", profile_sub);
        parameters.put("e_mail", e_mail);
        parameters.put("profile_image", profile_image);

    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
