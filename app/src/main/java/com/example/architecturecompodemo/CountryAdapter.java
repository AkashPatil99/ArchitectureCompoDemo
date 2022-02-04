package com.example.architecturecompodemo;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.OnClickListener;
import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

class CountryItemViewHolder extends ViewHolder{
    private TextView countryName,countryCode;
    private CountryAdapter.CountryAdapterListener countryAdapterListener;
    private com.example.architecturecompodemo.MainActivity mainActivity;

    public CountryItemViewHolder(@NonNull View itemView, com.example.architecturecompodemo.MainActivity mainActivity) {
        super(itemView);
//        this.countryAdapterListener = countryListener;
        this.mainActivity = mainActivity;
        countryName = itemView.findViewById(R.id.countryName);
        countryCode = itemView.findViewById(R.id.countryCode);
    }

    public void bind(@NonNull com.example.architecturecompodemo.Country country, int position) {
        countryName.setText(country.getCountryName());
        countryCode.setText(country.getCountryCode());

        countryName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.checkBackground(position);
            }
        });
    }

}

public class CountryAdapter extends Adapter<ViewHolder>{
    private List<com.example.architecturecompodemo.Country> countryList;
    CountryAdapterListener countryAdapterListener;
    private com.example.architecturecompodemo.MainActivity mainActivity;

    public interface CountryAdapterListener{
        void changeBackground(int position);
    }

    public CountryAdapter(List<com.example.architecturecompodemo.Country> countryList, com.example.architecturecompodemo.MainActivity mainActivity){
        this.countryList = countryList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_layout, parent, false);
        return new CountryItemViewHolder(view,mainActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ((CountryItemViewHolder) holder).bind(this.countryList.get(position),position);

    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

}
