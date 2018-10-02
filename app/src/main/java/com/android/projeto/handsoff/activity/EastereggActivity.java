package com.android.projeto.handsoff.activity;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.projeto.handsoff.R;

public class EastereggActivity extends AppCompatActivity {

    //Seguinte erro: Attempt to invoke virtual method 'android.content.res.Resources android.content.Context.getResources()' on a null object reference
    final MediaPlayer mp = MediaPlayer.create(EastereggActivity.this, R.raw.risada_easteregg);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easteregg);

        mp.start();
    }
}
