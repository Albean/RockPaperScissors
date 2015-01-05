package com.sevenhogs.rockpaperscissors;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    ImageView picture;        //reference to dice picture
    Random rng = new Random();    //generate random numbers
    SoundPool dice_sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    int sound_id;        //Used to control sound stream return by SoundPool
    Handler handler;    //Post message to start roll
    Timer timer = new Timer();    //Used to implement feedback to user
    boolean rolling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picture = (ImageView) findViewById(R.id.imageView1);
        handler = new Handler(callback);
    }

    //User pressed dice, lets start
    public void HandleClick(View arg0) {
        if (!rolling) {
            rolling = true;
            //Pause to allow image to update
            timer.schedule(new Roll(), 400);
            //Intent intent = new Intent(this, Cube.class);
        }
    }

    //When pause completed message sent to callback
    class Roll extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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







    //Receives message from timer to start dice roll
    Handler.Callback callback = new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            //Get roll result
            //Remember nextInt returns 0 to 5 for argument of 6
            //hence + 1
            switch (rng.nextInt(3) + 1) {
                case 1:
                    picture.setImageResource(R.drawable.ic_hand);
                    break;
                case 2:
                    picture.setImageResource(R.drawable.ic_rock);
                    break;
                case 3:
                    picture.setImageResource(R.drawable.ic_scissors);
                    break;

                default:
            }
            rolling = false;    //user can press again
            return true;
        }
    };

    //Clean up
    @Override
    protected void onPause() {
        super.onPause();
        dice_sound.pause(sound_id);
    }

    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}