package com.example.android.bakeapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.android.bakeapp.utils.AppWidgetAlarm;

/**
 * Created by bjoern on 10.05.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: Here we build the widget
 */
public class BakeAppWidget extends AppWidgetProvider {

    public static final String UPDATE_MEETING_ACTION = "com.example.android.bakeapp.action.intent";

    public static final String ACTION_AUTO_UPDATE = "AUTO_UPDATE";

    public int WidgetID;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; ++i) {
            RemoteViews remoteViews
                    = updateWidgetListView(context, appWidgetIds[i]);

            appWidgetManager.updateAppWidget(appWidgetIds[i],
                    remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context,
                                             int appWidgetId) {
        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.bake_app_widget);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        WidgetID = appWidgetId;
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(
                svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(R.id.listViewWidget, svcIntent);

        //Here we are starting the single step view
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        startActivityIntent.setAction(UPDATE_MEETING_ACTION);

        PendingIntent startActivityPendingIntent
                = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setPendingIntentTemplate(R.id.listViewWidget, startActivityPendingIntent);
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        return remoteViews;
    }

    @Override
    public void onEnabled(Context context) {
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.startAlarm();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        // stop alarm
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.stopAlarm();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_AUTO_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            if (appWidgetManager != null) {
                ComponentName name = new ComponentName(context, BakeAppWidget.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(name);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);
            }
        }
    }
}

