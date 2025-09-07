package com.example.myfinance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransactionDao {
    @Insert
    void insert(Transaction transaction);

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT SUM(amount) FROM transactions WHERE isIncome = 1")
    LiveData<Float> getTotalIncome();

    @Query("SELECT SUM(amount) FROM transactions WHERE isIncome = 0")
    LiveData<Float> getTotalExpenditure();

    @Query("DELETE FROM transactions")
    void deleteAll();

    @Query("SELECT category, SUM(amount) as totalAmount FROM transactions WHERE isIncome = 0 GROUP BY category")
    LiveData<List<CategorySpending>> getSpendingByCategory();

    @Delete
    void delete(Transaction transaction);
}