package com.example.newsrecent.newsrecent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<Newsinfo> {

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if(listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.news_adapter, parent, false);
        }
        Newsinfo currentNews = (Newsinfo) getItem(position);

        TextView forTitle = (TextView)listitemView.findViewById(R.id.newsTitle);
        forTitle.setText(currentNews.nTitle);

        TextView forAName = (TextView)listitemView.findViewById(R.id.authorName);
        forAName.setText(currentNews.nAuthorName);

        TextView forSNAme = (TextView)listitemView.findViewById(R.id.sourceName);
        forSNAme.setText((CharSequence) currentNews.nName);

        TextView forDescription = (TextView)listitemView.findViewById(R.id.description);
        forDescription.setText(currentNews.nDescription);

        return listitemView;

    }

    NewsAdapter(Context context, ArrayList<Newsinfo> newsinfoArrayList){
        super(context,0,newsinfoArrayList);
    }


}
