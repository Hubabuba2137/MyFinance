package com.example.myfinance;

// CORRECT IMPORTS: Make sure you have these and no others for Entity
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String category;
    public float amount;
    public boolean isIncome;
}