package com.example.assignment;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.RVHolder> {
    Context context;
    ArrayList<CustomDataModel> customDataModels;
    private int selectedPosition = -1 ;
    private SparseBooleanArray mSparseBooleanArray;
    private static OnItemClickListener onItemClickListener;

    public CustomAdapter(Context context, ArrayList<CustomDataModel> customDataModels){
        this.context = context;
        this.customDataModels = customDataModels;
    }


    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_rv_items, null);
     return new RVHolder(itemLayoutView);
    }

    private int row_index;
    @Override
    public void onBindViewHolder(@NonNull final RVHolder holder, final int position) {
        final CustomDataModel customDataModel = customDataModels.get(position);
        OnItemClickListener onItemClickListener;
        holder.labelTV.setText(customDataModel.getLabelName());
       holder.checkBox.setChecked(customDataModel.isSelected());
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
            }
        });

        if(selectedPosition != -1){
            if(position == selectedPosition){
                holder.checkBox.setChecked(true);
            }
            else{
                holder.checkBox.setChecked(false);
            }
        }

    }

    @Override
    public int getItemCount() {
        return customDataModels.size();
    }
    public void setOnItemClickListener (final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CustomDataModel getCheckedItems() {
        if(selectedPosition != -1){
            return customDataModels.get(selectedPosition);
        }
        else{
            return null;
        }

    }


    public static class  RVHolder extends RecyclerView.ViewHolder
           // implements View.OnClickListener
    {

        TextView labelTV;
        CheckBox checkBox;
        LinearLayout custom_rv_items_layout;

        public RVHolder(@NonNull View itemView) {
            super(itemView);
            labelTV= itemView.findViewById(R.id.cus_rv_item_tv);
            checkBox = itemView.findViewById(R.id.cus_rv_item_checkbox);
            custom_rv_items_layout = itemView.findViewById(R.id.custom_rv_items_layout);
           // custom_rv_items_layout.setOnClickListener(this);
        }


         public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }

    public interface OnItemClickListener {

        public void setOnItemClick(View view, int position);

    }


    class CheckListener implements CompoundButton.OnCheckedChangeListener {
        private CheckBox checkbox;
        private int position;
        public CheckListener(CheckBox checkbox,int position) {
            this.checkbox = checkbox;
            this.position=position;

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                checkbox.setChecked(true);
                selectedPosition = position;
                CustomAdapter.this.notifyDataSetChanged();
            } else {
                checkbox.setChecked(false);

            }
            buttonView.setChecked(isChecked);

        }
        }




}
