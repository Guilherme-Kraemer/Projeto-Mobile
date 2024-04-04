package com.guilherme.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guilherme.myapplication.databinding.PillItemBinding;
import com.guilherme.myapplication.model.Pill;

import java.util.ArrayList;

public class PillAdapter extends RecyclerView.Adapter<PillAdapter.PillViewHolder> {

    private final ArrayList<Pill> pillList;
    private final Context context;

    public PillAdapter(ArrayList<Pill> pillList, Context context) {
        this.pillList = pillList;
        this.context = context;
    }

    @NonNull
    @Override
    public PillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PillItemBinding listItem;
        listItem = PillItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PillViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PillViewHolder holder, int position) {
        holder.binding.imgPill.setBackgroundResource(pillList.get(position).getImgPill());
        holder.binding.txtPillName.setText(pillList.get(position).getPillName());
        holder.binding.txtPillDescription.setText(pillList.get(position).getPillDescription());
        holder.binding.txtTime.setText(pillList.get(position).getPilltime());
    }

    @Override
    public int getItemCount() {
        return pillList.size();
    }

    public static class PillViewHolder extends RecyclerView.ViewHolder {

        PillItemBinding binding;
        public PillViewHolder(PillItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
