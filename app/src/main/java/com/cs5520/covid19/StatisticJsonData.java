package com.cs5520.covid19;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * JSONArray objects which holds the up-to-date data from Covid data provider.
 * Can be accessed by any classes.
 * Should be initialized in HomePage.onCreate()
 */
public class StatisticJsonData {
    private static final String apiKey = "8ab097fc87734505af67bba31f012eb8";
    public static final String countyApiUrlString =
            "https://api.covidactnow.org/v2/counties.json?apiKey=" + apiKey;
    public static final String stateApiUrlString =
            "https://api.covidactnow.org/v2/states.json?apiKey=" + apiKey;
    public static final String vaccinationProviderApiUrlString =
            "https://data.cdc.gov/resource/5jp2-pgaw.json";

    // State-level Covid-19 statistic information JsonArray, initialized on HomePage creation.
    public static JSONArray stateJsonArray = null;
    // Array-level Covid-19 statistic information JsonArray, initialized on HomePage creation.
    public static JSONArray countyJsonArray = null;
    // Mapping relationships between state and county, initialized on HomePage creation.
    public static HashMap<String, ArrayList<String>> stateHasCountyMap = null;

    public static JSONArray providerLocation = null;

    /**
     * After having fetched the JsonArray from Covid-19 API,
     * Parse the mapping relationships between state and county from JsonArray.
     */
    public static void parseStateToCountyMap() {
        try {
            if (StatisticJsonData.countyJsonArray != null) {
                StatisticJsonData.stateHasCountyMap = new HashMap<String, ArrayList<String>>();
                for (int i = 0; i < StatisticJsonData.countyJsonArray.length(); i++) {
                    JSONObject obj = StatisticJsonData.countyJsonArray.getJSONObject(i);
                    String stateName = obj.getString("state");
                    String countyName = obj.getString("county");
                    if (StatisticJsonData.stateHasCountyMap.containsKey(stateName)) {
                        StatisticJsonData.stateHasCountyMap.get(stateName).add(countyName);
                    } else {
                        StatisticJsonData.stateHasCountyMap.put(stateName, new ArrayList<String>());
                        StatisticJsonData.stateHasCountyMap.get(stateName).add(countyName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
