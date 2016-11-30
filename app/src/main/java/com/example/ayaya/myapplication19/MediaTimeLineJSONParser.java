package com.example.ayaya.myapplication19;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by ayaya on 2016/11/25.
 */
//コード参考：https://sites.google.com/site/technoute/android/thread/params
//コード参考:http://qiita.com/a_nishimura/items/19cf3f60ad1dd3f66a84
public class MediaTimeLineJSONParser extends AsyncTask<String, Void, String>
        implements DialogInterface.OnCancelListener{
   private final String TAG = "MediaTimeLineJSONParser";
   private ProgressDialog pDialog;
    private String MediaTimeLineItems = null;
    private String hasMoreItems=null;
    private String newLatentCount = null;

    /*
       * バックグラウンドで実行する処理
       *
       *  @param params: Activityから受け渡されるデータ
       *  @return onPostExecute()へ受け渡すデータ
       */
    @Override
    protected String doInBackground(String... params) {
        String urlSt = params[0]; //URL平文
         //this.jsonObjectName =params[1]; //JSONオブジェクト名
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
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("Please wait");
        pDialog.setMessage("Loading data...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.setOnCancelListener(this);
        pDialog.setMax(100);
        pDialog.setProgress(0);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject JSONObj = new JSONObject(result);
            this.MediaTimeLineItems = JSONObj.getString("items_html");
            this.hasMoreItems = JSONObj.getString("has_more_items");
            this.newLatentCount = JSONObj.getString("new_latent_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String readInputStream(InputStream in) throws IOException{
       StringBuilder sb = new StringBuilder();
        String st;
        BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        while((st = br.readLine()) != null){
            sb.append(st);
            publishProgress();
            Log.d(TAG, "readInputStream");
        }
        try{
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
        //k
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }

    public Document getMediaTimeLineHTMLDoc(){
        return Jsoup.parse(this.MediaTimeLineItems);
    }
    public Boolean hasMoreItems(){
        return this.hasMoreItems.equals("true");
    }
    public int showNumberOfItems(){
        return Integer.parseInt(this.newLatentCount);
    }
}
