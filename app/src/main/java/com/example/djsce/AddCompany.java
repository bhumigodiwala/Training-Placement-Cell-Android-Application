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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddCompany extends AppCompatActivity {

    EditText cnameEt, locationEt, job_profileEt, salaryEt, cgpaEt, dateEt, deadlineEt;
    String cname, location, job_profile, salary, cgpa, branches;
    Date date, deadline;
    CheckBox ce,extc,mech,bio,prod,it;
    FirebaseDatabase database;
    DatabaseReference companyRef;
    FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;
    Company company;
    Uri broshureURI;
    TextView filename,file;
    String broshure_file;
    Button uploadBtn;
    ImageButton download;
    TextView bFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);
        setTitle("Add New Company");

        company = (Company) getIntent().getSerializableExtra("company");
        database= FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        bFile = findViewById(R.id.file);
        cnameEt = findViewById(R.id.cname);
        locationEt = findViewById(R.id.location);
        job_profileEt = findViewById(R.id.job_profile);
        salaryEt = findViewById(R.id.salary);
        cgpaEt = findViewById(R.id.cgpa);


        bio = findViewById(R.id.bio);
        ce =findViewById(R.id.ce);
        extc = findViewById(R.id.extc);
        mech = findViewById(R.id.mech);
        it = findViewById(R.id.it);
        prod = findViewById(R.id.prod);




        dateEt = findViewById(R.id.dateOfVisit);
        deadlineEt = findViewById(R.id.deadline);

        filename = findViewById(R.id.filename);
        uploadBtn = findViewById(R.id.upload_brochure);
        file = findViewById(R.id.file);
        download = (ImageButton) findViewById(R.id.download);

        Button save_btn = findViewById(R.id.save);

        if(company != null)
        {
            cnameEt.setText(company.getCname());
            locationEt.setText(company.getLocation());
            job_profileEt.setText(company.getJob_profile());
            salaryEt.setText(company.getSalary());
            cgpaEt.setText(company.getCgpa());

            branches = company.getBranches();

            if(branches.contains("Bio"))
                bio.setChecked(true);
            if(branches.contains("Mech"))
                mech.setChecked(true);
            if(branches.contains("EXTC"))
                extc.setChecked(true);
            if(branches.contains("Prod"))
                prod.setChecked(true);
            if(branches.contains("Comp"))
                ce.setChecked(true);
            if(branches.contains("IT"))
                it.setChecked(true);


            Calendar calendar= Calendar.getInstance();
            calendar.setTime(company.getDate());

            int day=calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            month++;
            int year = calendar.get(Calendar.YEAR);

            dateEt.setText(day + "/ " + month+ "/" + year);


            calendar.setTime(company.getDeadline());


            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
            deadlineEt.setText(day + "/ " + month+ "/" + year);
            broshure_file = company.getPdf_url();
            if(broshure_file.equals("kachra")){
                bFile.setText("BROCHURE NOT UPLOADED");
            }
            else{
                bFile.setText("BROCHURE ALREADY UPLOADED");
            }


        }
        else{
            bFile.setText("BROCHURE NOT UPLOADED");
        }
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(AddCompany.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    selectPDF();
                }
                else{
                    ActivityCompat.requestPermissions(AddCompany.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(broshure_file!=null && !broshure_file.equals("kachra")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(broshure_file));
                    startActivity(i);
                }
                else{
                    Toast.makeText(AddCompany.this, "No Brochure Uploaded", Toast.LENGTH_SHORT).show();
                }

            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cname = cnameEt.getText().toString().trim();
                location = locationEt.getText().toString().trim();
                job_profile = job_profileEt.getText().toString().trim();
                salary = salaryEt.getText().toString().trim();
                cgpa = cgpaEt.getText().toString().trim();

                branches = "";

                if(it.isChecked()){

                    branches = branches + "IT\n";

                }
                if(bio.isChecked()){
                    branches = branches + "Biomed\n";
                }
                if(extc.isChecked()){
                    branches = branches + "EXTC\n";
                }
                if(mech.isChecked()){
                    branches = branches + "Mechanical\n";
                }
                if(ce.isChecked()){
                    branches = branches + "Computer Engineering\n";
                }
                if(prod.isChecked()){
                    branches = branches + "Production";
                    Log.d("TAG","Went in Production"+branches);
                }


                //date = dateEt.getText().toString();
                //deadline = deadlineEt.getText().toString();

                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(dateEt.getText().toString());
                    deadline = new SimpleDateFormat("dd/MM/yyyy").parse(deadlineEt.getText().toString());
                    Log.d("TAG", "date = " + date + "\ndeadline = "+ deadline);
                }catch (ParseException p)
                {
                    Toast.makeText(AddCompany.this, "Date field format not as expected(DD/MM/YYYY)", Toast.LENGTH_SHORT).show();

                }

                companyRef = database.getReference("Companies").child(cname);

                if(cname.equals("") || location.equals("") || job_profile.equals("") ||
                        salary.equals("") || cgpa.equals("") || branches.equals("") || date.equals("") || deadline.equals(""))
                {
                    Toast.makeText(AddCompany.this, "No Field Should Remain Empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    company = new Company(cname, job_profile, location, salary, cgpa, date, deadline, branches, "kachra");

                    if (broshure_file != null) {
                        company.setPdf_url(broshure_file);
                    }


                    companyRef.setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (broshureURI != null) {
                                uploadResume();
                            } else {

                                Toast.makeText(AddCompany.this, "Company Added Successfully", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(AddCompany.this, ViewCompanies.class);
                                i.putExtra("flag", "cmp");
                                startActivity(i);
                            }


                        }
                    });
                }

            }
        });
    }

    private void uploadResume() {


        final String fileName =  cname;
        final StorageReference storageReference = firebaseStorage.getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading Brochure File");
        progressDialog.setProgress(0);
        progressDialog.show();



        storageReference.child("Companies").child(fileName).putFile(broshureURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                StorageReference filepath = storageReference.child("Companies").child(fileName);

                final String[] url = new String[1];

                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url[0] = uri.toString();
                        Log.d("TAG","getpath gives = " + url[0]);
                        companyRef.child("pdf_url").setValue(url[0]).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //Toast.makeText(AddCompany.this,"Uploaded Brochure Successfully",Toast.LENGTH_SHORT).show();

                                        Toast.makeText(AddCompany.this,"Company Added Successfully",Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(AddCompany.this,ViewCompanies.class);
                                        i.putExtra("flag","cmp");
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
                Toast.makeText(AddCompany.this,"Failed to upload brochure try again",Toast.LENGTH_SHORT).show();

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
            Toast.makeText(AddCompany.this,"Grant Permisssions to upload file",Toast.LENGTH_SHORT).show();
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
            broshureURI = data.getData();
            filename.setText("The file selecetd is : "+ data.getData().getLastPathSegment());
        }
        else{
            Toast.makeText(AddCompany.this,"Please select file",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed(){

        Intent i = new Intent(AddCompany.this, TPO.class);
        i.putExtra("flag", "cmp");
        startActivity(i);
    }
}
