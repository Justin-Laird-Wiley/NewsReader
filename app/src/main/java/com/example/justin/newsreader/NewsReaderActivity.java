package com.example.justin.newsreader;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NewsReaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    //  String constant of search URL at the Guardian newspaper; search uses tag environment/birds, and gets first ten results
    public static final String GUARDIAN_SEARCH =
            "https://content.guardianapis.com/search?tag=environment/birds&api-key=2ed00ebb-1cac-401a-a0e4-cef0b29dbd4e";

    //  String constant to be used with Log entries
    private static final String LOG_TAG = NewsReaderActivity.class.getSimpleName();

    //  int constant used as the Loader ID
    private static final int LOADER_ID = 1;

    //  Declare an Adapter
    private NewsContentAdapter mAdapter;

    //  Declare an View for the progress bar
    private View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        //  Declare earthquakeListView and attach it to ListView list
        ListView articleListView = (ListView) findViewById(R.id.list);

        //  Create a new adapter that takes an empty list of earthquakes as an input
        mAdapter = new NewsContentAdapter(this, new ArrayList<Article>());

        //  Set mAdapter as the Adapter to populate ListView earthquakeListView
        articleListView.setAdapter(mAdapter);

        //  Create variable "progress" and attach it to xml View "progress"
        progress = findViewById(R.id.progress);

        //  Attach mEmptyView to TextView; set it as the empty view for articleListView
        TextView mEmptyView = (TextView) findViewById(R.id.empty);
        articleListView.setEmptyView(mEmptyView);

        //  Set up an onItemClickListener to open up the URL associated with each
        //  news item in displayed list
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //  Set currentArticle to Article object i being processed by Adapter mAdaper
                Article currentArticle = mAdapter.getItem(position);

                //  Get the URL from the current earthquake and convert to a URI
                Uri articleUri = Uri.parse(currentArticle.getUrl());

                //  Create an web Intent to open up URI earthquakeUri
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                //  Set the Intent running
                startActivity(websiteIntent);
            }
        });

        //  Set up connectivity manager to test for network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //  If the network is present and connected, then set the Loader running
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            //  Oops!  There's no internet; kill the progress bar, and display "No internet connection" text
            progress.setVisibility(View.GONE);
            mEmptyView.setText(R.string.no_internet);
        }
    }

    /**
     *  Method to initialize new Loader object
     *  @param id - Unique ID of Loader
     *  @param args - Bundle used to pass in any needed arguments
     *  @return - Returns Loader object
     */
    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(NewsReaderActivity.this, GUARDIAN_SEARCH);
    }

    /**
     *  Method run after Loader finishes extracting data and creating list of Articles
     *
     *  @param loader - Loader that has finished running
     *  @param data - List of Articles grabbed by the Loader
     */
    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {

        //  Since the asynchronous task is done, kill the progress bar
        progress.setVisibility(View.GONE);

        //  Clear the Adapter of stale data
        mAdapter.clear();

        //  If the data pointer is not null, and we have data, then pass the data to the Adapter
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    /**
     *  Method called reset the Adapter when the Loader is woken up
     * @param loader - Loader to be woken up
     */
    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }
}


