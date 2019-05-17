package com.example.expenseshare.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.expenseshare.DataBase.Storage;
import com.example.expenseshare.ExpenseDetailActivity;
import com.example.expenseshare.Objects.Person;
import com.example.expenseshare.R;
import com.example.expenseshare.Wrappers.PersonCursorWrapper;

import java.util.ArrayList;

import static android.graphics.Color.rgb;

public class PersonRecyclerViewAdapter extends RecyclerView.Adapter<PersonRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Person> people = new ArrayList<>();
    private Listener listener;
    private double totalAmount;
    private int totalPeople;
    private Context context;

    public interface Listener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public PersonRecyclerViewAdapter(ArrayList<Person> people) {
        this.people = people;
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public void setListener(PersonRecyclerViewAdapter.Listener listener) {
        this.listener = listener;
    }

    @Override
    public PersonRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_person, parent, false);
        return new PersonRecyclerViewAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(PersonRecyclerViewAdapter.ViewHolder holder, final int position) {
        final Storage storage = Storage.getInstance();
        CardView cv = holder.cardView;
        TextView header = (TextView) cv.findViewById(R.id.name);
        TextView numberOfPeople = (TextView) cv.findViewById(R.id.people);
        TextView amount = (TextView) cv.findViewById(R.id.amount);

        final DialogInterface.OnClickListener positiveButtonClicklistener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                storage.updatePersonPaid(people.get(position));
                notifyDataSetChanged();
            }
        };

        LinearLayout ln = cv.findViewById(R.id.background);
        header.setText(people.get(position).getName());
        numberOfPeople.setText(people.get(position).getPeopleToPayFor() + " Pers.");
        if(people.get(position).getAmountToPay() > 0) {
            amount.setText(people.get(position).getAmountToPay() + "");

        }
        else {
            amount.setText(people.get(position).calcAmountToPay(totalPeople,totalAmount) + "");
        }

        if(!people.get(position).hasPaid())
            ln.setBackgroundColor(rgb(255, 0, 0));
        if(people.get(position).hasPaid())
            ln.setBackgroundColor(rgb(11, 255, 7));


        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setHint("Beløb");
                ab.setView(input);

                ab.setPositiveButton("Gem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        people.get(position).setAmountToPay(Double.parseDouble(input.getText().toString()));
                        totalAmount -= people.get(position).getAmountToPay();
                        totalPeople -= people.get(position).getPeopleToPayFor();
                        storage.updateAmountToPay(people.get(position));
                        notifyDataSetChanged();
                    }
                });

                ab.setNegativeButton("Afbryd",null);
                ab.show();


            }
        });

        cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                people.get(position).setPaid(true);
                Log.d("Long click on a Person", "onLongClick:  Person Is...... " + people.get(position).getId() + " " + people.get(position).hasPaid());
                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                ab.setMessage("Har denne Person Betalt?");
                ab.setCancelable(true);
                ab.setPositiveButton((CharSequence) "Ja", positiveButtonClicklistener );
                ab.setNegativeButton((CharSequence)"Nej",null);
                ab.show();

                return true;
            }
        });
    }

    public void updatePeopleList(ArrayList listOfPeople) {
        this.people = listOfPeople;
        this.notifyDataSetChanged();
    }


    public void setContext(Context context) { this.context = context; }

    /*
    the two following methods must be used with this adapter firstly
     */
    //for setting total cost from expense to calculate each
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    //also for calculating amount each person has to pay
    public void setTotalPeople(int totalPeople){
        this.totalPeople = totalPeople;
    }


}

