package com.example.gianlucanadirvillalba.englishwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity
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
}
