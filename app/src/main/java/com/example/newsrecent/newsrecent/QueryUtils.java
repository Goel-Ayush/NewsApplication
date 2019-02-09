package com.example.newsrecent.newsrecent;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static String LOG_TAG = QueryUtils.class.getSimpleName();
    public static Context context;
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
    public QueryUtils(Context context){
        this.context = context;
    }
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
            int abc = urlConnection.getResponseCode();
            Log.i("abcdef1","responsecode: " + abc);
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.i("abcdef", "response code: " + urlConnection.getResponseCode());
            } else {
                Log.i(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }


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

    public static List<Newsinfo> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
//            offlineIt(jsonResponse,"JsonResponse");

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Newsinfo> newsinfoList = extractFeatureFromJson(jsonResponse);
        // Return the list of {@link Earthquake}s
        return newsinfoList;
    }

    private static List<Newsinfo> extractFeatureFromJson(String NewsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(NewsJSON)) {
            return null;
        }

        List<Newsinfo> newsinfoList = new ArrayList<>();

        try{

            JSONObject root = new JSONObject(NewsJSON);

            JSONArray  NewsArray = root.getJSONArray("articles");

            for(int i =0; i<NewsArray.length();i++) {

                JSONObject currentNews1 = NewsArray.getJSONObject(i);
                JSONObject currentNews = currentNews1.getJSONObject("source");
                String nNAme = currentNews.getString("name").trim();
                String aName = currentNews1.getString("author");
                String nTitle = currentNews1.getString("title");
                String nDescription = currentNews1.getString("description").trim();
                String uri = currentNews1.getString("url");
                if (aName == "null") {

                    aName = "";

                }

                if (nDescription == "null" || nDescription == "") {
                    nDescription = "**Text not found Will be Updated Soon**";
                }

                newsinfoList.add(new Newsinfo(nNAme, aName, nTitle, nDescription,uri));
            }

        }
        catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        finally {

            return newsinfoList;
        }
    }



}



