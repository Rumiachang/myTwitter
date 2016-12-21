package com.example.ayaya.myapplication19;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by ayaya on 2016/12/16.
 */

public class Utils {
    public static void showToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    //コードパクリ元：http://fantom1x.blog130.fc2.com/blog-entry-120.html
    //設定値
    private static final int DEFAULT_READ_LENGTH = 8192;      //一度に読み込むバッファサイズ

    //ストリームから読み込み、バイト配列で返す
    public static final byte[] readStream(InputStream inputStream, int readLength) throws IOException {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream(readLength);  //一時バッファのように使う
        final byte[] bytes = new byte[readLength];    //read() 毎に読み込むバッファ
        final BufferedInputStream bis = new BufferedInputStream(inputStream, readLength);

        try {
            int len = 0;
            while ((len = bis.read(bytes, 0, readLength)) > 0) {
                byteStream.write(bytes, 0, len);    //ストリームバッファに溜め込む
            }
            return byteStream.toByteArray();    //byte[] に変換

        } finally {
            try {
                byteStream.reset();     //すべてのデータを破棄
                bis.close();            //ストリームを閉じる
            } catch (Exception e) {
                //IOException
            }
        }
    }

    //ストリームから読み込み、テキストエンコードして返す
    public static final String loadText(InputStream inputStream, String charsetName)
            throws IOException, UnsupportedEncodingException {
        return new String(readStream(inputStream, DEFAULT_READ_LENGTH), charsetName);
    }
    private static final String DEFAULT_ENCODING = "UTF-8";//デフォルトのエンコード

    //assets フォルダから、テキストファイルを読み込む(Android 用)
    public static final String loadTextAsset(String fileName, Context context) throws IOException {
        final AssetManager assetManager = context.getAssets();
        InputStream is = assetManager.open(fileName);
        return loadText(is, DEFAULT_ENCODING);
    }

}
