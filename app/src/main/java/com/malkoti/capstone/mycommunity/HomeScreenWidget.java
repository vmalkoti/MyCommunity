package com.malkoti.capstone.mycommunity;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.malkoti.capstone.mycommunity.utils.FirebaseAuthUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Implementation of App Widget functionality.
 */

// http://www.vogella.com/tutorials/AndroidWidgets/article.html
// https://developer.android.com/guide/topics/appwidgets/
// https://github.com/firebase/firebase-jobdispatcher-android#user-content-firebase-jobdispatcher-

// AlarmManager
//    https://www.sitepoint.com/alarmmanager-and-sleepy-android-apps/


public class HomeScreenWidget extends AppWidgetProvider {
    private static final String LOG_TAG = "DEBUG_" + HomeScreenWidget.class.getSimpleName();
    public static final String HOMESCREEN_WIDGET_UPDATE= "com.malkoti.capstone.mycommunity.HOMESCREEN_WIDGET_UPDATE";

    private static final int UPDATE_FREQ_MILLIS = 6000;


    /**
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.d(LOG_TAG, "updateAppWidget: Updating app widget static method");

        // Construct the RemoteViews object - move this in service
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_home_screen);

        if(FirebaseAuthUtil.isUserSignedIn()) {
            widgetText = "User signed in.";
        } else {
            widgetText = "Not signed in.";
        }

        views.setTextViewText(R.id.appwidget_status_tv, widgetText);
        views.setTextViewText(R.id.appwidget_datetime_tv, getCurrentTimeStamp());


        // Create an Intent to launch MainActivity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_status_tv, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(LOG_TAG, "onUpdate: Updating app widget callback method");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            //updateAppWidget(context, appWidgetManager, appWidgetId);
            startUpdateService(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled: Setting alarm");
        setWidgetUpdateAlarm(context, UPDATE_FREQ_MILLIS);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled: Clearing alarm");
        clearWidgetUpdateAlarm(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.d(LOG_TAG, "onReceive: Broadcast received. " + intent.getAction());

        if(HOMESCREEN_WIDGET_UPDATE.equals(intent.getAction())) {
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(),
                    HomeScreenWidget.this.getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] widgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int id : widgetIds) {
                //updateAppWidget(context, appWidgetManager, id);
                startUpdateService(context, id);
            }
        }
    }

    /**
     *
     * @param context
     * @param repeatInMillis
     */
    private void setWidgetUpdateAlarm(Context context, int repeatInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MILLISECOND, repeatInMillis);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC,
                calendar.getTimeInMillis(), repeatInMillis, getWidgetUpdateIntent(context));


        Log.d(LOG_TAG, "setWidgetUpdateAlarm: Alarm set");
    }

    /**
     *
     * @param context
     */
    private void clearWidgetUpdateAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getWidgetUpdateIntent(context));

        Log.d(LOG_TAG, "setWidgetUpdateAlarm: Alarm cleared");
    }

    /**
     *
     * @param context
     * @return
     */
    private PendingIntent getWidgetUpdateIntent(Context context) {
        Intent intent = new Intent(context, HomeScreenWidget.class);
        intent.setAction(HOMESCREEN_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /**
     *
     * @return
     */
    public static String getCurrentTimeStamp() {
        return SimpleDateFormat.getDateTimeInstance().format(new Date());
    }

    private void startUpdateService(Context context, int appWidgetId) {
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setAction(UpdateWidgetJobService.INTENT_ACTION);
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
        */
        UpdateWidgetJobService.enqueueWork(context, intent);
    }

    /**
     * IntentService to update widget
     */
    public static class UpdateWidgetService extends IntentService {

        public UpdateWidgetService() {
            super(UpdateWidgetService.class.getSimpleName());
            //NotificationCompat.Builder builder = NotificationCompat.Builder(this, "");
            startForeground(1, new Notification());
        }


        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            Log.d(LOG_TAG, "onHandleIntent: update widget via service");

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int incomingAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            if(incomingAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                HomeScreenWidget.updateAppWidget(this, appWidgetManager, incomingAppWidgetId);
            }
        }
    }

    /**
     * JobIntentService implementation for Android O compatibility
     */
    public static class UpdateWidgetJobService extends JobIntentService {
        private static final int JOB_ID = 1001;
        public static final String INTENT_ACTION = "UPDATE_WIDGET_JOB";


        public static void enqueueWork(Context context, Intent work) {
            enqueueWork(context, UpdateWidgetJobService.class, JOB_ID, work);
        }

        @Override
        protected void onHandleWork(@NonNull Intent intent) {
            Log.d(LOG_TAG, "onHandleWork: update widget with JobIntentService");

            if(intent.getAction().equals(INTENT_ACTION)) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int incomingAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);

                if (incomingAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                    HomeScreenWidget.updateAppWidget(this, appWidgetManager, incomingAppWidgetId);
                }
            }
        }
    }
}

