package uk.ac.shef.oak.jobserviceexample;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {

    private static final String CUSTOM = "Tamara";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(CUSTOM, "se povika onStartJob");
        new RESTget().execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
