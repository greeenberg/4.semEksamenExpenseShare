package com.example.expenseshare.Objects;

import java.time.LocalDate;
import java.util.ArrayList;

public class Expense {
    private long id;
    private String name;
    private double totalAmount;
    private String creator;
    private LocalDate date;

    ArrayList<Person> people = new ArrayList<>();


    public Expense(String name, double totalAmount, String creator, LocalDate date){
        this.creator =creator;
        this.name = name;
        this.totalAmount = totalAmount;
        this.id = -1;
        this.date = date;
    }

    public Expense(long id,String name, double totalAmount, String creator, LocalDate date){
        this.creator =creator;
        this.name = name;
        this.totalAmount = totalAmount;
        this.id = id;
        this.date = date;
    }

    public String getCreator() {
        return creator;
    }

    public String getName() {
        return name;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public LocalDate getDate() {
        return date;
    }

    public Person CreatePerson(String name, double peopleToPayFor){
        Person p = new Person(name, peopleToPayFor);
        people.add(p);
        p.addExpense(this);
        return p;
    }

    public Person CreatePerson(long id,String name, double peopleToPayFor){
        Person p = new Person(id,name, peopleToPayFor);
        people.add(p);
        p.addExpense(this);
        return p;
    }

    @Override
    public String toString() {
        return name + " Ialt: " + totalAmount;
    }
}
