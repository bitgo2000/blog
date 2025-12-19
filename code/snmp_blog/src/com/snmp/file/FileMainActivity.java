package com.snmp.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.snmp.blog.R;
import com.snmp.utils.SnmpApplication;
import com.snmp.utils.SnmpFile;

public class FileMainActivity extends Activity {
    private static final String FILE_PRE = "/data/user/0/com.snmp.blog/files/blog-main";
    public static final String TAG = FileMainActivity.class.getSimpleName();
    private FileListView mFileListView;
    private TextView mTitle;
    private File mCurrentFile;
    private FileListAdapter mFileAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.blog);
        initView();

    }

    public void initView() {
        findViewById(R.id.blog_title_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SearchDialog.showSearchDialog(GoodListActivity.this,
                // mGoodDataList);
                try {
                    Intent intent = new Intent();
                    intent.setClassName(SnmpApplication.getInstance().getPackageName(),
                            FileDetailActivity.class.getName());

                    Bundle bundle = new Bundle();
                    bundle.putString("title", "aaa");
                    bundle.putString("content", "aaa");
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    SnmpApplication.getInstance().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mTitle = (TextView) findViewById(R.id.blog_title_txt);
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mTitle.setText("bitlong");

        mFileListView = new FileListView(this);
        mFileListView.setDivider(null);
        mFileListView.init(this);
        refresh();
        ((LinearLayout) findViewById(R.id.root)).addView(mFileListView);
    }

    private void refresh() {
        mCurrentFile = new File(getFilesDir().getAbsolutePath() + "/" + SnmpFile.SNMP_BLOG_MAIN_DIR + "/");
        loadFile(mCurrentFile);
    }

    public void loadFile(File directory) {
        if (!directory.exists() || !directory.isDirectory()) {
            Toast.makeText(this, "file not exist", Toast.LENGTH_LONG).show();
            return;
        }
        mCurrentFile = directory;

        File[] files = directory.listFiles();
        List<File> fileList = new ArrayList<File>();
        if (files != null) {
            fileList.addAll(Arrays.asList(files));
        }

        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    return 1;
                } else {
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            }
        });

        if (mFileAdapter == null) {
            mFileAdapter = new FileListAdapter(fileList);
            mFileListView.setAdapter(mFileAdapter);
        } else {
            mFileAdapter.refresh(fileList);
        }

        mTitle.setText(getGithubTitle());
    }

    private String getGithubTitle() {
        String absolutePath = mCurrentFile.getAbsolutePath();
        return absolutePath.replace(FILE_PRE, "root");
    }

    @Override
    public void onBackPressed() {
        if (FILE_PRE.equals(mCurrentFile.getAbsolutePath())) {
            finish();
            return;
        }
        if (mCurrentFile != null && mCurrentFile.getParentFile() != null) {
            loadFile(mCurrentFile.getParentFile());
        } else {
            super.onBackPressed();
        }
    }
}
