package com.union.x100xpress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.R.layout.simple_list_item_1;

/**
 * Created by USG0011 on 05/22/17.
 */

public class WalletMainMenu  extends AppCompatActivity {

        ListView listView ;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.wal_menu_list);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Wallet Menu List");
            // Get ListView object from xml
            listView = (ListView) findViewById(R.id.wal_list);

            // Defined Array values to show in ListView
            String[] values = new String[] { "Wallet Creation/Amendment",
                    "Wallet Deposit",
                    "Wallet Token Reset",
                    "Wallet Enquiry",
                    "Password Reset",
                    "Wallet Withdrawal",
                    "Wallet  Transaction Enquiry",

            };

            // Define a new Adapter
            // First parameter - Context
            // Second parameter - Layout for the row
            // Third parameter - ID of the TextView to which the data is written
            // Forth - the Array of data

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    simple_list_item_1, android.R.id.text1, values);


            // Assign adapter to ListView
            listView.setAdapter(adapter);

            // ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

//                    // ListView Clicked item index
//                    int itemPosition     = position;
//
//                    // ListView Clicked item value
//                    String  itemValue    = (String) listView.getItemAtPosition(position);
//
//                    // Show Alert
//                    Toast.makeText(getApplicationContext(),
//                            "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
//                            .show();

                    switch (position){
                        case 0:
                            Intent amendBRIntent = new Intent(WalletMainMenu.this, AmendBR.class);
                            startActivity(amendBRIntent);
                            break;
                        case 1:
                            Intent walletDepositIntent = new Intent(WalletMainMenu.this, WalletDeposit.class);
                            startActivity(walletDepositIntent);
                            //									thread.start();
                            break;
                        case 2:
                            Intent walletTokenIntent = new Intent(WalletMainMenu.this, WalletTokenReset.class);
                            startActivity(walletTokenIntent);

                            break;
                        case 3:
                            Intent walletEnquiryIntent = new Intent(WalletMainMenu.this,Wal_Enquiry.class);
                            startActivity(walletEnquiryIntent);
                            break;

                        case 4:
                            Intent passwordResetIntent = new Intent(WalletMainMenu.this, PasswordReset.class);
                            startActivity(passwordResetIntent);
                            break;
                        case 5:
                            Intent walletWithdrawIntent = new Intent(WalletMainMenu.this, WalletWithdrawal.class);
                            startActivity(walletWithdrawIntent);
                            break;
                        case 6:
                            Intent walletTransEnquiryIntent = new Intent(WalletMainMenu.this, Wallet_Trans_Enquiry.class);
                            startActivity(walletTransEnquiryIntent);
                            break;


                    }//

                }

            });
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                WalletMainMenu.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    }

