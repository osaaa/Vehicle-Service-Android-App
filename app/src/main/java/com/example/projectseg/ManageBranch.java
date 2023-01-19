package com.example.projectseg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageBranch extends AppCompatActivity {
    DatabaseReference databaseBranchProfiles;
    DatabaseReference databaseService;
    List<Service> services;
    ListView listViewServicesBranch;
    String uid;

    BranchProfile profile;

    EditText editTextCity;
    EditText editTextAddress;
    EditText editTextPhone;
    EditText editTextOpen;
    EditText editTextClose;

    ArrayList<Service> branchServices;

    EditText editTextAddService;
    EditText editTextDeleteService;
    Button addServiceBtn;
    Button deleteServiceBtn;

    FirebaseAuth auth;
    Button goBackBtn;
    Button setUpdateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_branch);

        //RealTime database
        listViewServicesBranch = (ListView) findViewById(R.id.listViewServicesBranch);
        databaseBranchProfiles = FirebaseDatabase.getInstance().getReference("Branches");
        services = new ArrayList<>();
        databaseService = FirebaseDatabase.getInstance().getReference("Services");

        //EditTexts
        editTextCity = (EditText) findViewById(R.id.EditBranchCity);
        editTextAddress = (EditText) findViewById(R.id.EditBranchAddress);
        editTextPhone = (EditText) findViewById(R.id.EditBranchPhone);
        editTextOpen = (EditText) findViewById(R.id.EditStartHour);
        editTextClose = (EditText) findViewById(R.id.EditCloseHour);
        editTextAddService = (EditText) findViewById(R.id.EditAddService);
        editTextDeleteService = (EditText) findViewById(R.id.EditDeleteService);

        //Buttons
        addServiceBtn = (Button) findViewById(R.id.EditAddServiceBtn);
        deleteServiceBtn = (Button) findViewById(R.id.EditDeleteServiceBtn);
        goBackBtn = (Button) findViewById(R.id.goBackBranch);
        setUpdateBtn = (Button) findViewById(R.id.updateProfileBtn);

        //AuthFirebase
        auth = FirebaseAuth.getInstance();
        uid =  auth.getCurrentUser().getUid();


        //setting the values from before if just to update
        if(databaseBranchProfiles.child(uid)!=null){
            getBranchProfile();
        }

        //send back to branch main page
        goBackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), EmployeeActivity.class));
            }

        });
        //add Service branch
        addServiceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addServiceBranch();
            }

        });

        //delete service branch
        deleteServiceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                deleteServiceBranch();
            }

        });

        //update or create branch profile
        setUpdateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                createBranch();
            }

        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseService.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                services.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Service service = postSnapshot.getValue(Service.class);
                    services.add(service);
                }
                ServiceList serviceAdapter = new ServiceList(ManageBranch.this, services);
                listViewServicesBranch.setAdapter(serviceAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void createBranch(){
        String city = editTextCity.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String open = editTextOpen.getText().toString().trim();
        String close = editTextClose.getText().toString().trim();


        if (!TextUtils.isEmpty(address)) {
            if (!TextUtils.isEmpty(phone)) {
                if(TextUtils.isEmpty(city)){
                    Toast.makeText(this, "Error! Enter city name!", Toast.LENGTH_LONG).show();
                }else if(!TextUtils.isDigitsOnly(editTextPhone.getText().toString().trim())) {
                    Toast.makeText(this, "Error! Enter digits only for phone!", Toast.LENGTH_LONG).show();
                }else if(phone.length()!=10){
                    Toast.makeText(this, "phone number should be 10 digits!", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(open)){
                    Toast.makeText(this, "Error! Add opening hours!", Toast.LENGTH_LONG).show();
                }else if(!TextUtils.isDigitsOnly(open)){
                    Toast.makeText(this, "Error! Enter digits for hours!", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(close)){
                    Toast.makeText(this, "Error! Add closing hours!", Toast.LENGTH_LONG).show();
                }else if(!TextUtils.isDigitsOnly(close)){
                    Toast.makeText(this, "Error! Enter digits for hours!", Toast.LENGTH_LONG).show();
                } else {
                    String id = auth.getCurrentUser().getUid();
                    BranchProfile profile = new BranchProfile(id, city, address, phone, open, close, branchServices);
                    databaseBranchProfiles.child(id).setValue(profile);
                    Toast.makeText(this, "changes saved", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Error! Add phone number!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Error! Add address!", Toast.LENGTH_LONG).show();
        }
    }

    protected void addServiceBranch(){
        String addService = editTextAddService.getText().toString().trim();
        databaseService.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Service service = postSnapshot.getValue(Service.class);
                    if(service.getServiceName().equals(addService)){
                        branchServices.add(service);
                        showSuccessAddService();
                        break;
                    }else{
                        showNoSuccessAddService();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    protected void showSuccessAddService(){
        Toast.makeText(ManageBranch.this, "Added! now save changes!", Toast.LENGTH_LONG).show();
    }
    protected void showNoSuccessAddService(){
        Toast.makeText(ManageBranch.this, "Error! Type correctly or see service exists in list!", Toast.LENGTH_LONG).show();
    }


    protected void deleteServiceBranch(){
        String deleteService = editTextDeleteService.getText().toString().trim();
        if(!branchServices.isEmpty()){
            for (int i=0; i<branchServices.size();i++) {
                if(branchServices.get(i).getServiceName().equals(deleteService)){
                    branchServices.remove(i);
                    Toast.makeText(ManageBranch.this, "Deleted! now save changes!", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(ManageBranch.this, "Type name correctly Or see it exists in services offered!", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(ManageBranch.this, "Services offered by this branch is empty!!", Toast.LENGTH_LONG).show();
        }

    }

    private void getBranchProfile(){
        databaseBranchProfiles.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                profile = dataSnapshot.getValue(BranchProfile.class);
                if(profile!=null) {
                    editTextCity.setText(profile.getBranchCity());
                    editTextAddress.setText(profile.getBranchAddress());
                    editTextPhone.setText(profile.getBranchPhone());
                    editTextOpen.setText(profile.getBranchOpen());
                    editTextClose.setText(profile.getBranchClose());
                    branchServices = profile.getBranchServices();
                    if (branchServices == null) {
                        branchServices = new ArrayList<>();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageBranch.this, "something went wrong!!", Toast.LENGTH_LONG).show();
            }
        });
    }

}