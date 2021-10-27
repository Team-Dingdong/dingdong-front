package org.techtown.dingdong.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.home.Share;
import org.techtown.dingdong.login_register.AuthResponse;
import org.techtown.dingdong.login_register.LoginActivity;
import org.techtown.dingdong.login_register.LoginResponse;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.login_register.TokenRefreshRequest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class AccessTokenInterceptor implements Interceptor {

    private final Token tokenRepo;

    public AccessTokenInterceptor(Token tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);


        if(response.code() == 401){
            String accessToken = tokenRepo.getAccessToken();
            String refreshToken = tokenRepo.getRefreshToken();
            Context context = tokenRepo.getContext();
            TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(accessToken, refreshToken);
            Apiinterface apiinterface = Api.createService(Apiinterface.class);
            Call<LoginResponse> call = apiinterface.getRefresh(tokenRefreshRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                    if(response.isSuccessful()){
                        if(response.body().getResult().equals("REISSUE_SUCCESS")){
                            LoginResponse.Data mToken = response.body().getData();
                            Token token = new Token(mToken.getAccessToken(),mToken.getRefreshToken(),mToken.getExpireIn(),mToken.getTokentype());

                            SharedPreferences pref = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

                            pref.edit().putString("oauth.accesstoken", token.getAccessToken()).apply();
                            pref.edit().putString("oauth.refreshtoken", token.getRefreshToken()).apply();
                            pref.edit().putString("oauth.expire", token.getExpireIn()).apply();
                            pref.edit().putString("oauth.tokentype",token.getGrantType()).apply();
                        }
                    }
                    else if(response.body().getResult().equals("INVALID_REFRESH_TOKEN")){
                        Log.w("actokintcept,getref", "리프레시 토큰이 유효하지 않습니다");

                    }else if(response.body().getResult().equals("MISMATCH_REFRESH_TOKEN")){
                        Log.w("actokintcept,getref", "리프레시 토큰의 유저 정보가 일치하지 않습니다");

                    }else if(response.body().getResult().equals("REFRESH_TOKEN_NOT_FOUND")){
                        Log.w("actokintcept,getref", "로그아웃된 사용자입니다.");
                    }
                    else{
                        Log.d("actokintcept,getref", new Gson().toJson(response.errorBody()));
                        Log.d("actokintcept,getref", response.toString());
                        Log.d("actokintcept,getref", String.valueOf(response.code()));
                        Log.d("actokintcept,getref", response.message());
                        Log.d("actokintcept,getref", String.valueOf(response.raw().request().url().url()));
                        Log.d("actokintcept,getref", new Gson().toJson(response.raw().request()));
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d("actokintcept", t.toString());
                }
            });

        }

        return response;
    }
}
