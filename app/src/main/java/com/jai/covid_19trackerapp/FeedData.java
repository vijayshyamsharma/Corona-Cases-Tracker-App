package com.jai.covid_19trackerapp;

public class FeedData {

    String feedtitle,feedimage,feedcontent,feedsource,feedtime,originallink;

    public FeedData(String feedtitle, String feedimage, String feedcontent, String feedsource, String feedtime, String originallink) {
        this.feedtitle = feedtitle;
        this.feedimage = feedimage;
        this.feedcontent = feedcontent;
        this.feedsource = feedsource;
        this.feedtime = feedtime;
        this.originallink = originallink;

    }

    public String getFeedtitle() {
        return feedtitle;
    }

    public String getFeedimage() {
        return feedimage;
    }

    public String getFeedcontent() {
        return feedcontent;
    }

    public String getFeedsource() {
        return feedsource;
    }

    public String getFeedtime() {
        return feedtime;
    }

    public String getOriginallink() {
        return originallink;
    }
}
