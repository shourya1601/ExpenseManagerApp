package com.shourya.expensemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {

    private List<RVListitem> listItems;
    private Context context;

    public RVAdapter(List<RVListitem> listItems, Context context) {
        this.listItems=listItems;
        this.context=context;
    }

    @NonNull
    @Override
    public RVAdapter.RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_listitem,parent,false);
        RVViewHolder viewHolder=new RVViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.RVViewHolder holder, int position) {
        RVListitem listItem=listItems.get(position);
        holder.id=Integer.parseInt(listItem.getID());
        holder.tvDate.setText(listItem.getDate());
        holder.tvAmount.setText(listItem.getAmount());
        holder.tvCategory.setText(listItem.getCategory());
        holder.tvNote.setText(listItem.getNote());
        holder.listItem=listItem;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class RVViewHolder extends RecyclerView.ViewHolder {

        int id;
        TextView tvDate;
        TextView tvAmount;
        TextView tvCategory;
        TextView tvNote;
        RVListitem listItem;

        public RVViewHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.getId();
            tvDate=itemView.findViewById(R.id.tvDate);
            tvAmount=itemView.findViewById(R.id.tvAmt);
            tvCategory=itemView.findViewById(R.id.tvCat);
            tvNote=itemView.findViewById(R.id.tvNote);
            itemView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,update_listitem.class);
                    intent.putExtra("ID",id);
                    intent.putExtra("amount",tvAmount.getText().toString());
                    intent.putExtra("category",tvCategory.getText().toString());
                    intent.putExtra("date",tvDate.getText().toString());
                    intent.putExtra("note",tvNote.getText().toString());
                    context.startActivity(intent);
                }
            });
            itemView.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseAdapter da=new DatabaseAdapter(context);
                    try {
                        da.open();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean b=false;
                            try {
                                b=da.deleteIncomeData(id);
                                if (b)
                                    Toast.makeText(context, "Data deleted", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ((AppCompatActivity)context).recreate();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert=builder.create();
                    alert.setTitle("Alert Dialog");
                    alert.show();
                }
            });
        }
    }
}
