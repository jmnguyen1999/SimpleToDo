package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
/**
 * ItemsAdapter.java
 * Purpose: The adapter needed for using Recycler View: connection between data and View --> allows to display correct model data
 *
 * @author Josephine Nguyen
 * @version 1.0
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{
    //Expect to listen to long and short clicks:
    public interface OnLongClickListener{
        void onClick(int position);
    }
    public interface OnClickListener{
        void onClick(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    //Constructor, self-explanatory
    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    /**ViewHolder{}
     * Purpose: To access Views representing each row in the list
     */
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;

        //Constructor: tvItem = the text field of the given View
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        /**bind()
         * Purpose: Updates the data associated with the View, binds long and short click listeners (both return position of click)
         */
        public void bind(String data) {
            tvItem.setText(data);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    clickListener.onClick(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onClick(getAdapterPosition());
                    return true;
                }

            });
        }
    }


    @NonNull
    @Override
    /**onCreateViewHolder()
     * Purpose: called automatically, creates a ViewHolder, uses LayoutInflater to obtain a View
     */
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent,false);
        return new ViewHolder(todoView);
    }

    @Override
    /**onBindViewHolder()
     * Purpose: called automatically, binds data to ViewHolder
     */
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the data at "position", then bind:
        String data = items.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
