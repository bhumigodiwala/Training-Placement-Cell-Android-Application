package com.example.djsce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VerifyStudents extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference,mDatabaseReference2;
    private RecyclerView recyclerView;
    private StudentAdapter r;

    List<MyDetails> Students;
    List<String> userids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_students);
        Students = new ArrayList<>();
        userids = new ArrayList<>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("NotVerified");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Students.add(snapshot.getValue(MyDetails.class));
                    userids.add(snapshot.getKey());
                }
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
    private void updateUI() {
        recyclerView = findViewById(R.id.students);
        Log.d("TAG","Inside update ui function"+Students.size());
        //recyclerView.setHasFixedSize(true);
        r=new StudentAdapter(Students,this,userids);
        recyclerView.setAdapter(r);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
    }

    @Override
    public void onBackPressed(){

        Intent i = new Intent(VerifyStudents.this, DC.class);
        startActivity(i);
    }
}
