package karpenko.test.covidtrackerapi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import karpenko.test.covidtrackerapi.api.CountryData;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private Context context;
    private List<CountryData> list;

    public CountryAdapter(Context context, List<CountryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.country_sing_item, parent,false);

        return new CountryViewHolder(view);
    }

    public void filterList(List<CountryData> filter){

        list = filter;
        notifyDataSetChanged();

    }



    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {

        CountryData data = list.get(position);
        holder.countryTotal.setText(NumberFormat.getInstance().format(Integer.parseInt(data.getCases())));
        holder.countryName.setText(data.getCountry());
        holder.countryNumber.setText(String.valueOf(position+1));

        Map<String,String> img = data.getCountryInfo();
        Glide.with(context).load(img.get("flag")).into(holder.countryFlag);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context,MainActivity.class);
            intent.putExtra("country", data.getCountry());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {

        private TextView countryNumber, countryName, countryTotal;
        private ImageView countryFlag;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);

            countryFlag = itemView.findViewById(R.id.countryFlag);
            countryNumber = itemView.findViewById(R.id.countryNumber);
            countryName = itemView.findViewById(R.id.countryName);
            countryTotal = itemView.findViewById(R.id.countryTotal);

        }
    }

}
