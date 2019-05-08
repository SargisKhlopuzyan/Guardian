package com.sargis.kh.guardian.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class DataJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("LOG_TAG", "onStartJob");
        JobSchedulerUtil.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("LOG_TAG", "onStopJob");
        return true;
    }

}
