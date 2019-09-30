package com.ezen.sapoom.request;

        import com.android.volley.Response;
        import com.android.volley.toolbox.StringRequest;

        import java.util.HashMap;
        import java.util.Map;

public class ValidateRequest extends StringRequest {

    final static private String URL = "http://eor0601.dothome.co.kr/IdCheck.php";
    private Map<String, String> parameters;

    public ValidateRequest(String db_id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("db_id", db_id);

    }

    public Map<String, String> getParams() {
        return parameters;
    }

}
