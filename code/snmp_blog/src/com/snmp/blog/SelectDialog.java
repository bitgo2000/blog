package com.snmp.blog;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import com.snmp.utils.LogUtils;
import com.snmp.utils.PreferenceManager;
import com.snmp.utils.SnmpDialog;

public class SelectDialog {
    private static final String TAG = SelectDialog.class.getSimpleName();

    public static void showDialog(final Activity activity, final String data) {
        SnmpDialog dialog = new SnmpDialog(activity);
        dialog.setTitle("dialog");
        dialog.setMessage(data);
        PreferenceManager.putString("blog_list", data);

        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PreferenceManager.putString("blog_list", data);
                LogUtils.d(BlogListActivity.TAG, "bbbbbbbb1 " + data);
                if (!TextUtils.isEmpty(data)) {
                    ClipboardManager service = activity.getSystemService(ClipboardManager.class);
                    String label = "nostr";
                    service.setPrimaryClip(ClipData.newPlainText(label, data));
                }
            }
        });
        dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PreferenceManager.putString("blog_list", "aaa");
            }
        });
        dialog.show();
        Intent intent = new Intent("com.snmp.blogwidget.WidgetTimeService");
        intent.setPackage(activity.getPackageName());
        activity.startService(intent);
    }
    
    public static String getSelectPref() {
        String pref = PreferenceManager.getString("blog_list", "");
        return pref;
    }

}
