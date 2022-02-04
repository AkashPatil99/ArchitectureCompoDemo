package com.example.architecturecompodemo;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MainActivity extends AppCompatActivity{

    private ProgressDialog loadingDialog;
    private CountryListViewModel countryListViewModel;
    private CountDownViewModel countDownViewModel;
    private LinearLayout linearLayout;
    private Button startCountDown,resetCountDown;
    private TextView clockView;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.linearLayout);
        startCountDown = findViewById(R.id.startBtn);
        resetCountDown = findViewById(R.id.resetBtn);
        clockView = findViewById(R.id.clockView);

        ViewModelProvider viewModelProvider = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        countryListViewModel = viewModelProvider.get(CountryListViewModel.class);
        countDownViewModel = viewModelProvider.get(CountDownViewModel.class);

        setUpLiveData();

        startCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownViewModel.startCountDown();
            }
        });

        resetCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownViewModel.resetCountDown();
            }
        });

    }

    private void setUpLiveData() {
        countryListViewModel.getCountryLiveData().observe(this, new Observer<List<Country>>() {
            @Override
            public void onChanged(List<com.example.architecturecompodemo.Country> countries) {
                handleCountryList(countries);
            }
        });
        countryListViewModel.getRequestStatusLiveData().observe(this, new Observer<CountryListViewModel.RequestStatus>() {
            @Override
            public void onChanged(CountryListViewModel.RequestStatus requestStatus) {
                handleRequestStatus(requestStatus);
            }
        });

        countryListViewModel.getBackground().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                updateBackground(integer);
            }
        });

        countryListViewModel.getErrorValue().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                i = integer;
            }
        });

        countDownViewModel.getTimeLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String time) {
                clockView.setText(time);
            }
        });
    }

    private void updateBackground(Integer integer) {
        switch (integer%5){
            case 1:
                linearLayout.setBackgroundColor(Color.parseColor("#FF2196F3"));
                break;
            case 2:
                linearLayout.setBackgroundColor(Color.parseColor("#FFE91E63"));
                break;
            case 3:
                linearLayout.setBackgroundColor(Color.parseColor("#FFCDDC39"));
                break;
            case 0:
                linearLayout.setBackgroundColor(Color.parseColor("#FF009688"));
                break;
            default:
                linearLayout.setBackgroundColor(Color.parseColor("#FFFF5722"));
        }
    }

    private void handleRequestStatus(CountryListViewModel.RequestStatus requestStatus) {
        switch (requestStatus) {
            case IN_PROGRESS:
                showSpinner();
                break;
            case SUCCEEDED:
                hideSpinner();
                break;
            case FAILED:
                showError();
                break;
        }
    }

    private void showError() {
        if(i == 0){
            hideSpinner();
            Toast.makeText(this, "Countries list is unavailable", Toast.LENGTH_LONG).show();
        }
        countryListViewModel.setErrorValue(5);
    }

    private void hideSpinner() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void showSpinner() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingDialog.setTitle("Fetching countries");
            loadingDialog.setMessage("Please wait...");
            loadingDialog.setIndeterminate(true);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        loadingDialog.show();
    }

    private void handleCountryList(List<com.example.architecturecompodemo.Country> countryList) {
        CountryAdapter adapter = new CountryAdapter(countryList, this);
        RecyclerView recyclerView = findViewById(R.id.recyclerLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void checkBackground(int position){
        countryListViewModel.onBackgroundChange(position);
    }
}