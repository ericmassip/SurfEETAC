package com.example.juan.surfeetac;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Login extends Activity {

    //EditText username, password;
    public final static String EXTRA_MESSAGE = "";
    EditText nombreuser, password;
    String[] datos;
    //public String usuario;

    public static HttpClient httpclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nombreuser = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        datos = new String [2];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    private class Loguearse extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String str = "";
            try {


                httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urls[0]);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", urls[1]));
                nameValuePairs.add(new BasicNameValuePair("password", urls[2]));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
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

            System.out.println(datos[0]);
            if(result.equals("OK")){
                Intent intent = new Intent(getApplicationContext(),Main.class);

               VAR.usuario = datos[0];
                System.out.print(EXTRA_MESSAGE);

                startActivity(intent);
            }

            if(result.equals("FAIL"))
            {
                Toast.makeText(getApplicationContext(), "Invalid user or password. Try again",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //METODO que genera la tarea del logueo, desde aqui se ejecuta el asynctask que se comentaba anteriormente
    public void Login(View view) {
        Loguearse task = new Loguearse();
        datos[0] = nombreuser.getText().toString();
        datos[1] = password.getText().toString();

        task.execute(new String[] {"http://10.0.2.2:9008/application/LoginAndroid", datos[0],datos[1]});
    }

    //cargar vista par el registro
    public void Register(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
