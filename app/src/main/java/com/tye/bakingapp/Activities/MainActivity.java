package com.tye.bakingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.tye.bakingapp.Adapters.MainRecipeListAdapter;
import com.tye.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainRecipeListAdapter.ListItemClickListener {

    //Views
    @BindView(R.id.rv_recipes) RecyclerView mRecyclerView;

    private MainRecipeListAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecipeAdapter = new MainRecipeListAdapter(this, this);

        mRecipeAdapter.setNumberOfItems(18);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecipeAdapter);
    }

    @Override
    public void onListItemClicked(int position) {

        //Start details activity

    }
}
