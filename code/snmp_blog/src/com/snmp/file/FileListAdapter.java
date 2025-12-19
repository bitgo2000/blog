package com.snmp.file;

import java.io.File;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.snmp.blog.R;
import com.snmp.utils.SnmpApplication;

public class FileListAdapter extends BaseAdapter {
    private List<File> mFiles;

    public FileListAdapter(List<File> newFiles) {
        mFiles = newFiles;
    }

    public void refresh(List<File> newFiles) {
        mFiles = newFiles;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mFiles != null) {
            return mFiles.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int index) {
        if (mFiles != null) {
            return mFiles.get(index);
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
            covertView = LayoutInflater.from(SnmpApplication.getInstance()).inflate(R.layout.blog_list_item, parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.mIcon = (ImageView) covertView.findViewById(R.id.list_item_avatar);
            viewHolder.mContent = (TextView) covertView.findViewById(R.id.list_item_content);
            covertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) covertView.getTag();
        }

        File file = mFiles.get(position);
        String title = file.getName();
        viewHolder.mContent.setText(title);
        return covertView;
    }

    public static class ViewHolder {
        ImageView mIcon;
        TextView mContent;
        TextView mTimeStamp;
    }
}
