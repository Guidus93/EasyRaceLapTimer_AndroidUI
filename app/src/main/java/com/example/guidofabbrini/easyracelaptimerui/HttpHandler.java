package com.example.guidofabbrini.easyracelaptimerui;

/*
 * Created by guidofabbrini on 20/01/17.
 */

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

class HttpHandler {



    private static final String TAG = HttpHandler.class.getSimpleName();

    HttpHandler() {  //CONTOLLARNE IL FUNZIONAMENTO DEL PROGR SENZA QUESTA
    }

    String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            // Trying to establish an URL connection with the GET method
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // Reading the response from the URL
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in); // To String conversion
        } catch (MalformedURLException e) {
            // Either no legal protocol could be found in a specification string or the string could not be parsed
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            // Thrown to indicate that there is an error in the underlying protocol, such as a TCP error
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            // This class is the general class of exceptions produced by failed or interrupted I/O operations
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            //Checked exceptions need to be declared in a method or constructor's throws clause if they can be thrown
            // by the execution of the method or constructor and propagate outside the method or constructor boundary
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        //The principal operations on a StringBuilder are the append and insert methods,
        //the append method always adds these characters at the end of the builder

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}