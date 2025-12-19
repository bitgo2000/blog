package com.snmp.shanghan;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.snmp.blog.R;
import com.snmp.utils.SnmpApplication;

public class GoodListView extends ListView {
    protected GoodListAdapter mGoodAdapter;
    private Activity mActivity;

    public GoodListView(Context context) {
        super(context);
    }

    public GoodListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoodListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void init(Activity activity) {
        mActivity = activity;
        mGoodAdapter = new GoodListAdapter(null);
        setAdapter(mGoodAdapter);
        setOnItemClickListener(mItemClickListener);
    }

    private AdapterView.OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> av, View view, int position, long id) {
            GoodListView.this.onItemClick(view, id);
        }
    };

    protected void onItemClick(View view, long id) {
        int realPosition = (int) id;
        String data = ((GoodItemData) mGoodAdapter.getItem(realPosition)).mContent;
        try {
            Intent intent = new Intent();
            intent.setClassName(SnmpApplication.getInstance().getPackageName(), GoodDetailActivity.class.getName());

            Bundle bundle = new Bundle();
            bundle.putString("title", ((GoodItemData) mGoodAdapter.getItem(realPosition)).mTitle);
            bundle.putString("content", data);
            intent.putExtras(bundle);
            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);

            SnmpApplication.getInstance().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @ExportedProperty(category = "drawing")
    public boolean hasOverlappingRendering() {
        return false;
    }

    public class GoodListAdapter extends BaseAdapter {
        private ArrayList<GoodItemData> mDataList = new ArrayList<GoodItemData>();

        public GoodListAdapter(ArrayList<GoodItemData> appData) {
            mDataList = appData;
        }

        public void refresh(ArrayList<GoodItemData> data) {
            mDataList = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mDataList != null) {
                return mDataList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int index) {
            if (mDataList != null) {
                return mDataList.get(index);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int position, View covertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (covertView == null) {
                covertView = LayoutInflater.from(getContext()).inflate(R.layout.blog_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mIcon = (ImageView) covertView.findViewById(R.id.list_item_avatar);
                viewHolder.mContent = (TextView) covertView.findViewById(R.id.list_item_content);
                covertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) covertView.getTag();
            }

            String title = mDataList.get(position).mTitle;
            String content = mDataList.get(position).mContent;
            String itemTxt = title == null ? content : title;
            viewHolder.mContent.setText(itemTxt);
            return covertView;
        }

        public ArrayList<GoodItemData> getDataList() {
            return mDataList;
        }
    }

    private class ViewHolder {
        ImageView mIcon;
        TextView mContent;
        TextView mTimeStamp;
    }
}
