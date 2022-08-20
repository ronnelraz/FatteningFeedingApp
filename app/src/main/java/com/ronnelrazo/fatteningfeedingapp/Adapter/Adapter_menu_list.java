package com.ronnelrazo.fatteningfeedingapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.github.hariprasanths.bounceview.BounceView;
import com.google.android.material.button.MaterialButton;
import com.ronnelrazo.fatteningfeedingapp.R;
import com.ronnelrazo.fatteningfeedingapp.model.model_menu;

import java.util.List;

public class Adapter_menu_list extends RecyclerView.Adapter<Adapter_menu_list.ViewHolder> {
    Context mContext;
    List<model_menu> newsList;

    private String[] getMenuAccessArray;

    public Adapter_menu_list(List<model_menu> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_activity,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_menu getData = newsList.get(position);
        BounceView.addAnimTo(holder.menu);


        holder.menu.setText(getData.getTitle());
        holder.menu.setIconResource(getData.getIcon());

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, getData.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }





    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public MaterialButton menu;

        public ViewHolder(View itemView) {
            super(itemView);
             menu = itemView.findViewById(R.id.menu_item);


        }
    }
}
