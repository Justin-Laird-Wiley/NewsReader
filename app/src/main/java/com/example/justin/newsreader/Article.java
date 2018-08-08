package com.example.justin.newsreader;

/**
 *   This class creates Article objects which hold instances of title, date, and url
 */
public class Article {

    //  Local variables that hold instances of title, date, and url for each article
    private String mTitle;
    private String mDate;
    private String mSection;
    private String mUrl;

    //  Constructor
    public Article(String title, String date, String section, String url) {

        mTitle = title;
        mDate = date;
        mSection = section;
        mUrl = url;
    }

    /**
     *  This method returns the title of the article
     *  @return mTitle
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     *  This method returns the date of the article
     *  @return mDate
     */
    public String getDate() {
        return mDate;
    }

    /**
     *   This method returns the newspaper section the story came from; ex. Sports, Environment
     * @return
     */
    public String getSection() {
        return mSection;
    }

    /**
     *  This method returns the URL of the article
     *  @return
     */
    public String getUrl() {
        return mUrl;
    }
}
