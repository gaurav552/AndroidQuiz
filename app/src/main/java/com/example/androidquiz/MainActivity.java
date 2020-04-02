package com.example.androidquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button new_game, cont;
    private String category_name = "All Category";
    private String category_id = "";
    private String total_question = "10";
    private String game_difficulty = "";
    private int difficulty_id = 0;
    private TextView name;
    private JSONObject jObj;
    private List<String> list = new ArrayList<>();
    private List<String> id_list = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        new_game = findViewById(R.id.new_game);
        cont = findViewById(R.id.cont);
        new getCat().execute();
    }

    public void clicker(View v){

        if (v.getId() == new_game.getId()){

            final Intent game = new Intent(this,game.class);
            game.putExtra("category_name",category_name);
            game.putExtra("category_id", category_id);
            game.putExtra("difficulty", game_difficulty);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Total Questions");

// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            input.setText(total_question);
            input.setPadding(60,30,60,50);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

               if ( !input.getText().toString().isEmpty()){
                   total_question = input.getText().toString();

                   if (Integer.parseInt(total_question) <= 30 && Integer.parseInt(total_question) >= 10 ){
                       game.putExtra("total_questions", total_question);
                       startActivity(game);
                   } else {
                       Toast.makeText(getBaseContext(),
                               "Only 30 questions at most and 10 questions at least",
                               Toast.LENGTH_LONG).show();
                   }
               } else {
                   Toast.makeText(getBaseContext(),
                           "please write a number between 10 and 30",
                           Toast.LENGTH_LONG).show();
               }

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }

    }


    public void fillSpinner(){

        final Spinner spinner = findViewById(R.id.spinner);
        final Spinner difficulty = findViewById(R.id.difficulty);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(dataAdapter.getPosition(category_name));

        final String[] cat = new String[list.size()];
        final String[] cat_id = new String[id_list.size()];
        list.toArray(cat);
        id_list.toArray(cat_id);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    int index = parentView.getSelectedItemPosition();
                    category_name = cat[index];
                    if (index > 0){
                        category_id = cat_id[index-1];
                    } else if (index == 0){
                        category_id = "0";
                    }

                    Toast.makeText(getBaseContext(),
                            category_name+" Selected",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    name.setText(e.toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> difficulties = new ArrayList<>();
        difficulties.add("Any Difficulty");
        difficulties.add("Easy");
        difficulties.add("Medium");
        difficulties.add("Hard");
        ArrayAdapter<String> difficultAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficulties);
        difficultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        difficulty.setAdapter(difficultAdapter);
        final String[] diff = new String[difficulties.size()];
        difficulties.toArray(diff);
        difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                game_difficulty = difficulty.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        difficulty.setSelection(difficultAdapter.getPosition(game_difficulty));
    }


    private class getCat extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String line, newjson = "";
                URL urls = new URL("https://opentdb.com/api_category.php");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(urls.openStream(), StandardCharsets.UTF_16))) {
                    while ((line = reader.readLine()) != null) {
                        newjson += line;
                        // System.out.println(line);
                    }
                  System.out.println(newjson);
                    String json = newjson;
                    jObj = new JSONObject(json);
                    JSONArray jsonArray = jObj.getJSONArray("trivia_categories");
                    list.add("All Category");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        String namm = explrObject.getString("name");
                        namm = namm.replace("Entertainment: ", "");
                        list.add(namm);
                        id_list.add(explrObject.getString("id"));
                    }
                    Toast.makeText(getBaseContext(),
                            list.size(),
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void param) {
            SharedPreferences settings = getSharedPreferences("UserData", 0);
            total_question = settings.getString("amount","10");
            game_difficulty = settings.getString("diff","Any Difficulty");
            category_name = settings.getString("category","All Category");
            fillSpinner();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences settings = getSharedPreferences("UserData", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("diff",game_difficulty);
        editor.putString("amount",total_question);
        editor.putString("category",category_name);
        editor.apply();
    }

    public void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences("UserData", 0);
        total_question = settings.getString("amount","10");
        game_difficulty = settings.getString("diff","Any Difficulty");
        category_name = settings.getString("category","All Category");
    }
}

