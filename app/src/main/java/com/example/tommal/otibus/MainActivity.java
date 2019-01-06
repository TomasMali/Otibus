package com.example.tommal.otibus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tommal.otibus.JSOUP.JsoupSubito;
import com.example.tommal.otibus.JSOUP.SubitoSingleObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener  {
    DatabaseHelper myDb;
    private static ListView list_view;
    public static List<String> stockList = new ArrayList<>();
    List<SubitoSingleObject> obj = new ArrayList<>();
   private static EditText searchEditText;
   public static boolean started = false;
  public static  List<String>  old = new ArrayList<>();
  public static String s="";

    JsoupSubito js = null;

  public static final String SHARED_PREFS = "SharedPrefs";
  public static final String FiRST_TIME = "FirstTime";
  public static final String COUNTER = "Counter";
  public static final String QUERY = "Query";

  public static final String TAG = "MainActivity";

// sono in Refactoring
    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       js =  new JsoupSubito(this);

        setContentView(R.layout.activity_main);
        AlertReciver.mainContext = MainActivity.this;
        TimePickerFragmen.mainContext = MainActivity.this;

        myDb = new DatabaseHelper(this);

        (findViewById(R.id.buttonPicker)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment df = new TimePickerFragmen();
                df.show(getSupportFragmentManager(), "time picker");
            }
        });

        (findViewById(R.id.button_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                started=false;
                cancelAlarm();
            }
        });

            searchEditText = ((EditText) findViewById(R.id.editTextSearch));

        print();
        buttonSearch();

          if(loadData()) {
              startButton(true);
              Log.i(TAG,"**********************************************************************");

              SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
              SharedPreferences.Editor editor = sharedPreferences.edit();
              editor.putInt(COUNTER,0);
              editor.apply();

              saveData();
          }
          else {
              Log.i(TAG,"#########################################################################");
              startButton(false);

          }

    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FiRST_TIME, false);

        int i = sharedPreferences.getInt(COUNTER,0);
        editor.putInt(COUNTER,(i+1));

        editor.apply();
    }

    public void incrementPlusOne(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int i = sharedPreferences.getInt(COUNTER,0);
        editor.putInt(COUNTER,(i+1));
        editor.apply();
    }

    public int getCount(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        return sharedPreferences.getInt(COUNTER,0);
    }

    public boolean loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        return sharedPreferences.getBoolean(FiRST_TIME,true);
    }

    public void saveQuery(String query){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();;
        editor.putString(QUERY,query);
        editor.apply();
    }

    public String getQuery(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        return sharedPreferences.getString(QUERY,"");
    }



    public List<SubitoSingleObject> callExternal(){

        js.setCosa(s.length()>4?s:"");
        List<SubitoSingleObject> allSubjects = js.getAllSubjects();
        return allSubjects;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void listView() { // arriva false from top

        boolean sonoEntrato = false;

            for (SubitoSingleObject s : obj) {
                boolean trovato = findIfExsist(s, myDb.getSelectedItems());
                if (!trovato) {
                    Log.i(TAG,"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    if (!sonoEntrato) {
                        myDb.deleteFromTABLE_NAME_NEW();
                        sonoEntrato = true;
                    }

                    myDb.insertDataItems(s);
                    myDb.insertDataItemsNew(s);
                }
            }

        List<SubitoSingleObject> vecchio =   myDb.getSelectedItems();
        List<SubitoSingleObject> nuovo =  myDb.getSelectedItemsNew();



            stockList.clear();
            for (SubitoSingleObject s : myDb.getSelectedItemsNew()){
                stockList.add(s.getTitle() + "   " + s.getPrezzo() + "€" + "   " + s.getTimeStamp());
            }

        Log.i(TAG,"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + stockList.size());
        String[] stockArr = new String[stockList.size()];
        stockArr = stockList.toArray(stockArr);

        list_view = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.name_list,stockArr);
        list_view.setAdapter(adapter);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubitoSingleObject subitoSingleObject = obj.get(position);
                showMessage(subitoSingleObject.getTitle(), "Pubblicato:  "+subitoSingleObject.getTimeStamp(), "Prezzo:  "+ subitoSingleObject.getPrezzo() +"€",subitoSingleObject.getUrl());
            }
        });
    }

    public boolean hasChanges(){
        updateObj();

        boolean aux = false;
        List<SubitoSingleObject> fromDbOld = myDb.getSelectedItems();

        for (SubitoSingleObject s : obj) {
            boolean trovato = findIfExsist(s, fromDbOld);
            if (!trovato) {
               return true;
            }
        }
        return aux;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    private void buttonSearch() {
        ((Button)findViewById(R.id.buttonSearch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuery(searchEditText.getText().toString());
                startButton(false);
            }
        });
    }





    public boolean findIfExsist(SubitoSingleObject s, List<SubitoSingleObject> old) {
        for (SubitoSingleObject su : old){
            if(su.getUrl().equals(s.getUrl()))
                return true;
        }
        return false;
    }


    public void showMessage(String title, String pubblicato, String prezzo, String url) {


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(true);
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.setTitle( pubblicato + "\n" + prezzo + "\n");
        alert.setMessage( Html.fromHtml(title + "\n"  +"Per vedere l'annuncio " + "  <a href='" +url + "'>Vai alla pagina</a>"));
        AlertDialog Alert1 = alert.create();
        Alert1 .show();
        ((TextView)Alert1.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

    }



    public void startButton(final boolean check) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        int SDK_INT = android.os.Build.VERSION.SDK_INT;
                        if (SDK_INT > 8)
                        {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }
                        if(!check) {
                            updateObj();
                        }

                        if(!check)
                            listView();
                    }
                });

/*
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
*/
            }
        }).start();
    }


    public void updateObj(){
        s = "?q=" + searchEditText.getText().toString() + "/";
        js.setCosa(s.length() > 4 ? s : "");
        obj = js.getAllSubjects();
    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);

        updateTimeText(c);
        startAlarm(c);

    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlertReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(MainActivity.this,"Notification Canceled",Toast.LENGTH_LONG).show();

    }
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlertReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);

        if(c.before(Calendar.getInstance()))
            c.add(Calendar.DATE,1);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),15*60*1000,pendingIntent);
    }




    private void updateTimeText(Calendar c) {

        String timeText = "Notification set for ";
        timeText+= DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        Toast.makeText(MainActivity.this,timeText,Toast.LENGTH_LONG).show();
    }


    private void print() {
        (findViewById(R.id.buttonShow)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if(res.getCount() == 0){
                    showMessage("Error", "No data were found!");
                    return;
                }
                else
                {
                    StringBuffer query = new StringBuffer();
                    while (res.moveToNext()){
                        query.append("TITOLO: " + res.getString(4) + "\n");
                        query.append("ID: " + res.getString(0) + "\n");
                        query.append("INSERIMENTO: " + res.getString(2) + "\n");
                        query.append("PREZZO: " + res.getString(3) + "\n");

                    }
                    showMessage("Data",query.toString());
                }


            }
        });

    }

    private void showMessage(String title   , String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(true);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.show();

    }



}
