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

        startLocationService();

        //token.setContext(TownWhenStartActivity.this);

        //if 처음실행, 요청이 거부돼있을 때
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_READ_LOCATION);




        //받아온 위치 서버로 전달


        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.acesstoken","");
        String refresh = pref.getString("oauth.refreshtoken","");
        String expire = pref.getString("oauth.expire","");
        String tokentype = pref.getString("oauth.tokentype","");

        token = new Token(access, refresh, expire, tokentype);
        token.setContext(TownWhenStartActivity.this);

        Log.d(">??", String.valueOf(access));






        startLocationService();
        findlocal(token);





        //recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        //서버에서 받아온 동데이터 어댑터에 추가
        //테스트용 데이터








        /*adapter.setOnItemClickListener(new OnTownItemClickListener() { // 어댑터에 리스너 설정하기
            @Override
            public int onItemClick(TownAdapter.ViewHolder holder, View view, int position) { //아이템 클릭 이벤트
                localResponse.Data data = adapter.townlist.get(position);
                Townname = data.name;
               //번호구분해서 불러오기 int num = item.getNum();

                //서버에 데이터 전송, 동데이터 전달

                if(what){
                    Intent result = new Intent();
                    result.putExtra("town", Townname);
                    result.putExtra("what", true);
                    setResult(RESULT_OK, result);
                    finish();
                }
                else {
                    Intent result = new Intent();
                    result.putExtra("town", Townname);
                    result.putExtra("what", false);
                    setResult(RESULT_OK, result);
                    finish();
                }

                return view.getId();
            }
        }); */


        //현재위치로 찾기 버튼 리스너
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationService();
                findlocal(token);
            }
        });

        //리스트뷰 검색기능 change listener


    }
    private  void setUpRecyclerView(){
        recyclerView = (RecyclerView)findViewById(R.id.revi_TownList);

        townlist = getData();

        adapter = new TownAdapter(getApplicationContext(), townlist, what);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false ));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL); //밑줄
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.setOnItemClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

    }

    private ArrayList<TownItem> getData() {
        /*ArrayList<TownItem> test = new ArrayList<>();
        TownItem townitem1 = new TownItem("1","성북동");
        test.add(0,townitem1);
        TownItem townitem2 = new TownItem("2","삼선동");
        test.add(1,townitem2);
        TownItem townitem3 = new TownItem("3","동선동");
        test.add(2,townitem3);
        TownItem townitem4 = new TownItem("4","돈암1동");
        test.add(3,townitem4);
        TownItem townitem5 = new TownItem("5","돈암2동");
        test.add(4,townitem5);
        TownItem townitem6 = new TownItem("6","안암동");
        test.add(5,townitem6);
        TownItem townitem7 = new TownItem("7","보문동");
        test.add(6,townitem7);
        TownItem townitem8 = new TownItem("8","정릉1동");
        test.add(7,townitem8);
        TownItem townitem9 = new TownItem("9","정릉2동");
        test.add(8,townitem9);
        TownItem townitem10 = new TownItem("10","정릉3동");
        test.add(9,townitem10);*/


        ArrayList<TownItem> result = new ArrayList<>();

        Apiinterface apiinterface = Api.createService(Apiinterface.class, token, TownWhenStartActivity.this);
        Call<localResponse> call = apiinterface.getLocal(city, district);
        call.enqueue(new Callback<localResponse>() {
            @Override
            public void onResponse(Call<localResponse> call, Response<localResponse> response) {
                if(response.isSuccessful() && response.body().getCode().equals("LOCAL_READ_SUCCESS")) {

                   // ArrayList<TownItem> result = new ArrayList<>();
                    for(int i=0; i<response.body().code.length(); i++) {
                        result.add(i,response.body().getData().get(i));
                    }
                }
            }
            @Override
            public void onFailure(Call<localResponse> call, Throwable t) {
                Log.d("TAG", String.valueOf(t));
             }
        });
        return result;
      // return test;

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
                List<Address> address = null; //왜 추가 해놨지?
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
                        // 구 구하기address.get(0).toString()+

                        //----------여기에 서버연결-----------------------------
                       //서버에서 동데이터 받아와서 getdata()에서 추가해주기
                        setUpRecyclerView();


                    }
                }
            }
            else if(location == null){
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, gpsListener);
            }

        }catch (SecurityException e){
            e.printStackTrace();
        }
        //서버에 동데이터 리퀘스트


    }



    /*public void searchFilter(String searchText){
        filteredList.clear();

        for(int i=0; i< TownItemList.size(); i++){
            if(TownItemList.get(i).getTown().toLowerCase().contains(searchText.toLowerCase())){
                filteredList.add(TownItemList.get(i));
            }
            adapter.filterList(filteredList);
        }

       // adapter.notifyDataSetChanged();


    }
*/

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
                        townlist = (ArrayList<TownItem>) res.getData();
                        Log.d("log", townlist.toString()+"제대로 실행됨");
                        adapter = new TownAdapter(getApplicationContext(), townlist, what);
                        recyclerView.setAdapter(adapter);
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