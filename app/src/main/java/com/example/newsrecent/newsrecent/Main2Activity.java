package com.example.newsrecent.newsrecent;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Loader;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class Main2Activity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<Newsinfo>> {

    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private static final int NEWS_LOADER_ID = 1;
    private int count = 0;
    private static String APIKEY = "&apiKey=1409bbb2b3844910bdd62571fc21c5f0";
    private static String NewsApIRequestURL="https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=1409bbb2b3844910bdd62571fc21c5f0";
    private static String NewsApIRequestURL1="https://newsapi.org/v2/";
    private String Location;
    private String category;
    private ListView NewsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

    NewsApIRequestURL1 = NewsApIRequestURL1 + "top-headlines";
    Location = getUserCountry(this);
    NewsApIRequestURL1 = NewsApIRequestURL1 + "?country=" + Location +APIKEY;
    NewsApIRequestURL = NewsApIRequestURL1;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain);
        NewsView = (ListView) findViewById(R.id.list);
        mAdapter = new NewsAdapter(this,new ArrayList<Newsinfo>());
        NewsView.setAdapter(mAdapter);
        mEmptyStateTextView = (TextView)findViewById(R.id.empty_view);
        NewsView.setEmptyView(mEmptyStateTextView);
        NewsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Newsinfo clickedNews = (Newsinfo) NewsView.getItemAtPosition(i);
                Intent intent = new Intent(Main2Activity.this,fullNews.class);
                intent.putExtra("CLickedNewsText",clickedNews.getUrltopage());
                startActivity(intent);
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo !=null && networkInfo.isConnected()){
            android.app.LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID,null, this);
        }
        else{


                SharedPreferences appSharedPrefs = PreferenceManager
                        .getDefaultSharedPreferences(this.getApplicationContext());
                Gson gson = new Gson();
                String json = appSharedPrefs.getString("MyObject", "");
                Type type = new TypeToken<List<Newsinfo>>(){}.getType();
                List<Newsinfo> NewsInfoList = gson.fromJson(json, type);
                if(NewsInfoList== null){
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                    //EmptyTextView should be initialised Showing no Connection or Error in connection
                    mEmptyStateTextView.setText("No Internet Connection & stored content");
                }
                else{
                    mAdapter.clear();
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                    if(NewsInfoList != null && !NewsInfoList.isEmpty()){
                        mAdapter.addAll(NewsInfoList);
                    }
                    mEmptyStateTextView.setText("NO NEWS FOUND");
                                    }

                }


            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);

                        if (TextUtils.isEmpty(s)) {
                            NewsView.clearTextFilter();
        }
        else {
            NewsView.setFilterText(s);
        }

                return true;
            }
        });

        return true;
    }


    @Override
    public void onBackPressed() {
        count++;
        Toast toast = new Toast(this);

        if(count==2)
            super.onBackPressed();
        else
            toast.makeText(this,"Press once more to Exit",Toast.LENGTH_SHORT).show();
    }

    public android.content.Loader onCreateLoader(int i, Bundle bundle){
        NewsLoader a = new NewsLoader(this,NewsApIRequestURL);
        return a;
    }



    public void onLoadFinished(Loader<List<Newsinfo>> newsInfoLoader, List<Newsinfo> newsinfoList){
            mAdapter.clear();
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            if(newsinfoList != null && !newsinfoList.isEmpty()){
                mAdapter.addAll(newsinfoList);
            }
            else
                mEmptyStateTextView.setText("NO NEWS FOUND");
            offlineIt(newsinfoList);
    }

    public void onLoaderReset(Loader<List<Newsinfo>> newsLoader){

        mAdapter.clear();

    }



    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }

    public void offlineIt(List<Newsinfo> newsinfoList){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(newsinfoList);
        prefsEditor.putString("MyObject", json);
        prefsEditor.commit();
    }


}
