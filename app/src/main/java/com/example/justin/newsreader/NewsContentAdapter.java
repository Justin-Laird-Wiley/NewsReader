package com.example.justin.newsreader;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

/**
 *  Custom Adapter to take Article objects and construct View's for ListView
 */
public class NewsContentAdapter extends ArrayAdapter<Article> {

    private static final String LOG_TAG = NewsContentAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context          The current context. Used to inflate the layout file.
     * @param articleArrayList A List of Earthquake objects to display in a list
     */
    public NewsContentAdapter(Activity context, ArrayList<Article> articleArrayList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // The second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, articleArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //  Set local listItemView to passed in View convertView
        View listItemView = convertView;

        //  If the View is empty, inflate a new View
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        //  Set currentArticle to Article from List<> at position
        Article currentArticle = getItem(position);

        //  Attach title to TextView in listItemView
        TextView title = (TextView) listItemView.findViewById(R.id.title);
        //  Set TextView to title from currentArticle
        title.setText(currentArticle.getTitle());

        //  Attach articleDate to TextView in listItemView
        TextView articleDate = listItemView.findViewById(R.id.date);
        //  Call formatDate to put date from currentArticle into correct format
        String reformattedDate = formatDate(currentArticle.getDate());
        //  Set reformatted date as date in TextView
        articleDate.setText(reformattedDate);

        //  Attach articleSection to TextView in listItemView
        TextView articleSection = listItemView.findViewById(R.id.section);
        //  Set TextView to section from currentArticle
        articleSection.setText(currentArticle.getSection());

        //  Attach the LinearLayout of the list so background color can be set
        View linearLayout = listItemView.findViewById(R.id.parent_linear_layout);

        //  Use alternating colors for background of each list item
        if (position%2 == 0) {
            linearLayout.setBackgroundColor(listItemView.getResources().getColor(R.color.category_bird_spots));
        } else {
            linearLayout.setBackgroundColor(listItemView.getResources().getColor(R.color.category_trader_joes));
        }

        //  Return the constructed View
        return listItemView;
    }

    /**
     *  Method to reformat the date pulled for currentArticle; date from JSON object is in format yyyy-dd-MMThh:mm:ssZ;
     *  returns date as dd MMM yyyy
     *  @param inputString - String to be reformatted
     *  @return result - String of reformatted date
     */
    private String formatDate(String inputString) {

        //  Initialize result to null
        String result = null;

        //  Create input and output date templates
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

        try {
            //  Cut the timestamp off the end of the string, and then reformat the date to dd MMM yyyy
            java.util.Date date = inputFormat.parse(inputString.substring(0, 10));
            result = outputFormat.format(date);
        } catch (ParseException e) {
            //  What happened?  The date couldn't be formatted
            Log.e(LOG_TAG, "Failed to format date");
        }

        //  Return the reformatted date
        return result;
    }
}
