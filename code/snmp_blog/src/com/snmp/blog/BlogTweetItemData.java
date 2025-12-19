package com.snmp.blog;

import org.json.JSONException;
import org.json.JSONObject;

import com.snmp.utils.LogUtils;
import com.snmp.utils.SnmpApplication;

public class BlogTweetItemData {
    public String mId;
    public String mContent;
    public String mTimeStamp;
    public long mKind;
    public boolean mCrypto;

    public BlogTweetItemData() {
    }

    public void parseJson(JSONObject jsonObject) {
        try {
            JSONObject tweet = jsonObject.getJSONObject("tweet");

            mId = tweet.getString("id");
            mContent = tweet.getString("full_text");
            mKind = 1;
            if (mContent.contains("btc")
                    || mContent.contains(SnmpApplication.getInstance().getString(R.string.full_btc))) {
                mCrypto = true;
            }
            if (mContent.contains("@")) {
                mKind = 2;
            }
            JSONObject editInfo = tweet.getJSONObject("edit_info").getJSONObject("initial");
            String util = editInfo.getString("editableUntil");
            mTimeStamp = util.substring(0, Math.min(util.length(), 10));;
            LogUtils.d(BlogListActivity.TAG, "parseJsaaaaaaaaaaaaaaa111 " + mContent);
            LogUtils.d(BlogListActivity.TAG, "parseJson " + mContent);
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.d(BlogListActivity.TAG, "parseJson " + e);
        }
    }
}
