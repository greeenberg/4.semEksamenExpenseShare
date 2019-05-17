package com.example.expenseshare.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PaymentDataBase extends SQLiteOpenHelper {
    private static PaymentDataBase instance;
    private static Context appContext;

    private static final String DB_NAME = "WeShare";
    private static final int DB_VERSION = 2;

    public static void setAppContext(Context context){ appContext = context;}

    private PaymentDataBase(Context context){super(context,DB_NAME,null,DB_VERSION);}

    public static PaymentDataBase getInstance(){
        if(instance == null){
            instance = new PaymentDataBase(appContext);
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db,0,DB_VERSION);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        updateMyDatabase(db, oldVersion,newVersion);
    }


    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion < 1 ){
            db.execSQL("CREATE TABLE EXPENSE (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, TOTALAMOUNT FLOAT, CREATOR TEXT, DATE TEXT);");

            db.execSQL("CREATE TABLE PERSON (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PEOPLEPAIDFOR FLOAT, PAID INTEGER, EXPENSEID INTEGER, AMOUNTTOPAY FLOAT, FOREIGN KEY (EXPENSEID) REFERENCES EXPENSE(_id));");
        }
    }
}
