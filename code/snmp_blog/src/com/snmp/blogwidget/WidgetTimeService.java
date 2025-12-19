package com.snmp.blogwidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.snmp.blog.BlogListActivity;
import com.snmp.blog.R;
import com.snmp.blog.SelectDialog;
import com.snmp.utils.LogUtils;
import com.snmp.utils.PreferenceManager;

public class WidgetTimeService extends Service {
    private static String TAG = "WidgetTimeService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        LogUtils.i(TAG, "onCreate");
        startClockListener();
        updateAppBlogWidget33();
        updateAppBlogWidget32();
}

    @Override
    public void onDestroy() {
        unregisterReceiver(this.mTimeReceiver);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        updateAppBlogWidget33();
        updateAppBlogWidget32();
    }

    private BroadcastReceiver mTimeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            LogUtils.i(TAG, "onReceive intent.getAction()" + intent.getAction());
            if ("android.intent.action.TIMEZONE_CHANGED".equals(intent.getAction())
                    || "android.intent.action.TIME_TICK".equals(intent.getAction())
                    || "android.intent.action.TIME_CHANGED".equals(intent.getAction())
                    || "android.intent.action.TIME_SET".equals(intent.getAction())) {

                // PriceMgr.getInstance().postApi(WidgetTimeService.this);
                updateAppBlogWidget33();
                updateAppBlogWidget32();
            }
        }

    };

    public void startClockListener() {
        LogUtils.i(TAG, "startClockListener");
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.TIME_TICK");
        localIntentFilter.addAction("android.intent.action.TIME_CHANGED");
        localIntentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        localIntentFilter.addAction("android.intent.action.TIME_SET");
        localIntentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.mTimeReceiver, localIntentFilter);

    }

    public void updateAppBlogWidget33() {
        String data = SelectDialog.getSelectPref();
        LogUtils.i(TAG, "updateAppWatchWidget bbbbbb5 " + data);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.appwidget_provider_blog);

        remoteViews.setTextViewText(R.id.appwidget_text_blog, data);

        remoteViews.setOnClickPendingIntent(R.id.appwidget_blog_view,
                PendingIntent.getActivity(this, 0, new Intent(this, BlogListActivity.class), 201326592));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName componentName = new ComponentName(getApplicationContext(), BlogWidgetProvider33.class);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
    }

    public void updateAppBlogWidget32() {
        String data = SelectDialog.getSelectPref();
        LogUtils.i(TAG, "updateAppWatchWidget bbbbbb5 " + data);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.appwidget_provider_blog);

        remoteViews.setTextViewText(R.id.appwidget_text_blog, data);

        remoteViews.setOnClickPendingIntent(R.id.appwidget_blog_view,
                PendingIntent.getActivity(this, 0, new Intent(this, BlogListActivity.class), 201326592));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName componentName = new ComponentName(getApplicationContext(), BlogWidgetProvider32.class);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
    }
}