package com.snmp.shanghan;

import org.json.JSONException;
import org.json.JSONObject;

import com.snmp.utils.LogUtils;


public class GoodItemData {
    public String mId;
    public String mTitle;
    public String mName;
    public String mKeyword;
    public String mContent;
    public int mType;
    public long mTimeStamp;
    public long mKind;

    public GoodItemData() {
    }

    public void parseJson(JSONObject jsonObject) {
        try {
            mId = jsonObject.getString("id");
            mContent = jsonObject.getString("content");
            mTimeStamp = jsonObject.getLong("created_at");
            mKind = jsonObject.getLong("kind");
            parseTypeSHL();
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.d(GoodListActivity.TAG, "parseJson " + e);
        }
    }

    private void parseTypeSHL() {
        String key = "人纪-伤寒论";
        if (mContent.contains(key)) {
            mName = key;
            mType = 1;

            String target = "健康调理理疗";
            int index = mContent.indexOf(target);
            mTitle = mContent.substring(0, index);
        }
    }
}
