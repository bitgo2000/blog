package com.snmp.blog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.snmp.blog.BlogListView.BlogNostrListAdapter;
import com.snmp.blog.BlogListView.BlogTweetListAdapter;
import com.snmp.utils.LogUtils;
import com.snmp.utils.PreferenceManager;

public class BlogListActivity extends Activity {
    public static final String TAG = BlogListActivity.class.getSimpleName();
    public static final boolean TWEET = true;
    private static ArrayList<BlogTweetItemData> mTweetDataList = new ArrayList<BlogTweetItemData>();
    private static ArrayList<NostrItemData> mNostrDataList = new ArrayList<NostrItemData>();
    private BlogListView mBlogListView;
    private TextView mTitle;
    private int mSwitchIndex = 0;
    public boolean mEncrypt = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwitchIndex = PreferenceManager.getInt("blog_show_index", 0);
        if (Math.abs(PreferenceManager.getLong("last_enter_time", 0) - System.currentTimeMillis()) > 500) {
            // PreferenceManager.putLong("last_enter_time",
            // System.currentTimeMillis());
            // finish();
            // return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.blog);
        initView();

        Intent intent = new Intent("com.snmp.blogwidget.WidgetTimeService");
        intent.setPackage(getPackageName());
        startService(intent);
    }

    public void initView() {
        findViewById(R.id.blog_title_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialog.showSearchDialog(BlogListActivity.this, mTweetDataList);
            }
        });
        mTitle = (TextView) findViewById(R.id.blog_title_txt);
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEncrypt) {
                    return;
                }
                mSwitchIndex += 1;
                if (mSwitchIndex >= 3) {
                    mSwitchIndex = 0;
                }
                refresh();
                PreferenceManager.putInt("blog_show_index", mSwitchIndex);
            }
        });

        mBlogListView = new BlogListView(this);
        mBlogListView.setDivider(null);
        mBlogListView.init(this);
        refresh();
        ((LinearLayout) findViewById(R.id.root)).addView(mBlogListView);
    }

    private void refresh() {
        String assetStr = readFromAssets(this);
        if (TWEET) {
            parseTweets(assetStr);
        } else {
            parseNostr(assetStr);
        }
        ListAdapter adapter = mBlogListView.getAdapter();
        if (TWEET) {
            ((BlogTweetListAdapter) adapter).refresh(mTweetDataList);
        } else {
            ((BlogNostrListAdapter) adapter).refresh(mNostrDataList);
        }
    }

    private void parseTweets(String data) {
        mTweetDataList.clear();

        try {
            JSONObject jsonObject = new JSONObject(data);

            JSONArray array = jsonObject.getJSONArray("list");
            mTweetDataList.clear();
            for (int i = 0; i < array.length(); i++) {
                BlogTweetItemData item = new BlogTweetItemData();
                item.parseJson((JSONObject) array.get(i));
                if (item.mKind != 1) {
                    continue;
                }
                if (SelectDialog.getSelectPref().equals(item.mContent)) {
                    mTweetDataList.add(item);
                }
            }

            for (int i = 0; i < array.length(); i++) {
                BlogTweetItemData item = new BlogTweetItemData();
                item.parseJson((JSONObject) array.get(i));
                if (item.mKind != 1) {
                    continue;
                }
                int type = 2;

                if (item.mContent.contains("——")) {
                    type = 0;
                } else if (item.mContent.contains("btc") || item.mContent.contains(getString(R.string.full_btc))) {
                    type = 2;
                } else if (item.mContent.contains(getString(R.string.full_asset1))
                        || item.mContent.contains(getString(R.string.full_asset2))
                        || item.mContent.contains(getString(R.string.full_asset3))
                        || item.mContent.contains(getString(R.string.full_asset4))
                        || item.mContent.contains(getString(R.string.full_asset5))
                        || item.mContent.contains(getString(R.string.full_asset6))
                        || item.mContent.contains(getString(R.string.full_asset7))
                        || item.mContent.contains(getString(R.string.full_asset8))) {
                    type = 1;
                }

                if (mSwitchIndex == type) {
                    mTweetDataList.add(item);
                }

            }
            mTitle.setText("blog(" + mTweetDataList.size() + ")");
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e(TAG, "parseNostr " + e);
        }
    }

    private void parseNostr(String data) {
        mNostrDataList.clear();

        try {
            JSONObject jsonObject = new JSONObject(data);

            JSONArray array = jsonObject.getJSONArray("list");
            mNostrDataList.clear();
            for (int i = 0; i < array.length(); i++) {
                NostrItemData item = new NostrItemData();
                item.parseJson((JSONObject) array.get(i));
                if (item.mKind == 1) {
                    mNostrDataList.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e(TAG, "parseNostr " + e);
        }
    }

    private String readFromAssets(Context context) {
        BufferedReader bfr = null;
        AssetManager assets = context.getAssets();

        try {
            String name = "nostr-backup.js";
            if (TWEET) {
                name = "tweets.js";
            }
            InputStream open = assets.open(name);
            bfr = new BufferedReader(new InputStreamReader(open));

            InputStream is = open;
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            String result = new String(buffer, "utf8");
            LogUtils.d(TAG, "readFromAssets " + result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(TAG, "readFromAssets " + e);
        } finally {

            try {
                bfr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
