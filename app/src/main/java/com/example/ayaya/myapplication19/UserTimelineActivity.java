package com.example.ayaya.myapplication19;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

public class UserTimelineActivity extends AppCompatActivity implements FragmentOfUserMediasGrid.OnFragmentInteractionListener,
FragmentOfUserTimeLineList.OnFragmentInteractionListener, FragmentOfUsersFavoritesList.OnFragmentInteractionListener{
    private String screenName;
    private long userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        this.userId = intent.getLongExtra("USER_ID", -1);
        this.screenName = intent.getStringExtra("SCREEN_NAME");
        WebView myWebView = (WebView)findViewById(R.id.backgroundWebView);
        myWebView.loadUrl("file:///android_asset/downloader_media_timeline_image_url.html");
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        myWebView.getSettings().setJavaScriptEnabled(true);



        if (BuildConfig.DEBUG && userId == -1) {throw new AssertionError(); }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            //a
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Utils.showToast("ユーザーIDは"+String.valueOf(userId)+"スクリーンネームは"+screenName+"です", this);
        setFragment();
    }
    private void setFragment(){
        FragmentManager manager = getSupportFragmentManager();
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(manager ,this.userId, this.screenName);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
