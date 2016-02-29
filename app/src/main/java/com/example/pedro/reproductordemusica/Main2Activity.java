package com.example.pedro.reproductordemusica;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements ListView.OnItemClickListener, Button.OnClickListener{

    private ArrayList<String> mpList;
    private ArrayList<String> musics;
    private Intent path;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mpList = new ArrayList<>();

        Button bStop = (Button) findViewById(R.id.bStopMusic);
        bStop.setOnClickListener(this);
        try {
            ListView lv = (ListView) findViewById(R.id.listView);
            musics = getMusics();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, musics);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private ArrayList<String> getMusics() throws Exception{
        ArrayList<String> musicsList = new ArrayList<>();

        ContentResolver cr = getApplicationContext().getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cur = cr.query(uri, null, selection, null, sortOrder);

        if(cur != null) {
            if(cur.getCount() > 0) {
                while(cur.moveToNext()){
                    musicsList.add(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                    mpList.add(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA)));
                }
            }
        cur.close();
        }
        return  musicsList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        stopService(new Intent(this, SoundService.class));
        path = new Intent(this, SoundService.class);
        path.putExtra("music", mpList.get(position));
        path.putExtra("name", musics.get(position));
        startService(path);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bStopMusic:
                stopService(new Intent(this, SoundService.class));
                break;
        }
    }
}
