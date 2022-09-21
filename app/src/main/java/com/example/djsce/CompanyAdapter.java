package com.example.djsce;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CompanyAdapter extends  RecyclerView.Adapter<CompanyAdapter.MyViewHolder> {

    DatabaseReference databaseReference2;
    List<Company> companies;
    Context context;
    List<String> applied_companies;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReference1,databaseReference3;
    Company company;
    List<String> Students;

    private String[] months = {"January", "February", "March","April","May","June","July","August","September","October","November","December"};
    public CompanyAdapter(List<Company>companies, Context context,List<String> applied_companies,String userid){
        this.companies = companies;
        this.context = context;
        this.applied_companies = applied_companies;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("AppliedCompanies").child(userid);
        databaseReference1 = firebaseDatabase.getReference().child("EnrolledStudents");
        databaseReference2 = firebaseDatabase.getReference().child("MyDetails");
        databaseReference3 = firebaseDatabase.getReference().child("Companies");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_company,parent,false);

        MyViewHolder myViewHolder= new CompanyAdapter.MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        holder.cbranch.setText(companies.get(i).getBranches());
        holder.clocation.setText(companies.get(i).getLocation());
        holder.cjob_profile.setText(companies.get(i).getJob_profile());
        holder.cpackage.setText(companies.get(i).getSalary());

        holder.cname.setText(companies.get(i).getCname());
        Date date = companies.get(i).getDate();
        Date deadline = companies.get(i).getDeadline();

        Calendar calendar= Calendar.getInstance();
        calendar.setTime(date);

        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        int year = calendar.get(Calendar.YEAR);

        String s = months[month]+" "+day+" ,"+year;

        holder.cdate.setText(s);


        calendar.setTime(deadline);

        day=calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);

        year = calendar.get(Calendar.YEAR);

        s = months[month]+" "+day+" ,"+year;


        holder.cdeadline.setText(s);
        holder.cgpa.setText(companies.get(i).getCgpa());
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(companies.get(i).getPdf_url().equals("kachra")){
                    Toast.makeText(context,"No Brochure Present",Toast.LENGTH_SHORT).show();
                }
                else{
                    String url = companies.get(i).getPdf_url();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                }
            }
        });




        Log.d("TAG",Credential.type);
        if(!Credential.type.equals("Student"))
        {
            holder.apply.setVisibility(View.GONE);
            if(Credential.type.equals("tpo")) {
                holder.update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        company = companies.get(i);
                        Intent i = new Intent(context, AddCompany.class);
                        i.putExtra("company", (Serializable) company);
                        context.startActivity(i);
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        databaseReference3.child( companies.get(i).getCname()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().removeValue();
                                Intent i = new Intent(context,ViewCompanies.class);
                                i.putExtra("flag","cmp");
                                context.startActivity(i);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
            else{
                holder.delete.setVisibility(View.GONE);
                holder.update.setVisibility(View.GONE);
            }

            holder.getStudents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    company = companies.get(i);
                    Students = new ArrayList<>();
                    databaseReference1.child(company.getCname()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Students.add(snapshot.getKey());
                            }
                            Log.d("TAG","students " + Students);


                            final StringBuilder data = new StringBuilder();
                            final List<MyDetails> details = new ArrayList<>();
                            data.append("SrNo,Name,SAPid,EmailId,Contact,SSC%,HSC/Diploma%,CGPA,KTs,Branch");
                            for(int i = 0; i<Students.size(); i++){

                                databaseReference2.child(Students.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        details.add(dataSnapshot.getValue(MyDetails.class));

                                        Log.d("TAG",details.get(0).getName());
                                        //data.append("\n"+","+details[0].getName()+","+details[0].getDepartment());
                                        if(details.size() == Students.size()){


                                            Log.d("TAG","details size" + details.size());
                                            for(int i=0;i<details.size();i++)
                                            {

                                                data.append("\n"+i+","+details.get(i).getName()+","+Students.get(i)+","+details.get(i).getEmail()+
                                                        ","+details.get(i).getNumber() + ","+details.get(i).getxMarks()+
                                                        ","+ details.get(i).getXiiMarks()+","+details.get(i).getCgpa()+","+details.get(i).getKTs()+","+details.get(i).getDepartment());
                                            }
                                            try{
//                                                //saving the file into device
                                                FileOutputStream out = context.openFileOutput(company.getCname()+".csv", Context.MODE_PRIVATE);
                                                out.write((data.toString()).getBytes());
                                                out.close();

                                                //exporting

                                                File filelocation = new File(context.getFilesDir(), company.getCname()+".csv");
                                                Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
                                                Intent fileIntent = new Intent(Intent.ACTION_SEND);
                                                fileIntent.setType("text/csv");
                                                fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Enrolled Students for:"+company.getCname());
                                                fileIntent.putExtra(Intent.EXTRA_TEXT, path);
                                                fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                                                fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                                context.startActivity(Intent.createChooser(fileIntent, "Send mail"));






                                            }
                                            catch(Exception e){
                                                e.printStackTrace();
                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            //while(details.size() != Students.size());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

//                    Intent i = new Intent(context,AddCompany.class);
//                    i.putExtra("company",(Serializable) company);
//                    context.startActivity(i);
                }
            });

        }

        else{

            holder.update.setVisibility(View.GONE);
            holder.getStudents.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);


            if( applied_companies.contains(companies.get(i).getCname() )){

                holder.apply.setEnabled(false);
                holder.apply.setText("Already Applied");
            }
            else{
                holder.apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!Credential.isVerified){
                            Toast.makeText(context, "Please ask the Department Coordinator to Verify your Details", Toast.LENGTH_LONG).show();

                        }
                        else {


                            final float cgpa = Float.parseFloat(companies.get(i).getCgpa());
                            final String branches = companies.get(i).getBranches();
                            databaseReference2.child(Credential.userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    MyDetails myd = dataSnapshot.getValue(MyDetails.class);
                                    Date currentDate = new Date();
                                    if (Credential.isComplete && cgpa <= Float.parseFloat(myd.getCgpa()) && branches.contains(myd.getDepartment()) && currentDate.before(companies.get(i).getDeadline())) {
                                        databaseReference.child(companies.get(i).getCname()).setValue(true);
                                        databaseReference1.child(companies.get(i).getCname()).child(Credential.userid).setValue(true);
                                        holder.apply.setEnabled(false);
                                        holder.apply.setText("Already Applied");
                                    } else if (!Credential.isComplete) {
                                        Toast.makeText(context, "Please Complete Your Profile First", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(context, Profile.class);
                                        context.startActivity(i);
                                    } else {
                                        Toast.makeText(context, "You are not eligible for this Company", Toast.LENGTH_LONG).show();

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
        }


    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView cname;
        TextView clocation;
        TextView cpackage,cjob_profile,cdate, cdeadline,cpdf,cbranch,cgpa;
        Button apply,update,getStudents,delete;
        ImageButton download;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cname=(TextView)itemView.findViewById(R.id.cname);

            clocation = (TextView)itemView.findViewById(R.id.location);
            cjob_profile = (TextView)itemView.findViewById(R.id.profile);
            cdate = itemView.findViewById(R.id.date);
            cdeadline = itemView.findViewById(R.id.deadline);
            download = itemView.findViewById(R.id.download);
            cbranch = itemView.findViewById(R.id.branches);
            cpackage = itemView.findViewById(R.id.salary);
            apply = itemView.findViewById(R.id.apply);
            update =itemView.findViewById(R.id.update);
            getStudents = itemView.findViewById(R.id.getStudents);
            cgpa = itemView.findViewById(R.id.cgpa);
            delete = itemView.findViewById(R.id.delete);

        }
    }


    public void setCalendarEvent(Date date,String name){

        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, name);
        intent.putExtra(CalendarContract.Events.ALL_DAY, true);
        intent.putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                cal.getTime().getTime());
        context.startActivity(intent);

    }
}
