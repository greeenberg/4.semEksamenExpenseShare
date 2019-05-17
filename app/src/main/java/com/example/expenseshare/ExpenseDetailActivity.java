package com.example.expenseshare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;


import com.example.expenseshare.Adapters.PersonRecyclerViewAdapter;
import com.example.expenseshare.DataBase.Storage;
import com.example.expenseshare.Objects.Expense;
import com.example.expenseshare.Objects.Person;
import com.example.expenseshare.Wrappers.ExpenseCursorWraper;
import com.example.expenseshare.Wrappers.PersonCursorWrapper;

import java.util.ArrayList;

public class ExpenseDetailActivity extends AppCompatActivity {
    public static final String EXTRA_EXPENSE_ID ="id";
    public static final String EXTRA_EXPENSE_PEOPLE = "people";
    private long vacationId;
    private int totalPeople;
    private Storage storage;
    private Expense expense;
    private ArrayList<Person> people = new ArrayList<>();
    private RecyclerView recyclerView;
    private PersonRecyclerViewAdapter adapter;

    private double totalAmount;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_detail);
        storage = Storage.getInstance();
        vacationId = getIntent().getLongExtra(EXTRA_EXPENSE_ID, 0);
        Log.d("id Recieveth", "onCreate: " + vacationId);
        totalPeople = getIntent().getIntExtra(EXTRA_EXPENSE_PEOPLE, 0);

        ExpenseCursorWraper cursorWraper = storage.getExpense(vacationId);
        cursorWraper.moveToFirst();
        expense = cursorWraper.getExpense();
        Log.d("Expense from db", "onCreate: " + expense);

        TextView titel = findViewById(R.id.expenseName);
        TextView date = findViewById(R.id.date);
        TextView total = findViewById(R.id.totalAmount);
        TextView creator = findViewById(R.id.creator);

        titel.setText(expense.getName());
        date.setText(expense.getDate().toString());
        total.setText(expense.getTotalAmount() +"");
        creator.setText(expense.getCreator());



        //---------------------------------------
        PersonCursorWrapper pw = storage.getPersons(vacationId);

        while(pw.moveToNext()){
            people.add(pw.getPerson());
        }
        pw.close();



        recyclerView =(RecyclerView)findViewById(R.id.recyclePerson);
        adapter = new PersonRecyclerViewAdapter(people);

        totalAmount = expense.getTotalAmount();
        for (Person p : people) {
            if(p.getAmountToPay() >0) {
                totalAmount -= p.getAmountToPay();
                Log.d("AMount 1", "AMOUNT IS :........... " + p.getAmountToPay());
                totalPeople -= p.getPeopleToPayFor();
                Log.d("number of people", "number IS :........... " + p.getPeopleToPayFor());
            }
        }

        adapter.setTotalAmount(totalAmount);
        adapter.setTotalPeople(totalPeople);
        adapter.setContext(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setListener(new PersonRecyclerViewAdapter.Listener() {
            @Override
            public void onClick(int position) {
                PersonCursorWrapper pw = storage.getPersons(vacationId);

                while(pw.moveToNext()){
                    people.add(pw.getPerson());
                }
                pw.close();

                adapter.updatePeopleList(people);

            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);



    }
}
