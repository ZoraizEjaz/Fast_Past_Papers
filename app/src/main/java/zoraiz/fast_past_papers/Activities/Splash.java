package zoraiz.fast_past_papers.Activities;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import zoraiz.fast_past_papers.R;


public class Splash extends ActionBarActivity {

    ImageView imageView;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView=(ImageView)findViewById(R.id.bottom);

        animation= AnimationUtils.loadAnimation(Splash.this, R.anim.move);
        animation.setFillAfter(true);
        animation.setInterpolator(new BounceInterpolator());
        imageView.startAnimation(animation);


        final int welcomeScreenDisplay = 7000;
        /** create a thread to show splash up to splash time */
        Thread welcomeThread = new Thread() {

            int wait = 0;

            @Override
            public void run() {
                try {
                    super.run();
                    /**
                     * use while to get the splash time. Use sleep() to increase
                     * the wait variable for every 100L.
                     */
                    while (wait < welcomeScreenDisplay) {
                        sleep(50);
                        wait += 100;
                    }
                } catch (Exception e) {
                    System.out.println("EXc=" + e);
                } finally {
                    /**
                     * Called after splash times up. Do some action after splash
                     * times up. Here we moved to another main activity class
                     */
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                }
            }
        };
        welcomeThread.start();

    }

}
