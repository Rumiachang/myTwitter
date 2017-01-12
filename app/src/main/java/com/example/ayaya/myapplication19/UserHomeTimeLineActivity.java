package com.example.ayaya.myapplication19;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.image.SmartImageView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamAdapter;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

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


