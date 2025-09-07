package com.example.myfinance;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // 1. Find the toolbar and set it as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar_statistics);
        setSupportActionBar(toolbar);

        // 2. Get the action bar and enable the back ("Up") button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Statystyki Wydatków"); // Optional: Set a title
        }

        pieChart = findViewById(R.id.pieChart);
        setupPieChart();

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        transactionViewModel.getSpendingByCategory().observe(this, categorySpendings -> {
            if (categorySpendings != null && !categorySpendings.isEmpty()) {
                loadPieChartData(categorySpendings);
            } else {
                pieChart.clear();
                pieChart.setCenterText("Brak danych o wydatkach");
                pieChart.invalidate();
            }
        });

        pieChart = findViewById(R.id.pieChart);
        setupPieChart();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // android.R.id.home is the special ID for the back button
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.WHITE); // <-- CHANGE TO WHITE
        pieChart.setCenterText("Wydatki wg kategorii");
        pieChart.setCenterTextColor(Color.WHITE); // <-- ADD THIS
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false); // Legend is disabled, but if you enable it, set its color too
    }

    private void loadPieChartData(List<CategorySpending> spendings) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (CategorySpending spending : spendings) {
            entries.add(new PieEntry(spending.totalAmount, spending.category));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE); // <-- CHANGE TO WHITE

        pieChart.setData(data);
        pieChart.invalidate(); // Refresh the chart
    }
}