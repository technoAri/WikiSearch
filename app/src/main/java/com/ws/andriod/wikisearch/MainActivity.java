package com.ws.andriod.wikisearch;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DataModel> searchResults = new ArrayList<>();
    RecyclerAdapter adapter;
    String searchText;
    String wikiPageUrl;
    ImageView wikiImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {

        wikiImg = (ImageView) findViewById(R.id.wiki_logo);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        searchResults = new ArrayList<DataModel>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(getApplicationContext(), searchResults, recyclerView);
        recyclerView.setAdapter(adapter);

        if (recyclerView != null) {
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                    recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, final int position) {

                    Intent webViewIntent = new Intent(MainActivity.this, WebViewActivity.class);
                    webViewIntent.putExtra("wikiUrl", searchResults.get(position).getWikiPageUrl());
                    startActivity(webViewIntent);
                }
            }));
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void> {
        //ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
//            dialog = new ProgressDialog(MainActivity.this);
//            dialog.setTitle("Please Wait...");
//            dialog.setMessage("Loading results");
//            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            JSONObject jsonObject = Parser.getDataFromWeb();

            try {
                if (jsonObject != null) {
                    JSONObject wikiDatas = null;
                    try {
                        wikiDatas = jsonObject.getJSONObject("query");
                        JSONArray array = null;
                        try {
                            array = wikiDatas.getJSONArray("pages");
                            int lenArray = array.length();
                            if (lenArray > 0) {

                                for (int jIndex = 0; jIndex < lenArray; jIndex++) {

                                    DataModel model = new DataModel();
                                    JSONObject innerObject = array.getJSONObject(jIndex);
                                    String name = innerObject.getString("title");
                                    JSONObject terms = innerObject.getJSONObject("terms");
                                    String subTitle = terms.getString("description");

//                                  JSONObject img = innerObject.getJSONObject("thumbnail");
//                                  imageUrl = img.getString("source");

                                    String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/70/Example.png";
                                    String urlFormattedNamePath = name.replaceAll(" ", "_");
                                    wikiPageUrl = "https://en.wikipedia.org/wiki/" + urlFormattedNamePath;

                                    model.setTitle(name);
                                    model.setSubTitle(subTitle.substring(2, subTitle.length() - 2));
                                    model.setImaageURL(imageUrl);
                                    model.setWikiPageUrl(wikiPageUrl);
                                    searchResults.add(model);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //dialog.dismiss();
            if (searchResults.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "Data Not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        wikiImg.setVisibility(View.VISIBLE);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && searchResults.size() == 0)
                    wikiImg.setVisibility(View.VISIBLE);
                else if (hasFocus)
                    wikiImg.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                wikiImg.setVisibility(View.GONE);
                if (adapter != null)
                    searchText = query;
                recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                searchView.clearFocus();

                if (searchText.length() > 0 && searchResults.size() == 0)
                    wikiImg.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchResults.clear();
                wikiImg.setVisibility(View.GONE);
                if (adapter != null)
                    searchText = newText;
                recyclerView.setVisibility(View.VISIBLE);
                Key.SearchTerm = searchText;

                if (Key.SearchTerm.length() > 0) {
                    if (InternetConnection.checkConnection(getApplicationContext()))
                        new GetDataTask().execute();
                    else
                        Toast.makeText(getApplicationContext(), "Could not load! Check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                return true;
            }

        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}

