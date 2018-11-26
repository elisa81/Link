package com.womenproiot.www.link;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener {


    //상수정의
    static final int REQUEST_CODE = 2000;

    NaverMap naverMap=null;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //로딩화면 실행
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        //플로딩 추가버튼
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });

        //MapFragment 붙이기
        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            NaverMapOptions mapOptions = new NaverMapOptions().locationButtonEnabled(true);

            mapFragment = MapFragment.newInstance(mapOptions);
            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.map_fragment, mapFragment)
                                    .commit();
        }
        mapFragment.getMapAsync(this);

        //지도 화면에 현재위치 찾기 버튼 넣기위해
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        //출발지점 검색버튼 리스너 등록
        ((Button)findViewById(R.id.btnDepatureSearch)).setOnClickListener(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationSource = null;
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        // TODO: 2018-11-27  
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnDepatureSearch:
                    String query = ((EditText)findViewById(R.id.editTextDepaturePlace)).getText().toString();
                    ArrayList<JSONHelpeAsyncTask.PlaceDTO> result = null;

                    result = new JSONHelpeAsyncTask().execute(query).get();
                    // TODO: 2018-11-27 : 첫번째 값 지도에 찍기
                    setMarker(new LatLng(result.get(0).latitude,result.get(0).longitude));



                break;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setMarker(LatLng latLng) {
        String name = "ATTENDEE";

        Marker marker = new Marker(MarkerIcons.PINK);
        marker.setPosition(latLng);
        marker.setCaptionAlign(Align.Top);
        marker.setCaptionText(name);
        marker.setMap(naverMap);

        //markerList.add(marker);
        //Log.w("[kja]다각형","위도 : " + marker.getPosition().latitude+" / 경도 : "+ marker.getPosition().longitude);

        //marker.setOnClickListener(this::onClick);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        Intent intent  =null;
        switch (item.getItemId()) {
            case R.id.meetup_reg :
                intent = new Intent(this,MeetupRegActivity.class);
                break;
            case R.id.location_reg :
                intent = new Intent(this,LocationRegActivity.class);
                break;
            case R.id.attendees :
                intent = new Intent(this,AttendeesActivity.class);
                break;
            case R.id.search_places :
                intent = new Intent(this,SearchPlacesActivity.class);
                break;
            case R.id.result_places :
                intent = new Intent(this,ResultPlacesActivity.class);
                break;
            default:return false;
        }
        startActivityForResult(intent,REQUEST_CODE);
        return true;
    }*/
}
