package com.example.projectseg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequest extends AppCompatActivity {

    String branchId;
    Button goBackBtn;
    DatabaseReference profileDatabase;     //branch profiles database
    BranchProfile profile;

    TextView cityTextR;
    TextView addressTextR;
    TextView phoneTextR;
    TextView hoursTextR;
    TextView noServiceTextR;

    List<Service> servicesOfferedR;
    ListView listViewServicesOfferedBranchR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request);

        branchId = getIntent().getStringExtra("key");

        //send back customer to search
        goBackBtn = (Button) findViewById(R.id.goBackSearchBtn);
        goBackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), CustomerActivity.class));
            }
        });

        cityTextR = (TextView) findViewById(R.id.branchCityR);
        addressTextR = (TextView) findViewById(R.id.branchAddressR);
        phoneTextR = (TextView) findViewById(R.id.branchPhoneR);
        hoursTextR = (TextView) findViewById(R.id.branchHoursR);
        noServiceTextR = (TextView) findViewById(R.id.NoServiceTextR);

        profileDatabase = FirebaseDatabase.getInstance().getReference("Branches");

        servicesOfferedR = new ArrayList<>();
        listViewServicesOfferedBranchR = (ListView) findViewById(R.id.listViewServicesOfferedBranchR);

        ShowListService();
        getBranchProfile();

        //choose a branch by long click
        listViewServicesOfferedBranchR.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Service service = servicesOfferedR.get(i);
                Intent myIntent = new Intent(ServiceRequest.this, RequestServiceForm.class);
                myIntent.putExtra("keyService",service.getId());
                myIntent.putExtra("myBranchId", branchId);
                startActivity(myIntent);
                return true;
            }
        });

    }

    //to show services offered by branch
    protected void ShowListService() {
        profileDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    servicesOfferedR.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if(postSnapshot.getValue(BranchProfile.class).getId().equals(branchId)) {
                            if (postSnapshot.getValue(BranchProfile.class).getBranchServices() != null) {
                                for (int i = 0; i < postSnapshot.getValue(BranchProfile.class).getBranchServices().size(); i++) {
                                    Service service = postSnapshot.getValue(BranchProfile.class).getBranchServices().get(i);
                                    servicesOfferedR.add(service);
                                }
                                noServiceTextR.setText("");
                            } else {
                                noServiceTextR.setText("This branch has no service to offer yet!");
                            }
                        }
                    }
                    ServiceList serviceAdapter = new ServiceList(ServiceRequest.this, servicesOfferedR);
                    listViewServicesOfferedBranchR.setAdapter(serviceAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    //to get the branch info
    private void getBranchProfile(){
        profileDatabase.child(branchId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    profile = dataSnapshot.getValue(BranchProfile.class);
                    String cityF = "The branch city: " + profile.getBranchCity();
                    cityTextR.setText(cityF);
                    String addressF = "The branch Address: " + profile.getBranchAddress();
                    addressTextR.setText(addressF);
                    String phoneF = "The branch phone number: " + profile.getBranchPhone();
                    phoneTextR.setText(phoneF);
                    String hoursF = "open Mon-Fri, from ["+profile.getBranchOpen()+" am] to ["+profile.getBranchClose()+" pm]";
                    hoursTextR.setText(hoursF);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ServiceRequest.this, "something went wrong!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}