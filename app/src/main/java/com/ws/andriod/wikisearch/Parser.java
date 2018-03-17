package com.ws.andriod.wikisearch;

import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Rapidd08 on 16-03-2018.
 */

public class Parser {
    public static String MAIN_URL = "https://en.wikipedia.org//w/api.php?action=query&format=json&prop=" +
            "pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50" +
            "&pilimit=10&wbptterms=description&gpssearch=";//+Key.SearchTerm+"&gpslimit=10";

    public static final String TAG = "TAG";
    private static Response response;

    public static JSONObject getDataFromWeb() {
        try {
            OkHttpClient client = new OkHttpClient();
            client.setProtocols(Arrays.asList(Protocol.HTTP_1_1));
            Request request = new Request.Builder()
                    .url(MAIN_URL+Key.SearchTerm+"&gpslimit=20")
                    .build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }
}
