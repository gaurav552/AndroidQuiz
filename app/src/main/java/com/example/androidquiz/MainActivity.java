package com.example.androidquiz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button new_game, cont;
    private String category_name = "All Category";
    private String category_id = "";
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
        Intent game = new Intent(this,game.class);
        startActivity(game);
    }


    public void fillSpinner(){

        final Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
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
                        category_id = "";
                    }

                    Toast.makeText(getBaseContext(),
                            "Your Category is " + category_name +" id is "+ category_id,
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    name.setText(e.toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private class getCat extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String line, newjson = "";
                URL urls = new URL("https://opentdb.com/api_category.php");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(urls.openStream(), "UTF-8"))) {
                    while ((line = reader.readLine()) != null) {
                        newjson += line;
                        // System.out.println(line);
                    }
//                  System.out.println(newjson);
                    String json = newjson.toString();
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void param) {
            fillSpinner();
        }
    }
}

