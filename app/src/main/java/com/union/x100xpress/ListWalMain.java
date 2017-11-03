package com.union.x100xpress;

/**
 * Created by USG0011 on 06/05/17.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class ListWalMain extends AppCompatActivity {
    String details = null;
    ExpandableWalListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader; List<String> listDataHeader1;
    HashMap<String, List<String>> listDataChild; HashMap<String, List<String>> listDataChild1;
    String[] array;
    String[] data;
    String agentIdElement;
    String postedDateElement;
    String transDetailsElement;
    String debitElement;
    String creditElement;
    String balanceElement;
    String contraElement;
    String transNoElement;
    int ok;
    String subData;
    String subData1;
    String subData2;
    String subData3;
    String subData4;
    String subData5;
    String subData6;
    String subData7;
    String subData8;
    String subData9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_wal_main);
        setTitle("Wallet Transaction Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        details = getIntent().getExtras().getString("details").trim();
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.liWalExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableWalListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {

//        StringTokenizer tokenHead = new StringTokenizer(details, "|");
//        String tokenHeader;
//        int tokenHeadCount = tokenHead.countTokens();


         //Adding child data

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding child data
//        List<String> top250 = new ArrayList<String>();
        System.out.println("Do");
        int count = 0; int number = 0;
       data = details.split("\\|");
        int k=1;
      while(k<data.length){
          List<String> top250 = new ArrayList<String>();
          System.out.println(data[k]);
          String subData = data[k];
          System.out.println(subData);
          count = count + 1;


          StringTokenizer tokenSub = new StringTokenizer(subData, "~");
//            for (int j=0; j<=tokenSub.countTokens();j++) {


          agentIdElement = tokenSub.nextToken();
          System.out.println(agentIdElement);
          postedDateElement = tokenSub.nextToken();
          System.out.println(postedDateElement);
          transDetailsElement = tokenSub.nextToken();
          System.out.println(transDetailsElement);
          debitElement = tokenSub.nextToken();
          System.out.println(debitElement);
          creditElement = tokenSub.nextToken();
          balanceElement = tokenSub.nextToken();
          contraElement = tokenSub.nextToken();
          transNoElement = tokenSub.nextToken();



          // Adding Header data
          listDataHeader.add("#" + count + " " + transDetailsElement + " " + "-" + " " + postedDateElement);

          // Adding child data
          // List<String> top250 = new ArrayList<String>();
          top250.add("Balance: GHS " + balanceElement);
          top250.add("Debit Amount: GHS " + debitElement);
          top250.add("Credit Amount: GHS" + creditElement);
          top250.add("Contra Wallet: " + contraElement);
          top250.add("Posted Date: " + postedDateElement);
          top250.add("Trans Number: " + transNoElement);
          //top250.add("Agents ID: " + agentIdElement);

          // Header, Child data
          listDataChild.put(listDataHeader.get(number), top250);
            number++;
          k++;
      }
     //  System.out.println(Arrays.toString(data));
//        int length = data.length;
//        System.out.println(length);
//        StringTokenizer tokenHead = new StringTokenizer(details, "|");
//        System.out.println(tokenHead);


        //String tk = tokenHead.nextToken();
//


//        int tokenHeadCount = tokenHead.countTokens();
//        System.out.println(tokenHeadCount);

            //  for (int i=1;i <= length;i++){
            // subData = data[i];
//            System.out.println(subData);


//        for (int i=0;i<data.length;i++) {
////            String subData = tokenHead.nextToken().trim();
////            //System.out.println(subData.length());
////            System.out.println(subData);
//            String subData = data[i];
//            count = count + 1;
//
//
//            StringTokenizer tokenSub = new StringTokenizer(subData, "~");
////            for (int j=0; j<=tokenSub.countTokens();j++) {
//
//
//                agentIdElement = tokenSub.nextToken();
//                System.out.println(agentIdElement);
//                postedDateElement = tokenSub.nextToken();
//                System.out.println(postedDateElement);
//                transDetailsElement = tokenSub.nextToken();
//                System.out.println(transDetailsElement);
//                debitElement = tokenSub.nextToken();
//                System.out.println(debitElement);
//                creditElement = tokenSub.nextToken();
//                balanceElement = tokenSub.nextToken();
//                contraElement = tokenSub.nextToken();
//                transNoElement = tokenSub.nextToken();
//
//
//
//            // Adding Header data
//            listDataHeader.add("#" + count + " " + transDetailsElement + " " + "-" + " " + postedDateElement);
//
//            // Adding child data
//            // List<String> top250 = new ArrayList<String>();
//            top250.add("Balance: GHS " + balanceElement);
//            top250.add("Debit Amount: GHS " + debitElement);
//            top250.add("Credit Amount: GHS" + creditElement);
//            top250.add("Contra Wallet: " + contraElement);
//            top250.add("Posted Date: " + postedDateElement);
//            top250.add("Trans Number: " + transNoElement);
//            //top250.add("Agents ID: " + agentIdElement);
//
//            // Header, Child data
//            listDataChild.put(listDataHeader.get(i), top250);
//
//        }





//            subData4 = tokenHead.nextToken().trim();
//            subData5 = tokenHead.nextToken().trim();
//            subData6 = tokenHead.nextToken().trim();
//            subData7 = tokenHead.nextToken().trim();
//            subData8 = tokenHead.nextToken().trim();
//            subData9 = tokenHead.nextToken().trim();



//        //No.1
//        subData = tokenHead.nextToken().trim();
//        System.out.println("sub" + subData);
//        StringTokenizer tokenSub = new StringTokenizer(subData, "~");
//
//
//
//        //  StringTokenizer tokenSub = new StringTokenizer(subData, "~");
//
//        agentIdElement = tokenSub.nextToken().trim();
//        System.out.println(agentIdElement);
//        postedDateElement = tokenSub.nextToken().trim();
//        transDetailsElement = tokenSub.nextToken().trim();
//        debitElement = tokenSub.nextToken().trim();
//        creditElement = tokenSub.nextToken().trim();
//        balanceElement = tokenSub.nextToken().trim();
//        System.out.println(balanceElement);
//        contraElement = tokenSub.nextToken().trim();
//        transNoElement = tokenSub.nextToken().trim();
//
//
//        // Adding Header data
//       // listDataHeader.add(transDetailsElement + " " + "-" + " " + postedDateElement);
//        listDataHeader.add("#1" + " "  + transDetailsElement +" " + "-" + " " + postedDateElement);
//        // Adding child data
//        //List<String> top250 = new ArrayList<String>();
//
//        top250.add("Balance: GHS " + balanceElement);
//        top250.add("Debit Amount: GHS " + debitElement);
//        top250.add("Credit Amount: GHS" + creditElement);
//        //top250.add("Contra Wallet: " + contraElement);
//        top250.add("Posted Date: " + postedDateElement);
//        top250.add("Trans Number: " + transNoElement);
//        //top250.add("Agents ID: " + agentIdElement);
//
//        // Header, Child data
////        listDataChild.put(listDataHeader.get(0), top250);
//
//
////        //No.2
////        listDataHeader = new ArrayList<String>();
////        listDataChild= new HashMap<String, List<String>>();
//        subData1 = tokenHead.nextToken().trim();
//        System.out.println("sub1" +subData1);
//        StringTokenizer tokenSub1 = new StringTokenizer(subData1, "~");
////            for(int j=0;j<=8;j++)
//
//
//        //  StringTokenizer tokenSub = new StringTokenizer(subData, "~");
//
//    String    agentIdElement1 = tokenSub1.nextToken().trim();
//        System.out.println(agentIdElement1);
//     String   postedDateElement1 = tokenSub1.nextToken().trim();
//        String transDetailsElement1 = tokenSub1.nextToken().trim();
//        String debitElement1 = tokenSub1.nextToken().trim();
//        String creditElement1 = tokenSub1.nextToken().trim();
//        String balanceElement1 = tokenSub1.nextToken().trim();
//        System.out.println(balanceElement1);
//        String contraElement1 = tokenSub1.nextToken().trim();
//        String transNoElement1 = tokenSub1.nextToken().trim();
//
//
//        // Adding Header data
////        listDataHeader.add(transDetailsElement1 + " " + "-" + " " + postedDateElement1);
//        listDataHeader.add("#2" + " "  + transDetailsElement + " " + "-" + " " + postedDateElement1);
//
//        // Adding child data
//        List<String> top251 = new ArrayList<String>();
//
//        top251.add("Balance: GHS " + balanceElement1);
//        top251.add("Debit Amount: GHS " + debitElement1);
//        top251.add("Credit Amount: GHS" + creditElement1);
//        //top250.add("Contra Wallet: " + contraElement);
//        top251.add("Posted Date: " + postedDateElement1);
//        top251.add("Trans Number: " + transNoElement1);
//        //top250.add("Agents ID: " + agentIdElement);
//
//        // Header, Child data
//        listDataChild.put(listDataHeader.get(0), top250);
//        listDataChild.put(listDataHeader.get(1), top251);
//
//
//        //No.3
//
//        subData2 = tokenHead.nextToken().trim();
//        System.out.println(subData2);
//        StringTokenizer tokenSub2 = new StringTokenizer(subData2, "~");
////            for(int j=0;j<=8;j++)
//
//
//        //  StringTokenizer tokenSub = new StringTokenizer(subData, "~");
//
//       String agentIdElement2 = tokenSub2.nextToken().trim();
//       String postedDateElement2 = tokenSub2.nextToken().trim();
//       String transDetailsElement2 = tokenSub2.nextToken().trim();
//     String   debitElement2 = tokenSub2.nextToken().trim();
//       String creditElement2 = tokenSub2.nextToken().trim();
//       String balanceElement2 = tokenSub2.nextToken().trim();
//       String contraElement2 = tokenSub2.nextToken().trim();
//       String transNoElement2 = tokenSub2.nextToken().trim();
//
//
//        // Adding Header data
//        //listDataHeader.add(transDetailsElement2 + " " + "-" + " " + postedDateElement2);
//        listDataHeader.add("#3" + " "  + transDetailsElement + " " + "-" + " " + postedDateElement2);
//        // Adding child data
//        List<String> top252 = new ArrayList<String>();
//
//        top252.add("Balance: GHS " + balanceElement2);
//        top252.add("Debit Amount: GHS " + debitElement2);
//        top252.add("Credit Amount: GHS" + creditElement2);
//        //top250.add("Contra Wallet: " + contraElement);
//        top252.add("Posted Date: " + postedDateElement2);
//        top252.add("Trans Number: " + transNoElement2);
//        //top250.add("Agents ID: " + agentIdElement);
//
//        // Header, Child data
//        listDataChild.put(listDataHeader.get(2), top252);
////
////
//        //No.4
//        subData3 = tokenHead.nextToken().trim();
//        StringTokenizer tokenSub3 = new StringTokenizer(subData3, "~");
////            for(int j=0;j<=8;j++)
//
//
//        //  StringTokenizer tokenSub = new StringTokenizer(subData, "~");
//
//        agentIdElement = tokenSub3.nextToken().trim();
//        postedDateElement = tokenSub3.nextToken().trim();
//        transDetailsElement = tokenSub3.nextToken().trim();
//        debitElement = tokenSub3.nextToken().trim();
//        creditElement = tokenSub3.nextToken().trim();
//        balanceElement = tokenSub3.nextToken().trim();
//        contraElement = tokenSub3.nextToken().trim();
//        transNoElement = tokenSub3.nextToken().trim();
//
//
//        // Adding Header data
//        listDataHeader.add("#4" + " "  + transDetailsElement +" " + "-" + " " + postedDateElement);
//
//        // Adding child data
//        List<String> top253 = new ArrayList<String>();
//
//        top253.add("Balance: GHS " + balanceElement);
//        top253.add("Debit Amount: GHS " + debitElement);
//        top253.add("Credit Amount: GHS" + creditElement);
//        //top250.add("Contra Wallet: " + contraElement);
//        top253.add("Posted Date: " + postedDateElement);
//        top253.add("Trans Number: " + transNoElement);
//        //top250.add("Agents ID: " + agentIdElement);
//
//        // Header, Child data
//        listDataChild.put(listDataHeader.get(3), top253);
////
////
////        //No.5
//        subData4 = tokenHead.nextToken().trim();
//        StringTokenizer tokenSub4 = new StringTokenizer(subData4, "~");
////            for(int j=0;j<=8;j++)
//
//
//        //  StringTokenizer tokenSub = new StringTokenizer(subData, "~");
//
//        agentIdElement = tokenSub4.nextToken().trim();
//        postedDateElement = tokenSub4.nextToken().trim();
//        transDetailsElement = tokenSub4.nextToken().trim();
//        debitElement = tokenSub4.nextToken().trim();
//        creditElement = tokenSub4.nextToken().trim();
//        balanceElement = tokenSub4.nextToken().trim();
//        contraElement = tokenSub4.nextToken().trim();
//        transNoElement = tokenSub4.nextToken().trim();
//
//
//        // Adding Header data
//        listDataHeader.add("#5" + " "  + transDetailsElement + " " + "-" + " " + postedDateElement);
//
//        // Adding child data
//        List<String> top254 = new ArrayList<String>();
//
//        top254.add("Balance: GHS " + balanceElement);
//        top254.add("Debit Amount: GHS " + debitElement);
//        top254.add("Credit Amount: GHS" + creditElement);
//        //top250.add("Contra Wallet: " + contraElement);
//        top254.add("Posted Date: " + postedDateElement);
//        top254.add("Trans Number: " + transNoElement);
//        //top250.add("Agents ID: " + agentIdElement);
//
//        // Header, Child data
//        listDataChild.put(listDataHeader.get(4), top254);
//
//
////        //No.6
//        subData5 = tokenHead.nextToken().trim();
//        StringTokenizer tokenSub5 = new StringTokenizer(subData5, "~");
////            for(int j=0;j<=8;j++)
//
//
//        //  StringTokenizer tokenSub = new StringTokenizer(subData, "~");
//
//        agentIdElement = tokenSub5.nextToken().trim();
//        postedDateElement = tokenSub5.nextToken().trim();
//        transDetailsElement = tokenSub5.nextToken().trim();
//        debitElement = tokenSub5.nextToken().trim();
//        creditElement = tokenSub5.nextToken().trim();
//        balanceElement = tokenSub5.nextToken().trim();
//        contraElement = tokenSub5.nextToken().trim();
//        transNoElement = tokenSub5.nextToken().trim();
//
//
//        // Adding Header data
//        listDataHeader.add("#6" + " " + transDetailsElement + " " + "-" + " " + postedDateElement);
//
//        // Adding child data
//        List<String> top255 = new ArrayList<String>();
//
//        top255.add("Balance: GHS " + balanceElement);
//        top255.add("Debit Amount: GHS " + debitElement);
//        top255.add("Credit Amount: GHS" + creditElement);
//        //top250.add("Contra Wallet: " + contraElement);
//        top255.add("Posted Date: " + postedDateElement);
//        top255.add("Trans Number: " + transNoElement);
//        //top250.add("Agents ID: " + agentIdElement);
//
//        // Header, Child data
//        listDataChild.put(listDataHeader.get(5), top255);
//
////
////        //No.7
//        subData6 = tokenHead.nextToken().trim();
//        StringTokenizer tokenSub6 = new StringTokenizer(subData6, "~");
////            for(int j=0;j<=8;j++)
//
//
//        //  StringTokenizer tokenSub = new StringTokenizer(subData, "~");
//
//        agentIdElement = tokenSub6.nextToken().trim();
//        postedDateElement = tokenSub6.nextToken().trim();
//        transDetailsElement = tokenSub6.nextToken().trim();
//        debitElement = tokenSub6.nextToken().trim();
//        creditElement = tokenSub6.nextToken().trim();
//        balanceElement = tokenSub6.nextToken().trim();
//        contraElement = tokenSub6.nextToken().trim();
//        transNoElement = tokenSub6.nextToken().trim();
//
//
//        // Adding Header data
//        listDataHeader.add("#7" + " " + transDetailsElement + " " + "-" + " " + postedDateElement);
//
//        // Adding child data
//        List<String> top256 = new ArrayList<String>();
//
//        top256.add("Balance: GHS " + balanceElement);
//        top256.add("Debit Amount: GHS " + debitElement);
//        top256.add("Credit Amount: GHS" + creditElement);
//        //top250.add("Contra Wallet: " + contraElement);
//        top256.add("Posted Date: " + postedDateElement);
//        top256.add("Trans Number: " + transNoElement);
//        //top250.add("Agents ID: " + agentIdElement);
//
//        // Header, Child data
//        listDataChild.put(listDataHeader.get(6), top256);
//
////
////        //No.8
//        subData7 = tokenHead.nextToken().trim();
//        StringTokenizer tokenSub7 = new StringTokenizer(subData7, "~");
////            for(int j=0;j<=8;j++)
//
//
//        //  StringTokenizer tokenSub = new StringTokenizer(subData, "~");
//
//        agentIdElement = tokenSub7.nextToken().trim();
//        postedDateElement = tokenSub7.nextToken().trim();
//        transDetailsElement = tokenSub7.nextToken().trim();
//        debitElement = tokenSub7.nextToken().trim();
//        creditElement = tokenSub7.nextToken().trim();
//        balanceElement = tokenSub7.nextToken().trim();
//        contraElement = tokenSub7.nextToken().trim();
//        transNoElement = tokenSub7.nextToken().trim();
//
//
//        // Adding Header data
//        listDataHeader.add("#8" + " " + transDetailsElement + " " + "-" + " " + postedDateElement);
//
//        // Adding child data
//        List<String> top257 = new ArrayList<String>();
//
//        top257.add("Balance: GHS " + balanceElement);
//        top257.add("Debit Amount: GHS " + debitElement);
//        top257.add("Credit Amount: GHS" + creditElement);
//        //top250.add("Contra Wallet: " + contraElement);
//        top257.add("Posted Date: " + postedDateElement);
//        top257.add("Trans Number: " + transNoElement);
//        //top250.add("Agents ID: " + agentIdElement);
//
//        // Header, Child data
//        listDataChild.put(listDataHeader.get(7), top257);
//
//
////        //No.9
//        subData8 = tokenHead.nextToken().trim();
//        StringTokenizer tokenSub8 = new StringTokenizer(subData8, "~");
//
//        agentIdElement = tokenSub8.nextToken().trim();
//
//        postedDateElement = tokenSub8.nextToken().trim();
//
//        transDetailsElement = tokenSub8.nextToken().trim();
//
//        debitElement = tokenSub8.nextToken().trim();
//
//        creditElement = tokenSub8.nextToken().trim();
//        balanceElement = tokenSub8.nextToken().trim();
//
//        contraElement = tokenSub8.nextToken().trim();
//
//        transNoElement = tokenSub8.nextToken().trim();
//
//
//        // Adding Header data
//        listDataHeader.add("#9" + " " + transDetailsElement + " " + "-" + " " + postedDateElement);
//
//        // Adding child data
//        List<String> top258 = new ArrayList<String>();
//
//        top258.add("Balance: GHS " + balanceElement);
//        top258.add("Debit Amount: GHS " + debitElement);
//        top258.add("Credit Amount: GHS" + creditElement);
//        //top250.add("Contra Wallet: " + contraElement);
//        top258.add("Posted Date: " + postedDateElement);
//        top258.add("Trans Number: " + transNoElement);
//        //top250.add("Agents ID: " + agentIdElement);
//
//        // Header, Child data
//        listDataChild.put(listDataHeader.get(8), top258);
////
//////No.9
//        subData9 = tokenHead.nextToken().trim();
//        StringTokenizer tokenSub9 = new StringTokenizer(subData9, "~");
////            for(int j=0;j<=8;j++)
//
//
//        //  StringTokenizer tokenSub = new StringTokenizer(subData, "~");
//
//        agentIdElement = tokenSub9.nextToken().trim();
//
//        postedDateElement = tokenSub9.nextToken().trim();
//
//        transDetailsElement = tokenSub9.nextToken().trim();
//
//        debitElement = tokenSub9.nextToken().trim();
//
//        creditElement = tokenSub9.nextToken().trim();
//
//        balanceElement = tokenSub9.nextToken().trim();
//
//        contraElement = tokenSub9.nextToken().trim();
//
//        transNoElement = tokenSub9.nextToken().trim();
//
//
//        // Adding Header data
//        listDataHeader.add("#10" + " " + transDetailsElement + " " + "-" + " " + postedDateElement);
//
//        // Adding child data
//        List<String> top259 = new ArrayList<String>();
//
//        top259.add("Balance: GHS " + balanceElement);
//        top259.add("Debit Amount: GHS " + debitElement);
//        top259.add("Credit Amount: GHS" + creditElement);
//        //top250.add("Contra Wallet: " + contraElement);
//        top259.add("Posted Date: " + postedDateElement);
//        top259.add("Trans Number: " + transNoElement);
//        //top250.add("Agents ID: " + agentIdElement);
//
//        // Header, Child data
//        listDataChild.put(listDataHeader.get(9), top259);
    }








//        for (ok = 0; ok <= tokenHeadCount - 1; ok++) {
//            System.out.println("hello");
//            tokenHeader = tokenHead.nextToken().trim();
//            array = tokenHeader.split("~");
//            agentIdElement = array[0];
//            System.out.println("agent" + agentIdElement);
//            postedDateElement = array[1];
//            System.out.println("date" + postedDateElement);
//            transDetailsElement = array[2];
//            System.out.println("details" + transDetailsElement);
//            debitElement = array[3];
//            System.out.println("debit" + debitElement);
//            creditElement = array[4];
//            System.out.println("credit" + creditElement);
//            balanceElement = array[5];
//            System.out.println("bal" + balanceElement);
//            contraElement = array[6];
//            System.out.println("contra" + contraElement);
//            transNoElement = array[7];
//            System.out.println("trans:" + transNoElement);
//
//
//                // Adding Header data
//                listDataHeader.add(transDetailsElement + " " + "-" + " " + postedDateElement);
//
//
//
//
//                top250.add("Balance: GHS " + " " + balanceElement);
//                top250.add("Debit Amount: GHS " + " " + debitElement);
//                top250.add("Credit Amount: GHS" + " " + creditElement);
//                // top250.add("Contra Wallet: " + " " +contraElement);
//                top250.add("Posted Date: " + " " + postedDateElement);
//                top250.add("Trans Number: " + " " + transNoElement);
//                //top250.add("Agents ID: " + " " + agentIdElement);
//                listDataChild.put(listDataHeader.get(ok), top250); // Header, Child data
//
//        }




//            listDataHeader.add("Now Showing");
//            listDataHeader.add("Coming Soon..");
//
//        // Adding child data
//        List<String> top250 = new ArrayList<String>();
//        top250.add("The Shawshank Redemption");
//        top250.add("The Godfather");
//        top250.add("The Godfather: Part II");
//        top250.add("Pulp Fiction");
//        top250.add("The Good, the Bad and the Ugly");
//        top250.add("The Dark Knight");
//        top250.add("12 Angry Men");
//
//        List<String> nowShowing = new ArrayList<String>();
//        nowShowing.add("The Conjuring");
//        nowShowing.add("Despicable Me 2");
//        nowShowing.add("Turbo");
//        nowShowing.add("Grown Ups 2");
//        nowShowing.add("Red 2");
//        nowShowing.add("The Wolverine");
//
//        List<String> comingSoon = new ArrayList<String>();
//        comingSoon.add("2 Guns");
//        comingSoon.add("The Smurfs 2");
//        comingSoon.add("The Spectacular Now");
//        comingSoon.add("The Canyons");
//        comingSoon.add("Europa Report");
//
//        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), nowShowing);
//        listDataChild.put(listDataHeader.get(2), comingSoon);



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                ListWalMain.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
