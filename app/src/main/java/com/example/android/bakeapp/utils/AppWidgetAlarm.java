package com.example.android.bakeapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.android.bakeapp.BakeAppWidget;

import java.util.Calendar;

/**
 * Created by bjoern on 06.07.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: I used the solution from:
 * https://stackoverflow.com/questions/5476867/updating-app-widget-using-alarmmanager/
 * for automaticly updating the widget.
 */

public class AppWidgetAlarm {

    private final int ALARM_ID = 0;

    private Context mContext;

    public AppWidgetAlarm(Context context) {
        mContext = context;
    }

    public void startAlarm() {
        final int INTERVAL_MILLIS = 60000;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, INTERVAL_MILLIS);

        Intent alarmIntent = new Intent(BakeAppWidget.ACTION_AUTO_UPDATE);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager =
                (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        // RTC does not wake the device up
        alarmManager
                .setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), INTERVAL_MILLIS, pendingIntent);
    }

    public void stopAlarm() {
        Intent alarmIntent = new Intent(BakeAppWidget.ACTION_AUTO_UPDATE);
        PendingIntent pendingIntent =
                PendingIntent
                        .getBroadcast(mContext, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (
                AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
