package com.example.tommal.otibus.JSOUP;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.tommal.otibus.DatabaseHelper;
import com.example.tommal.otibus.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tommal.otibus.MainActivity.FiRST_TIME;

public class JsoupSubito {
   static String cosa ;
public static int conta = 0;
    public static final String SHARED_PREFS = "SharedPrefs";
    public static final String COUNTER = "Counter";
    public static final String TAG = "Jsoup";

    public static Context mainContext;

    public  void setCosa(String cosa){
        this.cosa = cosa;
    }

   public  JsoupSubito(Context mainContext){
        JsoupSubito.mainContext =  mainContext;
    }
    public JsoupSubito(){

    }

    public  List<SubitoSingleObject> getAllSubjects() {
     final   List<SubitoSingleObject> subitoSingleObjects = new ArrayList<>();
        Elements li = null ;
        try {
            String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";
            Document doc = Jsoup.connect("https://www.subito.it/annunci-veneto/affitto/appartamenti/padova/"+cosa).userAgent(userAgent).get();
            li = doc.select("ul.items_listing li article.item_list.view_listing");
            for (int i = 0; i < li.size(); i++){
                String titolo = li.get(i).select("div.item_list_section.item_description h2 a").first().text();
                String prezzoString = li.get(i).select("div.item_list_section.item_description span.item_price").first() != null ? li.get(i).select("div.item_list_section.item_description span.item_price").first().text().substring(0, li.get(i).select("div.item_list_section.item_description span.item_price").first().text().length() - 1).trim() : "";
                Float prezzo = li.get(i).select("div.item_list_section.item_description span.item_price").first() != null ? Float.parseFloat(prezzoString): 5.5F;
                String timeStamp = li.get(i).select("div.item_list_section.item_description span.item_info time").first().text();
                String link = li.get(i).select("div.item_list_section.item_image a").first().attr("href");
             subitoSingleObjects.add(new SubitoSingleObject(titolo,link,prezzo,timeStamp));
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
        /*
        if(((MainActivity) mainContext).getCount() == 2)
        subitoSingleObjects.add(new SubitoSingleObject("tomi","www.google.it",42F,"oggi"));
        if(((MainActivity)mainContext).getCount() == 3){
            subitoSingleObjects.add(new SubitoSingleObject("tomi1","www.google.it1",42F,"oggi1"));
            subitoSingleObjects.add(new SubitoSingleObject("tomi2","www.google.it2",42F,"oggi2"));
        }
        Log.i(TAG, "############################################      " +String.valueOf(((MainActivity)mainContext).getCount()));
        ((MainActivity)mainContext).incrementPlusOne();

        */
        return subitoSingleObjects;
    }




}
