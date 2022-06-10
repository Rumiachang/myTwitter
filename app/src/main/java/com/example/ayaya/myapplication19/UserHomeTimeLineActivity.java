package com.example.ayaya.myapplication19;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

//import androidx.appcompat.app.ActionBarActivity;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.ListViewCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class UserHomeTimeLineActivity extends AppCompatActivity {
    private ArrayList<twitter4j.User> followList;
    private TweetAdapter mAdapter;
    private ListView lv;
    private long userId;
    private Twitter mTwitter;
    private String screenName;
    final private String TAG = "UserHomeTimeLineActiv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_time_line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTwitter = TwitterUtils.getTwitterInstance(getApplicationContext());
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        this.userId = intent.getLongExtra("USER_ID", -1);
       this.screenName = intent.getStringExtra("SCREEN_NAME");
        Utils.showToast("ユーザーIDは"+String.valueOf(userId)+"スクリーンネームは"+screenName+"です", this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAdapter = new TweetAdapter(this);
        lv = (ListView) findViewById(R.id.listViewForUsersHomeTL);
        lv.setAdapter(mAdapter);

        followList = TwitterUtils.getFollowingUsers2(mTwitter, screenName);
        for (int i= 0; i<followList.size(); i++){
            Log.d(TAG, followList.get(i).getStatus().getText());
        }

    }
    private void createUserTimeLine() {
        AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    return mTwitter.getUserTimeline(userId);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> result) {
                if (result != null) {
                    for (twitter4j.Status status : result) {
                        mAdapter.add(status);
                    }
                    lv.setSelection(0);
                } else {
                    Utils.showToast("タイムラインの取得に失敗しました", getApplicationContext());
                }
            }
        };
        task.execute();
    }

}


