package com.example.androidquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class game extends AppCompatActivity {

    private String category_name, category_id, total_questions, url;
    private int score;
    private String questions;
    private List<String> canswers = new ArrayList<>();
    private List<String> ianswers = new ArrayList<>();
    private String correct_answer;
    private JSONArray openTrivia;
    private TextView question_number, question, loggy;
    private RadioButton ans1, ans2, ans3, ans4;
    private Button next;
    private LinearLayout ansLinBot, ansLinTop;
    private int quno = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loggy = findViewById(R.id.loggy);
        question_number = (TextView) findViewById(R.id.question_number);
        question = (TextView) findViewById(R.id.question);
        next = (Button) findViewById(R.id.next);
        ansLinBot = (LinearLayout) findViewById(R.id.ansLinBot);
        ansLinTop = (LinearLayout) findViewById(R.id.ansLinTop);
        ans1 = (RadioButton) findViewById(R.id.ans1);
        ans2 = (RadioButton) findViewById(R.id.ans2);
        ans3 = (RadioButton) findViewById(R.id.ans3);
        ans4 = (RadioButton) findViewById(R.id.ans4);
        question_number.setText("Question  1 / 10");
        Intent intent = getIntent();

        next.setEnabled(false);
        category_name = intent.getStringExtra("category_name");
        category_id = intent.getStringExtra("category_id");
        total_questions = intent.getStringExtra("total_questions");
        setTitle(category_name);
        url = Integer.parseInt(category_id) != 0 ? "https://opentdb.com/api.php?amount="+total_questions+"&category="+category_id : "https://opentdb.com/api.php?amount="+total_questions ;
        ans1.setText("1");
        ans2.setText("2");
        ans3.setText("3");
        ans4.setText("4");
        question_number.setText("Question  "+(quno)+" / "+total_questions);
        new getRes().execute();

    }

    public void runner(View v){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quno < openTrivia.length()){

                    try{
                        question_number.setText("Question  "+(quno+1)+" / "+total_questions);
                        JSONObject explrObject = openTrivia.getJSONObject(quno);
                        question.setText(explrObject.getString("question"));
                        correct_answer = explrObject.getString("correct_answer");
                        JSONArray temp = explrObject.getJSONArray("incorrect_answers");
                        ianswers.clear();
                        for (int i = 0; i < temp.length(); i++){
                            ianswers.add((String) temp.get(i));
                        }

                        int le = ianswers.size()-1;
                        int ran = (int)(Math.random() * le);
                        ianswers.add(ran, correct_answer);

                        if (ianswers.size() == 4){
                            ans1.setText(ianswers.get(0));
                            ans2.setText(ianswers.get(1));
                            ans3.setText(ianswers.get(2));
                            ans4.setText(ianswers.get(3));
                            ans4.setVisibility(View.VISIBLE);
                            ans3.setVisibility(View.VISIBLE);
                        } else if (ianswers.size() == 3){
                            ans1.setText(ianswers.get(0));
                            ans2.setText(ianswers.get(1));
                            ans3.setText(ianswers.get(2));
                            ans4.setVisibility(View.INVISIBLE);

                        } else if (ianswers.size() == 2){
                            ans1.setText(ianswers.get(0));
                            ans2.setText(ianswers.get(1));
                            ans4.setVisibility(View.INVISIBLE);
                            ans3.setVisibility(View.INVISIBLE);
                        }
                        loggy.setText(correct_answer);

                    } catch (Exception e){

                    }
                    quno++;
                } else {
                    over();
                }

            }
        });
    }

    public void over(){
        final Intent game_over = new Intent(this, game_over.class);
        startActivity(game_over);
    }

    private class getRes extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String line, newjson = "";
                URL urls = new URL(url);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(urls.openStream(), StandardCharsets.UTF_8))) {
                    while ((line = reader.readLine()) != null) {
                        newjson += line;
                    }

                    String json = newjson;
                    JSONObject jObj = new JSONObject(json);
                    openTrivia = jObj.getJSONArray("results");

                    JSONObject explrObject = openTrivia.getJSONObject(0);

                    questions = explrObject.getString("question");

                    correct_answer = explrObject.getString("correct_answer");
                    JSONArray temp = explrObject.getJSONArray("incorrect_answers");
                    for (int i = 0; i < temp.length(); i++){
                        ianswers.add((String) temp.get(i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
//                loggy.setText(e.toString());

            }
            return null;
        }

        @Override
        public void onPostExecute(Void param) {
            question.setText(questions);
            int le = ianswers.size()-1;

            int ran = (int)(Math.random() * le);
            ianswers.add(ran, correct_answer);

            loggy.setText(correct_answer);

            if (ianswers.size() == 4){
                ans1.setText(ianswers.get(0));
                ans2.setText(ianswers.get(1));
                ans3.setText(ianswers.get(2));
                ans4.setText(ianswers.get(3));
            } else if (ianswers.size() == 3){
                ans1.setText(ianswers.get(0));
                ans2.setText(ianswers.get(1));
                ans3.setText(ianswers.get(2));
                ans4.setVisibility(View.INVISIBLE);
            } else if (ianswers.size() == 2){
                ans1.setText(ianswers.get(0));
                ans2.setText(ianswers.get(1));
                ViewGroup parent = (ViewGroup) ansLinBot.getParent();
                parent.removeView(ansLinBot);
            }
//            runner();
            next.setEnabled(true);

        }
    }

}
