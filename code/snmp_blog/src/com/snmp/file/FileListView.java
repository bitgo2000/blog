package com.snmp.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.widget.AdapterView;
import android.widget.ListView;

import com.snmp.utils.LogUtils;
import com.snmp.utils.SnmpApplication;

public class FileListView extends ListView {
    private static final String TAG = "FileListView";
    private FileMainActivity mActivity;

    public FileListView(Context context) {
        super(context);
    }

    public FileListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FileListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void init(FileMainActivity activity) {
        mActivity = activity;
        setOnItemClickListener(mItemClickListener);
    }

    private AdapterView.OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> av, View view, int position, long id) {
            FileListView.this.onItemClick(view, id);
        }
    };

    protected void onItemClick(View view, long id) {
        int realPosition = (int) id;
        File file = (File) getAdapter().getItem(realPosition);
        if (file.isDirectory()) {
            mActivity.loadFile(file);
        } else {
            openFile(file);
        }
        openFile(file);
    }

    private void openFile(File file) {
        FileInputStream fin = null;
        try {
            int length = (int) file.length();
            byte[] buff = new byte[length];
            fin = new FileInputStream(file);
            fin.read(buff);
            fin.close();
            String data = new String(buff, "utf8");
            if (file.getAbsolutePath().toString().contains("snmp2")) {
                data = new String(buff, "GBK");
            }

            Intent intent = new Intent();
            intent.setClassName(SnmpApplication.getInstance().getPackageName(), FileDetailActivity.class.getName());

            Bundle bundle = new Bundle();
            bundle.putString("title", file.getName());
            bundle.putString("content", data);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            SnmpApplication.getInstance().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "openFile " + e);
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e) {
            }
        }
    }

    @Override
    @ExportedProperty(category = "drawing")
    public boolean hasOverlappingRendering() {
        return false;
    }

}
