package com.cs5520.covid19;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;

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

    public static JSONArray stateJsonArray = null;
    public static JSONArray countyJsonArray = null;

}
