/*
    HomeWork 07
    MainActivity.java
    Yateen Kedare | Rajdeep Rao
 */
package com.example.rajdeeprao.hw_07;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static com.example.rajdeeprao.hw_07.R.id.date;

public class MainActivity extends AppCompatActivity implements GetData.IData, PodcastAdapter.IData, PodcastGridAdapter.IData1{
    static ArrayList<PodcastItem> podcastItemsList;
    static ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mAdapterGrid;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManagerGrid;
    public MediaPlayer mediaPlayer;
    ImageButton PlayPauseButton;
    SeekBar seekbar;
    LinearLayout playerControlsLayout;
    int flag,playbackPosition ;
    Timer mTimer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        PlayPauseButton = (ImageButton) findViewById(R.id.imageButton2);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
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
        playerControlsLayout = (LinearLayout) findViewById(R.id.seekLinearLayout);

        PlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                    playbackPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                    PlayPauseButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                    mTimer.cancel();

                }
                else{
                    mediaPlayer.seekTo(playbackPosition);
                    mediaPlayer.start();
                    mTimer = new Timer();
                        mTimer.scheduleAtFixedRate(new TimerTask() {

                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        if(mediaPlayer != null) {
                                            seekbar.setProgress(mediaPlayer.getCurrentPosition());
                                            Log.d("SEEKBAR", "RUNNNN");
                                        }
                                    }
                                });
                            };
                        }, 1000,1000);


                    PlayPauseButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                }
            }
        });



        podcastItemsList=new ArrayList<>();
        new GetData(MainActivity.this).execute("https://www.npr.org/rss/podcast.php?id=510298");
        progressDialog= new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading Episodes");
        progressDialog.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PlayPauseButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
        switch (item.getItemId()) {

            case R.id.menu_action_change:

                if(flag==0) {
                    displayGridItems();
                    playerControlsLayout.setVisibility(View.INVISIBLE);
                    mTimer.cancel();
                    killMediaPlayer();
                }
                else
                    displayPodcastItems();
                return true;
            case R.id.menu_action_settings:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void displayPodcastItems() {
        flag=0;
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PodcastAdapter(MainActivity.this, R.layout.list_view, podcastItemsList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void displayGridItems(){
        flag=1;
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerGrid = new GridLayoutManager(MainActivity.this,2);
        mRecyclerView.setLayoutManager(mLayoutManagerGrid);

        // specify an adapter (see also next example)
        mAdapterGrid = new PodcastGridAdapter(MainActivity.this, R.layout.grid_view, podcastItemsList,this);
        mRecyclerView.setAdapter(mAdapterGrid);

    }

    static ArrayList<PodcastItem> parsePodcastItems(InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser= XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(in, "UTF-8");
        PodcastItem podcast= null;

        int event = parser.getEventType();
        int flag = 0;
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_TAG:

                    if (parser.getName().equals("item")) {
                        podcast = new PodcastItem();
                        flag = 1;
                    } else if (parser.getName().equals("title")) {
                        if (flag == 1)
                            podcast.setTitle(parser.nextText().trim());
                    } else if (parser.getName().equals("description")) {
                        if (flag == 1)
                            podcast.setDescription((parser.nextText().trim()));
                    } else if (parser.getName().equals("pubDate")) {
                        if (flag == 1)
                            podcast.setPublicationDate((parser.nextText().trim().substring(0, 16)));
                    } else if (parser.getName().equals("itunes:image")) {
                        if (flag == 1)
                            podcast.setImageURL(parser.getAttributeValue(null, "href"));
                    } else if (parser.getName().equals("itunes:duration")) {
                        if (flag == 1)
                            podcast.setDuration((parser.nextText().trim()));
                    } else if (parser.getName().equals("enclosure")) {
                        if (flag == 1)
                            podcast.setMP3URL(parser.getAttributeValue(null, "url"));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("item")) {
                        podcastItemsList.add(podcast);
                        Log.d("pcast", podcast.getTitle());
                        podcast= null;
                        flag = 0;
                    }
                    break;
                default:
                    break;
            }
            event = parser.next();
        }

        return podcastItemsList;
    }


    @Override
    public void returnedValue(ArrayList<PodcastItem> list) {
        podcastItemsList=list;
        Collections.sort(podcastItemsList, new Comparator<PodcastItem>() {
            @Override
            public int compare(PodcastItem o1, PodcastItem o2) {
                SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy");
                try {
                    Date date1=formatter.parse(o1.getPublicationDate());
                    Date date2=formatter.parse(o2.getPublicationDate());

                    if(date1.after(date2)){
                        return -1;
                    }else if(date2.after(date1)){
                        return 1;
                    }
                    else return 0;


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        displayPodcastItems();
    }

    @Override
    public void returnedValues(String URL) throws IOException {
       try {
            playerControlsLayout.setVisibility(View.VISIBLE);
            killMediaPlayer();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(URL);
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekbar.setMax(mediaPlayer.getDuration());

           mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
               @Override
               public void onCompletion(MediaPlayer mp) {
                   killMediaPlayer();
                   playerControlsLayout.setVisibility(View.INVISIBLE);
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

                               if(mediaPlayer != null) {
                                   seekbar.setProgress(mediaPlayer.getCurrentPosition());
                                   Log.d("SEEKBAR", "RUNNNN");
                               }
                           }
                       });
                   };
               }, 1000,1000);



       }catch (Exception e) {
           e.printStackTrace();
           playerControlsLayout.setVisibility(View.INVISIBLE);
       }
    }

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
                mTimer.cancel();

            }
            catch(Exception e) {
                e.printStackTrace();
            }
            playerControlsLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void returnedValuesGrid(String URL) throws IOException {
        playerControlsLayout.bringToFront();
        playerControlsLayout.setVisibility(View.VISIBLE);
        returnedValues(URL);
    }
}
