package com.example.newsrecent.newsrecent;

import android.Manifest;
import android.content.Context;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Loader;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class Main2Activity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<Newsinfo>> {

    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private static final int NEWS_LOADER_ID = 1;
    private int count = 0;
    private static String APIKEY = "1409bbb2b3844910bdd62571fc21c5f0";
    private static final String NewsApIRequestURL="https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=1409bbb2b3844910bdd62571fc21c5f0";
    private static final String NewsApIRequestURL1="https://newsapi.org/v2/";
    private String Location;
    private String category;
    private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {






            if(ActivityCompat.checkSelfPermission(Main2Activity.this,
                    ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Taking Default : INDIA ",Toast.LENGTH_LONG).show();
            }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain);
        ListView NewsView = (ListView) findViewById(R.id.list);
        mAdapter = new NewsAdapter(this,new ArrayList<Newsinfo>());

        NewsView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView)findViewById(R.id.empty_view);
        NewsView.setEmptyView(mEmptyStateTextView);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo !=null &&networkInfo.isConnected()){
            android.app.LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID,null, this);
        }
        else{
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText("no_internet_connection");
            //EmptyTextView should be initialised Showing no Connection or Error in connection

        }

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
        mEmptyStateTextView.setText("NO NEWS FOUND");
    }

    public void onLoaderReset(Loader<List<Newsinfo>> newsLoader){

        mAdapter.clear();

    }

}
