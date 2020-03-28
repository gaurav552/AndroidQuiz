package com.example.androidquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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
        LinearLayout summary = findViewById(R.id.summary);
        ArrayList<String> AllQ = intent.getStringArrayListExtra("Questions");
        ArrayList<String>  AllA = intent.getStringArrayListExtra("Answers");
        ArrayList<String>  AllUA = intent.getStringArrayListExtra("User");
        for (int i = 0; i<AllQ.size();i++){
            TextView que = new TextView(this);
            que.setText(AllQ.get(i));
            que.setLayoutParams(new LinearLayout.LayoutParams(900, ViewGroup.LayoutParams.MATCH_PARENT));
            summary.addView(que);
            TextView ans = new TextView(this);
            ans.setText("Correct: "+AllA.get(i));
            ans.setLayoutParams(new LinearLayout.LayoutParams(900, ViewGroup.LayoutParams.MATCH_PARENT));
            summary.addView(ans);
            TextView ansu = new TextView(this);
            ansu.setText("Your: "+AllUA.get(i));
            ansu.setLayoutParams(new LinearLayout.LayoutParams(900, ViewGroup.LayoutParams.MATCH_PARENT));
            summary.addView(ansu);
        }

    }

    public void home(){
        final Intent hom = new Intent(this, MainActivity.class);
        startActivity(hom);
    }
}
