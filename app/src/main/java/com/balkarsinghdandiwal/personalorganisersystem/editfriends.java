package com.balkarsinghdandiwal.personalorganisersystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class editfriends extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    private DatabaseManager mydManager;
    public EditText firstname;
    public EditText lastname;
    public EditText DateEdit;
    public EditText address;
    public EditText Gender;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private Button DeleteFriend, UpdateFriend;
    public boolean recInserted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfriends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DeleteFriend = (Button) findViewById(R.id.deletefriend);
        UpdateFriend = (Button) findViewById(R.id.editfriend);
        firstname = (EditText) findViewById(R.id.fname);
        lastname = (EditText) findViewById(R.id.lname);
        DateEdit = (EditText) findViewById(R.id.etxt_fromdate);
        Gender = (EditText) findViewById(R.id.gender);
        address = (EditText) findViewById(R.id.address);
        //String mySelectedText = radioSexButton.getText().toString();
        //String mydob = DateEdit.getText().toString();
       // String fme = firstname.getText().toString();
       // String lme = lastname.getText().toString();
        //String mytme = Timee.getText().toString();
       // String ltcn = address.getText().toString();
        mydManager = new DatabaseManager(this);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mydManager.openReadable();
        Intent intent = getIntent();
        long tem = intent.getLongExtra("friendselected", 0);
        tem = tem +1;
        // et2.setText(Long.toString(tem));
        ArrayList<String> productRows = new ArrayList<String>();
        //String[] columns = new String[] {"code", "product_name", "price"};
        Cursor c = mydManager.db.rawQuery("SELECT fname,lname,dob,sex,address FROM products WHERE id =" +tem , null);
        c.moveToFirst(); //"WHERE id = '" + tem + "'"
        while (c.isAfterLast() == false) {
            firstname.setText(c.getString(0));
            lastname.setText(c.getString(1));
            DateEdit.setText(c.getString(2));
            Gender.setText(c.getString(3));
            address.setText(c.getString(4));
            c.moveToNext();
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        long laID = DatabaseUtils.longForQuery(mydManager.db, "SELECT MAX(id) FROM products" , null);
        //long last = lastID-1;
        String im = Long.toString(laID);
        //tw.setText(im);

        UpdateData();
        DeleteData();

    }


    public void UpdateData() {
        UpdateFriend.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        long tem = intent.getLongExtra("friendselected", 0);
                        tem = tem +1;
                        boolean isUpdate = mydManager.updateData(firstname.getText().toString(), lastname.getText().toString(),DateEdit.getText().toString(),Gender.getText().toString(),address.getText().toString(),Long.toString(tem));
                        if(isUpdate == true)
                            Toast.makeText(editfriends.this,"Friend Updated",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(editfriends.this,"Friend not Updated",Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getBaseContext(),viewfriends.class);
                        startActivity(i);
                    }
                }
        );
    }

    public void DeleteData() {
        DeleteFriend.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        long tem = intent.getLongExtra("friendselected", 0);
                        tem = tem +1;
                        boolean deletedRows = mydManager.deleteData(firstname.getText().toString(), lastname.getText().toString(),DateEdit.getText().toString(),Gender.getText().toString(),address.getText().toString(),Long.toString(tem));
                        long lastID = DatabaseUtils.longForQuery(mydManager.db, "SELECT MAX(id) FROM products" , null);
                        int x = (int) lastID;
                        //tw.setText(Integer.toString(x));
                        int mx = 1;
                        Cursor cursor = mydManager.db.rawQuery("SELECT id FROM products", null);
                        cursor.moveToFirst();
                        do {
                            mydManager.db.execSQL("UPDATE products SET id = " + mx + " WHERE id = " + x);
                            x--;
                            mx++;
                        }while (cursor.moveToNext());
                        if(deletedRows == true)
                            Toast.makeText(editfriends.this,"Friend Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(editfriends.this,"Friend not Deleted",Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getBaseContext(),viewfriends.class);
                        startActivity(i);
                    }
                }
        );
    }

    public boolean addRec() {

        Intent i=new Intent(getBaseContext(),addfriends.class);
        startActivity(i);
        //productRec.setText("");
        //mydManager.close();
        return true;
    }
    public boolean viewRec() {

        Intent i=new Intent(getBaseContext(),viewfriends.class);
        startActivity(i);
        //productRec.setText("");
        //mydManager.close();
        return true;
    }
    public boolean removeRecs() {
        mydManager.clearRecords();
        Intent i=new Intent(getBaseContext(),viewfriends.class);
        startActivity(i);
        // productRec.setText("");
        return true;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

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



