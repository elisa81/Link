package com.womenproiot.www.link;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class JSONHelpeAsyncTask extends AsyncTask<String, Void, ArrayList<LinkDTO.Place>> {

    final static String NAVER_MAP_SEARCH_URL = "https://naveropenapi.apigw.ntruss.com/map-place/v1/search?query=";

    /*
     *장소 검색 API는 어떤 시설이나 지리적 위치의 장소 명칭을 질의어로 입력받아 최대 5개 장소의 장소 정보를 검색합니다.
     * */
    private ArrayList<LinkDTO.Place> getLocation(String query) {

        ArrayList<LinkDTO.Place> resultList=new ArrayList<>();
        HttpURLConnection conn=null;
        InputStream in=null;

        try {
            String encodeQuery = URLEncoder.encode(query,"UTF-8");
            String apiUrl = NAVER_MAP_SEARCH_URL+query+"&coordinate=127.1054328,37.3595963";
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "hzltr7zh5e");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", "sE5yynPMqq26Vak5BRntJcX8Z7FP3xy16qzV0qUl");
            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                in = new BufferedInputStream(conn.getInputStream());
            } else {
                in = new BufferedInputStream(conn.getErrorStream());
            }
            resultList = parseJson(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                if(in!=null) in.close();
                if(conn!=null) conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return resultList;
    }

    private ArrayList<LinkDTO.Place> parseJson(InputStream in) {
        ArrayList<LinkDTO.Place> result = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(getStringFromInputStream(in));
            JSONArray placeArray = json.getJSONArray("places");
            String name,rAddr,jAddr,phone,id;
            double lat,lon, dist;
            for (int i=0 ; i<placeArray.length() ; i++) {
                name = placeArray.getJSONObject(i).getString("name");
                rAddr = placeArray.getJSONObject(i).getString("road_address");
                jAddr = placeArray.getJSONObject(i).getString("jibun_address");
                phone = placeArray.getJSONObject(i).getString("phone_number");
                lon = placeArray.getJSONObject(i).getDouble("x");
                lat = placeArray.getJSONObject(i).getDouble("y");
                dist = placeArray.getJSONObject(i).getDouble("distance");
                id = placeArray.getJSONObject(i).getString("sessionId");

                result.add(new LinkDTO().new Place(name,rAddr,jAddr,phone,lat,lon,dist,id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getStringFromInputStream(InputStream in) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line = null;
        try{
            br= new BufferedReader((new InputStreamReader(in)));
            while((line=br.readLine()) != null) {
                sb.append(line);
            }
            Log.e("JSON",sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(br!=null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


    @Override
    protected ArrayList<LinkDTO.Place> doInBackground(String... strings) {
        ArrayList<LinkDTO.Place> result = getLocation(strings[0]);
        return result;
    }
}
