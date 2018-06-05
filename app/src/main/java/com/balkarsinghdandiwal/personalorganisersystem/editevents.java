package com.balkarsinghdandiwal.personalorganisersystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class editevents extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseManage mydManage;
    public EditText Name;
    public EditText Datee;
    public EditText Timee;
    public EditText Location;
    private Button DeleteEvent, UpdateEvent;
    public boolean recInserted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevents);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DeleteEvent = (Button) findViewById(R.id.deleteEvent);
        UpdateEvent = (Button) findViewById(R.id.editevent);
        Name = (EditText) findViewById(R.id.name);
        Datee = (EditText) findViewById(R.id.ddate);
        Timee = (EditText) findViewById(R.id.ttime);
        Location = (EditText) findViewById(R.id.location);
        mydManage = new DatabaseManage(this);
        mydManage.openReadable();
        Intent intent = getIntent();
        long tem = intent.getLongExtra("friendselected", 0);
        tem = tem +1;
        // et2.setText(Long.toString(tem));
        ArrayList<String> productRows = new ArrayList<String>();
        //String[] columns = new String[] {"code", "product_name", "price"};
        Cursor c = mydManage.db.rawQuery("SELECT name,datee,timee,address FROM event WHERE id =" +tem , null);
        c.moveToFirst(); //"WHERE id = '" + tem + "'"
        while (c.isAfterLast() == false) {
            Name.setText(c.getString(0));
            Datee.setText(c.getString(1));
            Timee.setText(c.getString(2));
            Location.setText(c.getString(3));
            c.moveToNext();
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        long laID = DatabaseUtils.longForQuery(mydManage.db, "SELECT MAX(id) FROM event" , null);
        //long last = lastID-1;
        String im = Long.toString(laID);
        //tw.setText(im);

        UpdateData();
        DeleteData();

    }

    public void UpdateData() {
        UpdateEvent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        long tem = intent.getLongExtra("friendselected", 0);
                        tem = tem +1;
                        boolean isUpdate = mydManage.updateData(Name.getText().toString(), Datee.getText().toString(),Timee.getText().toString(),Location.getText().toString(),Long.toString(tem));
                        if(isUpdate == true)
                            Toast.makeText(editevents.this,"Event Updated",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(editevents.this,"Event not Updated",Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getBaseContext(),ViewEvents.class);
                        startActivity(i);
                    }
                }
        );
    }

    public void DeleteData() {
        DeleteEvent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        long tem = intent.getLongExtra("friendselected", 0);
                        tem = tem +1;
                        boolean deletedRows = mydManage.deleteData(Name.getText().toString(), Datee.getText().toString(),Timee.getText().toString(),Location.getText().toString(),Long.toString(tem));
                        long lastID = DatabaseUtils.longForQuery(mydManage.db, "SELECT MAX(id) FROM event" , null);
                        int x = (int) lastID;
                        //tw.setText(Integer.toString(x));
                        int mx = 1;
                        Cursor cursor = mydManage.db.rawQuery("SELECT id FROM event", null);
                        cursor.moveToFirst();
                        do {
                            mydManage.db.execSQL("UPDATE event SET id = " + mx + " WHERE id = " + x);
                            x--;
                            mx++;
                        }while (cursor.moveToNext());
                        if(deletedRows == true)
                            Toast.makeText(editevents.this,"Event Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(editevents.this,"Event not Deleted",Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getBaseContext(),ViewEvents.class);
                        startActivity(i);
                    }
                }
        );
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
    public boolean removeRecs() {
        mydManage.clearRecords();
        Intent i=new Intent(getBaseContext(),ViewEvents.class);
        startActivity(i);
        // productRec.setText("");
        return true;
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
        if (id == R.id.remove_rows) {
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
}
