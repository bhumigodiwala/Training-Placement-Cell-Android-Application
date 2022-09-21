package com.example.djsce;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class Student extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("HOME");
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //header = findViewById(R.id.header_student);
        //header.setText("Student : "+ Credential.userid);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        final String userid = getIntent().getStringExtra("userid");
       final  String type = getIntent().getStringExtra("type");
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.nav_gallery){
                    Intent i = new Intent(Student.this,Profile.class);
                    i.putExtra("flag","cmp");
                    startActivity(i);
                }

                if(destination.getId() == R.id.nav_send){
                    Intent i = new Intent(Student.this,ViewCompanies.class);
                    i.putExtra("flag","cmp");
                    startActivity(i);
                }
                if(destination.getId() == R.id.nav_slideshow){
                    Intent i = new Intent(Student.this,ViewCompanies.class);
                    i.putExtra("flag","mycmp");
                    startActivity(i);
                }
                if(destination.getId() == R.id.nav_tools){

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://drive.google.com/open?id=185KMpNBKmQ1alZ6YrBrzxNAKIHqTVDO1"));
                    startActivity(browserIntent);
                }
                if(destination.getId() == R.id.nav_share){
                    Intent i = new Intent(Student.this,MainActivity.class);
                    startActivity(i);
                }



            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed(){


    }
}
