package com.example.guidofabbrini.easyracelaptimerui;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    // A dialog showing a progress indicator and an optional text message or view

    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://pastebin.com/raw/puYnpK96";
    //http://192.168.42.1/api/v1/monitor

    ArrayList<HashMap<String, String>> dataStream;
    /**FAMILY TREE of the HashMap implementation
     Map Interface is an object that maps keys to values
     public abstract class AbstractMap --> implements Map < K , V >
     public class HashMap extends AbstractMap<K, V> implements Map<K, V>
     This implementation provides all of the optional map operations*/


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataStream =new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetData().execute();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://pastebin.com/raw/puYnpK96"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            // DOVE STAMPA QUESTO?

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                        /* It's a public class which extends directly Object, it returns a set of
                         modifiable name/value mappings*/

                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("data");

                    // looping through All data
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject d = data.getJSONObject(i);
                        String position = d.getString("position");
                        JSONObject pilot = d.getJSONObject("pilot");
                        String name = pilot.getString("name");
                        String quad = pilot.getString("quad");
                        String team = pilot.getString("team");
                        String lapcount = d.getString("lap_count");
                        String avg_lap_time = d.getString("avg_lap_time");
                        Float avg_lap_time_sec = (Float.parseFloat(avg_lap_time)/1000);
                        String avg_lap_totext = Float.toString(avg_lap_time_sec);

                        // tmp hash map for single contact
                        HashMap<String, String> positions = new HashMap<>();

                        // adding each child node to HashMap key => value
                        positions.put("position", position);
                        positions.put("name", name);
                        positions.put("lapcount", lapcount);
                        positions.put("avg_lap_totext", avg_lap_totext);

                        // adding contact to contact list
                        dataStream.add(positions);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Make sure you connected your device to EasyRaceLapTimer WIFI!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //       Updating parsed JSON data into ListView

            //Extended Adapter that is the bridge between a ListView and the data that backs the list,
            //the ListView can display any data provided that it is wrapped in a ListAdapter
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, dataStream,
                    R.layout.list_item, new String[]{"position", "name", "lapcount","avg_lap_totext"}, new int[]{R.id.position,
                    R.id.name, R.id.lapcount,R.id.avg_lap_totext});
            lv.setAdapter(adapter);
        }

    }
}


