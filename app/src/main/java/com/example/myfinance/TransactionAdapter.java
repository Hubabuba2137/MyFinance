package com.example.myfinance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;

public class TransactionAdapter extends ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(Transaction transaction);
    }
    private final OnDeleteClickListener listener;

    public TransactionAdapter(@NonNull DiffUtil.ItemCallback<Transaction> diffCallback, OnDeleteClickListener listener) {
        super(diffCallback);
        this.listener = listener; // And it must be stored here
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ... this method stays the same
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_transaction, parent, false);
        return new TransactionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction current = getItem(position);
        holder.bind(current, listener);
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTransactionName;
        private final TextView tvTransactionAmount;
        private final ImageButton btnDelete; // 1. Variable must be declared

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTransactionName = itemView.findViewById(R.id.tvTransactionName);
            tvTransactionAmount = itemView.findViewById(R.id.tvTransactionAmount);
            btnDelete = itemView.findViewById(R.id.btnDelete); // 2. Button must be found by its ID
        }

        public void bind(final Transaction transaction, final OnDeleteClickListener listener) {
            tvTransactionName.setText(transaction.name);
            tvTransactionAmount.setText(String.format("%.2f", transaction.amount));

            // ... code for setting text color ...

            // 3. The OnClickListener MUST be set here
            btnDelete.setOnClickListener(v -> {
                if (listener != null) { // Good practice to check for null
                    listener.onDeleteClick(transaction);
                }
            });
        }
    }

    // This class helps the adapter efficiently update the list
    static class TransactionDiff extends DiffUtil.ItemCallback<Transaction> {
        @Override
        public boolean areItemsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem.name.equals(newItem.name) &&
                    oldItem.amount == newItem.amount &&
                    oldItem.isIncome == newItem.isIncome;
        }
    }
}