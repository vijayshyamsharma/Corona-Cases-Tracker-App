package com.jai.covid_19trackerapp;

public class StateData {

    String stateName,recovered,deaths,active,confirmed;

    public StateData(String stateName, String recovered, String deaths, String active, String confirmed) {
        this.stateName = stateName;
        this.recovered = recovered;
        this.deaths = deaths;
        this.active = active;
        this.confirmed = confirmed;
    }

    public String getStateName() {
        return stateName;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getActive() {
        return active;
    }

    public String getConfirmed() {
        return confirmed;
    }
}
