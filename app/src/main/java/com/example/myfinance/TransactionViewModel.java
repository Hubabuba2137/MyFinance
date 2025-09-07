package com.example.myfinance;

import android.app.Application; // <-- Make sure this import is present
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private final TransactionRepository repository;
    private final LiveData<List<Transaction>> allTransactions;
    private final LiveData<Float> totalIncome;
    private final LiveData<Float> totalExpenditure;
    private final LiveData<List<CategorySpending>> spendingByCategory;

    // --- THIS IS THE CONSTRUCTOR THAT WAS MISSING OR INCORRECT ---
    public TransactionViewModel (Application application) {
        super(application);
        repository = new TransactionRepository(application);
        allTransactions = repository.getAllTransactions();
        totalIncome = repository.getTotalIncome();
        totalExpenditure = repository.getTotalExpenditure();
        spendingByCategory = repository.getSpendingByCategory();
    }

    LiveData<List<Transaction>> getAllTransactions() { return allTransactions; }
    LiveData<Float> getTotalIncome() { return totalIncome; }
    LiveData<Float> getTotalExpenditure() { return totalExpenditure; }
    public LiveData<List<CategorySpending>> getSpendingByCategory() { return spendingByCategory; }

    public void insert(Transaction transaction) { repository.insert(transaction); }
    public void deleteAll() { repository.deleteAll(); }

    public void delete(Transaction transaction) { // <-- ADD THIS ENTIRE METHOD
        repository.delete(transaction);
    }
}