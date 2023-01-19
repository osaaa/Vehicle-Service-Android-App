package com.example.projectseg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {
    Button logOutBtn;
    Button toEditProfile;
    Button toSeeRequests;

    FirebaseAuth auth;
    DatabaseReference mDatabase;           //users database
    DatabaseReference profileDatabase;     //branch profiles database
    String uid;
    User user;
    BranchProfile profile;

    EditText managerText;
    TextView cityText;
    TextView addressText;
    TextView phoneText;
    TextView hoursText;

    TextView noServiceText;

    List<Service> servicesOffered;
    ListView listViewServicesOfferedBranchP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        logOutBtn = (Button) findViewById(R.id.logOutBtnBranch);
        toEditProfile = (Button) findViewById(R.id.toEditP);
        toSeeRequests = (Button) findViewById(R.id.seeRequestBtn);

        auth = FirebaseAuth.getInstance();
        uid =  auth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        profileDatabase = FirebaseDatabase.getInstance().getReference("Branches");

        managerText = (EditText)findViewById(R.id.manager);
        cityText = (TextView) findViewById(R.id.branchCityP);
        addressText = (TextView) findViewById(R.id.branchAddressP);
        phoneText = (TextView) findViewById(R.id.branchPhoneP);
        hoursText = (TextView) findViewById(R.id.branchHoursP);
        noServiceText = (TextView) findViewById(R.id.NoServiceText);

        servicesOffered = new ArrayList<>();
        listViewServicesOfferedBranchP = (ListView) findViewById(R.id.listViewServicesOfferedBranch);

        ShowListService();

        if(!uid.isEmpty()){
            getUserData();
        }
        getBranchProfile();

        //logout the Employee
        logOutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                Toast.makeText(EmployeeActivity.this, "successfully logged out!!", Toast.LENGTH_LONG).show();
                finish();
            }

        });
        //to see requests
        toSeeRequests.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), BranchRequest.class));
            }

        });
        //to Edit profile
        toEditProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), ManageBranch.class));
                finish();
            }

        });

    }
    //to show services offered by branch
    protected void ShowListService() {
        profileDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    servicesOffered.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if(postSnapshot.getValue(BranchProfile.class).getId().equals(uid)) {
                            if (postSnapshot.getValue(BranchProfile.class).getBranchServices() != null) {
                                for (int i = 0; i < postSnapshot.getValue(BranchProfile.class).getBranchServices().size(); i++) {
                                    Service service = postSnapshot.getValue(BranchProfile.class).getBranchServices().get(i);
                                    servicesOffered.add(service);
                                }
                                noServiceText.setText("");
                            } else {
                                noServiceText.setText("There is no service to show! Add a service!");
                            }
                        }
                    }
                    ServiceList serviceAdapter = new ServiceList(EmployeeActivity.this, servicesOffered);
                    listViewServicesOfferedBranchP.setAdapter(serviceAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //to get manager name
    private void getUserData() {
        mDatabase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                String theText = user.username + ", this branch of Byblos is managed by you.";
                managerText.setText(theText);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeActivity.this, "something went wrong!!", Toast.LENGTH_LONG).show();
            }
        });

    }

    //to get the branch info
    private void getBranchProfile(){
        profileDatabase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    profile = dataSnapshot.getValue(BranchProfile.class);
                    String cityF = "The branch city: " + profile.getBranchCity();
                    cityText.setText(cityF);
                    String addressF = "The branch Address: " + profile.getBranchAddress();
                    addressText.setText(addressF);
                    String phoneF = "The branch phone number: " + profile.getBranchPhone();
                    phoneText.setText(phoneF);
                    String hoursF = "open Mon-Fri, from ["+profile.getBranchOpen()+" am] to ["+profile.getBranchClose()+" pm]";
                    hoursText.setText(hoursF);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeActivity.this, "something went wrong!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}