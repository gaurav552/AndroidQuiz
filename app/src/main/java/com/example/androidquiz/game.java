package com.example.androidquiz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class game extends AppCompatActivity {

    private String category_name, category_id, total_questions, url;
    private int score = 0;
    private List<String> ianswers = new ArrayList<>();
    private ArrayList<String> uans = new ArrayList<>();
    private String correct_answer;
    private JSONArray openTrivia;
    private TextView question_number, question;
    private RadioButton ans1, ans2, ans3, ans4;
    private Button next;
    private int quno = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        question_number = (TextView) findViewById(R.id.question_number);
        question = (TextView) findViewById(R.id.question);
        next = (Button) findViewById(R.id.next);
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
        String difficulty = intent.getStringExtra("difficulty").toLowerCase();
        if (difficulty == "Any Difficulty"){
            difficulty = "";
        }
        setTitle(category_name);
        url = Integer.parseInt(category_id) != 0 ? "https://opentdb.com/api.php?amount="+total_questions+"&category="+category_id+"&difficulty="+difficulty : "https://opentdb.com/api.php?amount="+total_questions+"&difficulty="+difficulty ;
        ans2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        ans4.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        new getRes().execute();

    }

    public void UAnswer(View v){
        if (v.getId() == ans1.getId()){
            ans1.setChecked(true);
            ans2.setChecked(false);
            ans3.setChecked(false);
            ans4.setChecked(false);
        } else if (v.getId() == ans2.getId()){
            ans2.setChecked(true);
            ans1.setChecked(false);
            ans3.setChecked(false);
            ans4.setChecked(false);
        } else if (v.getId() == ans3.getId()){
            ans3.setChecked(true);
            ans2.setChecked(false);
            ans1.setChecked(false);
            ans4.setChecked(false);
        } else if (v.getId() == ans4.getId()){
            ans4.setChecked(true);
            ans2.setChecked(false);
            ans3.setChecked(false);
            ans1.setChecked(false);
        }
    }

    public void runner(View v) throws JSONException {
        if (ans1.isChecked() || ans2.isChecked() || ans3.isChecked() || ans4.isChecked()){
            if (quno < openTrivia.length()){
                scoreKeeper();
                textChanger();
            } else {
                scoreKeeper();
                over();
            }
        }
    }
    public void scoreKeeper(){
        if (ans1.isChecked()){
            if (ans1.getText() == correct_answer){
                score = score + 10;
            }
            uans.add(ans1.getText().toString());
        } else if (ans2.isChecked()){
            if (ans2.getText() == correct_answer){
                score = score + 10;
            }
            uans.add(ans2.getText().toString());
        } else if (ans3.isChecked()){
            if (ans3.getText() == correct_answer){
                score = score + 10;
            }
            uans.add(ans3.getText().toString());
        } else if (ans4.isChecked()){
            if (ans4.getText() == correct_answer){
                score = score + 10;
            }
            uans.add(ans4.getText().toString());
        }
    }

    public void over() throws JSONException {
        final Intent game_over = new Intent(this, game_over.class);
        game_over.putExtra("score", Integer.toString(score));
        ArrayList<String> Questions = new ArrayList<>();
        ArrayList<String> Answers = new ArrayList<>();
        for (int i = 0; i < openTrivia.length(); i++){
            JSONObject explrObject = openTrivia.getJSONObject(i);
            Questions.add(explrObject.getString("question"));
            Answers.add(explrObject.getString("correct_answer"));
        }
        game_over.putStringArrayListExtra("Questions", Questions);
        game_over.putStringArrayListExtra("Answers", Answers);
        game_over.putStringArrayListExtra("User", uans);

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

                }
            } catch (Exception e) {
                e.printStackTrace();
//                loggy.setText(e.toString());
            }
            return null;
        }

        @Override
        public void onPostExecute(Void param) {
            textChanger();
            next.setEnabled(true);
        }
    }

    public void textChanger(){
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
                ans1.setText(ianswers.get(0).toString());
                ans2.setText(ianswers.get(1).toString());
                ans3.setText(ianswers.get(2).toString());
                ans4.setText(ianswers.get(3).toString());
                ans4.setVisibility(View.VISIBLE);
                ans3.setVisibility(View.VISIBLE);

            } else if (ianswers.size() == 3){
                ans1.setText(ianswers.get(0).toString());
                ans2.setText(ianswers.get(1).toString());
                ans3.setText(ianswers.get(2).toString());
                ans4.setVisibility(View.INVISIBLE);
                ans3.setVisibility(View.VISIBLE);

            } else if (ianswers.size() == 2){
                ans1.setText(ianswers.get(0).toString());
                ans2.setText(ianswers.get(1).toString());
                ans4.setVisibility(View.INVISIBLE);
                ans3.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e){

        }

        ans1.setChecked(false);
        ans2.setChecked(false);
        ans3.setChecked(false);
        ans4.setChecked(false);
        quno++;
    }

}
