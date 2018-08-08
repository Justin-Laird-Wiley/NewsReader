package com.example.justin.newsreader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 *  Loader that extends AsyncTaskLoader; runs off the main thread to retrieve data from URL,
 *  and put it in a List<> object.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    //  String to store news story URL
    private String mUrl;

    //  String constant for name of Log entries
    private static final String LOG_TAG = ArticleLoader.class.getSimpleName();

    //  Constructor; uses super constructor with subclass Context; also stores URL of each story
    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    /**
     *  Overridden method that starts Loader
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     *  Overridden method that makes asynchronous call to load data from website
     *  @return result - List<> of Articles
     */
    @Override
    public List<Article> loadInBackground() {

        //  If there is URL, don't try to download data
        if (mUrl == null) {
            return null;
        }
        //  Call fetchEarthquakeData and retrieve data from URL; store in result
        List<Article> result = QueryUtils.fetchEarthquakeData(mUrl);

        //  Return List<> result
        return result;
    }
}
