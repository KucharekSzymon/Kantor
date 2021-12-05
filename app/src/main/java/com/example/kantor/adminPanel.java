package com.example.kantor;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class adminPanel extends AppCompatActivity {
    DatabaseHelper db;
    EditText walutaName,walutaKupno,walutaSprzedarz,login,haslo;
    Button walutaDodawanie;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private ListView list;
    Button back,Zaloguj,zmien,usun;
    String Nazwa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        db = new DatabaseHelper(this);

        Zaloguj = findViewById(R.id.loginBTN);
        Zaloguj.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login = findViewById(R.id.editTextTextPersonName2);
                        haslo = findViewById(R.id.editTextTextPassword);

                        if(db.ifAdmin(login.getText().toString(),haslo.getText().toString())){
                            setContentView(R.layout.waluta_add);
                            zmien = findViewById(R.id.btnZmien);
                            usun = findViewById(R.id.btnUsun);
                            walutaName = findViewById(R.id.editTextTextPersonName);
                            walutaKupno = findViewById(R.id.editTextNumber);
                            walutaSprzedarz = findViewById(R.id.editTextNumber2);
                            walutaDodawanie = findViewById(R.id.button2);
                            back = findViewById(R.id.button4);
                            addWaluta();

                            usun.setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(!Nazwa.equals("")){
                                                db.usun(Nazwa);
                                                Toast.makeText(getApplicationContext(),"Usunieto "+Nazwa,Toast.LENGTH_SHORT).show();
                                                showWaluty();
                                            }
                                            else
                                                Toast.makeText(getApplicationContext(),"Podaj nazwe!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                            zmien.setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(!Nazwa.equals("")){
                                                db.zmien(Nazwa,walutaName.getText().toString(),walutaKupno.getText().toString(),walutaSprzedarz.getText().toString());
                                                Toast.makeText(getApplicationContext(),"Zmieniono "+Nazwa,Toast.LENGTH_SHORT).show();
                                                showWaluty();
                                            }
                                            else
                                                Toast.makeText(getApplicationContext(),"Błędna dane!",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                            );


                            list = (ListView) findViewById(R.id.listadmin);
                            arrayList = new ArrayList<String>();
                            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
                            list.setAdapter(adapter);

                            ListView listView = findViewById(R.id.listadmin);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Object item = parent.getItemAtPosition(position);
                                    String text = item.toString();
                                        String[] waluta = text.split(" │ ");
                                         Nazwa = waluta[0];

                                         walutaName.setText(waluta[0]);
                                         walutaKupno.setText(waluta[1]);
                                         walutaSprzedarz.setText(waluta[2]);
                                }
                            });

                            showWaluty();
                            back();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Błędna dane!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


    }

    public void addWaluta(){
        walutaDodawanie.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!walutaName.getText().toString().equals("") && !walutaKupno.getText().toString().equals("") && !walutaSprzedarz.getText().toString().equals("")) {
                            boolean in = db.addWaluta(walutaName.getText().toString(), walutaKupno.getText().toString(), walutaSprzedarz.getText().toString());
                            if (in) {
                                Toast.makeText(adminPanel.this, "Dodano " + walutaName.getText().toString(), Toast.LENGTH_SHORT).show();
                                showWaluty();
                            } else
                                Toast.makeText(adminPanel.this, "Wystapil blad ", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(adminPanel.this, "Wpisano puste wartości!", Toast.LENGTH_SHORT).show();

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
            arrayList.add(res.getString(0)+" │ " + res.getString(1)+" │ " + res.getString(2)+" │ ");
        }
        adapter.notifyDataSetChanged();
    }
    public void back(){
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent1 = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(myIntent1);
                    }
                }
        );
    }
}
