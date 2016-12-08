package com.example.ayaya.myapplication19;

/**
 * Created by ayaya on 2016/10/26.
 */

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.ArrayRes;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TwitterUtils {

    private static final String ARR_OF_TOKEN = "arr_of_token";
    private static final String ARR_OF_TOKEN_SECRET = "arr_of_token_secret";
    private static final String CURRENT_TOKEN = "current_token";
    private static final String CURRENT_TOKEN_SECRET = "current_token_secret";
    private static final String PREF_NAME = "twitter_access_token";
    private static List<String> arrOfUserToken =new ArrayList<>();
    private static List<String> arrOfUserSecret =new ArrayList<>();

    /**
     * Twitterインスタンスを取得します。アクセストークンが保存されていれば自動的にセットします。
     *
     * @param context
     * @return
     */
    public static Twitter getTwitterInstance(Context context) {
        TwitterFactory factory = new TwitterFactory(setConfig(context));
        return factory.getInstance();
    }

    public static Configuration setConfig(Context context){
        ConfigurationBuilder cb =new ConfigurationBuilder();
        cb.setOAuthConsumerKey(context.getString(R.string.twitter_consumer_key));
        cb.setOAuthConsumerSecret(context.getString(R.string.twitter_consumer_secret));
        cb.setOAuthAccessToken(loadAccessToken(context));
        cb.setOAuthAccessTokenSecret(loadAccessTokenSecret(context));
        return cb.build();
    }
    /**
     * アクセストークンをプリファレンスに保存します。
     *
     * @param context
     * @param accessToken
     */
    public static void storeAccessToken(Context context, AccessToken accessToken) {
        /* if(arrOfUserToken.indexOf(accessToken.getToken()) != -1 &&
                arrOfUserSecret.indexOf(accessToken.getTokenSecret()) != -1) {
            arrOfUserToken.add(accessToken.getToken());
            arrOfUserSecret.add(accessToken.getTokenSecret()); */
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        Editor editor = preferences.edit();

        if (preferences.getString(ARR_OF_TOKEN, null) == null && preferences.getString(ARR_OF_TOKEN_SECRET, null) == null
               /* && preferences.getString(CURRENT_TOKEN, null) == null && preferences.getString(CURRENT_TOKEN_SECRET,null) == null */) {
            arrOfUserToken.add(accessToken.getToken());
            arrOfUserSecret.add(accessToken.getTokenSecret());
            editor.putString(ARR_OF_TOKEN, gson.toJson(arrOfUserToken));
            editor.putString(ARR_OF_TOKEN_SECRET, gson.toJson(arrOfUserSecret));
            editor.putString(CURRENT_TOKEN, accessToken.getToken());
            editor.putString(CURRENT_TOKEN_SECRET, accessToken.getTokenSecret());
            editor.apply();
        } else {
            List<String> arrOfUserTokenDeserialized = new ArrayList<>();
            List<String> arrOfUserTokenSecretDeserialized = new ArrayList<>();
            arrOfUserTokenDeserialized = gson.fromJson(preferences.getString(ARR_OF_TOKEN, null), ArrayList.class);
            arrOfUserTokenSecretDeserialized = gson.fromJson(preferences.getString(ARR_OF_TOKEN_SECRET, null), ArrayList.class);

            if (arrOfUserTokenDeserialized.indexOf(accessToken.getToken()) == -1 &&
                    arrOfUserTokenSecretDeserialized.indexOf(accessToken.getTokenSecret()) == -1) {
                arrOfUserTokenDeserialized.add(accessToken.getToken());
                arrOfUserTokenSecretDeserialized.add(accessToken.getTokenSecret());
                editor.putString(ARR_OF_TOKEN, gson.toJson(arrOfUserTokenDeserialized));
                editor.putString(ARR_OF_TOKEN_SECRET, gson.toJson(arrOfUserTokenSecretDeserialized));
                editor.apply();
            } else {
                editor.apply();
            }
        }
    }
    /**
     * アクセストークンをプリファレンスから読み込みます。
     *
     * @param context
     * @return
     */

    public static String loadAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        List<String> arrOfUserTokenDeserialized = new ArrayList<>();
        arrOfUserTokenDeserialized = gson.fromJson(preferences.getString(ARR_OF_TOKEN, null), ArrayList.class);
        String token = null;
        if (arrOfUserTokenDeserialized != null) {
            token = arrOfUserTokenDeserialized.get(arrOfUserTokenDeserialized.size() - 1);
        }

        if (token != null ) {
            return token;
        }else {
            return null;
        }
    }

    public static String loadAccessTokenSecret(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
         List<String> arrOfUserTokenSecretDeserialized = new ArrayList<>();

         arrOfUserTokenSecretDeserialized = gson.fromJson(preferences.getString(ARR_OF_TOKEN_SECRET, null), ArrayList.class);

         String tokenSecret = null;
        if (arrOfUserTokenSecretDeserialized != null) {

             tokenSecret = arrOfUserTokenSecretDeserialized.get(arrOfUserTokenSecretDeserialized.size() - 1);
        }

        if (tokenSecret != null ) {
            return tokenSecret;
        }else {
            return null;
        }
    }

    public static void deleteToken(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(ARR_OF_TOKEN, null);
        editor.putString(ARR_OF_TOKEN_SECRET, null);
        editor.putString(CURRENT_TOKEN, null);
        editor.putString(CURRENT_TOKEN_SECRET, null);

        editor.apply();
    }



    /**
     * アクセストークンが存在する場合はtrueを返します。
     *
     * @return
     */
    public static boolean hasAccessToken(Context context) {
        return loadAccessToken(context) != null && loadAccessTokenSecret(context) != null;
    }
}