package com.example.gianlucanadirvillalba.englishwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

//TODO aggiungere modifica delle parole nella lista

public class ListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
{
    private RecyclerView mRecyclerView;
    private ArrayList<String> mData;
    private RecyclerAdapter mAdapter;
    private FloatingActionButton mFab;
    private int mDbSize;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Intent intent = getIntent();
        mData = intent.getStringArrayListExtra("array");
        mDbSize = intent.getIntExtra("size", 0);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MyApplication.getAppContext(), NewWord.class);
                intent.putExtra("size", mDbSize);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        newText = newText.toLowerCase();
        ArrayList<String> newList = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++)
        {
            String name = mData.get(i).toLowerCase();
            if (name.contains(newText))
                newList.add(mData.get(i));
        }
        mAdapter.setFilter(newList);
        return true;
    }
}
