package com.example.djsce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewCompanies extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference,mDatabaseReference2;
    private RecyclerView recyclerView;
    private CompanyAdapter r;

    List<Company> companies, applied ;
    List<String> appliedCompanies;
    String userid,type,flag;

    public ViewCompanies() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_companies);
        setTitle("View Companies");

        companies = new ArrayList<Company>();
        appliedCompanies = new ArrayList<String>();
        applied = new ArrayList<Company>();
        userid = Credential.userid;
        type = Credential.type;
        flag = getIntent().getStringExtra("flag");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Companies");
        mDatabaseReference2 = mFirebaseDatabase.getReference().child("AppliedCompanies").child(userid);

        mDatabaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    appliedCompanies.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    companies.add(snapshot.getValue(Company.class));

                }

                if(flag.equals("mycmp")) {


                    for(int i=0;i<companies.size();i++)
                    {
                        if(appliedCompanies.contains(companies.get(i).getCname()))
                        {
                            applied.add(companies.get(i));
                        }
                    }
                    companies.clear();
                    companies = applied;
                }

                updateUI();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void updateUI() {
        recyclerView = findViewById(R.id.companies);
        Log.d("TAG","Inside update ui function");
        //recyclerView.setHasFixedSize(true);
        r=new CompanyAdapter(companies,this,appliedCompanies,userid);
        recyclerView.setAdapter(r);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
    }


    @Override
    public void onBackPressed(){

        Intent i;
        if(Credential.type.equals("tpo"))
        {
            i = new Intent(ViewCompanies.this, TPO.class);
        }
        else if(Credential.type.equals("dc"))
        {
            i = new Intent(ViewCompanies.this, DC.class);
        }
        else{
            i = new Intent(ViewCompanies.this, Student.class);
        }
        
        i.putExtra("flag", "cmp");
        startActivity(i);
    }
}
