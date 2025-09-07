package com.example.myfinance;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

// NO IMPORT FOR androidx.room.Transaction!!!

class TransactionRepository {

    private TransactionDao transactionDao;
    private LiveData<List<Transaction>> allTransactions;
    private LiveData<Float> totalIncome;
    private LiveData<Float> totalExpenditure;
    private LiveData<List<CategorySpending>> spendingByCategory;

    TransactionRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        transactionDao = db.transactionDao();
        allTransactions = transactionDao.getAllTransactions();
        totalIncome = transactionDao.getTotalIncome();
        totalExpenditure = transactionDao.getTotalExpenditure();

        spendingByCategory = transactionDao.getSpendingByCategory();
    }

    LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
    }

    LiveData<Float> getTotalIncome() {
        return totalIncome;
    }

    LiveData<Float> getTotalExpenditure() {
        return totalExpenditure;
    }

    void insert(Transaction transaction) {
        // This line is correct because it is passing YOUR Transaction
        AppDatabase.databaseWriteExecutor.execute(() -> {
            transactionDao.insert(transaction);
        });
    }

    void deleteAll() { // <-- ADD THIS ENTIRE METHOD
        AppDatabase.databaseWriteExecutor.execute(() -> {
            transactionDao.deleteAll();
        });
    }

    LiveData<List<CategorySpending>> getSpendingByCategory() {
        return spendingByCategory;
    }

    void delete(Transaction transaction) { // <-- ADD THIS ENTIRE METHOD
        AppDatabase.databaseWriteExecutor.execute(() -> {
            transactionDao.delete(transaction);
        });
    }
}