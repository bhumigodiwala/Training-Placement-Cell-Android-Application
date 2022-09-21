package com.example.djsce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button login_btn;
    EditText uid;
    EditText pass;
    String userid;
    String password;
    FirebaseDatabase database;
    DatabaseReference usersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        database= FirebaseDatabase.getInstance();


        login_btn=(Button)findViewById(R.id.login_button);
        pass = (EditText)findViewById(R.id.password);
        uid = (EditText)findViewById(R.id.userid) ;


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = pass.getText().toString();
                userid = uid.getText().toString();


                usersRef = database.getReference("Users").child(userid);
                if(usersRef == null)
                {
                    Toast.makeText(MainActivity.this,"invalid userid or password",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);

                            if(user== null) {
                                Toast.makeText(MainActivity.this, "invalid userid", Toast.LENGTH_SHORT).show();
                            }
                            else if(user.getPassword() == null || user.getUserid() == null)
                            {
                                Toast.makeText(MainActivity.this, "invalid userid", Toast.LENGTH_SHORT).show();

                            }
                            else if(user.getPassword().equals(password) && user.getUserid().equals(userid)){
                                Toast.makeText(MainActivity.this,"Correct Credentials",Toast.LENGTH_SHORT).show();

                                Credential.userid = user.getUserid();
                                Credential.type = user.getType();
                                Credential.isComplete = user.getisComplete();
                                Credential.isVerified = user.getisVerified();

                                Log.d("TAG","Is verified"+Credential.isVerified);
                                Log.d("TAG","Is Complete"+Credential.isComplete);

                                if(user.getType().equals("Student")){

                                    Intent i;
                                    if(Credential.isComplete)
                                        i = new Intent(MainActivity.this,Student.class);
                                    else
                                        i = new Intent(MainActivity.this, Profile.class);
                                    startActivity(i);
                                }
                                else if(user.getType().equals("tpo")){
                                    Intent i = new Intent(MainActivity.this,TPO.class);
                                    startActivity(i);
                                }
                                else if(user.getType().equals("dc")){
                                    Intent i = new Intent(MainActivity.this,DC.class);
                                    startActivity(i);
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this,"invalid userid or password",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }
        });

    }
    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
