package uk.ac.shef.oak.jobserviceexample;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    //primanje na job-ovite vo JSON format
    private static final String CUSTOM = "Tamara";

    private static final String BASE_URL = "http://localhost:5000/getjobs";

    static String getInfo(){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String responseJSONString = null;

        try {
            Uri builtURI = Uri.parse(BASE_URL).buildUpon().build();
            URL requestUrl = new URL(builtURI.toString());

            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.d("conn", "connection made");

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
                Log.d("conn", line);
            }

            if (builder.length() == 0){
                return null;
            }

            responseJSONString = builder.toString();

        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
                Log.d("conn", "disconnected");
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        Log.d(CUSTOM,responseJSONString);
        return responseJSONString;
    }

}
