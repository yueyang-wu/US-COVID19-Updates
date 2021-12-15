package com.cs5520.covid19;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class HomePage extends AppCompatActivity {
    Button btn_vaccinationFinder;
    Button button_us_statistic;
    private Button btn_statisticDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // check and ask for user permission to share their current location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // Initialize StatisticJsonData
        if (StatisticJsonData.stateJsonArray == null || StatisticJsonData.countyJsonArray == null)
            new requestJsonCovidStatistics().execute();

        // set vaccination_finder button to open VaccinationFinder activity
        btn_vaccinationFinder = (Button) findViewById(R.id.vaccination_finder);
        btn_vaccinationFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StatisticJsonData.providerLocation != null)
                    startActivity(new Intent(HomePage.this, VaccinationFinder.class));
                else
                    Toast.makeText(
                            getApplicationContext(),
                            "Provider data not prepared! Please wait!",
                            Toast.LENGTH_SHORT).show();
            }
        });
        // set Statistics button on homepage to direct to information display
        btn_statisticDisplay = (Button) findViewById(R.id.homepage_seeStatistics);
        btn_statisticDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStatisticDisplay();
            }
        });
        // set Statistics_UnitedStates button to open activity
        button_us_statistic = (Button) findViewById(R.id.btn_united_states_statistic);
        button_us_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StatisticJsonData.stateJsonArray != null && StatisticJsonData.countyJsonArray != null)
                    startActivity(new Intent(getApplicationContext(), StatisticUnitedStatesDisplay.class));
                else
                    Toast.makeText(
                            getApplicationContext(),
                            "API data not prepared! Please wait!",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openStatisticDisplay() {
        Intent intent = new Intent(this, StatisticDisplay.class);
        startActivity(intent);
    }

    private class requestJsonCovidStatistics extends AsyncTask<String, String, JSONArray[]> {
        /**
         * Given a Url, get the content from the Url as String.
         *
         * @param urlString the given Url
         * @return the String content get from the Url
         * @throws Exception general exceptions
         */
        private String readUrl(String urlString) throws Exception {
            BufferedReader reader = null;
            try {
                URL url = new URL(urlString);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuffer buffer = new StringBuffer();
                int read;
                char[] chars = new char[1024];
                while ((read = reader.read(chars)) != -1)
                    buffer.append(chars, 0, read);
                return buffer.toString();
            } finally {
                if (reader != null)
                    reader.close();
            }
        }

        /**
         * Get state and county JSONArrays from API Urls.
         *
         * @param strings don't need to be given
         * @return acquired JSONArrays, length expected to be 2, state and county.
         */
        @Override
        protected JSONArray[] doInBackground(String... strings) {
            String stateJsonStringFromUrl = "";
            String countyJsonStringFromUrl = "";
            String vaccinationProviderJsonStringFromUrl = "";
            JSONArray[] result = new JSONArray[]{null, null, null};
            try {
                stateJsonStringFromUrl = this.readUrl(StatisticJsonData.stateApiUrlString);
                countyJsonStringFromUrl = this.readUrl(StatisticJsonData.countyApiUrlString);
                vaccinationProviderJsonStringFromUrl = this.readUrl(StatisticJsonData.vaccinationProviderApiUrlString);
                result[0] = new JSONArray(stateJsonStringFromUrl);
                result[1] = new JSONArray(countyJsonStringFromUrl);
                result[2] = new JSONArray(vaccinationProviderJsonStringFromUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        /**
         * Pop up a toast to tell the user whether today's data is successfully acquired.
         *
         * @param jsonArrays The acquired JsonArray data of state-level and county-level
         */
        @Override
        protected void onPostExecute(JSONArray[] jsonArrays) {
            // validate if covid data acquired successfully
            if (jsonArrays[0] != null && jsonArrays[1] != null) {
                StatisticJsonData.stateJsonArray = jsonArrays[0];
                StatisticJsonData.countyJsonArray = jsonArrays[1];
                StatisticJsonData.parseStateToCountyMap();
                Toast.makeText(
                        getApplicationContext(),
                        getApplicationContext().getString(R.string.homepage_covid_info_update_success),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        getApplicationContext().getString(R.string.homepage_covid_info_update_failure),
                        Toast.LENGTH_SHORT).show();
            }

            // validate if vaccination provider data acquired successfully
            if (jsonArrays[2] != null) {
                StatisticJsonData.providerLocation = jsonArrays[2];
                Toast.makeText(
                        getApplicationContext(),
                        getApplicationContext().getString(R.string.homepage_vaccination_info_update_success),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        getApplicationContext().getString(R.string.homepage_vaccination_info_update_failure),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}