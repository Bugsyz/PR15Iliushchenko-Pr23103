package com.example.pr15_23101_fi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeelingAdapter extends RecyclerView.Adapter<FeelingAdapter.FeelingViewHolder> {

    private final List<FeelingItem> feelingList;
    private final Context context;
    private OnFeelingClickListener listener;
    private int selectedPosition = -1;

    public interface OnFeelingClickListener {
        void onFeelingClick(FeelingItem feelingItem, int position);
    }

    public FeelingAdapter(List<FeelingItem> feelingList, Context context) {
        this.feelingList = feelingList;
        this.context = context;
    }

    public void setOnItemClickListener(OnFeelingClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeelingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feeling, parent, false);
        return new FeelingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeelingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FeelingItem item = feelingList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.ivIcon.setImageResource(item.getIconResId());

        if (position == selectedPosition) {
            holder.cardFeeling.setCardBackgroundColor(0xFF7C9A92);
            holder.ivIcon.setColorFilter(0xFFFFFFFF);
            holder.tvTitle.setTextColor(0xFFFFFFFF);
        } else {
            holder.cardFeeling.setCardBackgroundColor(0xFFFFFFFF);
            holder.ivIcon.setColorFilter(0xFF2C3E3A);
            holder.tvTitle.setTextColor(0xFFFFFFFF);
        }

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = position;

            if (previousPosition != -1) {
                notifyItemChanged(previousPosition);
            }
            notifyItemChanged(position);

            if (listener != null) {
                listener.onFeelingClick(item, position);
            }

            Toast.makeText(context, "Выбрано: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return feelingList.size();
    }

    public void clearSelection() {
        int previousPosition = selectedPosition;
        selectedPosition = -1;
        if (previousPosition != -1) {
            notifyItemChanged(previousPosition);
        }
    }

    public static class FeelingViewHolder extends RecyclerView.ViewHolder {
        CardView cardFeeling;
        ImageView ivIcon;
        TextView tvTitle;

        public FeelingViewHolder(@NonNull View itemView) {
            super(itemView);
            cardFeeling = itemView.findViewById(R.id.card_feeling);
            ivIcon = itemView.findViewById(R.id.iv_feeling_icon);
            tvTitle = itemView.findViewById(R.id.tv_feeling_title);
        }
    }
}