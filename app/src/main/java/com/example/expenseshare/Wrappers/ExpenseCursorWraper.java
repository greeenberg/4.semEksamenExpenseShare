package com.example.expenseshare.Wrappers;

import android.database.Cursor;


import com.example.expenseshare.Objects.Expense;

import java.time.LocalDate;

public class ExpenseCursorWraper extends android.database.CursorWrapper{

    public ExpenseCursorWraper(Cursor cursor){super(cursor);}

    public Expense getExpense(){
        long id = getLong(getColumnIndex("_id"));
        String name = getString(getColumnIndex("NAME"));
        double totalAmount = getDouble(getColumnIndex("TOTALAMOUNT"));
        String creator = getString(getColumnIndex("CREATOR"));
        String dateString = getString(getColumnIndex("DATE"));
        LocalDate date = LocalDate.parse(dateString);

        return new Expense(id, name, totalAmount, creator, date);
    }
}
