package com.snmp.file;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.snmp.blog.R;
import com.snmp.utils.MarkDownView;

public class FileDetailActivity extends Activity {
    private MarkDownView mMarkDownView;
    private String mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.file_detail);
        initView();
    }

    private void initView() {
        String name = getIntent().getStringExtra("title");
        mContent = getIntent().getStringExtra("content");

        mMarkDownView = (MarkDownView) findViewById(R.id.markdown_detail_txt);
        TextView titleView = (TextView) findViewById(R.id.detail_title).findViewById(R.id.detail_title_txt);
        titleView.setText(name.replace(".txt", ""));
        findViewById(R.id.detail_title_back).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.detail_title_txt).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.detail_title_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        mMarkDownView.loadMDFile(mContent);
    }

    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.removeExtra(android.content.Intent.EXTRA_TEXT);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mContent);
        startActivity(Intent.createChooser(sharingIntent, null));
    }

}
