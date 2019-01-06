package com.example.tommal.otibus.JSOUP;

public class Configuration {
    String cosa;
    String prezzoMax;

    public Configuration(String cosa, String prezzoMax) {
        this.cosa = cosa;
        this.prezzoMax = prezzoMax;
    }

    public String getCosa() {
        return cosa;
    }

    public void setCosa(String cosa) {
        this.cosa = cosa;
    }

    public String getPrezzoMax() {
        return prezzoMax;
    }

    public void setPrezzoMax(String prezzoMax) {
        this.prezzoMax = prezzoMax;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "cosa='" + cosa + '\'' +
                ", prezzoMax='" + prezzoMax + '\'' +
                '}';
    }
}
