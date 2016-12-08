package com.example.ayaya.myapplication19;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import twitter4j.Status;

public class ChangingAccountsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changing_accounts);
        ListView lv = (ListView)findViewById(R.id.listView2);

    }

}
class ChangingAccountsAdapter extends ArrayAdapter<Status> {

    private LayoutInflater mInflater;

    public ChangingAccountsAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_accounts, null);
        }
        Status item = getItem(position);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(item.getUser().getName());
        TextView screenName = (TextView) convertView.findViewById(R.id.screenName);
        screenName.setText("@" + item.getUser().getScreenName());
        SmartImageView icon = (SmartImageView) convertView.findViewById(R.id.icon);
        icon.setImageUrl(item.getUser().getProfileImageURL());
        return convertView;
    }
}
