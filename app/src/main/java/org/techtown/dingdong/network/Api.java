package org.techtown.dingdong.network;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;
import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.login_register.LoginResponse;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.login_register.TokenRefreshRequest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    public static String BASE_URL = "http://3.38.61.13:8080/";
    public static Retrofit retrofit;
    private static Context context;
    private static Token token;
    public static final String ANDROID_EMULATOR_LOCALHOST = "10.0.2.2";
    public static final String SERVER_PORT = "8080";



    public static <S> S createService(Class<S> serviceClass) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, Token accessToken, Context c) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(new AccessTokenInterceptor(accessToken));
                /*.readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS);*/
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Log.d("토큰", "정상");

        if(accessToken != null){
            context = c;
            token = accessToken;
            Log.d("토큰", "if문들어감");
            final Token token  = accessToken;
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Log.d("토큰", "토큰저장됨");

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept","application/json")
                            .header("Content-type", "application/json")
                            .header("Authorization",
                                    token.getGrantType() + " " + token.getAccessToken())
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();

                    return chain.proceed(request);
                }
            });

        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        Log.d("토큰", "리턴잘됨");
        return retrofit.create(serviceClass);
    }


    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }


}
