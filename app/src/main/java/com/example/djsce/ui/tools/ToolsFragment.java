package com.example.djsce.ui.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.djsce.Notice;
import com.example.djsce.R;
import com.example.djsce.Student;
import com.example.djsce.ui.home.HomeViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    FirebaseDatabase database;
    DatabaseReference usersRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);



        final TextView head1 = root.findViewById(R.id.head1);
        final TextView news1 = root.findViewById(R.id.news1);
        final TextView head2 = root.findViewById(R.id.head2);
        final TextView news2 = root.findViewById(R.id.news2);
        final TextView head3 = root.findViewById(R.id.head3);
        final TextView news3 = root.findViewById(R.id.news3);
        final TextView head4 = root.findViewById(R.id.head4);
        final TextView news4 = root.findViewById(R.id.news4);


        database= FirebaseDatabase.getInstance();
        usersRef = database.getReference("Notices");

        usersRef.child("news1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Notice notice1 = dataSnapshot.getValue(Notice.class);
                head1.setText(notice1.getHead());
                news1.setText(notice1.getContent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        usersRef.child("news2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Notice notice2 = dataSnapshot.getValue(Notice.class);
                head2.setText(notice2.getHead());
                news2.setText(notice2.getContent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        usersRef.child("news3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Notice notice3 = dataSnapshot.getValue(Notice.class);
                head3.setText(notice3.getHead());
                news3.setText(notice3.getContent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        usersRef.child("news4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Notice notice4 = dataSnapshot.getValue(Notice.class);
                head4.setText(notice4.getHead());
                news4.setText(notice4.getContent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //homeViewModel.getText().observe(this, new Observer<String>() {
        //  @Override
        //public void onChanged(@Nullable String s) {

        //    }
        //});
        return root;

    }

}