package com.example.ayaya.myapplication19;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by ayaya on 2016/12/16.
 */

public class Utils {
    public static void showToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


}
