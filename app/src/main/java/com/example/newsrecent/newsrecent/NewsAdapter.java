package com.example.newsrecent.newsrecent;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<Newsinfo> implements Filterable {


    private ArrayList<Newsinfo> filteredList;
    private FriendFilter friendFilter;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.news_adapter, parent, false);
        }
        Newsinfo currentNews = (Newsinfo) getItem(position);

        TextView forTitle = (TextView) listitemView.findViewById(R.id.newsTitle);
        forTitle.setText(currentNews.nTitle);

        TextView forAName = (TextView) listitemView.findViewById(R.id.authorName);
        forAName.setText(currentNews.nAuthorName);

        TextView forSNAme = (TextView) listitemView.findViewById(R.id.sourceName);
        forSNAme.setText((CharSequence) currentNews.nName);

        TextView forDescription = (TextView) listitemView.findViewById(R.id.description);
        forDescription.setText(currentNews.nDescription);

        return listitemView;

    }

    NewsAdapter(Context context, ArrayList<Newsinfo> NewsInfoList) {

        super(context, 0, NewsInfoList);

    }

    public Filter getFilter() {
        if (friendFilter == null) {
            friendFilter = new FriendFilter();
        }

        return friendFilter;
    }

    private class FriendFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(getContext());
            Gson gson = new Gson();
            String json = appSharedPrefs.getString("MyObject", "");
            Type type = new TypeToken<List<Newsinfo>>(){}.getType();
            List<Newsinfo> NewsInfoList = gson.fromJson(json, type);


            if (constraint != null && constraint.length() > 0) {
                ArrayList<Newsinfo> tempList = new ArrayList<>();


                // search content in friend list
                for (int i=0; i < NewsInfoList.size();i++) {
                    if (NewsInfoList.get(i).getnDescription().contains(constraint.toString().toLowerCase())) {
                        tempList.add(NewsInfoList.get(i));
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = NewsInfoList.size();
                filterResults.values = NewsInfoList;
            }

            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Newsinfo>) results.values;
            notifyDataSetChanged();
        }


    }
}
