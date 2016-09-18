package com.example.juan.surfeetac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juan.surfeetac.SpotXmlParser.Spot;

public class Spots extends Activity {

    private TextView textView;

    EditText spotname;
    String[] datos;

    //private static final String URL = "http://10.0.2.2:9000";


    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spots);
        textView = (TextView) findViewById(R.id.TextView01);

        spotname = (EditText) findViewById(R.id.txtSpot);
        datos = new String [1];

        LoadSpots task = new LoadSpots();
        task.execute(new String[]{"http://192.168.1.136:9008/surfeetac/gotospotsandroid"});
    }


    private class LoadSpots extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {

                //HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(urls[0]);

                // Execute HTTP Post Request
                HttpResponse serverresponse = Login.httpclient.execute(httpget);

                //readStream(serverresponse.getEntity().getContent());

                SpotXmlParser SpotsXmlParser = new SpotXmlParser();
                List<Spot> spots = SpotsXmlParser.parse(serverresponse.getEntity().getContent());

                for(Spot spot: spots)
                {
                    String s = spot.name;
                    String x = spot.description;
                    response += s +"\n"+ x +"\n";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
    }

    private class AddSpotTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String str = "";
            try {


                //httpclient = Login.httpclient;
                HttpPost httppost = new HttpPost(urls[0]);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("spotname", urls[1]));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                System.out.println(nameValuePairs);

                HttpResponse response = Login.httpclient.execute(httppost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                str = reader.readLine();

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(str);
            return str;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);

            if(result.equals("OK")){
                Toast.makeText(getApplicationContext(), "Spot added" ,
                        Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Invalid spot. Try again",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void AddSpot(View view) {
        AddSpotTask task = new AddSpotTask();
        datos[0] = spotname.getText().toString();
        task.execute(new String[] {"http://192.168.1.136:9008/surfeetac/anadirSpotAndroid", datos[0]});
    }

}
