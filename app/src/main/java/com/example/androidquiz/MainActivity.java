package com.example.androidquiz;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button new_game, cont;
    private String category = "All";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fillSpinner();
        new_game = findViewById(R.id.new_game);
        cont = findViewById(R.id.cont);
    }

    public void clicker(View v){
        Intent game = new Intent(this,game.class);
        startActivity(game);
    }

    public void fillSpinner(){
        Spinner spinner = findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        list.add("All");
        list.add("Movie");
        list.add("Music");
        list.add("General Knowledge");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        final String[] cat = new String[list.size()];
        list.toArray(cat);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int index = parentView.getSelectedItemPosition();
                category = cat[index];
                Toast.makeText(getBaseContext(),
                        "Your Category is " + category,
                        Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }
}