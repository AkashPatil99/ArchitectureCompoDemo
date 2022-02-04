package com.example.architecturecompodemo;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CountDownViewModel extends AndroidViewModel {

//    private MutableLiveData<String> timeLiveData = new MutableLiveData<String>("00:05:00");
    private CountDownTimer countDownTimer;
    private MediatorLiveData<String> timeLiveData = new MediatorLiveData<String>();
    public MutableLiveData<String> liveData = new  MutableLiveData<String>("00:05:00");


    public CountDownViewModel(@NonNull Application application) {
        super(application);
        timeLiveData.addSource(liveData, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                timeLiveData.setValue(s);
            }
        });
    }


    public void startCountDown() {
        setTime("start");
    }

    private void setTime(String status) {
        if((countDownTimer == null) && status.equals("start")){                 //set new timer when click on START
            countDownTimer = new CountDownTimer(300000, 1000) {
                public void onTick(long millisUntilFinished) {
                    NumberFormat numberFormat = new DecimalFormat("00");
                    long hour = (millisUntilFinished / 3600000) % 24;
                    long min = (millisUntilFinished / 60000) % 60;
                    long sec = (millisUntilFinished / 1000) % 60;
                    liveData.postValue(numberFormat.format(hour) + ":" + numberFormat.format(min) + ":" + numberFormat.format(sec));
                }

                public void onFinish() {
                    timeLiveData.postValue("00:00:00");
                }
            }.start();
        }
        else if((countDownTimer != null) && status.equals("reset")){            //cancel the previous timer and set new timer when click on RESET
            countDownTimer.cancel();

            countDownTimer = new CountDownTimer(300000, 1000) {
                public void onTick(long millisUntilFinished) {
                    NumberFormat numberFormat = new DecimalFormat("00");
                    long hour = (millisUntilFinished / 3600000) % 24;
                    long min = (millisUntilFinished / 60000) % 60;
                    long sec = (millisUntilFinished / 1000) % 60;
                    timeLiveData.postValue(numberFormat.format(hour) + ":" + numberFormat.format(min) + ":" + numberFormat.format(sec));
                }

                public void onFinish() {
                    timeLiveData.postValue("00:00:00");
                }
            }.start();
        }
        else {
            //Nothing
        }
    }

    public void resetCountDown() {
        setTime("reset");
    }

    public LiveData<String> getTimeLiveData() {
        return timeLiveData;
    }

}
