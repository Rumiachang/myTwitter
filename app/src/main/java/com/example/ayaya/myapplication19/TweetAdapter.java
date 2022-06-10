package com.example.ayaya.myapplication19;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import twitter4j.Status;

/**
 * Created by ayaya on 2017/01/12.
 */

class TweetAdapter extends ArrayAdapter<Status> {

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
