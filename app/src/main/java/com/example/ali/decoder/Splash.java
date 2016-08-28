package com.example.ali.decoder;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.content.Intent;

/**
 * Created by Ali on 7/22/2015.
 */
public class Splash extends Activity {
    MediaPlayer mp;
    @Override
    protected void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.splash);
        mp = MediaPlayer.create(this,R.raw.load);

        final ImageView pic = (ImageView) findViewById(R.id.splashImage);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);

        pic.startAnimation(an);



        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mp.start();

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                mp.stop();
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}