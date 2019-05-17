package com.example.expenseshare.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.expenseshare.DataBase.Storage;
import com.example.expenseshare.MainActivity;
import com.example.expenseshare.Objects.Expense;
import com.example.expenseshare.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
private ArrayList<Expense> expenses = new ArrayList<>();
private Listener listener;
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
    public RecyclerViewAdapter(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
    public void setListener (Listener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_expense, parent, false);
        return new RecyclerViewAdapter.ViewHolder(cv);
    }
    @Override
    public void onBindViewHolder (RecyclerViewAdapter.ViewHolder holder, final int position) {
        CardView cv = holder.cardView;
        TextView header = (TextView)cv.findViewById(R.id.NameText);
        TextView totalAmount = (TextView)cv.findViewById(R.id.AmountText);
        header.setText(expenses.get(position).getName());
        totalAmount.setText(expenses.get(position).getTotalAmount() + "");
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogInterface.OnClickListener positiveButtonClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("positive Click", "onClick:  Button Clicked JAh");
                        Storage storage = Storage.getInstance();
                        storage.removeExpense(expenses.get(position));
                        Log.d("Expense remove done", "onClick: ");
                        notifyDataSetChanged();
                        ((MainActivity)context).updateCards();

                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                ab.setMessage("Skal Denne Udgift Slettes?");
                ab.setPositiveButton("Ja",positiveButtonClick);
                ab.setNegativeButton("Nej", null);
                ab.show();
                return false;
            }
        });
    }
    public void updateExpenseList(ArrayList listOfExpenses) {
        this.expenses = listOfExpenses;
        this.notifyDataSetChanged();
    }


    public void setContext(Context context) {
        this.context = context;
    }
}
