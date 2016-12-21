package com.example.ayaya.myapplication19;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by ayaya on 2016/12/21.
 */

public class JSInterface {
    private Context context = null;
    private final String  TAG = "JSInterface";
    public JSInterface(Context context){
        this.context = context;
    }
    public JSInterface(){

    }
    @JavascriptInterface
    public void showLogFromJS(String str){
        Log.d(TAG, str);
    }
}
