package com.cs5520.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticDisplay extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button btn_back;
    CountryCodePicker countryCodePicker;
    TextView todayCases, cases, active, recovered, todayRecovered, deaths, todayDeaths;

    String country;
    TextView mFilter; //used in recyclerView
    Spinner spinner;
    String[] types = {"Cases", "Deaths", "Recovered", "Active"};
    //One list fo recycleView and one for normal data
    private List<ModelClass> modelClassList;
    private List<ModelClass> modelClassList2;

    PieChart mPieChart;
    RecyclerView recyclerView;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_display);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomePage();
            }
        });

        countryCodePicker= findViewById(R.id.ccp);
        active = findViewById(R.id.totalactive);
        deaths = findViewById(R.id.totaldeaths);
        todayDeaths = findViewById(R.id.todaydeaths);
        recovered = findViewById(R.id.totalrecovered);
        todayRecovered = findViewById(R.id.todayrecovered);
        cases = findViewById(R.id.totalcases);
        todayCases = findViewById(R.id.todaytotal);

        mPieChart =findViewById(R.id.piechart);
        spinner = findViewById(R.id.spinner);
        mFilter = findViewById(R.id.filter);
        recyclerView=findViewById(R.id.recyclerview);

        modelClassList=new ArrayList<>();
        modelClassList2 = new ArrayList<>();

        spinner.setOnItemSelectedListener(this);//parse 'this' content
        ArrayAdapter arrayAdapter;
        arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<ModelClass>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<ModelClass>> call, @NonNull Response<List<ModelClass>> response) {
                modelClassList2.addAll(Objects.requireNonNull(response.body())); //add all data to recycler view
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelClass>> call, Throwable t) {
                Log.e("StatisticDisplay", "Something wrong at Spinner " + t.getMessage());
                Toast.makeText(StatisticDisplay.this, "Something wrong when at spinner", Toast.LENGTH_SHORT).show();
            }
        });

        //RecyclerView
        adapter = new Adapter(getApplicationContext(),modelClassList2);
        recyclerView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        //code picker
        countryCodePicker.setAutoDetectedCountry(true);
        country=countryCodePicker.getSelectedCountryName();

        //if user change the country, this function will be called.
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country = countryCodePicker.getSelectedCountryName();
                fetchData();
            }
        });

        fetchData();
    }

    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    /**
     * Method to get data from online API
     */
    private void fetchData() {
        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelClass>> call, @NonNull Response<List<ModelClass>> response) {
                assert response.body() != null;
                modelClassList.addAll(response.body());
                for(int i = 0; i < modelClassList.size(); i++){
                    if(modelClassList.get(i).getCountry().equals(country)){
                        active.setText((modelClassList.get(i).getActive()));
                        todayDeaths.setText((modelClassList.get(i).getTodayDeaths()));
                        todayRecovered.setText((modelClassList.get(i).getTodayRecovered()));
                        todayCases.setText((modelClassList.get(i).getTodayCases()));
                        cases.setText((modelClassList.get(i).getCases()));
                        deaths.setText((modelClassList.get(i).getDeaths()));
                        recovered.setText((modelClassList.get(i).getRecovered()));

                        int active, total, recovered, deaths;

                        active=Integer.parseInt(modelClassList.get(i).getActive());
                        total = Integer.parseInt(modelClassList.get(i).getTodayCases());
                        recovered = Integer.parseInt(modelClassList.get(i).getRecovered());
                        deaths = Integer.parseInt(modelClassList.get(i).getDeaths());

                        updateGraph(active, total, recovered, deaths);

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelClass>> call, @NonNull Throwable t) {
                Log.e("MainActivity", "Something wrong at FetchData" + t.getMessage());
                Toast.makeText(StatisticDisplay.this, "Somthing wrong when at fetchData", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void updateGraph(int active, int total, int recovered, int deaths) {
        mPieChart.clearChart();
        mPieChart.addPieSlice(new PieModel("Confirm", total, Color.parseColor("#FFB701")));
        mPieChart.addPieSlice(new PieModel("Active", active, Color.parseColor("#FF4CAF50")));
        mPieChart.addPieSlice(new PieModel("Recovered", recovered, Color.parseColor("#38ACCD")));
        mPieChart.addPieSlice(new PieModel("Deaths", deaths, Color.parseColor("#F55c47")));
        mPieChart.startAnimation();

    }

    /**
     * This method is used to send selection to adapter
     * @param parent
     * @param view
     * @param position, pass the index of choices in types[] = {"cases, deaths, recovered, active"};
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = types[position];
        mFilter.setText(item); //show filter with selected item
        adapter.filter(item); //pass the item to be filtered in the adapter
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}