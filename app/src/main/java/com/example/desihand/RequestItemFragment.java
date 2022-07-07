package com.example.desihand;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class RequestItemFragment extends Fragment {
    View view;
    PlacesClient placesClient, placesClient1;
    AutocompleteSupportFragment autocompleteFragment, autocompleteFragment1;
    Button btnSubmit;
    ImageView mPickDateButton;
    DatabaseReference databaseReference;
    com.google.firebase.auth.FirebaseUser FirebaseUser;
    GetRequestItem item;
    TextView edtName;
    private EditText mShowSelectedDateText, edtDesc;
    MaterialDatePicker.Builder materialDateBuilder;
    String Toplace, Fromplace, date, desc, FirebaseUserid, name;

    public RequestItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_request_item, container, false);
        autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.Fautocomplete_fragment1);
        autocompleteFragment1 = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.Fautocomplete_fragment2);
        mPickDateButton = view.findViewById(R.id.Fpick_date_button);
        mShowSelectedDateText = view.findViewById(R.id.Fshow_selected_date);
        edtDesc = view.findViewById(R.id.Fedt_desc);
        edtName = view.findViewById(R.id.Fname);
        btnSubmit = view.findViewById(R.id.Fbtn_submit);

        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), apiKey);
        }
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
                    edtName.setText(name);
                   // Toast.makeText(getActivity(), "Hello!!" + name, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Wronggg!!", Toast.LENGTH_SHORT).show();
            }
        });
        placesClient = Places.createClient(getContext());
        autocompleteFragment.setTypeFilter(TypeFilter.GEOCODE);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onError(@NonNull Status status) {
              //  Toast.makeText(getActivity(), status.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Cancelled",  status.toString());
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Toplace = place.getName().trim();
                // Toast.makeText(getApplicationContext(), Toplace, Toast.LENGTH_SHORT).show();
            }
        });
        placesClient1 = Places.createClient(getContext());
        autocompleteFragment1.setTypeFilter(TypeFilter.GEOCODE);
        autocompleteFragment1.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS));
        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onError(@NonNull Status status) {
               // Toast.makeText(getActivity(), status.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Cancelled",  status.toString());
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Fromplace = place.getName().trim();
                //   Toast.makeText(getApplicationContext(), Fromplace, Toast.LENGTH_SHORT).show();
            }
        });
        //end code of from Location

        materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        mPickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");

            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                date = materialDatePicker.getHeaderText();
                mShowSelectedDateText.setText(date);
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(Toplace) && TextUtils.isEmpty(Fromplace) && TextUtils.isEmpty(date) && TextUtils.isEmpty(desc)) {
                    Toast.makeText(getActivity(), "All Fields are Mandatory,Please select location..!:", Toast.LENGTH_SHORT).show();
                } else {
                    desc = edtDesc.getText().toString().trim();
                    addDatatoFirebase(Toplace, Fromplace, date, desc, name);
                }
            }
        });
        return view;

    }

    private void addDatatoFirebase(String toplace, String fromplace, String date, String desc, String name) {
        item = new GetRequestItem(toplace, fromplace, date, desc, name);
        // Toast.makeText(getApplicationContext(), "Details:"+desc , Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference("RequestItem").child(FirebaseAuth.
                getInstance().getCurrentUser().getUid()).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_SHORT).show();
                    autocompleteFragment.setText("");
                    autocompleteFragment1.setText("");
                    mShowSelectedDateText.setText("");
                    edtDesc.setText("");

                } else {
                    Toast.makeText(getActivity(), " Failed!!.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}