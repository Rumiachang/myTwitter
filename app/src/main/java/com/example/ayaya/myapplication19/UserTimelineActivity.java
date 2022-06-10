package com.example.ayaya.myapplication19;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;

public class UserTimelineActivity extends AppCompatActivity implements FragmentOfUserMediasGrid.OnFragmentInteractionListener,
FragmentOfUserTimeLineList.OnFragmentInteractionListener, FragmentOfUsersFavoritesList.OnFragmentInteractionListener{
    private String screenName;
    private long userId;
    private String htmlString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        this.userId = intent.getLongExtra("USER_ID", -1);
        this.screenName = intent.getStringExtra("SCREEN_NAME");
        final WebView myWebView = (WebView)findViewById(R.id.backgroundWebView);
        //myWebView.loadUrl("https://twitter.com/i/profiles/show/shONe_Banana/media_timeline");
        //myWebView.loadUrl("file:///android_asset/download.html");

        try {
            String htmlFileName = "download.html";
            htmlString= Utils.loadTextAsset(htmlFileName, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String DOMAIN = "https://twitter.com/";
        myWebView.loadDataWithBaseURL(DOMAIN, htmlString, "text/html", "UTF-8", null);
        WebSettings settings = myWebView.getSettings();
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        myWebView.addJavascriptInterface(new JSInterface(), "NativeMethods");
        myWebView.setWebViewClient(new WebViewClient(){
            
            @Override
            public void onPageFinished(WebView view, String url){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                   myWebView.evaluateJavascript("getImageUrlJSONArr('shONe_Banana');", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            Log.d("Log", s);
                        }
                    });

                };
            }
        });




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
