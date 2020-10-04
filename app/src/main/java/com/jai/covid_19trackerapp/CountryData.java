package com.jai.covid_19trackerapp;

public class CountryData {

    String todayCases,totalCases,totalDeaths,totalRecovered,contName,flag;

    public CountryData(String todayCases, String totalCases, String totalDeaths, String totalRecovered,String contName,String flag) {
        this.todayCases = todayCases;
        this.totalCases = totalCases;
        this.totalDeaths = totalDeaths;
        this.totalRecovered = totalRecovered;
        this.contName=contName;
        this.flag=flag;

    }

    public String getFlag() {
        return flag;
    }

    public String getContName() {
        return contName;
    }



    public String getTodayCases() {
        return todayCases;
    }

    public String getTotalCases() {
        return totalCases;
    }

    public String getTotalDeaths() {
        return totalDeaths;
    }

    public String getTotalRecovered() {
        return totalRecovered;
    }
}
