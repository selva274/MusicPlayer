package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
//Widgets
    Button for_button,back_button,pause_button,play_button;

    TextView title,time_left,time_right;
    SeekBar seekBar;
//mediaplayer
    MediaPlayer mediaPlayer;

    //handler
    Handler handler=new Handler();
    //variable
    double starttime=0;
    double endtime=0;
    int forwardtime=1000;
    int backwardtime=1000;
    static int oneTimeOnly=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_button=findViewById(R.id.play_button);
        pause_button=findViewById(R.id.pause_button);
        for_button=findViewById(R.id.forward_button);
        back_button=findViewById(R.id.backward_button);

        time_left=findViewById(R.id.time_left_text);
        time_right=findViewById(R.id.time_right_text);
        title=findViewById((R.id.title));

        seekBar=findViewById(R.id.seekBar);
        mediaPlayer= MediaPlayer.create(this,
               R.raw.darshana);
        title.setText(getResources().getIdentifier(
                "darshana",
                "raw",
                getPackageName()
        ));
        seekBar.setClickable(false);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });
        pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });
        for_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp=(int) starttime;
                if((temp+forwardtime)<=endtime){
                    starttime=starttime+forwardtime;
                    mediaPlayer.seekTo((int) starttime);
                }else{
                    Toast.makeText(MainActivity.this,"Can't jump",Toast.LENGTH_SHORT);
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp=(int) starttime;
                if((temp-backwardtime)>0){
                    starttime=starttime-backwardtime;
                    mediaPlayer.seekTo((int) starttime);
                }else{
                    Toast.makeText(MainActivity.this,"Can't go back",Toast.LENGTH_SHORT);
                }
            }
        });



    }

    private void playMusic() {
        mediaPlayer.start();
        endtime=mediaPlayer.getDuration();
        starttime=mediaPlayer.getCurrentPosition();
        if(oneTimeOnly==0){
            seekBar.setMax((int) endtime);
            oneTimeOnly=1;
        }
        time_right.setText(String.format("%d min : %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) endtime),
                TimeUnit.MILLISECONDS.toSeconds((long) endtime)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes
                                ((long) endtime))
                ));
        seekBar.setProgress((int) starttime);
        seekBar.postDelayed(UpdatesongTime,100);
        //creating runnable

    }
    private Runnable UpdatesongTime=new Runnable() {
        @Override
        public void run() {
            starttime=mediaPlayer.getCurrentPosition();
            time_left.setText(String.format("%d min : %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) starttime),
                    TimeUnit.MILLISECONDS.toSeconds((long) starttime)-
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes
                                    ((long) starttime))
            ));
            seekBar.setProgress((int) starttime);
            seekBar.postDelayed(this,100);
        }
    };




}