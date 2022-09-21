package com.example.djsce;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {
    List<MyDetails> Students;
    List<String> userids;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference2;

    public StudentAdapter(List<MyDetails> Students, Context context,List<String> userids){
        this.Students = Students;
        this.context = context;
        this.userids = userids;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("NotVerified");
        databaseReference2 = firebaseDatabase.getReference().child("Users");
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_student,parent,false);

        StudentAdapter.MyViewHolder myViewHolder= new StudentAdapter.MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {

        holder.sname.setText(Students.get(i).getName());
        holder.phone.setText(Students.get(i).getNumber());
        holder.email.setText(Students.get(i).getEmail());
        holder.department.setText(Students.get(i).getDepartment());
        holder.cgpa.setText(Students.get(i).getCgpa());

        holder.kt.setText(Students.get(i).getKTs());


        holder.verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(userids.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();
                        databaseReference2.child(userids.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("isVerified").setValue(true);

                                Toast.makeText(context, "Verified Student Successfully", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(context,DC.class);
                                context.startActivity(i);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        holder.feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String mail="mailto:"+Students.get(i).getEmail();
                intent.setData(Uri.parse(mail));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return Students.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sname,department,email,cgpa,phone,kt;
        Button verify,feedback;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sname = itemView.findViewById(R.id.sname);
            department = itemView.findViewById(R.id.department);
            email = itemView.findViewById(R.id.email);
            cgpa = itemView.findViewById(R.id.cgpa);
            phone = itemView.findViewById(R.id.number);
            verify = itemView.findViewById(R.id.verify);
            feedback = itemView.findViewById(R.id.feedback);
            kt = itemView.findViewById(R.id.kts);
        }

    }
}
