package com.example.newsrecent.newsrecent;

import android.Manifest;
import android.content.Context;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Loader;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    NewsApIRequestURL1 = NewsApIRequestURL1 + "top-headlines";
    Location = getUserCountry(this);
    String loc = Location;
    NewsApIRequestURL1 = NewsApIRequestURL1 + "?country=" + Location +APIKEY;
    NewsApIRequestURL = NewsApIRequestURL1;
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

        if(networkInfo !=null && networkInfo.isConnected()){
            android.app.LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID,null, this);
        }
        else{
            String Response = PreferenceManager.getDefaultSharedPreferences(this).getString("JsonResponse", "0");
            // Update empty state with no connection error message
            if(Response == "0") {

                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
                mEmptyStateTextView.setText("No Internet Connection");
            }
            else{

                FileInputStream fis = null;
                ObjectInputStream ois = null;
                List<Newsinfo> newsinfoList = null;

                try {
                    // reading binary data
                    fis = new FileInputStream("SaveArrayList.ser");
                    // converting binary-data to java-object
                    ois = new ObjectInputStream(fis);
                    newsinfoList = (ArrayList<Newsinfo>) ois.readObject();

                }
                catch (FileNotFoundException fnf) {
                    fnf.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException cnf) {
                    cnf.printStackTrace();
                }
                mAdapter.clear();
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
                if(newsinfoList != null && !newsinfoList.isEmpty()){
                    mAdapter.addAll(newsinfoList);
                }
                mEmptyStateTextView.setText("NO NEWS FOUND");

            }
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
}
