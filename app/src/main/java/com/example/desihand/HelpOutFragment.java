package com.example.desihand;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HelpOutFragment extends Fragment {
    AutocompleteSupportFragment autocomplete_fragment3, autocomplete_fragment4;
    Button btn_submit;
    ImageView btn_date_pick;
    TextView textViewLatLong;
    EditText show_selected_date;
    GetHelpOut item1;
    int email_send;
    String key;
    MaterialDatePicker.Builder materialDateBuilder1;
    PlacesClient placesClient, placesClient1;
    DatabaseReference databaseReference;
    String Fromplace1, Toplace1, date,firebaseUserid,address,name;
    ListView list;
    View view;
    String reqname,reqaddress,reqaddress1,reqemail;
    com.google.firebase.auth.FirebaseUser firebaseUser;

    public HelpOutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help_out, container, false);
        textViewLatLong = view.findViewById(R.id.latLongTV);
        autocomplete_fragment3 = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment3);
        autocomplete_fragment4 = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment4);
        btn_date_pick = view.findViewById(R.id.Hpick_date_button);

        list = view.findViewById(R.id.list);
        show_selected_date = view.findViewById(R.id.show_selected_date);
        String apiKey = getString(R.string.api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), apiKey);
        }
        placesClient = Places.createClient(getContext());
        autocomplete_fragment3.setTypeFilter(TypeFilter.GEOCODE);
        autocomplete_fragment3.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));
        autocomplete_fragment3.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                //Toast.makeText(getActivity(), status.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Cancelled",  status.toString());
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Toplace1 = place.getName().trim();
            }
        });
        placesClient1 = Places.createClient(getContext());
        autocomplete_fragment4.setTypeFilter(TypeFilter.GEOCODE);
        autocomplete_fragment4.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));
        autocomplete_fragment4.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
               // Toast.makeText(getActivity(), status.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Cancelled",  status.toString());
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
                materialDatePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");

            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override

            public void onPositiveButtonClick(Object selection) {
                date = materialDatePicker.getHeaderText();
                show_selected_date.setText(date);
            }
        });

        btn_submit = view.findViewById(R.id.btn_help_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(Toplace1) && TextUtils.isEmpty(Fromplace1) && TextUtils.isEmpty(date)) {
                    Toast.makeText(getActivity(), "All Fields are Mandatory,Please select location..!:", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(Toplace1, Fromplace1, date);
                     reqaddress1=RetriveValuey();
                }
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                databaseReference = FirebaseDatabase.getInstance().getReference("userdetails");
                firebaseUserid = firebaseUser.getUid();
                databaseReference.child(firebaseUserid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userdetails userprofile = snapshot.getValue(userdetails.class);
                        if (userprofile != null) {
                            name = userprofile.name;
                            address=userprofile.address;
                            reqemail=userprofile.email;
                           // Toast.makeText(getActivity(), "Addreess!!" + name+ " "+reqemail+ "Requester:"+reqaddress1+ ""+reqname, Toast.LENGTH_SHORT).show();
                            LatLng latLngFrom = getLatLngFromPlace(address);
                            LatLng latLngTo = getLatLngToPlace(reqaddress1);
                            if (latLngFrom != null && latLngTo != null) {
                                double dis = distance(latLngFrom, latLngTo);
                                String Dist = String.format("%.2f", dis / 1000 * 0.621371);
                                Toast.makeText(getActivity(), "Distance is: " + Dist + "miles", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("Lat Lng", "Lat Lng Not Found");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Cancelled", "Address not found");
                    }
                });



            }

        });

        return view;
    }

    private void addDatatoFirebase(String toplace1, String fromplace1, String date) {
        item1 = new GetHelpOut(toplace1, fromplace1, date);
        FirebaseDatabase.getInstance().getReference("HelpOut").child(FirebaseAuth.
                getInstance().getCurrentUser().getUid()).setValue(item1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_SHORT).show();
                    autocomplete_fragment3.setText("");
                    autocomplete_fragment4.setText("");
                    show_selected_date.setText("");

                } else {
                    Toast.makeText(getActivity(), " Failed!!.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private LatLng getLatLngFromPlace(String FromAddress) {

        Geocoder geocoder = new Geocoder(getContext());
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

        Geocoder geocoder = new Geocoder(getContext());
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

    public static double distance(LatLng latLngFrom, LatLng latLngTo)
    {
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

    private String RetriveValuey() {

        ArrayList  arrayList = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, arrayList);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("RequestItem");
       Query query = databaseReference.orderByChild("date").equalTo(date);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                  //  Sara is requesting a package to be picked up

                                  arrayList.add(snapshot.child("name").getValue() + " is requesting");
                                  key =  snapshot.getKey();
                                  adapter.notifyDataSetChanged();

                              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                int firstSpace = selectedItem.indexOf(" "); // detect the first space character
                String firstName = selectedItem.substring(0, firstSpace);
             //  Toast.makeText(getActivity(), " reqemail:" + reqemail, Toast.LENGTH_SHORT).show();
                sendEmail(reqemail);
                if (email_send==1)
                {
                    FirebaseDatabase.getInstance().getReference("HelpOutDone").child(FirebaseAuth.
                            getInstance().getCurrentUser().getUid()).setValue(selectedItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Successfully Added HelpOutDone", Toast.LENGTH_SHORT).show();
                            }
                               else {
                                Toast.makeText(getActivity(), " Failed!!.", Toast.LENGTH_SHORT).show();
                            }
                            String item = adapter.getItem(i);
                            adapter.remove(item);
                            adapter.notifyDataSetChanged();
                            FirebaseDatabase.getInstance().getReference().child("RequestItem").child(key).
                                    removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getActivity(), " Deleted!!.", Toast.LENGTH_SHORT).show();
                                }

                            });

                    }
                    });

                }
                else
                    Log.d("Email does not send", "Error in email sending");

                // here add the code of update table


                databaseReference = FirebaseDatabase.getInstance().getReference().child("userdetails");
                Query query = databaseReference.orderByChild("name").equalTo(firstName);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userdetails userprofile = snapshot.getValue(userdetails.class);
                        if (userprofile != null) {
                            reqname = userprofile.name;
                            reqaddress = userprofile.address;


                           /* Intent intent = new Intent(getContext(), HelpOutList.class);
                            intent.putExtra("Req_name", reqname);
                            intent.putExtra("Req_address", reqaddress);
                            startActivity(intent);*/
                            
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return  reqaddress;
    }

    private void sendEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        Toast.makeText(getActivity(), "Email:"+email, Toast.LENGTH_SHORT).show();
        String[] strArray = new String[] {email};
        emailIntent.putExtra(Intent.EXTRA_EMAIL,strArray);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "DesiHand:Helper is here t help you!!");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Congratulations!! Your request has been accepted");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.d("Finished sending email...", "Email send");
            email_send=1;

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    }


