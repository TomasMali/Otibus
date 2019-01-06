package com.example.tommal.otibus;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import com.example.tommal.otibus.JSOUP.JsoupSubito;
import com.example.tommal.otibus.JSOUP.SubitoSingleObject;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tommal.otibus.MainActivity.s;

public class AlertReciver extends BroadcastReceiver {
    public static MainActivity mainContext;
    DatabaseHelper myDb;


    @Override
    public void onReceive(Context context, Intent intent) {

        // qui fare il controllo per sabato o domenica
        myDb = new DatabaseHelper(context);


        Calendar cal = Calendar.getInstance();

      //  ((MainActivity)deviceInfo.applicationContext).startButton();
       // mainContext.showMessage("prova", "prova", "ciao" , "https://www.google.it/");

        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFS,MODE_PRIVATE);
        String query = sharedPreferences.getString(MainActivity.QUERY,"");
        JsoupSubito js = new JsoupSubito();
        s = "?q=" + query + "/";
        js.setCosa(s.length() > 4 ? s : "");
        List<SubitoSingleObject> obj = js.getAllSubjects();

        boolean aux = false;
        List<SubitoSingleObject> fromDbOld = myDb.getSelectedItems();

        for (SubitoSingleObject s : obj) {
            boolean trovato = findIfExsist(s, fromDbOld);
            if (!trovato) {
                aux = true;
            }
        }
               if(aux) {
                   NotificationHelper notificationHelper = new NotificationHelper(context);
                   NotificationCompat.Builder nb = notificationHelper.getChannel1Notification("Annuncio ", "Trovato nuovo annuncio");
                   notificationHelper.getmManager().notify(1, nb.build());
              }

    }
    public boolean findIfExsist(SubitoSingleObject s, List<SubitoSingleObject> old) {
        for (SubitoSingleObject su : old){
            if(su.getUrl().equals(s.getUrl()))
                return true;
        }
        return false;
    }



}
