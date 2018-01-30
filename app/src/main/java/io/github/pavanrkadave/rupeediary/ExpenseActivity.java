package io.github.pavanrkadave.rupeediary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {

    private ListView expenseList;

    private List<ExpenseObject> expensesList;

    //Firebase Database Reference.
    private DatabaseReference expenseReference;
    private ProgressBar progressBar;

    //Firebase User
    String mUsername;

    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mUsername = user.getDisplayName();

        progressBar = findViewById(R.id.progress_bar);

        expenseReference = FirebaseDatabase.getInstance().getReference("expenses").child(mUsername);

        expenseList = findViewById(R.id.expense_list);

        expensesList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        expenseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                expensesList.clear();
                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()){
                    ExpenseObject expenseObj = expenseSnapshot.getValue(ExpenseObject.class);
                    expensesList.add(expenseObj);
                }

                ExpenseAdapter adapter = new ExpenseAdapter(ExpenseActivity.this,expensesList);
                progressBar.setVisibility(View.GONE);
                expenseList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        expenseList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                ExpenseObject obj = expensesList.get(i);
                String desc = obj.getDescription();
                showDeleteDialog(desc);

                return false;
            }
        });


    }

    private void showDeleteDialog(final String obj) {


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Do you really want to delete the expense?")
                .setTitle("Delete?");
        alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteData(obj);

            }
        });

        alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.create();
        alert.show();
        
    }

    private void deleteData(String obj) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("expenses").child(mUsername).child(obj);
        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ExpenseActivity.this, "Data deleted!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ExpenseActivity.this, "Error Deleting data!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

