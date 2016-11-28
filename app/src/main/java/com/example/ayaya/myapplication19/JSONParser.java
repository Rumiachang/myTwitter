package com.example.ayaya.myapplication19;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ayaya on 2016/11/25.
 */
//コード参考：https://sites.google.com/site/technoute/android/thread/params
//コード参考:http://qiita.com/a_nishimura/items/19cf3f60ad1dd3f66a84
public class JSONParser extends AsyncTask<String, Void, String> {
    private String jsonObjectName;
    /*
        * バックグラウンドで実行する処理
        *
        *  @param params: Activityから受け渡されるデータ
        *  @return onPostExecute()へ受け渡すデータ
        */
    @Override
    protected String doInBackground(String... params) {
        String urlSt = params[0]; //URL平文
        this.jsonObjectName =params[1]; //JSONオブジェクト名
        HttpURLConnection con;
        URL url;
        String readSt = "";
        try{
            url = new URL(urlSt);
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setInstanceFollowRedirects(false);
            con.setDoInput(true);
            con.setDoOutput(false);
            con.connect();
            InputStream in = con.getInputStream();
            readSt =readInputStream(in);

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return readSt;
    }
    /*
     * メインスレッドで実行する処理
     *
     *  @param param: doInBackground()から受け渡されるデータ
     */
    @Override
    protected void onPostExecute(String result) {
        List<String> stList= new ArrayList<String>();
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(result).getJSONArray(this.jsonObjectName);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int i = 0; i<jsonArr.length(); i++){
            try {
                stList.add(jsonArr.getString(i));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public String readInputStream(InputStream in) throws IOException{
       StringBuilder sb = new StringBuilder();
        String st;
        BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        while((st = br.readLine()) != null){
            sb.append(st);
        }
        try{
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
