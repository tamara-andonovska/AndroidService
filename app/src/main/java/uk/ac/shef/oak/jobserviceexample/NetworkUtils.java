package uk.ac.shef.oak.jobserviceexample;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    //primanje na job-ovite vo JSON format
    private static final String CUSTOM = "Tamara";

    private static final String BASE_URL = "http://192.168.100.137:5000/getjobs/hardware";

    public static String getInfo(){
        Log.d(CUSTOM, "se povika getInfo");
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String responseJSONString = null;

        try {
            Uri builtURI = Uri.parse(BASE_URL).buildUpon().build();
            URL requestUrl = new URL(builtURI.toString());
            Log.d(CUSTOM, "Trying to make connection...");

            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.d(CUSTOM, "Connection made");

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
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
                Log.d(CUSTOM, "disconnected");
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        Log.d(CUSTOM,responseJSONString + "\nod getInfo");
        return responseJSONString;
    }

    public static void sendInfo(String jsonInputString) throws IOException{
        Log.d(CUSTOM, "in sendInfo"); //voopsto ne vleguva ovde??
        URL url = new URL ("http://192.168.100.137:5000/postresults");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        con.setDoOutput(true);

        Log.d(CUSTOM, "Connection for POST made"); //ne se pravi konekcija, ne stiga do ovde

        //JSON String need to be constructed for the specific resource.
        //We may construct complex JSON using any third-party JSON libraries such as jackson or org.json
        //String jsonInputString = "{\"name\": \"Upendra\", \"job\": \"Programmer\"}";

        try(OutputStream os = con.getOutputStream()){
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = con.getResponseCode();
        Log.d(CUSTOM, Integer.toString(code));

        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            Log.d(CUSTOM, response.toString());
        }
    }

}
