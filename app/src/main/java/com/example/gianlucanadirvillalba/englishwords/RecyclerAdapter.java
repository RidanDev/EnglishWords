package com.example.gianlucanadirvillalba.englishwords;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gianlucanadirvillalba on 11/11/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.WordsHolder>
{
    private LayoutInflater mLayoutInflater = LayoutInflater.from(MyApplication.getAppContext());
    private ArrayList<String> mData = new ArrayList<>();

    public ArrayList<String> getmData()
    {
        return mData;
    }

    public RecyclerAdapter(ArrayList<String> data)
    {
        mData = data;
    }

    @Override
    public WordsHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mLayoutInflater.inflate(R.layout.custom_word, parent, false);
        return new WordsHolder(view);
    }

    @Override
    public void onBindViewHolder(WordsHolder holder, int position)
    {
        String[] array = mData.get(position).split("-");
        holder.englishView.setText(array[0].substring(0, array[0].length() - 1));
        holder.italianView.setText(array[1].substring(1, array[1].length()));
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }


    public class WordsHolder extends RecyclerView.ViewHolder
    {
        private TextView englishView;
        private TextView italianView;

        public WordsHolder(View itemView)
        {
            super(itemView);
            englishView = (TextView) itemView.findViewById(R.id.english_word);
            italianView = (TextView) itemView.findViewById(R.id.italian_word);
        }
    }

    public void setFilter(ArrayList<String> newList)
    {
        mData = new ArrayList<>();
        mData.addAll(newList);
        notifyDataSetChanged();
    }
}
