package karpenko.test.covidtrackerapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import karpenko.test.covidtrackerapi.api.ApiInterface;
import karpenko.test.covidtrackerapi.api.ApiUtilities;
import karpenko.test.covidtrackerapi.api.CountryData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm, totalActive, totalRecovered, totalDeath, totalTest;
    private TextView todayConfirm, todayRecovered, todayDeath, lastUpdateData, country;
    private PieChart pieChart;

    String countryInfo = "Ukraine";

    private List<CountryData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        if(getIntent().getStringExtra("country")!= null ){
            countryInfo = getIntent().getStringExtra("country");
        }

        initialization();

        country.setText(countryInfo);
        country.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,ChoseCountryActivity.class)));

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());
                        for (int i = 0; i<list.size(); i++){
                            if(list.get(i).getCountry().equals(countryInfo)){
                                int confirm = Integer.parseInt(list.get(i).getCases()) ;
                                int active = Integer.parseInt(list.get(i).getActive()) ;
                                int recovered = Integer.parseInt(list.get(i).getRecovered()) ;
                                int death = Integer.parseInt(list.get(i).getDeaths()) ;
                                int tests = Integer.parseInt(list.get(i).getTests());
                                //int updated = Integer.parseInt(list.get(i).getUpdated());

                                totalActive.setText(NumberFormat.getInstance().format(active));
                                totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                                totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                                totalDeath.setText(NumberFormat.getInstance().format(death));
                                totalTest.setText(NumberFormat.getInstance().format(tests));

                                todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                                todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                                todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));

                                //lastUpdateData.setText(updated);

                                setText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Confirm", confirm, getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Active", active, getResources().getColor(R.color.blue_pie)));
                                pieChart.addPieSlice(new PieModel("Recovered", recovered, getResources().getColor(R.color.pink)));
                                pieChart.addPieSlice(new PieModel("Death", death, getResources().getColor(R.color.light_gray)));

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Something wrong. Try later", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setText(String updated) {

        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        long milliseconds = Long.parseLong(updated);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        lastUpdateData.setText( format.format(calendar.getTime()));

    }

    private void initialization() {

        todayConfirm = findViewById(R.id.todayConfirm);
        todayRecovered = findViewById(R.id.todayRecovered);
        todayDeath = findViewById(R.id.todayDeath);
        totalConfirm = findViewById(R.id.totalConfirm);
        totalActive = findViewById(R.id.totalActive);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalDeath = findViewById(R.id.totalDeath);
        totalTest = findViewById(R.id.totalTest);
        pieChart = findViewById(R.id.piechart);
        lastUpdateData = findViewById(R.id.lastUpdateData);
        country = findViewById(R.id.country);

    }
}