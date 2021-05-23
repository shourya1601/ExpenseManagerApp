package com.shourya.expensemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapterCategories extends RecyclerView.Adapter<RVAdapterCategories.RVViewHolderCategories> {

    private List<RVListItemCategories> listItems;
    private Context context;

    public RVAdapterCategories(List<RVListItemCategories> listItems, Context context) {
        this.listItems=listItems;
        this.context=context;
    }

    @NonNull
    @Override
    public RVAdapterCategories.RVViewHolderCategories onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_listitem_categories,parent,false);
        RVAdapterCategories.RVViewHolderCategories viewHolder=new RVAdapterCategories.RVViewHolderCategories(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapterCategories.RVViewHolderCategories holder, int position) {
        RVListItemCategories listItem=listItems.get(position);
        holder.id=Integer.parseInt(listItem.getID());
        holder.tvAmount.setText(listItem.getAmount());
        holder.tvCategory.setText(listItem.getCategory());
        holder.listItem=listItem;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public interface SelectedItem{
        void selectedItem(RVListItemCategories listitem);
    }

    public class RVViewHolderCategories extends RecyclerView.ViewHolder {

        int id;
        TextView tvAmount;
        TextView tvCategory;
        RVListItemCategories listItem;

        public RVViewHolderCategories(@NonNull View itemView) {
            super(itemView);
            id=itemView.getId();
            tvAmount=itemView.findViewById(R.id.tvAmount);
            tvCategory=itemView.findViewById(R.id.tvCategory);


            itemView.findViewById(R.id.btn_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,selectedCategory.class);
                    intent.putExtra("ID",id);
                    intent.putExtra("category",tvCategory.getText().toString());
                    context.startActivity(intent);
                }
            });

            itemView.findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,update_category.class);
                    intent.putExtra("ID",id);
                    intent.putExtra("category",tvCategory.getText().toString());
                    context.startActivity(intent);
                }
            });

            itemView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseAdapter da=new DatabaseAdapter(context);
                    try {
                        da.open();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setMessage("This category along with data will completely be deleted");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            boolean b=false;
                            boolean c=false;
                            try {
                                b=da.deleteDataCategory(tvCategory.getText().toString());
                                c=da.deleteIncomeCategory(tvCategory.getText().toString());
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
