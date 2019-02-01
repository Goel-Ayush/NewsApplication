package com.example.newsrecent.newsrecent;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class NewsLoader extends AsyncTaskLoader {
   public String murl;
    public NewsLoader(Context context,String url){
        super(context);
        murl = url;
    }
    @Override
    protected void onStartLoading() {
       forceLoad();
    }

    @Override
    public List<Newsinfo> loadInBackground() {

        if (murl == null) {
            return null;
        }

        List<Newsinfo> newsinfolist = QueryUtils.fetchNewsData(murl);
        return newsinfolist;
    }
}
