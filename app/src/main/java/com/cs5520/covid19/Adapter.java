package com.cs5520.covid19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    /**
     * m is the selection indicator;
     */
    int m = 1;
    Context context;
    List<ModelClass> countryList;

    public Adapter(Context context, List<ModelClass> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.layout_item, parent, false);
//        return new ViewHolder(view);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        ModelClass modelClass= countryList.get(position);
        if(m==1){
            holder.cases.setText(modelClass.getCases());
        }else if(m == 2){
            holder.cases.setText(modelClass.getRecovered());
        }else if(m == 3){
            holder.cases.setText(modelClass.getDeaths());
        }else{
            holder.cases.setText(modelClass.getActive());
        }
        holder.country.setText(modelClass.getCountry()); //continue the process

    }

    @Override
    public int getItemCount() {
        System.out.println(countryList.size());
        return countryList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView cases, country;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cases = itemView.findViewById(R.id.countrycase);
            country = itemView.findViewById(R.id.countryname);
        }
    }

    public void filter(String charText) {
        if(charText.equals("Cases")){
            m=1;
        }else if(charText.equals("Recovered")){
            m=2;
        }else if(charText.equals("Deaths")){
            m=3;
        }else{
            m=4;
        }

        notifyDataSetChanged();
    }

}
