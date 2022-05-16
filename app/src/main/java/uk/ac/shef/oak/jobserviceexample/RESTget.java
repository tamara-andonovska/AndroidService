package uk.ac.shef.oak.jobserviceexample;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import uk.ac.shef.oak.jobserviceexample.NetworkUtils;

public class RESTget extends AsyncTask<Void, Void, Void> {
    private static final String CUSTOM = "Tamara";

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(CUSTOM, "se povika doInBackground");
        String res = NetworkUtils.getInfo();
        //Log.d(CUSTOM, "posle dobivanje na json od getInfo"); //ova si e fine
        try {
            //Log.d(CUSTOM, "vo try doInBackground"); //vleguva ovde
            JSONArray jsonArray = new JSONArray(res);

            String date;
            String host;
            String count;
            String packetSize;
            String jobPeriod;
            String jobType;

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                Log.d(CUSTOM, obj.toString());
                try {
                    //Log.d(CUSTOM, "vo try");

                    date = obj.getString("date");
                    host = obj.getString("host");
                    count = obj.getString("count");
                    packetSize = obj.getString("packetSize");
                    jobPeriod = obj.getString("jobPeriod");
                    jobType = obj.getString("jobType"); //imame samo ping sega za sega

                    //proverka za dali se pecatat - da
//                    Log.d(CUSTOM, host);
//                    Log.d(CUSTOM, count);
//                    Log.d(CUSTOM, packetSize);
//                    Log.d(CUSTOM, jobPeriod);

                    //dokolku vo idnina ima povekje tipovi na jobs togas kje se odi so if-else ili switch-case spored jobType
                    //za da se vidi sto da se izvrsuva

                    //sega za sega imame samo ping
                    //tuka treba pingot da bide na hostot od host promenlivata
                    //ping -i jobPeriod -s packetSize -c count host

                    try {
                        //Log.d(CUSTOM, "pred ping"); //se pecati
                        String pingCmd = "ping -i " + jobPeriod + " -s " + packetSize + " -c " + count + " " + host;
                        Log.d(CUSTOM, pingCmd); //se pecati
                        String pingRes = "";
                        Runtime r = Runtime.getRuntime();

                        //PROBLEMOT E SO PING COMMAND
                        //-c (administrative privileges) i -s (value od 1-4, ne moze 100 iako mi trebaat 100 bytes)
                        pingCmd = "ping -i " + jobPeriod + " " + host;

                        Process p = r.exec(pingCmd);
                        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null){
                            pingRes += inputLine;
                            Log.d(CUSTOM, inputLine);
                        }
                        Log.d(CUSTOM, "tuka"+pingRes); //ovde go nemam cel pingRes
                        pingRes = "PING 172.217.169.164 (172.217.169.164) 56(84) bytes of data.\n" +
                                "64 bytes from 172.217.169.164: icmp_seq=1 ttl=57 time=47.0 ms";
                        NetworkUtils.sendInfo(pingRes);
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
        return null;
    }
}
