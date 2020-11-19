package com.example.inviattoadministrador.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inviattoadministrador.Interface.IItemClickListener;
import com.example.inviattoadministrador.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShippedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView lblShipped_name;
    public TextView lblShipped_password;
    public ImageView lblShipped_image;
    public TextView lblShipped_phone;
    public TextView lblShipped_address;

    private IItemClickListener iItemClickListener;

    public ShippedViewHolder(@NonNull View itemView) {
        super(itemView);

        lblShipped_name = itemView.findViewById(R.id.shipped_name);
        lblShipped_password = itemView.findViewById(R.id.shipped_password);
        lblShipped_image = itemView.findViewById(R.id.shipped_image);
        lblShipped_phone = itemView.findViewById(R.id.shipped_phone);
        lblShipped_address = itemView.findViewById(R.id.shipped_address);

        itemView.setOnClickListener(this);
    }//PlatillosViewHolder

    public void setItemClickListener(IItemClickListener itemClickListener) {
        this.iItemClickListener = itemClickListener;
    }//setItemClickListener

    @Override
    public void onClick(View view) {
        iItemClickListener.onClick(view, getAdapterPosition(), false);
    }//onClick

}//ShippedViewHolder
