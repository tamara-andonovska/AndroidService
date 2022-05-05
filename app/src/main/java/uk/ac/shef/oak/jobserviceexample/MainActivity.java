/*
 * Copyright (c) 2019. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package uk.ac.shef.oak.jobserviceexample;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import uk.ac.shef.oak.jobserviceexample.restarter.RestartServiceBroadcastReceiver;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String CUSTOM = "Tamara";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //finish(); //crashnuvase koga stavav finish() ovde

        if (getSupportLoaderManager().getLoader(0) != null){
            Log.d(CUSTOM, "initLoader from main");
            getSupportLoaderManager().initLoader(0, null, this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(getApplicationContext());
        }
        finish();
    }

    public void connect(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null){
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()){
            //tuka treba da e prakjanjeto
            Bundle queryBundle = new Bundle(); //ova
            queryBundle.putString("queryString", "prakjaj"); //nez dali mi treba se ova
            Log.i(CUSTOM,"restartLoader from Main");
            getSupportLoaderManager().restartLoader(0, queryBundle, this); //ovde mozda null mesto queryBundle ili uopste ne mi trba restart
        } else {
            Toast.makeText(this, "No network", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new Loader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            Log.d(CUSTOM, "onLoadFinished in Main");
            JSONObject jsonObject = new JSONObject(data);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            String date;
            String host;
            String count;
            String packetSize;
            String jobPeriod;
            String jobType;

            for (int i = 0; i < itemsArray.length(); i++){
                JSONObject obj = itemsArray.getJSONObject(i);
                try {
                    date = obj.getString("date");
                    host = obj.getString("host");
                    count = obj.getString("count");
                    packetSize = obj.getString("packetSize");
                    jobPeriod = obj.getString("jobPeriod");
                    jobType = obj.getString("jobType");
                    Log.d("json", obj.toString());


                    //dokolku vo idnina ima povekje tipovi na jobs togas kje se odi so if-else ili switch-case spored jobType
                    //za da se vidi sto da se izvrsuva

                    //sega za sega imame samo ping
                    //tuka treba pingot da bide na hostot od host promenlivata
                    //ping -i jobPeriod -s packetSize -c count host

                    //dopolnitelno treba da se vidi periodicno da se pravi
                    Runtime runtime = Runtime.getRuntime();
                    try {
                        String pingCmd = "ping -i " + jobPeriod + " -s " + packetSize + " -c " + count + " " + host;
                        //Process mip = runtime.exec(pingCmd); ???
                        Log.d("json", pingCmd);
                        String pingRes = "";
                        Runtime r = Runtime.getRuntime();
                        Process p = r.exec(pingCmd);
                        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null){
                            pingRes += inputLine;
                            Log.d(CUSTOM, inputLine);
                            Log.d(CUSTOM, pingRes);
                        }
                        in.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
                Log.d(CUSTOM, obj.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
