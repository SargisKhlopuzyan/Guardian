package com.sargis.kh.guardian.services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.sargis.kh.guardian.utils.Constants;

public class JobSchedulerUtil {

    // schedule the start of the service every 10 - 30 seconds
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, DataJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(Constants.JobScheduler.JOB_SCHEDULER_ID, serviceComponent);
        builder.setMinimumLatency(/*30 * */60 * 1000); // wait at least
        builder.setOverrideDeadline(/*31 * */ 2 +  60 * 1000); // maximum delay
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

}