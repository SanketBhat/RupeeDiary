package io.github.pavanrkadave.rupeediary;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pavan on 1/29/2018.
 */

public class ExpenseAdapter extends ArrayAdapter<ExpenseObject> {

    private Activity context;
    private List<ExpenseObject> expenses;

    public ExpenseAdapter(Activity context, List<ExpenseObject> expenses) {
        super(context, R.layout.expense_item, expenses);
        this.context = context;
        this.expenses = expenses;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.expense_item,null,true);

        TextView money = listViewItem.findViewById(R.id.money);
        TextView description = listViewItem.findViewById(R.id.description);
        TextView dateView = listViewItem.findViewById(R.id.dateView);
        TextView day = listViewItem.findViewById(R.id.day);

        ExpenseObject myExpense = expenses.get(position);
        money.setText(myExpense.getMoney()+ " Rs");
        description.setText(myExpense.getDescription());
        dateView.setText(myExpense.getDateToday());
        day.setText(myExpense.getDayToday());


        return listViewItem;
    }
}
