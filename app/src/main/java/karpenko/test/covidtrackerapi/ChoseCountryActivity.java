package karpenko.test.covidtrackerapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import karpenko.test.covidtrackerapi.api.ApiUtilities;
import karpenko.test.covidtrackerapi.api.CountryData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoseCountryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CountryData> list;
    private ProgressDialog progressDialog;
    private SearchView searchView;
    private CountryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_country);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.countryRecyclerView);
        list = new ArrayList<>();
        adapter = new CountryAdapter(this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Search...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                assert response.body() != null;
                list.addAll(response.body());

                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ChoseCountryActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                
                filter(s.toString());
                
                return true;
            }
        });

    }

    private void filter(String text) {

        List<CountryData> filterList = new ArrayList<>();

        for(CountryData items : list){
            if(items.getCountry().toLowerCase().contains(text.toLowerCase())){
                filterList.add(items);
            }
        }

        adapter.filterList(filterList);

    }


}