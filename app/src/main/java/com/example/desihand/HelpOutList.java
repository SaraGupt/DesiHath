package com.example.desihand;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class HelpOutList extends AppCompatActivity {
    // Initialize variable
    RecyclerView recyclerView;
    ItemClickListener itemClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_out_list);


    }
}