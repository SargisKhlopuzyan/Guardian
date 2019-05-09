package com.sargis.kh.guardian.services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.sargis.kh.guardian.utils.Constants;

public class JobSchedulerUtil {

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, DataJobService.class);

        JobInfo.Builder builder = new JobInfo.Builder(Constants.JobScheduler.JOB_SCHEDULER_ID, serviceComponent);
        builder.setMinimumLatency(30 * 60 * 100); // wait at least
        builder.setOverrideDeadline(30 * 60 * 100 + 1); // maximum delay
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        JobInfo jobInfo = builder.build();

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(jobInfo);
    }

}
