package com.example.expenseshare;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.expenseshare.Adapters.RecyclerViewAdapter;
import com.example.expenseshare.DataBase.PaymentDataBase;
import com.example.expenseshare.DataBase.Storage;
import com.example.expenseshare.Objects.Expense;
import com.example.expenseshare.Wrappers.ExpenseCursorWraper;
import com.example.expenseshare.Wrappers.PersonCursorWrapper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Storage storage;
    private RecyclerViewAdapter adapter;
    private ArrayList<Expense> expenses = new ArrayList<>();
    private ExpenseCursorWraper ew;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PaymentDataBase.setAppContext(this);
        PaymentDataBase db = PaymentDataBase.getInstance();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        storage = Storage.getInstance();
        ew = storage.getExpenses();
        while(ew.moveToNext()){
            expenses.add(ew.getExpense());
        }
        ew.close();
        adapter = new RecyclerViewAdapter(expenses);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setListener(new RecyclerViewAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Log.d("ID for VAlgte ", "onClick: "+ expenses.get(position).getId());
                expenseDetail(position);

            }
        });

        adapter.setContext(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void expenseDetail(int position){
        Log.d("Expense id giveth", "expenseDetail: " + (expenses.get(position).getId()));
        int totalPeople = 0;

        Intent intent = new Intent(this, ExpenseDetailActivity.class);
        intent.putExtra(ExpenseDetailActivity.EXTRA_EXPENSE_ID, expenses.get(position).getId());


        intent.putExtra("TOTALAMOUNT", expenses.get(position).getTotalAmount());
        Log.d("Total Amount giveth", "expenseDetail: " + expenses.get(position).getTotalAmount());

        PersonCursorWrapper pw = storage.getPersons(position +1);

        while (pw.moveToNext()){
            totalPeople += pw.getPerson().getPeopleToPayFor();
            Log.d("The People:", "expenseDetail: " + pw.getPerson());
        }
        pw.close();
        intent.putExtra(ExpenseDetailActivity.EXTRA_EXPENSE_PEOPLE, totalPeople);
        this.startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_create) {
            Intent intent = new Intent(this, CreateActivity.class);
            startActivityForResult(intent, 1);
            onActivityResult(1,1, null);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateCards(){
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ew = storage.getExpenses();
        expenses.clear();
        while(ew.moveToNext()){
            expenses.add(ew.getExpense());
        }
        ew.close();

        adapter.notifyDataSetChanged();
    }
}
