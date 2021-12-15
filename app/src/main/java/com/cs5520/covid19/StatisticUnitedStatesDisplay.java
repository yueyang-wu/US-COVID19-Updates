package com.cs5520.covid19;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class StatisticUnitedStatesDisplay extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String[] stateArray = StatisticJsonData.stateHasCountyMap.keySet().toArray(new String[0]);
    TextView stateTotalCasesText, stateTotalDeathsText, stateNewCasesText, stateNewDeathsText;
    TextView countyTotalCasesText, countyTotalDeathsText, countyNewCasesText, countyNewDeathsText;
    TextView stateRiskLevelText, countyRiskLevelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unitedstates_statistic);

        stateTotalCasesText = findViewById(R.id.state_total_cases);
        stateTotalDeathsText = findViewById(R.id.state_total_deaths);
        stateNewCasesText = findViewById(R.id.state_new_cases);
        stateNewDeathsText = findViewById(R.id.state_new_deaths);
        countyTotalCasesText = findViewById(R.id.county_total_cases);
        countyTotalDeathsText = findViewById(R.id.county_total_deaths);
        countyNewCasesText = findViewById(R.id.county_new_cases);
        countyNewDeathsText = findViewById(R.id.county_new_deaths);
        stateRiskLevelText = findViewById(R.id.state_risk_level);
        countyRiskLevelText = findViewById(R.id.county_risk_level);

        Spinner stateSpinner = (Spinner) findViewById(R.id.state_spinner);
        stateSpinner.setOnItemSelectedListener(this);
        Arrays.sort(stateArray);
        ArrayAdapter stateArrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, stateArray);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateArrayAdapter);

        Spinner countySpinner = (Spinner) findViewById(R.id.county_spinner);
        countySpinner.setOnItemSelectedListener(this);
        String[] emptyArray = {};
        ArrayAdapter countyArrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, emptyArray);
        countyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countySpinner.setAdapter(countyArrayAdapter);

        Button btn_back = (Button) findViewById(R.id.btn_us_statistic_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatisticUnitedStatesDisplay.this, HomePage.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.state_spinner:
                String selectedState = parent.getItemAtPosition(position).toString();
                ArrayList countyArrayList = StatisticJsonData.stateHasCountyMap.get(selectedState);
                JSONObject stateJson = null;
                int stateCases = 0;
                int stateDeaths = 0;
                int stateNewCases = 0;
                int stateNewDeaths = 0;
                int stateRiskLevel = 0;

                // Get state Json data
                try {
                    for (int i = 0; i < StatisticJsonData.stateJsonArray.length(); i++) {
                        JSONObject obj = StatisticJsonData.stateJsonArray.getJSONObject(i);
                        String jsonStateName = obj.getString("state");
                        if (jsonStateName.equals(selectedState)) {
                            stateJson = obj;
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Parse state statistic data
                if (stateJson != null) {
                    try {
                        JSONObject statisticJsonObj = stateJson.getJSONObject("actuals");
                        stateCases = statisticJsonObj.getInt("cases");
                        stateDeaths = statisticJsonObj.getInt("deaths");
                        stateNewCases = statisticJsonObj.getInt("newCases");
                        stateNewDeaths = statisticJsonObj.getInt("newDeaths");
                        JSONObject riskLevelJsonObj = stateJson.getJSONObject("riskLevels");
                        stateRiskLevel = riskLevelJsonObj.getInt("overall");

                        stateTotalCasesText.setText("Total Cases: " + stateCases);
                        stateTotalDeathsText.setText("Total Deaths: " + stateDeaths);
                        stateNewCasesText.setText("New Cases: " + stateNewCases);
                        stateNewDeathsText.setText("New Deaths: " + stateNewDeaths);
                        stateRiskLevelText.setText("Risk Level (0 - 5): " + stateRiskLevel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Update the county spinner based on selected state
                Object[] counties = countyArrayList.toArray();
                String[] countyStrings = new String[counties.length];
                for (int i = 0; i < counties.length; i++)
                    countyStrings[i] = String.valueOf(counties[i]);

                this.updateCountySpinner(countyStrings);

                break;

            case R.id.county_spinner:
                String selectedCounty = parent.getItemAtPosition(position).toString();
                JSONObject countyJson = null;
                int countyCases = 0;
                int countyDeaths = 0;
                int countyNewCases = 0;
                int countyNewDeaths = 0;
                int countyRiskLevel = 0;

                // Get county Json data
                try {
                    for (int i = 0; i < StatisticJsonData.countyJsonArray.length(); i++) {
                        JSONObject obj = StatisticJsonData.countyJsonArray.getJSONObject(i);
                        String jsonCountyName = obj.getString("county");
                        if (jsonCountyName.equals(selectedCounty)) {
                            countyJson = obj;
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Parse county statistic data
                if (countyJson != null) {
                    try {
                        JSONObject statisticJsonObj = countyJson.getJSONObject("actuals");
                        countyCases = statisticJsonObj.getInt("cases");
                        countyDeaths = statisticJsonObj.getInt("deaths");
                        countyNewCases = statisticJsonObj.getInt("newCases");
                        countyNewDeaths = statisticJsonObj.getInt("newDeaths");
                        JSONObject riskLevelJsonObj = countyJson.getJSONObject("riskLevels");
                        countyRiskLevel = riskLevelJsonObj.getInt("overall");

                        countyTotalCasesText.setText("Total Cases: " + countyCases);
                        countyTotalDeathsText.setText("Total Deaths: " + countyDeaths);
                        countyNewCasesText.setText("New Cases: " + countyNewCases);
                        countyNewDeathsText.setText("New Deaths: " + countyNewDeaths);
                        countyRiskLevelText.setText("Risk Level (0 - 5): " + countyRiskLevel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateCountySpinner(String[] counties) {
        Spinner countySpinner = (Spinner) findViewById(R.id.county_spinner);
        Arrays.sort(counties);
        ArrayAdapter countyArrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, counties);
        countyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countySpinner.setAdapter(countyArrayAdapter);
    }

    private void updateStatisticInfo(String state, String county) {

    }
}

