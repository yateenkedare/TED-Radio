/*
    HomeWork 07
    EpisodeActivity.java
    Yateen Kedare | Rajdeep Rao
 */

package com.example.rajdeeprao.hw_07;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class EpisodeActivity extends AppCompatActivity {

        MediaPlayer mediaPlayer;
    SeekBar seekbar;
    Timer mTimer;
        int playbackPosition=0,flag = 0;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_episode);

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);

            seekbar = (SeekBar) findViewById(R.id.episodeSeekbar);

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Log.d("SEEKBAR", String.valueOf(progress));
                    if(fromUser) {
                        mediaPlayer.seekTo(progress);
                        playbackPosition = progress;
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            ArrayList<String> objDetails=new ArrayList<String>();
            objDetails=getIntent().getStringArrayListExtra("PODCAST");
            String title=objDetails.get(0);
            String imgUrl=objDetails.get(1);
            String description=objDetails.get(2);
            String publishedDate=objDetails.get(3);
            String duration=objDetails.get(4);
            final String audio=objDetails.get(5);

            Log.d("EPISODE:",title);
            Log.d("EPISODE:",description);

            final ImageView imageView= (ImageView) findViewById(R.id.episodeImage);
            TextView titleView= (TextView) findViewById(R.id.episodeTitle);
            TextView descriptionView= (TextView) findViewById(R.id.description);
            TextView durationView= (TextView) findViewById(R.id.episodeDuration);
            TextView dateView= (TextView) findViewById(R.id.episodeDate);

            Picasso.with(EpisodeActivity.this)
                    .load(imgUrl)
                    .into(imageView);


            titleView.setText(title);
            descriptionView.setText("Description: "+description);
            dateView.setText("Published Date: "+publishedDate);
            durationView.setText("Duration: "+duration);

            killMediaPlayer();
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audio);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    killMediaPlayer();
                }
            });

            final ImageView media= (ImageView) findViewById(R.id.episodePlay);
            media.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(0==flag)
                    {
                        media.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                        flag++;
                        try {
                            killMediaPlayer();
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource(audio);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            seekbar.setMax(mediaPlayer.getDuration());
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    killMediaPlayer();
                                    mTimer.cancel();

                                }
                            });
                            mTimer = new Timer();
                            mTimer.scheduleAtFixedRate(new TimerTask() {

                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {

                                            if (mediaPlayer != null) {
                                                seekbar.setProgress(mediaPlayer.getCurrentPosition());
                                                Log.d("SEEKBAR1", "RUNNNN");
                                            }
                                        }
                                    });
                                }

                                ;
                            }, 1000, 1000);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    else if(mediaPlayer != null && mediaPlayer.isPlaying())
                    {
                        media.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                        playbackPosition = mediaPlayer.getCurrentPosition();
                        mediaPlayer.pause();
                        mTimer.cancel();
                    }
                    else{
                        mediaPlayer.seekTo(playbackPosition);
                        mediaPlayer.start();
                        try{
                            mTimer.cancel();
                        }catch (Exception e){}
                        mTimer = new Timer();
                        mTimer.scheduleAtFixedRate(new TimerTask() {

                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        if (mediaPlayer != null) {
                                            seekbar.setProgress(mediaPlayer.getCurrentPosition());
                                            Log.d("SEEKBAR2", "RUNNNN");
                                        }
                                    }
                                });
                            }

                            ;
                        }, 1000, 1000);
                        media.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                    }
                }
            });


        }

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
