package com.example.bucketlist;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> ItemList;

    public ItemAdapter(List<Item> ItemList, itemListener itemListener) {
        this.ItemList = ItemList;
        this.itemListener = itemListener;
    }

    private itemListener itemListener;

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_cell, null);

        // Return a new holder instance
        ItemAdapter.ViewHolder viewHolder = new ItemAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder viewHolder, int i) {

        Item item = ItemList.get(i);


        viewHolder.titleView.setText(item.getTitle());
        viewHolder.descriptionView.setText(item.getDescription());

        if (ItemList.get(i).isCompleted()) {
            viewHolder.titleView.setPaintFlags(viewHolder.titleView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.descriptionView.setPaintFlags(viewHolder.descriptionView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.checkBox.setChecked(true);
        } else {
            viewHolder.titleView.setPaintFlags(viewHolder.titleView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.descriptionView.setPaintFlags(viewHolder.descriptionView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView descriptionView;
        CheckBox checkBox;


        public ViewHolder(View itemView) {

            super(itemView);
            titleView = itemView.findViewById(R.id.itemTitle);
            descriptionView = itemView.findViewById(R.id.itemDescription);
            checkBox = itemView.findViewById(R.id.checkBox);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onItemClick(ItemList.get(getAdapterPosition()));
                }
            });
        }

    }


}
