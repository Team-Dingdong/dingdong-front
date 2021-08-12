package org.techtown.dingdong.login_register;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
//인증문자를 받기 위해서 서버로 휴대폰번호 전송
public class Validation extends StringRequest {
    //서버 URL 설정
    final static private String URL="";
    private Map<String, String> map;

    public Validation(String phone, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map= new HashMap<>();
        map.put("phone", phone);
    }

    @Override
    protected Map<String, String> getPostParams() throws AuthFailureError {
        return map;
    }
}
