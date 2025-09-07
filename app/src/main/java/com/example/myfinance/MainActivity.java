package com.example.myfinance;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TransactionAdapter.OnDeleteClickListener {

    private TransactionViewModel transactionViewModel;
    private TextView tvTotalIncome, tvTotalExpenditure;
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        // 2. Update how the adapter is created
        adapter = new TransactionAdapter(new TransactionAdapter.TransactionDiff(), this);
        recyclerView.setAdapter(adapter);


        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionViewModel.getAllTransactions().observe(this, transactions -> {
            adapter.submitList(transactions);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalExpenditure = findViewById(R.id.tvTotalExpenditure);
        final TransactionAdapter adapter = new TransactionAdapter(new TransactionAdapter.TransactionDiff(), this);
        recyclerView.setAdapter(adapter);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionViewModel.getAllTransactions().observe(this, transactions -> {
            adapter.submitList(transactions);
        });

        transactionViewModel.getTotalIncome().observe(this, income -> {
            if (income != null) {
                tvTotalIncome.setText(String.format("%.2f", income));
            } else {
                tvTotalIncome.setText("0.00");
            }
        });

        transactionViewModel.getTotalExpenditure().observe(this, expenditure -> {
            if (expenditure != null) {
                tvTotalExpenditure.setText(String.format("%.2f", expenditure));
            } else {
                tvTotalExpenditure.setText("0.00");
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // --- START OF REPLACEMENT CODE ---

            // 1. Create an AlertDialog Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Dodaj transakcję");

            // 2. Inflate the custom layout
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_transaction, null);
            builder.setView(dialogView);

            // 3. Get references to the widgets inside the inflated layout
            final EditText etTransactionName = dialogView.findViewById(R.id.etTransactionName);
            final EditText etAmount = dialogView.findViewById(R.id.etAmount);
            final Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);

            // 4. Set up the Spinner
            ArrayAdapter<CharSequence> adapter_n = ArrayAdapter.createFromResource(this,
                    R.array.categories, android.R.layout.simple_spinner_item);
            adapter_n.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapter_n);

            // 5. Set the dialog's buttons and their click listeners
            builder.setPositiveButton("Zapisz", (dialog, which) -> {
                // This is the code that runs when the "Save" button is clicked
                String name = etTransactionName.getText().toString().trim();
                String amountStr = etAmount.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();

                if (name.isEmpty() || amountStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Proszę wypełnić wszystkie pola", Toast.LENGTH_SHORT).show();
                    return;
                }

                float amount = Float.parseFloat(amountStr);
                boolean isIncome = category.equals("Dochód");

                Transaction transaction = new Transaction();
                transaction.name = name;
                transaction.amount = amount;
                transaction.category = category;
                transaction.isIncome = isIncome;

                // Use the same ViewModel to insert the new transaction
                transactionViewModel.insert(transaction);

                Toast.makeText(MainActivity.this, "Transakcja zapisana", Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("Anuluj", (dialog, which) -> {
                // Just close the dialog
                dialog.cancel();
            });

            // 6. Create and show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();

            // --- END OF REPLACEMENT CODE ---
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This line inflates the menu file and adds the items to the top bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId(); // Use a variable for the item ID
        if (itemId == R.id.action_delete_all) {
            showDeleteConfirmationDialog();
            return true;
        } else if (itemId == R.id.action_statistics) { // <-- ADD THIS ELSE IF BLOCK
            // Start the StatisticsActivity
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        // Create a confirmation dialog to prevent accidental deletion.
        new AlertDialog.Builder(this)
                .setTitle("Resetuj dane")
                .setMessage("Czy na pewno chcesz usunąć wszystkie transakcje? Tej operacji nie można cofnąć.")
                .setIcon(android.R.drawable.ic_dialog_alert) // A standard warning icon
                .setPositiveButton("Tak, usuń", (dialog, which) -> {
                    // If the user clicks "Yes", call the deleteAll method in the ViewModel.
                    transactionViewModel.deleteAll();
                    Toast.makeText(this, "Wszystkie dane zostały zresetowane.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Anuluj", null) // "Cancel" does nothing but close the dialog.
                .show();
    }

    @Override
    public void onDeleteClick(Transaction transaction) {
        new AlertDialog.Builder(this)
                .setTitle("Usuń transakcję")
                .setMessage("Czy na pewno chcesz usunąć '" + transaction.name + "'?")
                .setPositiveButton("Tak", (dialog, which) -> {
                    transactionViewModel.delete(transaction);
                    Toast.makeText(this, "Transakcja usunięta", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Anuluj", null)
                .show();
    }
}