package org.techtown.dingdong.mytown;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.R;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TownWhenStartActivity extends AppCompatActivity implements TownAdapter.OnItemClickListener{
    static final int PERMISSION_REQUEST_READ_LOCATION = 0x00000001;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 ;

    Toolbar toolbar;
    Boolean what;
    Button btn_location;
    TownAdapter adapter;
    RecyclerView recyclerView;
    SearchView search_town;
    String Townname, city, district; //선택된 동 이름
    // 검색하는 기능 ArrayList<Town> TownItemList, filteredList;
    ArrayList<TownItem> townlist = new ArrayList<TownItem>();

    double latitude;
    double longtitude;

    Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town_when_start);


        what= getIntent().getBooleanExtra("what", true);
        btn_location = findViewById(R.id.btn_location);
        toolbar= findViewById(R.id.toolbar4);

        search_town = findViewById(R.id.action_search);
        //edit_town.addTextChangedListener(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        recyclerView = (RecyclerView)findViewById(R.id.revi_TownList);


        adapter = new TownAdapter(getApplicationContext(), townlist, what);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false ));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL); //밑줄
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.setOnItemClickListener(this);


        //if 처음실행, 요청이 거부돼있을 때
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_READ_LOCATION);




        //받아온 위치 서버로 전달


        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access, refresh, expire, tokentype);
        token.setContext(TownWhenStartActivity.this);

        Log.d(">??", String.valueOf(access));


        startLocationService();


        //현재위치로 찾기 버튼 리스너
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationService();

            }
        });

        //리스트뷰 검색기능 change listener


    }
    private  void setUpRecyclerView(){


        adapter = new TownAdapter(getApplicationContext(), townlist, what);
        recyclerView.setAdapter(adapter);

    }


    private void startLocationService() { //현재위치 주소로 변환하고 서버로 구 데이터 보내기
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //LocationManager객체 참조
        GPSListener gpsListener = new GPSListener();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }

            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //이전에 확인했던 위치 정보 가져오기
            if (location != null) {
               latitude = location.getLatitude();
               longtitude = location.getLongitude();
                Geocoder g = new Geocoder(this);
                List<Address> address = null;
                Log.d("Log","latitude: "+latitude+"longtitude: "+longtitude );


                try {
                    address = g.getFromLocation(latitude, longtitude, 10);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("LOG", "입출력오류");
                }
                if (address != null) {
                    if (address.size() == 0) {
                        Log.d("주소찾기 오류", "주소찾기 오류");
                    } else {
                        String localregion = address.get(0).getAddressLine(0);
                        String[] str = localregion.split("\\s+");
                        city = str[1];
                        district = str[2];
                        Log.d("찾은 주소", city+district);

                        if(!city.isEmpty()){
                            Log.d("LOG","startlocation");
                            findlocal(token);
                        }
                        else {

                        }


                    }
                }
            }
            else if(location == null){
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, gpsListener);
            }

        }catch (SecurityException e){
            e.printStackTrace();
        }

    }



    private class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longtitude = location.getLongitude();
            Log.d("Log","latitude: "+latitude+"longtitude: "+longtitude );
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }

    public void findlocal(Token token){
        Apiinterface apiinterface = Api.createService(Apiinterface.class , token, TownWhenStartActivity.this);
        Call<localResponse> call = apiinterface.getLocal(city, district);
        call.enqueue(new Callback<localResponse>() {
            @Override
            public void onResponse(Call<localResponse> call, Response<localResponse> response) {
                if(response.isSuccessful() && response.body()!= null){
                    if(response.body().getCode().equals("LOCAL_READ_SUCCESS")) {
                        localResponse res = response.body();
                        townlist.addAll(res.getData());
                        //townlist = (ArrayList<TownItem>) res.getData();
                        Log.d("log", "제대로 실행됨");
                        adapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onFailure(Call<localResponse> call, Throwable t) {
                Log.d("동네 목록 조회", "뭐가 잘못된거지?"+ t);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                Intent resultIntent = new Intent();
                resultIntent.putExtra("town","");
                if(what ==true ) {
                    setResult(1,resultIntent );
                }
                else{
                    setResult(2, resultIntent);
                }
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onItemClick(View view, TownItem item) {
        view.setBackgroundColor(Color.GRAY);
        String num = item.getId();
        String name = item.getName();
        //두개 데이터 가지고 화면전환
        Log.d("데이터확인",num+" "+name);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("town",name);
        resultIntent.putExtra("num",num);
        if(what ==true ) {
            setResult(1,resultIntent );
        }
        else{
            setResult(2, resultIntent);
        }
        finish();
    }


}