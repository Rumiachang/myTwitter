package com.example.ayaya.myapplication19;

/**
 * Created by ayaya on 2016/10/26.
 */

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

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
    private static long cursor;
   private static PagableResponseList<twitter4j.User> rawData = null;
    private static boolean isLastAPICallSuccess = true;
    private static int continuousErrorCount = 0;
    private static long lastAPICallSuccessTime = 0;
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

    public static ArrayList<twitter4j.User> getFollowingUsers2(final Twitter twitter, final String screenName){
        long start = System.currentTimeMillis();
        long end = 0;

        ArrayList<twitter4j.User> dataToReturn = new ArrayList<twitter4j.User>();
        int apiCallCount = 0;
        continuousErrorCount = 0;
        isLastAPICallSuccess = true;
        lastAPICallSuccessTime = 0;

        cursor = -1;
        while(true){
            try {
                if(isLastAPICallSuccess)
                    lastAPICallSuccessTime = System.currentTimeMillis();
                AsyncTask<Void, Void, PagableResponseList<User>> task = new AsyncTask<Void, Void, PagableResponseList<User>>() {
                    @Override
                    protected PagableResponseList<User> doInBackground(Void... voids) {
                        try {
                            return rawData = twitter.getFriendsList(screenName, cursor);
                        } catch (TwitterException e) {
                            e.printStackTrace();
                            isLastAPICallSuccess = false;
                            String errorCode = e.getMessage().substring(0, 3);
                            if (errorCode.startsWith("5") || errorCode.startsWith("4")) {
                                continuousErrorCount++;
                                if (continuousErrorCount >= 3) {
                                    System.err.println("return null because of three continuous error");
                                    return null;
                                }
                                long currentTime = System.currentTimeMillis();
                                if (currentTime - lastAPICallSuccessTime > 3000) {
                                    System.err.println("return null because of The interval of the error is too long. " + (double) (currentTime - lastAPICallSuccessTime) / 1000 + "seconds");
                                    return null;
                                }
                                System.err.println(e.getMessage());
                            }
                            return null;
                        }
                    }

                };
                 //TODO: ネットワーク関係の例外発生，修正しろ
                apiCallCount++;
            } catch (TwitterException e) {
                isLastAPICallSuccess = false;
                String errorCode = e.getMessage().substring(0, 3);
                if(errorCode.startsWith("5") || errorCode.startsWith("4")) {
                    continuousErrorCount++;
                    if(continuousErrorCount >= 3) {
                        System.err.println("return null because of three continuous error");
                        return null;
                    }
                    long currentTime = System.currentTimeMillis();
                    if(currentTime - lastAPICallSuccessTime > 3000){
                        System.err.println("return null because of The interval of the error is too long. " + (double)(currentTime - lastAPICallSuccessTime)/1000 + "seconds");
                        return null;
                    }

                    System.err.println(e.getMessage());
                    continue;
                }

                end = System.currentTimeMillis();
                System.err.println("error " + apiCallCount + ", " + screenName + ", " + (double)(end - start)/1000 + "seconds " + ": " + e.getMessage());
                return null;
            }
            isLastAPICallSuccess = true;
            continuousErrorCount = 0;

            if(rawData == null || rawData.isEmpty())
                break;

            dataToReturn.addAll(rawData);
            System.out.println(screenName + ", " + cursor + ", " + (double)(System.currentTimeMillis() - lastAPICallSuccessTime)/1000 + "seconds");

            if(!rawData.hasNext())
                break;

            cursor = rawData.getNextCursor();
        }
        end = System.currentTimeMillis();
        System.out.println("" + screenName + " time:" + (double)(end - start)/1000 + "seconds " + apiCallCount + "counts, " + dataToReturn.size());

        return dataToReturn;
    }
}