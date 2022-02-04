package com.example.architecturecompodemo;

public class Country {
    private String countryName;
    private String countryCode;

    public Country(String countryName, String countryCode){
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }
}
