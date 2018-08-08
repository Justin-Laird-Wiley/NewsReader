package com.example.justin.newsreader;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *  Class of utility functions to make HTTP connection and extract JSON data from URL passed in
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    //
    public static List<Article> fetchEarthquakeData(String requestUrl) {

        //  Create URl object
        URL url = createUrl(requestUrl);

        //  Declare and initialize string to hold JSON string
        String jsonResponse = null;

        //  Make HTTP request to the URL, and return JSON string if successful
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        //  Extract earthquake data from JSON string and put it in list earthquakes
        List<Article> articles = extractJsonFeature(jsonResponse);
        Log.i(LOG_TAG, "fetchEarthquakeData");

        //  Return the list earthquakes
        return articles;
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing a JSON response.
     */

    private static List<Article> extractJsonFeature(String articleJSON) {

        //  If the input string is empty, skip extraction and return null
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Article> articles = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            //  Create a JSONObject out SAMPLE_JSON_RESPONSE
            JSONObject jsonRootObj = new JSONObject(articleJSON);

            //  Find JSON object "response" below top level; this is where the data we want is
            JSONObject jsonItemObj =  jsonRootObj.getJSONObject("response");

            //  Retrieve array "results"
            JSONArray articleArray = jsonItemObj.getJSONArray("results");

            //  Use for loop to pull data out each item in "results"
            for (int i = 0; i < articleArray.length(); i++) {

                // For each earthquake entry, work down to the JSON "properties" object
                JSONObject currentArticle = articleArray.getJSONObject(i);

                //  Retrieve title, date, section, and URL
                String thisTitle = currentArticle.getString("webTitle");
                String thisDate = currentArticle.getString("webPublicationDate");
                String thisSection = currentArticle.getString("sectionName");
                String thisUrl = currentArticle.getString("webUrl");

                //  Create a new Article object; pass in title, date, section, and url
                articles.add(new Article(thisTitle, thisDate, thisSection, thisUrl));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of earthquakes
        return articles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
