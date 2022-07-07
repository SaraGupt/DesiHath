package com.example.desihand;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class HelpOut extends AppCompatActivity {
    AutocompleteSupportFragment autocomplete_fragment3, autocomplete_fragment4;
    Button btn_submit, btn_date_pick, btn_next;
    TextView textViewLatLong;
    EditText show_selected_date;
    GetHelpOut item1;
    MaterialDatePicker.Builder materialDateBuilder1;
    PlacesClient placesClient, placesClient1;
    DatabaseReference databaseReference;
    String Fromplace1, Toplace1, date;
    private List<GetHelpOut> arrayList;
    RecyclerView recyclerView;
    ItemClickListener itemClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_out);
/*
       textViewLatLong = findViewById(R.id.latLongTV);
        autocomplete_fragment3 = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment3);
        autocomplete_fragment4 = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment4);
        btn_date_pick = findViewById(R.id.pick_date_button);
        btn_next = findViewById(R.id.btn_next);
        recyclerView = findViewById(R.id.recycler_view);
        show_selected_date = findViewById(R.id.show_selected_date);
        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        // Create a new Places client instance.
        placesClient = Places.createClient(this);
        autocomplete_fragment3.setTypeFilter(TypeFilter.CITIES);
        autocomplete_fragment3.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS));
        autocomplete_fragment3.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Toplace1 = place.getName().trim();
            }
        });
        // Create a new Places client instance.
        placesClient1 = Places.createClient(this);
        autocomplete_fragment4.setTypeFilter(TypeFilter.CITIES);
        autocomplete_fragment4.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS));
        autocomplete_fragment4.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Fromplace1 = place.getName().trim();
            }
        });

        materialDateBuilder1 = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder1.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder1.build();
        btn_date_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override

            public void onPositiveButtonClick(Object selection) {
                date = materialDatePicker.getHeaderText();
                show_selected_date.setText(date);
            }
        });

        btn_submit = findViewById(R.id.btn_help_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(Toplace1) && TextUtils.isEmpty(Fromplace1) && TextUtils.isEmpty(date)) {
                    Toast.makeText(getApplicationContext(), "All Fields are Mandatory,Please select location..!:", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(Toplace1, Fromplace1, date);
                    RetriveValuey();
                }

                LatLng latLngFrom = getLatLngFromPlace(Fromplace1);
                LatLng latLngTo = getLatLngToPlace(Toplace1);
                if (latLngFrom != null && latLngTo != null) {
                    // Log.d("Agraaaa Lat Lng of From Location: ", " " + latLngFrom.latitude + " " + latLngFrom.longitude);
                    //Log.d("Delhiiii Lat Lng of To Location: ", " " + latLngTo.latitude + " " + latLngFrom.longitude);
                    double dis = distance(latLngFrom, latLngTo);
                    //  Log.d("Distance btw two location: ", " Distance" + dis);
                    String Dist = String.format("%.2f", dis / 1000 * 0.621371);
                    Toast.makeText(getApplicationContext(), "Distance is: " + Dist + "miles", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Lat Lng", "Lat Lng Not Found");
                }

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HelpOutList.class);
                startActivity(intent);
            }
        });

    }

    private void addDatatoFirebase(String toplace1, String fromplace1, String date) {
        item1 = new GetHelpOut(toplace1, fromplace1, date);
        FirebaseDatabase.getInstance().getReference("HelpOut").child(FirebaseAuth.
                getInstance().getCurrentUser().getUid()).setValue(item1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                    autocomplete_fragment3.setText("");
                    autocomplete_fragment4.setText("");
                    show_selected_date.setText("");

                } else {
                    Toast.makeText(getApplicationContext(), " Failed!!.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private LatLng getLatLngFromPlace(String FromAddress) {

        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> FromAddressList;

        try {
            FromAddressList = geocoder.getFromLocationName(FromAddress, 1);
            if (FromAddressList != null) {
                Address singleaddress = FromAddressList.get(0);
                LatLng FromLatLng = new LatLng(singleaddress.getLatitude(), singleaddress.getLongitude());
                return FromLatLng;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private LatLng getLatLngToPlace(String ToAddress) {

        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> ToAddressList;

        try {
            ToAddressList = geocoder.getFromLocationName(ToAddress, 1);
            if (ToAddressList != null) {
                Address singleaddress = ToAddressList.get(0);
                LatLng ToLatLng = new LatLng(singleaddress.getLatitude(), singleaddress.getLongitude());
                return ToLatLng;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static double distance(LatLng latLngFrom, LatLng latLngTo) {
        try {
            Location location1 = new Location("latLngFrom");
            location1.setLatitude(latLngFrom.latitude);
            location1.setLongitude(latLngFrom.longitude);
            Location location2 = new Location("latLngTo");
            location2.setLatitude(latLngTo.latitude);
            location2.setLongitude(latLngTo.longitude);
            double distance = location1.distanceTo(location2);

            return distance;
        } catch (Exception e) {

            e.printStackTrace();

        }
        return 0;
    }

    private void RetriveValuey() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("RequestItem");
        Log.d("Tag", "Queryyy" + databaseReference);
        Query query = databaseReference.orderByChild("date").equalTo(date);
        //Query query=databaseReference;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s = "";
                arrayList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                  /* s = s + "\n" + "Date : " + snapshot.child("date").getValue().toString() + "\n" +
                            "Description : " + snapshot.child("desc").getValue().toString() + "\n" +
                            "From : " + snapshot.child("fromplace").getValue().toString() + "\n" +
                            "Name : " + snapshot.child("name").getValue().toString() + "\n" +
                            "To : " + snapshot.child("toplace").getValue(String.class);*/

                    //  Sara is requesting a package to be picked up
               //     s = s + snapshot.child("name").getValue().toString() + " is requesting a package to be " +
                //            "picked up(-- miles from your home) \n \n";

        /*GetHelpOut post = snapshot.getValue(GetHelpOut.class);
                    arrayList.add(post);
                    // Initialize listener
                    itemClickListener = new ItemClickListener() {
                        @Override
                        public void onClick(String s) {
                            // Notify adapter
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            // Display toast
                            Toast.makeText(getApplicationContext(), "Selected : " + s, Toast.LENGTH_SHORT).show();
                        }

                    };
                    // Set layout manager
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // Initialize adapter
                    adapter = new MainAdapter((ArrayList<GetHelpOut>) arrayList, itemClickListener);

                    // set adapter
                    recyclerView.setAdapter(adapter);

                    Log.i("Mytag", "onDataChange: " + arrayList.toString());
                    textViewLatLong.setText(s);

                }
 }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
     }); */

        }


}

