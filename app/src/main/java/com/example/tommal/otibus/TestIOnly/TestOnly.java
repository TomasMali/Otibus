package com.example.tommal.otibus.TestIOnly;


import com.example.tommal.otibus.DatabaseHelper;
import com.example.tommal.otibus.JSOUP.JsoupSubito;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class TestOnly {
    
    public static void main(String [] a){


       // System.out.println(JsoupSubito.getAllSubjects());

        Collection listOne = new ArrayList(Arrays.asList("milan","dingo", "elpha", "hafil", "meat", "iga", "peeta"));
        Collection listTwo = new ArrayList(Arrays.asList("milan","dingo", "elpha", "hafil", "meat", "iga"));

        listOne.retainAll( listTwo );
        System.out.println( listOne );
    }


}
