package com.example.djsce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    EditText nameET, emailET, numberET, departmentET, cgpaET, xiiMakrsET, xMarksET,ktET;
    TextView filename,file,sapid;
    String name;
    String email;
    String number;
    String department;
    private Spinner spinner1;
    String cgpa;
    String kt;
    String xiiMarks;
    String xMarks;
    Uri resumeURI;
    String resume_file;
    Button uploadBtn, saveBtn;
    FirebaseDatabase database;
    DatabaseReference usersRef;
    FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;
    String userid;

    List<String> departments = new ArrayList<>();
    ImageButton download;
    MyDetails myDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        departments.add("Computer Engineering");
        departments.add("EXTC");
        departments.add("Mechanical");
        departments.add("Production");
        departments.add("Biomed");
        departments.add("IT");



        //userid = getIntent().getStringExtra("userid");
        userid = Credential.userid;
        Log.d("TAG","The userid is "+userid);
        database= FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        nameET = findViewById(R.id.name);
        emailET = findViewById(R.id.email_id);
        numberET = findViewById(R.id.contact_no);



        spinner1 = findViewById(R.id.department);

        ArrayAdapter<String> aa = new ArrayAdapter<String> (
                this, android.R.layout.simple_spinner_item,departments );

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(aa);






        ktET = findViewById(R.id.kts);
        cgpaET = findViewById(R.id.cgpa);
        xiiMakrsET = findViewById(R.id.XIImarks);
        xMarksET = findViewById(R.id.Xmarks);
        filename = findViewById(R.id.filename);
        saveBtn = (Button)findViewById(R.id.save_details);
        uploadBtn = findViewById(R.id.upload_resume);
        file = findViewById(R.id.file);
        download = (ImageButton) findViewById(R.id.download);

        sapid = findViewById(R.id.sap_id);
        sapid.setText(Credential.userid);

        usersRef = database.getReference("MyDetails").child(userid);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myDetails = dataSnapshot.getValue(MyDetails.class);
                if(myDetails != null)
                {

                    nameET.setText(myDetails.getName());
                    numberET.setText(myDetails.getNumber());
                    cgpaET.setText(myDetails.getCgpa());
                    ktET.setText(myDetails.getKTs());
                    spinner1.setSelection(departments.indexOf(myDetails.getDepartment()));
                    emailET.setText(myDetails.getEmail());
                    xiiMakrsET.setText(myDetails.getXiiMarks());
                    xMarksET.setText(myDetails.getxMarks());
                    if(myDetails.getResumeURL().equals("kachra")){
                        file.setText("Resume not uploaded");
                    }
                    else{
                        file.setText("Resume File uploaded");
                        resume_file = myDetails.getResumeURL();
                        Log.d("TAG","The Resume url is"+resume_file);
                    }

                }
                else{

                    file.setText("Resume File Not Uploaded");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    selectPDF();
                }
                else{
                    ActivityCompat.requestPermissions(Profile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resume_file!=null && !resume_file.equals("kachra")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(resume_file));
                    startActivity(i);
                }
                else{
                    Toast.makeText(Profile.this, "No Resume Uploaded", Toast.LENGTH_SHORT).show();
                }

            }
        });



        saveBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){


                name = nameET.getText().toString();
                email = emailET.getText().toString();
                number = numberET.getText().toString();
                department = String.valueOf(spinner1.getSelectedItem());
                cgpa = cgpaET.getText().toString();
                kt = ktET.getText().toString();
                xiiMarks = xiiMakrsET.getText().toString();
                xMarks = xMarksET.getText().toString();

                if(name.equals("") || email.equals("") || number.equals("") ||
                        department.equals("") || cgpa.equals("") || xiiMarks.equals("") || xMarks.equals(""))
                {
                    Toast.makeText(Profile.this, "No field should remain empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final MyDetails mydetails = new MyDetails(name,email,number,department,cgpa,xiiMarks,xMarks,"kachra",kt);
                    if(resume_file != null)
                        mydetails.setResumeURL(resume_file);
                    usersRef.setValue(mydetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(!Credential.isComplete )
                        {
                            database.getReference("Users").child(userid).child("isComplete").setValue(true).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Credential.isComplete = true;
                                            Log.d("TAG","Profile value set to true");
                                        }
                                    });
                        }

                        database.getReference("Users").child(userid).child("isVerified").setValue(false);
                        database.getReference("NotVerified").child(userid).setValue(mydetails);
                        Credential.isVerified = false;


                        if(resumeURI!=null){
                            uploadResume();
                        }
                        else
                        {
                            Toast.makeText(Profile.this,"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Profile.this,Student.class);
                            startActivity(i);
                        }


                    }
                });
                }





            }
        });
    }

    private void uploadResume() {


        final String fileName =  userid;
        final StorageReference storageReference = firebaseStorage.getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading Resume File");
        progressDialog.setProgress(0);
        progressDialog.show();



        storageReference.child("Resumes").child(fileName).putFile(resumeURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                StorageReference filepath = storageReference.child("Resumes").child(fileName);

                final String[] url = new String[1];

                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url[0] = uri.toString();
                        Log.d("TAG","getpath gives = " + url[0]);
                        usersRef.child("resumeURL").setValue(url[0]).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(Profile.this,"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(Profile.this,"Uploaded Resume Successfully",Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(Profile.this,Student.class);
                                        startActivity(i);

                                    }
                                }
                        );

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this,"Failed to upload resume try again",Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int currentProgress = (int)(100* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectPDF();
        }
        else{
            Toast.makeText(Profile.this,"Grant Permisssions to upload file",Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPDF() {
        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 86 && resultCode == RESULT_OK && data != null){
            resumeURI = data.getData();
            filename.setText("The file selecetd is : "+ data.getData().getLastPathSegment());
        }
        else{
            Toast.makeText(Profile.this,"Please select file",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed(){

        Intent i = new Intent(Profile.this, Student.class);
        i.putExtra("flag", "cmp");
        startActivity(i);
    }
}
