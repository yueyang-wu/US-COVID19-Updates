package com.cs5520.covid19;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Initialize StatisticJsonData
        new requestJsonCovidStatistics().execute();
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
            JSONArray[] result = new JSONArray[]{null, null};
            try {
                stateJsonStringFromUrl = this.readUrl(StatisticJsonData.stateApiUrlString);
                countyJsonStringFromUrl = this.readUrl(StatisticJsonData.countyApiUrlString);
                result[0] = new JSONArray(stateJsonStringFromUrl);
                result[1] = new JSONArray(countyJsonStringFromUrl);
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
            if (jsonArrays[0] != null && jsonArrays[1] != null) {
                StatisticJsonData.stateJsonArray = jsonArrays[0];
                StatisticJsonData.countyJsonArray = jsonArrays[1];
                Toast.makeText(
                        getApplicationContext(),
                        getApplicationContext().getString(R.string.homepage_toast_update_success),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        getApplicationContext().getString(R.string.homepage_toast_update_failure),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}