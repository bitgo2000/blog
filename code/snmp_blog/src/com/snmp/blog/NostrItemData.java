package com.snmp.blog;

import org.json.JSONException;
import org.json.JSONObject;

import com.snmp.utils.LogUtils;


public class NostrItemData {
    public String mId;
    public String mContent;
    public long mTimeStamp;
    public long mKind;

    public NostrItemData() {
    }

    public void parseJson(JSONObject jsonObject) {
        try {
            mId = jsonObject.getString("id");
            mContent = jsonObject.getString("content");
            mTimeStamp = jsonObject.getLong("created_at");
            mKind = jsonObject.getLong("kind");
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.d(BlogListActivity.TAG, "parseJson " + e);
        }
    }
}
