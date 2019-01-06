package com.example.tommal.otibus.JSOUP;

public class SubitoSingleObject {
    private String title;
    private String url;
    private Float prezzo;
    private String timeStamp;

    public SubitoSingleObject(String title, String url, Float prezzo, String timeStamp) {
        this.title = title;
        this.url = url;
        this.prezzo = prezzo;
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Float getPrezzo() {
        return prezzo;
    }

    public String getTimeStamp(){return timeStamp; }

    @Override
    public String toString() {
        return "SubitoSingleObject{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", prezzo=" + prezzo +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
