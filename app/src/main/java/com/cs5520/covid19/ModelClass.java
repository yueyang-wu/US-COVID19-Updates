package com.cs5520.covid19;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class is for data to fetch from the API. The vars should be the same as in API.
 */
public class ModelClass {
    //values want to fetch
//    @SerializedName("cases")
//    @Expose
//    String cases;
//
//    @SerializedName("todayCases")
//    @Expose
//    String todayCases;
//
//    @SerializedName("deaths")
//    @Expose
//    String deaths;
//
//    @SerializedName("todayDeaths")
//    @Expose
//    String todayDeaths;
//
//    @SerializedName("recovered")
//    @Expose
//    String recovered;
//
//    @SerializedName("todayRecovered")
//    @Expose
//    String todayRecovered;
//
//    @SerializedName("active")
//    @Expose
//    String active;
//
//    @SerializedName("country")
//    @Expose
//    String country;

    String cases, todayCases, deaths, todayDeaths,recovered, todayRecovered, active, country;

    public ModelClass(String cases, String todayCases, String deaths, String todayDeaths, String recovered, String todayRecovered, String active, String country) {
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.recovered = recovered;
        this.todayRecovered = todayRecovered;
        this.active = active;
        this.country = country;
    }

    public String getCases() {
        return cases;
    }

    public String getTodayCases() {
        return todayCases;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getTodayDeaths() {
        return todayDeaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getTodayRecovered() {
        return todayRecovered;
    }

    public String getActive() {
        return active;
    }

    public String getCountry() {
        return country;
    }


}
