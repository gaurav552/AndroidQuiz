package com.example.androidquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class game_over extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Button home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home();
            }
        });
        Intent intent = getIntent();
        String score = intent.getStringExtra("score");
        TextView point = findViewById(R.id.score);
        point.setText(score);
        TextView logg = findViewById(R.id.log);

    }

    public void home(){
        final Intent hom = new Intent(this, MainActivity.class);
        startActivity(hom);
    }
}
