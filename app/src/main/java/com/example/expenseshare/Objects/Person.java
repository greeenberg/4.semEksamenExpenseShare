package com.example.expenseshare.Objects;

import java.text.DecimalFormat;

public class Person {
private long id;
private String name;
private double PeopleToPayFor;
private boolean paid;
private Expense expense;
//used to store specific amount to pay if there is one
private double amountToPay;



public  Person (String name, double peopleToPayFor){
    this.name = name;
    this.PeopleToPayFor = peopleToPayFor;
    this.paid = false;
    this.amountToPay = 0;
    this.id = -1;
}

public Person(long id,String name, double peopleToPayFor){
    this.name = name;
    this.PeopleToPayFor = peopleToPayFor;
    this.paid = false;
    this.amountToPay = 0;
    this.id = id;
}

//bruges som fordelingsnøgle
public String calcAmountToPay(int totalPeople, double totalAmount){
    double amount = (totalAmount/totalPeople) * getPeopleToPayFor();
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    return df.format(amount);
}

public void addExpense(Expense ex){ this.expense = ex; }


public double getPeopleToPayFor() {
    return PeopleToPayFor;
}

public String getName() {
    return name;
}

public boolean hasPaid() {
    return paid;
}

public void setPaid(boolean paid) {
    this.paid = paid;
}

public Expense getExpense() { return expense; }

public void setAmountToPay(double amountToPay) { this.amountToPay = amountToPay; }

public double getAmountToPay() { return amountToPay; }

    public long getId() { return id; }

    @Override
public String toString() {
    return name + " Betalt: " + paid + " " + amountToPay;
}
}

