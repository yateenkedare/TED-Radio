/*
    HomeWork 07
    GetData.java
    Yateen Kedare | Rajdeep Rao
 */

package com.example.rajdeeprao.hw_07;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rajdeeprao on 3/10/17.
 */

public class GetData extends AsyncTask<String,Void,ArrayList<PodcastItem>> {
    IData activity;

    GetData(IData activity){this.activity=activity;}

    @Override
    protected ArrayList<PodcastItem> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
                try {
                    return MainActivity.parsePodcastItems(con.getInputStream());
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<PodcastItem> list) {
        super.onPostExecute(list);
        MainActivity.progressDialog.dismiss();
        activity.returnedValue(list);
    }

    interface IData{
        public void returnedValue(ArrayList<PodcastItem> list);
    }

}
