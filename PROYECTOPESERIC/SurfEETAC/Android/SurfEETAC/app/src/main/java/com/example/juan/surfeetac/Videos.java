package com.example.juan.surfeetac;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Videos extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_videos, menu);
        return true;
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

    public void GoWatchVideo1(View v) {
        String link = "https://www.youtube.com/watch?v=7-xPB3miZDo";
        Intent intent = null;
        intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }

    public void GoWatchVideo2(View v) {
        String link = "https://www.youtube.com/watch?v=LN0iM_6GUQo";
        Intent intent = null;
        intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }

    public void GoWatchVideo3(View v) {
        String link = "https://www.youtube.com/watch?v=MDXwRjaGFSo";
        Intent intent = null;
        intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }

    public void GoWatchVideo4(View v) {
        String link = "https://www.youtube.com/watch?v=wFaR0vOxIK8";
        Intent intent = null;
        intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }

    public void GoWatchVideo5(View v) {
        String link = "http://www.redbull.com/es/es/surfing/stories/1331705063171/mason-ho-video-made-in-hawaii-capitulo-1";
        Intent intent = null;
        intent = new Intent(intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
}
