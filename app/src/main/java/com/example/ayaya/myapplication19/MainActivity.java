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


public class MainActivity extends AppCompatActivity {
    private TweetAdapter mAdapter;
    private ListView lv;
    private Handler mHandler = new Handler();
    private Twitter mTwitter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        } else {
            mAdapter = new TweetAdapter(this);
            lv=(ListView) findViewById(R.id.listView1);
            registerForContextMenu(lv);
            lv.setAdapter(mAdapter);
            mTwitter = TwitterUtils.getTwitterInstance(this);
            MyStreamAdapter mMyStreamAdapter = new MyStreamAdapter();
            reloadTimeLine();
            TwitterStreamFactory twitterStreamFactory =
                    new TwitterStreamFactory(TwitterUtils.setConfig(getApplicationContext()));
            // 2. TwitterStream をインスタンス化する
            TwitterStream twitterStream = twitterStreamFactory.getInstance();
            // ユーザーストリーム操作
                // 4. TwitterStream に UserStreamListener を実装したインスタンスを設定する
                twitterStream.addListener(mMyStreamAdapter);
                // 5. TwitterStream#user() を呼び出し、ユーザーストリームを開始する
                twitterStream.user();

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TweetActivity.class);
                startActivity(intent);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (view.getId()){
                    case R.id.iconButton:
                        Status status = mAdapter.getItem(pos);
                        assert status != null;
                        Utils.showToast("アイコンがクリックされたよ", getApplicationContext());
                        long userId = status.getUser().getId();
                        String screenName= status.getUser().getScreenName();
                      Intent intent = new Intent(getApplicationContext(), UserTimelineActivity.class);
                        intent.putExtra("USER_ID", userId);
                        intent.putExtra("SCREEN_NAME", screenName);
                        startActivity(intent);
                        break;
                }
            }
        });
        /*
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                          @Override
                                          public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                            //  switch (view.getId()){
                                                 // case R.id.iconButton:
                                                      Utils.showToast("アイコンが長押しクリックされたよ", getApplicationContext());
                                                      String[] strItems ={
                                                              "この人の見ているTLを見る"
                                                      };
                                                      final Status status = mAdapter.getItem(pos);
                                                      assert status != null;
                                                     /* new AlertDialog.Builder(getApplicationContext()).setTitle("メニュー").setItems(strItems, new DialogInterface.OnClickListener() {
                                                          @Override
                                                          public void onClick(DialogInterface dialogInterface, int i) {
                                                              switch (i){
                                                                  case 0:
                                                                      Utils.showToast("この人の見ているTLを表示します.", getApplicationContext());
                                                                      long userId = status.getUser().getId();
                                                                      String screenName= status.getUser().getScreenName();
                                                                      Intent intent = new Intent(getApplicationContext(), UserHomeTimeLineActivity.class);
                                                                      intent.putExtra("USER_ID", userId);
                                                                      intent.putExtra("SCREEN_NAME", screenName);
                                                                      startActivity(intent);
                                                                      break;
                                                              }
                                                          }
                                                      }).show();

                                             // }
                                              return true;
                                          }
        });
        */

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    static final int CONTEXT_MENU1_ID = 0;
    static final int CONTEXT_MENU2_ID = 1;

    //コード参考:http://techbooster.org/android/ui/7490/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        //コンテキストメニューの設定
        menu.setHeaderTitle("アカウントに対する操作");
        //Menu.add(int groupId, int itemId, int order, CharSequence title)
        menu.add(0, CONTEXT_MENU1_ID, 0, "この人の見ているTLを表示する");
        menu.add(0, CONTEXT_MENU2_ID, 0, "メニュー2");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //コード参考:http://androidguide.nomaki.jp/html/memo_app/memo_app_listope.html
        switch (item.getItemId()) {
            case CONTEXT_MENU1_ID:
                Status status = mAdapter.getItem(info.position);
                assert status != null;
                long userId = status.getUser().getId();
                String screenName= status.getUser().getScreenName();
                Utils.showToast("ユーザーIDは"+String.valueOf(userId)+"スクリーンネームは"+screenName+"です", this);
                Intent intent = new Intent(getApplicationContext(), UserHomeTimeLineActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("SCREEN_NAME", screenName);
                startActivity(intent);
                return true;
            case CONTEXT_MENU2_ID:
                //TODO:メニュー押下時の操作
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_refresh:
                reloadTimeLine();
                return true;

            case R.id.menu_delete_token_and_token_secret:{
                TwitterUtils.deleteToken(getApplicationContext());
                Intent intent = new Intent(this, TwitterOAuthActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            case R.id.menu_tweet:{
                Intent intent = new Intent(this, TweetActivity.class);
                startActivity(intent);
                return true;
            }

        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reloadTimeLine() {
        AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    return mTwitter.getHomeTimeline();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> result) {
                if (result != null) {
                    mAdapter.clear();
                    for (twitter4j.Status status : result) {
                        mAdapter.add(status);
                    }
                    lv.setSelection(0);
                } else {
                    showToast("タイムラインの取得に失敗しました");
                }
            }
        };
        task.execute();
    }

    //コード参考：https://gist.github.com/takke/c050c93e57e976385d8b
    class MyStreamAdapter extends UserStreamAdapter {
        @Override
        public void onStatus (final Status status){

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.insert(status, 0);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
class TweetAdapter extends ArrayAdapter<twitter4j.Status> {

    private LayoutInflater mInflater;

    public TweetAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_tweet, null);
        }
        Status item = getItem(position);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        assert item != null;
        name.setText(item.getUser().getName());
        TextView screenName = (TextView) convertView.findViewById(R.id.screenName);
        screenName.setText("@" + item.getUser().getScreenName());
        TextView text = (TextView) convertView.findViewById(R.id.text);
        text.setText(item.getText());
        SmartImageView iconButton = (SmartImageView) convertView.findViewById(R.id.iconButton);
        iconButton.setImageUrl(item.getUser().getProfileImageURL());

        //コード参考:http://atgb.cocolog-nifty.com/astimegoesby/2011/02/listviewactivit.html
        iconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView)parent).performItemClick(view, position, view.getId());
            }
        });
        iconButton.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                ((ListView)parent).performLongClick();
                return true;
            }
        });
        return convertView;
    }
}
