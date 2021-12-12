package com.cs5520.covid19;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * This Interface is required to use Retrofit2 to fetch data from web
 */

public interface ApiInterface {
    String BASE_URL = "https://disease.sh/v3/covid-19/";
    //Get array of all countries
    @GET("countries")
    Call<List<ModelClass>> getCountryData();

}
