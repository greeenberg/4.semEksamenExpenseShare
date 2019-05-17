package com.example.expenseshare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expenseshare.DataBase.Storage;
import com.example.expenseshare.Objects.Expense;
import com.example.expenseshare.Objects.Person;

import java.time.LocalDate;

public class CreateActivity extends AppCompatActivity {

    private Storage storage;

    private TextView expenseName;
    private TextView expensePrice;

    private TextView person1name;
    private TextView person1people;

    private TextView person2name;
    private TextView person2people;

    private TextView person3name;
    private TextView person3people;

    private TextView person4name;
    private TextView person4people;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);

        storage = Storage.getInstance();

        expenseName = findViewById(R.id.expenseName);
        expensePrice = findViewById(R.id.expensePrice);

        person1name = findViewById(R.id.person1Name);
        person1people = findViewById(R.id.person1People);

        person2name = findViewById(R.id.person2Name);
        person2people = findViewById(R.id.person2People);

        person3name = findViewById(R.id.person3Name);
        person3people = findViewById(R.id.person3People);

        person4name = findViewById(R.id.person4Name);
        person4people = findViewById(R.id.person4People);
    }




    public void createAction(View view){


        if(expenseName.getText().length() < 1){
            Toast.makeText(this,"Udgiftens navn skal udfyldes",Toast.LENGTH_LONG).show();
            return;
        }
        if(expensePrice.getText().length()<1){
            Toast.makeText(this,"Udgiftens Pris skal udfyldes",Toast.LENGTH_LONG).show();
            return;
        }
        if(person1name.getText().length() <1 || person2name.getText().length() <1) {
           Toast.makeText(this,"Person 1 og 2 skal udfyldes",Toast.LENGTH_LONG).show();
            return;
        }

        else {
            Expense ex = new Expense(expenseName.getText() + "", Double.parseDouble(expensePrice.getText().toString()), person1name.getText() + "", LocalDate.now());
            long exId = storage.insertExpense(ex);
            ex.setId(exId);
            Person p = ex.CreatePerson(person1name.getText() + "", Double.parseDouble(person1people.getText().toString()));
            p.setPaid(true);
            storage.insertPerson(p);


            if (person2name.getText().length() > 0) {
                p = ex.CreatePerson(person2name.getText() + "", Double.parseDouble(person2people.getText().toString()));
                storage.insertPerson(p);
            }

            if (person3name.getText().length() > 0) {
                Log.d("Der var noget i 3?!?!?!?!", "createAction: " + person3name.getText());
                p = ex.CreatePerson(person3name.getText() + "", Double.parseDouble(person3people.getText().toString()));
                storage.insertPerson(p);
            }

            if (person4name.getText().length() > 0) {
                Log.d("Der var noget i 4?!?!?!?!", "createAction: " + person4name.getText());
                p = ex.CreatePerson(person4name.getText() + "", Double.parseDouble(person4people.getText().toString()));
                storage.insertPerson(p);
            }

            setResult(1);

            finish();
        }
    }
}
