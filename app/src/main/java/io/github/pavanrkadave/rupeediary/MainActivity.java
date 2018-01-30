package io.github.pavanrkadave.rupeediary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    //UI Elements of the MainActivity.
    private TextInputEditText moneyInput;
    private TextInputEditText descriptionInput;
    private Button saveData;


    //Intent to show the expenses
    Intent intent;


    //Firebase Database Reference.
    private DatabaseReference expenseReference;

    //Firebase Auth Variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthState;

    //Current user info
    public String mUsername;

    //Static variables
    private static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //Expense Activity intent to display the expenses.
        intent = new Intent(MainActivity.this, ExpenseActivity.class);

        //Firebase Database Reference
        expenseReference = FirebaseDatabase.getInstance().getReference("expenses");

        //UI Initialization
        moneyInput = findViewById(R.id.spent_money_editText);
        descriptionInput = findViewById(R.id.description_editText);
        saveData = findViewById(R.id.save_expene);

        //Handling click event on the save data button.
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Methond invoke to save the data.
                saveExpenses();
            }
        });

        mAuthState = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //User is SignedIn.
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    //User is Not SignedIn.
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .setTheme(R.style.UITheme)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
            };
        }


    private void onSignedInInitialize(String displayName) {
        mUsername = displayName;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //Inflate the menu_main.xml to display the menu.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Handling the click event on the menu item.
        switch (item.getItemId()) {

            case R.id.show_expenses:
                startActivity(intent);
                break;
            case R.id.sign_out: AuthUI.getInstance()
                    .signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Signed Out!", Toast.LENGTH_SHORT).show();

                        }
                    });
            break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void saveExpenses() {
        //Extract text from the text fields.
        String moneySpent = moneyInput.getText().toString();
        String description = descriptionInput.getText().toString();

        //get the date and day as strings fro the Simpledate format class.
        String date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        String day = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date());

        //If the text field is not empty save the data to firebase.
        if (!TextUtils.isEmpty(moneySpent) && !TextUtils.isEmpty(description)) {
            //Save the data
            ExpenseObject newExpense = new ExpenseObject(moneySpent, description, date, day);
            expenseReference.child(mUsername).push().setValue(newExpense);
            Toast.makeText(this, "Expense Added!", Toast.LENGTH_SHORT).show();

            //Clear the field once data is saved to cloud.
            moneyInput.setText("");
            descriptionInput.setText("");

            //Start the ExpenseActivity.
            startActivity(intent);

        } else {
            //If Either of the field is empty Toast a Message.
            Toast.makeText(this, "Both Fields Required!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                mUsername = user.getDisplayName();
                Log.v("User",mUsername);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthState != null) {
            mAuth.removeAuthStateListener(mAuthState);
        }
    }

}
