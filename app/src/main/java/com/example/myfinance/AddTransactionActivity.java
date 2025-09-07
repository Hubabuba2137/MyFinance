package com.example.myfinance;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText etTransactionName, etAmount;
    private Spinner spinnerCategory;
    private TransactionViewModel transactionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        etTransactionName = findViewById(R.id.etTransactionName);
        etAmount = findViewById(R.id.etAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        Button btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        btnSave.setOnClickListener(v -> saveTransaction());
    }

    private void saveTransaction() {
        String name = etTransactionName.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (name.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Proszę wypełnić wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

        float amount = Float.parseFloat(amountStr);
        // This line is very important! The string "Dochód" must match exactly.
        boolean isIncome = category.equals("Dochód");

        // --- DEBUGGING LOG ---
        Log.d("FinanceApp", "Saving Transaction: Name=" + name + ", Amount=" + amount + ", Category=" + category + ", isIncome=" + isIncome);

        Transaction transaction = new Transaction();
        transaction.name = name;
        transaction.amount = amount;
        transaction.category = category;
        transaction.isIncome = isIncome;

        transactionViewModel.insert(transaction);
        finish();
    }
}