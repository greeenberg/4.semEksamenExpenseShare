package com.example.expenseshare.Wrappers;

import android.database.Cursor;

import com.example.expenseshare.Objects.Person;


public class PersonCursorWrapper extends android.database.CursorWrapper {

    public PersonCursorWrapper (Cursor cursor) {super(cursor);}

    public Person getPerson(){
      long id = getLong(getColumnIndex("_id"));
      String name = getString(getColumnIndex("NAME"));
      double personPaidFor = getDouble(getColumnIndex("PEOPLEPAIDFOR"));
      boolean paid = getInt(getColumnIndex("PAID")) == 1;
      Person p = new Person(id,name,personPaidFor);
      p.setAmountToPay(getDouble(getColumnIndex("AMOUNTTOPAY")));
      p.setPaid(paid);

      return p;
    }
}
