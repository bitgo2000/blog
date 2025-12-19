package com.snmp.blog;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.snmp.file.FileMainActivity;
import com.snmp.utils.SnmpApplication;
import com.snmp.utils.Utils;

public class SearchDialog {
    private static final String TAG = "SearchDialog";
    private static String mLastKeyworkd = "";
    private static String mSearchResult = "";
    private static ArrayList<BlogTweetItemData> mTweetDataList = new ArrayList<BlogTweetItemData>();

    public static void showSearchDialog(final Activity activity, ArrayList<BlogTweetItemData> tweetDataList) {
        mTweetDataList = tweetDataList;
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("please input keyword");
        final EditText editText = new EditText(activity);
        editText.setText(mLastKeyworkd);
        builder.setView(editText);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String result = editText.getText().toString();
                if ("1983".equals(result)) {
                    ((BlogListActivity)activity).mEncrypt = false;
                    return;
                }
                if ("2000".equals(result)) {
                    Intent intent = new Intent();
                    intent.setClassName(SnmpApplication.getInstance().getPackageName(),
                            FileMainActivity.class.getName());
                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    SnmpApplication.getInstance().startActivity(intent);
                    return;
                }
                if (!TextUtils.isEmpty(result)) {
                    mSearchResult = "";
                    mLastKeyworkd = result;
                    search(activity, result);
                } else {
                    Utils.toast("null input");
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final Dialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
            }
        });

        dialog.show();
    }

    public static void showSearchResultDialog(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("search result list");
        final TextView result = new TextView(activity);
        result.setText(mSearchResult);
        //result.setTextColor(Color.WHITE);
        builder.setView(result);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final Dialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
            }
        });

        dialog.show();
    }

    public static void search(final Activity activity, String keyword) {
        for (int i = 0; i < mTweetDataList.size(); i++) {
            if (mTweetDataList.get(i).mContent.contains(keyword)) {
                mSearchResult += mTweetDataList.get(i).mContent + "\n";
            }
        }

        showSearchResultDialog(activity);
    }

}
