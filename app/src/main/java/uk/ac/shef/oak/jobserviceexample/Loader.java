package uk.ac.shef.oak.jobserviceexample;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class Loader extends AsyncTaskLoader<String>{

    private static final String CUSTOM = "Tamara";

    Loader(Context context){
        super(context);
    }

    @Nullable
    @Override
    public String loadInBackground() {
        Log.d(CUSTOM, "sleep from loadInBackground in Loader");
        try {
            Thread.sleep(36000); //treba da se budi na sekoi 10min = 10*60*60 = 36000ms
            Log.d("conn", "thread in loader sleeps");
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return NetworkUtils.getInfo();
    }
}
