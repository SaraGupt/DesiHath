package com.example.desihand;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DesiHandDashboard extends Fragment {
    View view;
    TextView txt_secondly,txt_thirds;
    DatabaseReference databaseReference;
    com.google.firebase.auth.FirebaseUser FirebaseUser;
    String FirebaseUserid,name, y=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_desi_hand_dashboard, container, false);
        txt_secondly = view.findViewById(R.id.second_label);
        txt_thirds = view.findViewById(R.id.third_label);

        //Get Name from Data base
        FirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("userdetails");
        FirebaseUserid = FirebaseUser.getUid();
        databaseReference.child(FirebaseUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userdetails userprofile = snapshot.getValue(userdetails.class);
                if (userprofile != null) {
                    name = userprofile.name;
                  //  txt_secondly.setText(name);

                   databaseReference = FirebaseDatabase.getInstance().getReference().child("RequestItem");
                    Query query = databaseReference.orderByChild("name").equalTo(name);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                GetRequestItem user1 = snapshot.getValue(GetRequestItem.class);
                             y=user1.getName();
                              if (y!=null)
                              {
                                  txt_secondly.setText("Your request is ready to pick");
                              }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                  //  Toast.makeText(getActivity(), "query:  "+query, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Wronggg!!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}