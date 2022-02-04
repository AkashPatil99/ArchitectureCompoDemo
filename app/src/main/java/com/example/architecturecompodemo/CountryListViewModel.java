package com.example.architecturecompodemo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryListViewModel extends AndroidViewModel implements Response.Listener<String>,Response.ErrorListener {

    private static final String API_URL = "https://api.printful.com/countries";
    private static final String RESPONSE_ENTRY_KEY = "result";
    private static final String RESPONSE_COUNTRY_NAME_KEY = "name";
    private static final String RESPONSE_COUNTRY_CODE_KEY = "code";

    private RequestQueue queue;

    private MutableLiveData<List<com.example.architecturecompodemo.Country>> countryLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestStatus> requestStatusLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> backgroundColor = new MutableLiveData<>();
    private MutableLiveData<Integer> errorValue = new MutableLiveData<>();

    public enum RequestStatus {IN_PROGRESS, FAILED, SUCCEEDED}


    public CountryListViewModel(@NonNull Application application) {
        super(application);

        queue = Volley.newRequestQueue(application);
        requestStatusLiveData.postValue(RequestStatus.IN_PROGRESS);
        fetchCountries();

    }



    public LiveData<List<com.example.architecturecompodemo.Country>> getCountryLiveData() {
        return countryLiveData;
    }

    public LiveData<RequestStatus> getRequestStatusLiveData() {
        return requestStatusLiveData;
    }

    public LiveData<Integer> getBackground(){
        return backgroundColor;
    }

    public LiveData<Integer> getErrorValue() {
        return errorValue;
    }



    public void setErrorValue(int i) {
        errorValue.postValue(i);
    }

    public void onBackgroundChange(int position){
        backgroundColor.postValue(position);
    }

    private void fetchCountries() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL, this, this);
        queue.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        requestStatusLiveData.postValue(RequestStatus.FAILED);
    }

    @Override
    public void onResponse(String response) {
        try {
            List<com.example.architecturecompodemo.Country> countries = parseResponse(response);
            countryLiveData.postValue(countries);
            requestStatusLiveData.postValue(RequestStatus.SUCCEEDED);
        } catch (JSONException e) {
            e.printStackTrace();
            requestStatusLiveData.postValue(RequestStatus.FAILED);
        }
    }

    private List<com.example.architecturecompodemo.Country> parseResponse(String response) throws JSONException {

        List<com.example.architecturecompodemo.Country> models = new ArrayList<>();
        JSONObject res = new JSONObject(response);
        JSONArray entries = res.optJSONArray(RESPONSE_ENTRY_KEY);

        if (entries == null) {
            return models;
        }
        int count = 0;
        String name,code;

        while (count < entries.length()) {
            JSONObject movieJson = entries.getJSONObject(count);
            name = movieJson.getString(RESPONSE_COUNTRY_NAME_KEY);
            code = movieJson.getString(RESPONSE_COUNTRY_CODE_KEY);
            com.example.architecturecompodemo.Country country = new com.example.architecturecompodemo.Country(name, code);
            models.add(country);
            count++;
        }

        return models;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
