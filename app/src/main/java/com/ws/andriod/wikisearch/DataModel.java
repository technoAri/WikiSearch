package com.ws.andriod.wikisearch;

/**
 * Created by Rapidd08 on 16-03-2018.
 */

public class DataModel {
    private String title;
    private String subTitle;
    private String imaageURL;
    private String wikiPageUrl;

    public String getWikiPageUrl() {
        return wikiPageUrl;
    }

    public void setWikiPageUrl(String wikiPageUrl) {
        this.wikiPageUrl = wikiPageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImaageURL() {
        return imaageURL;
    }

    public void setImaageURL(String imaageURL) {
        this.imaageURL = imaageURL;
    }
}
