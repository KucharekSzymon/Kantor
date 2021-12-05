package com.example.kantor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText ilosc;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private ListView list;
    Button admin,zamien;
    Spinner spinner,spinner2;
    String out = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        spinner = findViewById(R.id.spinner);
        admin = findViewById(R.id.button3);
        spinner2 = findViewById(R.id.spinner2);
        loadSpinnerData();
        list = (ListView) findViewById(R.id.listadmin);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        list.setAdapter(adapter);

        showWaluty();
        adminPanel();
        exchange();
    }
    public void exchange(){
        ilosc = findViewById(R.id.editTextNumber3);
        zamien = findViewById(R.id.button5);

        zamien.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DecimalFormat df = new DecimalFormat("###.##");
                        if(!ilosc.getText().toString().equals("")){
                            String walutaA = spinner.getSelectedItem().toString();
                            String walutaB = spinner2.getSelectedItem().toString();
                            Float kurs = db.exchange(walutaA,walutaB);
                            if (kurs == Float.parseFloat("-99"))
                                Toast.makeText(getApplicationContext(),"Podano takie same waluty!",Toast.LENGTH_SHORT).show();
                            else {
                                if(walutaA == "PLN") {
                                    Float ile = Float.parseFloat(ilosc.getText().toString());
                                    Float licznik = 1 / kurs;
                                    out = df.format(ile * licznik);
                                    Toast.makeText(getApplicationContext(), "Zamieniasz " + ilosc.getText().toString() + walutaA + " na " + out + walutaB, Toast.LENGTH_SHORT).show();
                                }
                                else if (walutaB == "PLN"){
                                    Float ile = Float.parseFloat(ilosc.getText().toString());
                                    Float licznik = kurs;
                                    out = df.format(ile * licznik);
                                    Toast.makeText(getApplicationContext(), "Zamieniasz " + ilosc.getText().toString() + walutaA + " na " + out + walutaB, Toast.LENGTH_SHORT).show();
                                }
                                else if(!walutaA.equals("PLN") && !walutaB.equals("PLN")){
                                    Float ile = Float.parseFloat(ilosc.getText().toString());
                                    Float licznik = 1 * kurs;
                                    Log.d("Pierwsza zmiana",Float.toString(ile*licznik));
                                    Float again = db.exchange("PLN",walutaB);
                                    out = df.format((ile * licznik)*(1/again));
                                    Toast.makeText(getApplicationContext(), "Zamieniasz " + ilosc.getText().toString() + walutaA + " na " + out + walutaB, Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Wpisz poprawną liczbe!",Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void adminPanel(){
        admin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent1 = new Intent(getApplicationContext(),adminPanel.class);
                        startActivity(myIntent1);
                    }
                }
        );
    }

    public void showWaluty(){
        Cursor res = db.getWaluty();
        if(res.getCount() == 0){
            //pusta tabelka
            return;
        }
        arrayList.clear();
        arrayList.add("Waluta | Cena kupna | Cena sprzedazy");
        while(res.moveToNext()){
            arrayList.add(res.getString(0)+" | " + res.getString(1)+" | " + res.getString(2)+" | ");
        }
        adapter.notifyDataSetChanged();
    }
    private void loadSpinnerData() {
        // database handler
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        // Spinner Drop down elements
        List<String> lables = db.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter);
    }

}