package com.example.expenseshare.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.expenseshare.Objects.Expense;
import com.example.expenseshare.Objects.Person;
import com.example.expenseshare.Wrappers.ExpenseCursorWraper;
import com.example.expenseshare.Wrappers.PersonCursorWrapper;

import java.time.LocalDate;

public class Storage {
    private PaymentDataBase sqlLiteHelper = PaymentDataBase.getInstance();

    private Storage (){ }

    private static Storage storage;

    public static  Storage getInstance(){
        if(storage == null){
            storage = new Storage();
            storage.initStorage();
        }
        return  storage;
    }

    public long insertExpense(Expense expense){
        SQLiteDatabase db = sqlLiteHelper.getReadableDatabase();
        ContentValues expenseValues = new ContentValues();
        expenseValues.put("NAME", expense.getName());
        expenseValues.put("TOTALAMOUNT", expense.getTotalAmount());
        expenseValues.put("CREATOR", expense.getCreator());
        expenseValues.put("DATE", expense.getDate().toString());
        return db.insert("EXPENSE",null,expenseValues);
    }

    public long insertPerson(Person p){
        SQLiteDatabase db = sqlLiteHelper.getReadableDatabase();
        ContentValues personValues = new ContentValues();
        personValues.put("NAME", p.getName());
        personValues.put("PEOPLEPAIDFOR", p.getPeopleToPayFor());
        personValues.put("EXPENSEID", p.getExpense().getId());
        personValues.put("PAID", p.hasPaid());

        return db.insert("PERSON", null,personValues);
    }



    public void initStorage(){
        if(getExpenses().getCount() == 0){
            LocalDate date = LocalDate.now();
            Expense ex1;
            insertExpense(ex1 = new Expense( 1,"Bowling", 250.0, "Henning",date ));
           Person p1 = ex1.CreatePerson("Henning", 2);
           p1.setPaid(true);
            ex1.CreatePerson("Primdal", 1);
            for(Person p : ex1.getPeople()){
                insertPerson(p);
            }
            Expense ex2;
            insertExpense(ex2 = new Expense( 2,"Champagne", 500.0, "Henning", date));
            Person p2 = ex2.CreatePerson("Henning", 1);
            p2.setPaid(true);
            ex2.CreatePerson("Per", 1);
            ex2.CreatePerson("Mona", 1);
            for(Person p : ex2.getPeople()){
                insertPerson(p);
            }
        }
    }

    public ExpenseCursorWraper getExpenses(){
        SQLiteDatabase db = sqlLiteHelper.getReadableDatabase();
        Cursor cursor = db.query("EXPENSE",new String[]{"_id","NAME","TOTALAMOUNT", "CREATOR", "DATE"},null,null,null,null,null);
        return  new ExpenseCursorWraper(cursor);
    }

    public ExpenseCursorWraper getExpense(long id){
        SQLiteDatabase db = sqlLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from EXPENSE where _id = ?", new String[]{id + ""});
        return new ExpenseCursorWraper(cursor);
    }

    //looks for all the people to a certain expense with the ID for said expense
    public PersonCursorWrapper getPersons(long expenseID){
        SQLiteDatabase db = sqlLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from PERSON where EXPENSEID = ?", new String[]{expenseID + ""});
        Log.d("GetPersons", "getPersons: ");
        return  new PersonCursorWrapper(cursor);
    }

    public void updatePersonPaid(Person p){
        SQLiteDatabase db = sqlLiteHelper.getReadableDatabase();
        ContentValues personVal = new ContentValues();
        personVal.put("PAID", 1);
        db.update("PERSON", personVal,"_id =" + p.getId() , null);
//        Cursor cursor = db.rawQuery("update PERSON set PAID = 0 where _id = ?", new String[]{personID + ""});
        Log.d("UPdate ID", "updatePersonPaid: " + p.getId());
//        return cursor;
    }

    public void removeExpense(Expense ex){
        SQLiteDatabase db = sqlLiteHelper.getReadableDatabase();
//        db.rawQuery("Delete from EXPENSE where _id = ?", new String[]{ex.getId() + ""});
        db.execSQL("Delete from EXPENSE where _id = ?", new Object[]{ex.getId()});
        Log.d("Removed Expense", "removeExpense: " + ex);
    }

    public void updateAmountToPay(Person p){
        SQLiteDatabase db = sqlLiteHelper.getReadableDatabase();
        ContentValues personVal = new ContentValues();
        personVal.put("AMOUNTTOPAY", p.getAmountToPay());
        db.update("PERSON", personVal,"_id =" + p.getId() , null);
        Log.d("UPdated Person", "updateAmountToPay:  Updated to....." + p.getAmountToPay());
    }
}
