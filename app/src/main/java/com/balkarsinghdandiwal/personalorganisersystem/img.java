package com.balkarsinghdandiwal.personalorganisersystem;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class img extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "img";
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton btnSelectImage;
    AppCompatImageView imgView;
    TextView text;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        btnSelectImage = (FloatingActionButton) findViewById(R.id.btnSelectImage);
        text = (TextView) findViewById(R.id.textView9);
        imgView = (AppCompatImageView) findViewById(R.id.imgView);


        dbHelper = new DBHelper(img.this);
        loadImageFromDB();

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openImageChooser();
            }
        });
    }

    void showMessage(String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    // Choose an image from Gallery
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();

                if (null != selectedImageUri) {

                    // Saving to Database...
                    if (saveImageInDB(selectedImageUri)) {
                        showMessage("Image Saved in Database...");
                        imgView.setImageURI(selectedImageUri);
                    }

                    // Reading from Database after 3 seconds just to show the message
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (loadImageFromDB()) {
                                showMessage("Image Loaded from Database...");
                            }
                        }
                    }, 3000);
                }

            }
        }
    }

    //@Override
    //public void onClick(View v) {
    //     openImageChooser();
    // }


    // Save the
    Boolean saveImageInDB(Uri selectedImageUri) {

        try {
            Intent intent = getIntent();
            String te = intent.getStringExtra("friendname");
            dbHelper.open();
            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            byte[] inputData = utils.getBytes(iStream);
            dbHelper.insertImage(inputData,te);
            dbHelper.close();
            return true;
        } catch (IOException ioe) {
            Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            dbHelper.close();
            return false;
        }

    }

    Boolean loadImageFromDB() {
        dbHelper.open();
        Intent intent = getIntent();
        long tem = intent.getLongExtra("friendselect", 0);
        tem = tem +1;
        //text.setText(Long.toString(tem));
        Cursor c = dbHelper.mDb.rawQuery("SELECT name FROM ImagesTable WHERE id =" +tem , null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            text.setText(c.getString(0));
            c.moveToNext();
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        try {
            byte[] bytes = dbHelper.retreiveImageFromDB(Long.toString(tem));
            imgView.setImageBitmap(utils.getImage(bytes));
            dbHelper.close();
            // Show Image from DB in ImageView
            //imgView.setImageBitmap(utils.getImage(bytes));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "<loadImageFromDB> Error : " + e.getLocalizedMessage());
            dbHelper.close();
            return false;
        }
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
        getMenuInflater().inflate(R.menu.img, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clrimg) {
            dbHelper.clearRecords();
            Toast.makeText(getBaseContext(),"All Images Deleted",Toast.LENGTH_LONG).show();
            Intent i=new Intent(getBaseContext(),navigation.class);
            startActivity(i);
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
