package com.balkarsinghdandiwal.personalorganisersystem;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewEvents extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseManage mydManage;
    private TextView response;
    private ListView Listevents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        response = (TextView)findViewById(R.id.response);
        Listevents = (ListView)findViewById(R.id.listevents);
        mydManage = new DatabaseManage(this);
        mydManage.openReadable();
        ArrayList<String> tableContent = mydManage.retrieveRows();
        response.setText("Stored Events");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, tableContent);
        Listevents.setAdapter(adapter);

        ArrayList<String> tableContentt = mydManage.retrieveRow();
        ArrayList<Date> dateList = new ArrayList<Date>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");

        for (String dateString : tableContentt) {
            try {
                dateList.add(simpleDateFormat.parse(dateString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (Date date : dateList) {
            //System.out.println("Date " + simpleDateFormat.format(date));


            //Date eventDate = format.parse(date[position]);
            Date currDate = new Date();
            if(date.compareTo(currDate) <= 0){
                Log.d("date",date.toString() + "\n" + currDate.toString());
                Listevents.setBackgroundColor(Color.CYAN);
            }else{
                Listevents.setBackgroundColor(Color.RED);
            }
            ;
        }


        Listevents.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                long item = Listevents.getAdapter().getItemId(position);
                String st = (String) Listevents.getAdapter().getItem(position);
                Toast.makeText(getApplicationContext(), st + " selected", Toast.LENGTH_LONG).show();
                Intent i=new Intent(getBaseContext(),editevents.class);
                i.putExtra("friendselected", item);
                startActivity(i);
            }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id ==R.id.remove_rows){
        removeRecs();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Home) {


            try {

                Intent i=new Intent(getBaseContext(),navigation.class);
                startActivity(i);
                //finish();

            } catch (Exception e) {

            }

            // Handle the camera action
        }else if (id == R.id.AddFriends) {


            try {

                Intent i=new Intent(getBaseContext(),addfriends.class);
                startActivity(i);
                //finish();

            } catch (Exception e) {

            }

            // Handle the camera action
        } else if (id == R.id.ViewFriends) {
            try {

                Intent i=new Intent(getBaseContext(),viewfriends.class);
                startActivity(i);
                //finish();

            } catch (Exception e) {

            }

        } else if (id == R.id.Addimage) {
            try {

                Intent i=new Intent(getBaseContext(),addimg.class);
                startActivity(i);
                //finish();

            } catch (Exception e) {

            }

        }  else if (id == R.id.AddEvents) {
            try {

                Intent i=new Intent(getBaseContext(),addevents.class);
                startActivity(i);
                //finish();

            } catch (Exception e) {

            }

            // Handle the camera action
        } else if (id == R.id.ViewEvents) {
            try {

                Intent i=new Intent(getBaseContext(),ViewEvents.class);
                startActivity(i);
                //finish();

            } catch (Exception e) {

            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean insertRec() {

        Intent i=new Intent(getBaseContext(),navigation.class);
        startActivity(i);
        //productRec.setText("");
        //mydManager.close();
        return true;
    }
    public boolean viewRec() {

        Intent i=new Intent(getBaseContext(),ViewEvents.class);
        startActivity(i);
        //productRec.setText("");
        //mydManager.close();
        return true;
    }
    public boolean removeRecs() {
        mydManage.clearRecords();
        Toast.makeText(ViewEvents.this,"All Events are deleted",Toast.LENGTH_LONG).show();
        Intent i=new Intent(getBaseContext(),addevents.class);
        startActivity(i);
        // productRec.setText("");
        return true;
    }
}
