package com.sargis.kh.guardian.services;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.sargis.kh.guardian.GuardianApplication;
import com.sargis.kh.guardian.HomePageActivity;
import com.sargis.kh.guardian.models.DataResponse;
import com.sargis.kh.guardian.network.calls.Data;
import com.sargis.kh.guardian.network.calls.GetDataCallback;
import com.sargis.kh.guardian.utils.Constants;

import java.util.List;

import okhttp3.ResponseBody;

public class DataJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {

        SharedPreferences pref = GuardianApplication.getContext().getSharedPreferences(Constants.SharedPreferences.NAME,0); // 0 - for private mode
        String lastWebPublicationDate = pref.getString(Constants.SharedPreferences.LAST_WEB_PUBLICATION_DATE, null);

        if (lastWebPublicationDate == null) {
            JobSchedulerUtil.scheduleJob(getApplicationContext()); // reschedule the job
            return false;
        }

        Data.getDataSearchedByFromDate(new GetDataCallback<DataResponse>() {
            @Override
            public void onSuccess(DataResponse dataResponse) {
                if (dataResponse != null
                        &&  dataResponse.getResponse() != null
                        && dataResponse.getResponse().results != null
                        && dataResponse.getResponse().results.size() > 0) {

                    if (dataResponse.getResponse().results.size() > 0) {
                        Log.e("LOG_TAG", "webPublicationDate: " + dataResponse.getResponse().results.get(0).webPublicationDate);

                        String lastWebPublicationDate = dataResponse.getResponse().results.get(0).webPublicationDate;
                        if (isAppOnForeground(GuardianApplication.getContext())) {
                            //TODO
                            Log.e("LOG_TAG", "*** 1");
                        } else {
                            Log.e("LOG_TAG", "*** 2");
                            showNotification(GuardianApplication.getContext(), "New Data Is Available: " + lastWebPublicationDate);
                        }
                    }
                }
            }

            @Override
            public void onError(int errorCode, ResponseBody errorResponse) {

            }

            @Override
            public void onFailure(Throwable failure) {

            }
        }, lastWebPublicationDate);


        JobSchedulerUtil.scheduleJob(getApplicationContext()); // reschedule the job
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("LOG_TAG", "onStopJob");
        return false;
    }

    private void showNotification(Context context, String message){

        Intent intent = new Intent(context, HomePageActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"default")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Guardian")
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("com.myApp");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    Constants.Notification.NOTIFICATION_CHANNEL_ID,
                    Constants.Notification.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        notificationManager.notify(Constants.Notification.NOTIFICATION_ID, builder.build());
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

}
