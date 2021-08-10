package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {

    ArrayList<File> songList = new ArrayList<>();
    TextView textView;
    int position;
    String songName;
    MediaPlayer mediaPlayer;
    ImageView playButton;
    ImageView leftArrayButton, rightArrowButton;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        playButton = findViewById(R.id.playButton);
        leftArrayButton = findViewById(R.id.leftArrowButton);
        rightArrowButton = findViewById(R.id.rightArrowButton);
        textView = findViewById(R.id.songView);
        seekBar = findViewById(R.id.seekBar);

        getSupportActionBar().hide();

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        songList = (ArrayList) bundle.getParcelableArrayList("songs");
        position = i.getIntExtra("position",0);

        Uri uri = Uri.parse(songList.get(position).toString());
        mediaPlayer = MediaPlayer.create(MusicActivity.this, uri);
        seekBar.setMax(mediaPlayer.getDuration());

        playButton.setOnClickListener(v -> {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                playButton.setImageResource(R.drawable.ic_baseline_play_circle_24);
            }
            else{
                mediaPlayer.start();
                playButton.setImageResource(R.drawable.ic_baseline_pause_circle_24);
                updateSeekBar();
            }
        });

        leftArrayButton.setOnClickListener(v -> {
            position--;
            play();
            setText();
        });

        rightArrowButton.setOnClickListener(v -> {
            position++;
           play();
           setText();
        });

        play();
        setText();
        changeFromSeekBar();
    }

    public void setText(){
        songName = songList.get(position).getName();
        textView.setText(songName);
    }

    public void play(){
        mediaPlayer.release();
        Uri uri = Uri.parse(songList.get(position).toString());
        mediaPlayer = MediaPlayer.create(MusicActivity.this, uri);
        seekBar.setMax(mediaPlayer.getDuration());

        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.ic_baseline_play_circle_24);
        }
        else{
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.ic_baseline_pause_circle_24);
            updateSeekBar();
        }
    }

    public void changeFromSeekBar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void updateSeekBar(){
        if(mediaPlayer.getCurrentPosition() >= mediaPlayer.getDuration()){
            playButton.setImageResource(R.drawable.ic_baseline_play_circle_24);
            return;
        }

        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateSeekBar();
            }
        },1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mediaPlayer.release();
    }
}


