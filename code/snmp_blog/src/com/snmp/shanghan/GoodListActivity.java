package com.snmp.shanghan;

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

import com.snmp.blog.R;
import com.snmp.shanghan.GoodListView.GoodListAdapter;
import com.snmp.utils.LogUtils;
import com.snmp.utils.PreferenceManager;

public class GoodListActivity extends Activity {
    public static final String TAG = GoodListActivity.class.getSimpleName();
    private ArrayList<GoodItemData> mGoodDataList = new ArrayList<GoodItemData>();
    private GoodListView mGoodListView;
    private TextView mTitle;
    private int mSwitchIndex = 0;

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
                SearchDialog.showSearchDialog(GoodListActivity.this, mGoodDataList);
            }
        });
        mTitle = (TextView) findViewById(R.id.blog_title_txt);
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwitchIndex += 1;
                if (mSwitchIndex >= 3) {
                    mSwitchIndex = 0;
                }
                refresh();
                PreferenceManager.putInt("blog_show_index", mSwitchIndex);
            }
        });

        mGoodListView = new GoodListView(this);
        mGoodListView.setDivider(null);
        mGoodListView.init(this);
        refresh();
        ((LinearLayout) findViewById(R.id.root)).addView(mGoodListView);
    }

    private void refresh() {
        String assetStr = readFromAssets(this);
        parseNostr(assetStr);
        ListAdapter adapter = mGoodListView.getAdapter();
        ((GoodListAdapter) adapter).refresh(mGoodDataList);
    }

    private void parseNostr(String data) {
        mGoodDataList.clear();

        try {
            JSONObject jsonObject = new JSONObject(data);

            JSONArray array = jsonObject.getJSONArray("list");
            mGoodDataList.clear();
            for (int i = 0; i < array.length(); i++) {
                GoodItemData item = new GoodItemData();
                item.parseJson((JSONObject) array.get(i));
                if (item.mKind == 1) {
                    mGoodDataList.add(item);
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
