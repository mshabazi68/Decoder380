package com.example.ali.decoder;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Button dec = (Button) findViewById(R.id.dec_btn);
        Button enc = (Button) findViewById(R.id.enc_btn);
        Button ck = (Button) findViewById(R.id.createKey_btn);
        mp = MediaPlayer.create(this,R.raw.click);

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Intent decIntent = new Intent(MainActivity.this,Decrypt.class);
                startActivity(decIntent);
            }
        });
        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Intent encIntent = new Intent(MainActivity.this, Encrypt.class);
                startActivity(encIntent);
            }
        });

        ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Intent ckIntent = new Intent(MainActivity.this, CreateKey.class);
                startActivity(ckIntent);

            }
        });






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


}
