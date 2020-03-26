package com.example.androidquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

public class game extends AppCompatActivity {

    String  category_name, category_id, total_questions, url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();

        category_name = intent.getStringExtra("category_name");
        category_id = intent.getStringExtra("category_id");
        total_questions = intent.getStringExtra("total_questions");
        setTitle(category_name);
        url = Integer.parseInt(category_id) != 0 ? "https://opentdb.com/api.php?amount="+total_questions+"&category="+category_id : "https://opentdb.com/api.php?amount="+total_questions ;

    }
}
