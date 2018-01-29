package io.github.pavanrkadave.rupeediary;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //UI Elements of the MainActivity.
    private TextInputEditText moneyInput;
    private TextInputEditText descriptionInput;
    private Button saveData;

    //Firebase Database Reference.
    private DatabaseReference expenseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expenseReference = FirebaseDatabase.getInstance().getReference("expenses");

        moneyInput = findViewById(R.id.spent_money_editText);
        descriptionInput = findViewById(R.id.description_editText);
        saveData = findViewById(R.id.save_expene);

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveExpenses();
            }
        });

    }

    private void saveExpenses() {
        String moneySpent = moneyInput.getText().toString();
        String description = descriptionInput.getText().toString();

        if (!TextUtils.isEmpty(moneySpent) && !TextUtils.isEmpty(description)) {
            //Save the data
            long unixTime = System.currentTimeMillis() / 1000L;
            String time = String.valueOf(unixTime);
            ExpenseObject newExpense = new ExpenseObject(moneySpent, description, time);
            expenseReference.push().setValue(newExpense);
            Toast.makeText(this, "Expense Added!", Toast.LENGTH_SHORT).show();
            moneyInput.setText("");
            descriptionInput.setText("");

        } else {
            //If Either of the field is empty Toast a Message.
            Toast.makeText(this, "Both Fields Required!", Toast.LENGTH_SHORT).show();
        }
    }
}
