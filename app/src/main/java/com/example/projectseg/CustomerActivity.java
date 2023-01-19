package com.example.projectseg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
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

public class CustomerActivity extends AppCompatActivity {

    FirebaseAuth auth;

    List<BranchProfile> branchesFound;
    DatabaseReference profileDatabase;     //branch profiles database
    ListView listViewBranchesFound;

    EditText editTextCity;
    EditText editTextOpening;
    EditText editTextServiceName;

    TextView showingResults;

    Button goToSeeRequests;
    Button logOutBtn;
    Button searchByCityBtn;
    Button searchByHourBtn;
    Button searchByService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);


        auth = FirebaseAuth.getInstance();

        //logout the customer
        logOutBtn = (Button) findViewById(R.id.logOutBtnCustomer);
        logOutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                Toast.makeText(CustomerActivity.this, "successfully logged out!!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        //send the customer to see status requests
        goToSeeRequests = (Button) findViewById(R.id.seeRequestStatusBtn);
        goToSeeRequests.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), CustomerRequestStatus.class));
            }
        });

        //list and database
        listViewBranchesFound = (ListView) findViewById(R.id.listViewBranchesFound);
        branchesFound = new ArrayList<>();
        profileDatabase = FirebaseDatabase.getInstance().getReference("Branches");

        //search fields
        editTextCity = (EditText) findViewById(R.id.editTextCityS);
        editTextOpening = (EditText) findViewById(R.id.editTextHourS);
        editTextServiceName = (EditText) findViewById(R.id.editTextServiceS);
        showingResults = (TextView) findViewById(R.id.searchResults);

        //upon click search by address
        searchByCityBtn = (Button) findViewById(R.id.searchByAddBtn);
        searchByCityBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                branchesFound.clear();
                editTextOpening.setText("");
                editTextServiceName.setText("");
                ShowListService1();
            }
        });
        //upon click search by hour
        searchByHourBtn = (Button) findViewById(R.id.searchByHourBtn);
        searchByHourBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                branchesFound.clear();
                editTextCity.setText("");
                editTextServiceName.setText("");
                ShowListService2();
            }
        });
        //upon click search by service
        searchByService = (Button) findViewById(R.id.searchByServiceBtn);
        searchByService.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                branchesFound.clear();
                editTextOpening.setText("");
                editTextCity.setText("");
                ShowListService3();
            }
        });

        //choose a branch by long click
        listViewBranchesFound.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                BranchProfile profile = branchesFound.get(i);
                Intent myIntent = new Intent(CustomerActivity.this, ServiceRequest.class);
                myIntent.putExtra("key",profile.getId());
                startActivity(myIntent);
                return true;
            }
        });


    }

    //to show branch profiles found by city
    protected void ShowListService1() {

        String city = editTextCity.getText().toString().trim();
        if(TextUtils.isEmpty(city)){
            Toast.makeText(CustomerActivity.this, "Error! please enter city!(case sensitive)", Toast.LENGTH_LONG).show();
            showingResults.setText("See results here");
        }else {
            profileDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        branchesFound.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if(postSnapshot.getValue(BranchProfile.class).getBranchCity()!=null) {
                                if (postSnapshot.getValue(BranchProfile.class).getBranchCity().equals(city)) {
                                    BranchProfile profile = postSnapshot.getValue(BranchProfile.class);
                                    branchesFound.add(profile);
                                }
                            }
                        }
                        if (!branchesFound.isEmpty()) {
                            showingResults.setText("Results found based on your search by city");
                        } else {
                            showingResults.setText("No matched found based on your search");
                        }
                        BranchProfileList branchAdapter = new BranchProfileList(CustomerActivity.this, branchesFound);
                        listViewBranchesFound.setAdapter(branchAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
    //to show branch profiles found by hour
    protected void ShowListService2() {

        String open = editTextOpening.getText().toString().trim();
        if(TextUtils.isEmpty(open)){

            Toast.makeText(CustomerActivity.this, "Error! please enter opening hour", Toast.LENGTH_LONG).show();
            showingResults.setText("See results here");
        }
        else if(!TextUtils.isDigitsOnly(open)){

            Toast.makeText(CustomerActivity.this, "Error! please enter only digits for hour", Toast.LENGTH_LONG).show();
        } else {
            profileDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        branchesFound.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if(postSnapshot.getValue(BranchProfile.class).getBranchOpen()!=null) {
                                if (postSnapshot.getValue(BranchProfile.class).getBranchOpen().equals(open)) {
                                    BranchProfile profile = postSnapshot.getValue(BranchProfile.class);
                                    branchesFound.add(profile);
                                }
                            }
                        }
                        if (!branchesFound.isEmpty()) {
                            showingResults.setText("Results found based on your search by hours");
                        } else {
                            showingResults.setText("No matched found based on your search");
                        }
                        BranchProfileList branchAdapter = new BranchProfileList(CustomerActivity.this, branchesFound);
                        listViewBranchesFound.setAdapter(branchAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
    //to show branch profiles found by service name
    protected void ShowListService3() {

        String serviceNameS = editTextServiceName.getText().toString().trim();
        if(TextUtils.isEmpty(serviceNameS)){

            Toast.makeText(CustomerActivity.this, "Error! please enter service name!", Toast.LENGTH_LONG).show();
            showingResults.setText("See results here");
        }else {
            profileDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        branchesFound.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if (postSnapshot.getValue(BranchProfile.class).getBranchServices() != null) {
                                for (int i = 0; i < postSnapshot.getValue(BranchProfile.class).getBranchServices().size(); i++) {
                                    if (postSnapshot.getValue(BranchProfile.class).getBranchServices().get(i).getServiceName().equals(serviceNameS)) {
                                        BranchProfile profile = postSnapshot.getValue(BranchProfile.class);
                                        branchesFound.add(profile);
                                        break;
                                    }
                                }
                            }
                        }
                        if (!branchesFound.isEmpty()) {
                            showingResults.setText("Results found based on your search by service name");
                        } else {
                            showingResults.setText("No matched found based on your search");
                        }
                        BranchProfileList branchAdapter = new BranchProfileList(CustomerActivity.this, branchesFound);
                        listViewBranchesFound.setAdapter(branchAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}