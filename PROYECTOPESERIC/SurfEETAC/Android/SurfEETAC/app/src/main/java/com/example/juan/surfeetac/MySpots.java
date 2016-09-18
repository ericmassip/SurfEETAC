package com.example.juan.surfeetac;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juan.surfeetac.SpotXmlParser.Spot;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MySpots extends Activity {

    private TextView textView;
    //private static final String URL = "http://10.0.2.2:9000";
    public final static String EXTRA_MESSAGE = "";
    EditText spotname;
    String[] datos;


    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_spots);
        textView = (TextView) findViewById(R.id.TextView01);

        spotname = (EditText) findViewById(R.id.txtSpot);

        datos = new String [1];

        LoadMySpots task = new LoadMySpots();
        task.execute(new String[]{"http://192.168.1.136:9000/surfeetac/gotomyspotsandroid"});

    }


    private class LoadMySpots extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {

                //HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(urls[0]);

                // Just in case you have to add parameters to http request

                //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                //nameValuePairs.add(new BasicNameValuePair("password", "secret"));
                //nameValuePairs.add(new BasicNameValuePair("fullname", "Bob"));
                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse serverresponse = Login.httpclient.execute(httpget);

                //readStream(serverresponse.getEntity().getContent());

                SpotXmlParser MySpotsXmlParser = new SpotXmlParser();
                List<Spot> spots = MySpotsXmlParser.parse(serverresponse.getEntity().getContent());

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

    private class DeleteSpotTask extends AsyncTask<String, Void, String> {
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
                Toast.makeText(getApplicationContext(), "Spot deleted" ,
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),Main.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Invalid spot. Try again",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),Main.class);
                startActivity(intent);
            }
        }
    }

    /*public void GoToSpots(View view) {
        LoadSpots task = new LoadSpots();
        task.execute(new String[]{"http://192.168.1.39:9000/surfeetac/gotospotsandroid"});

    }*/

    public void DeleteSpot(View view) {
        DeleteSpotTask task = new DeleteSpotTask();
        datos[0] = spotname.getText().toString();
        task.execute(new String[] {"http://192.168.1.136:9000/surfeetac/borrarSpotAndroid", datos[0]});
    }

}

