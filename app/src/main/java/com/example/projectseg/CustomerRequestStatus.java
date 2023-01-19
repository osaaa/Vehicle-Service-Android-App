package com.example.projectseg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.awt.font.TextAttribute;
import java.util.ArrayList;

public class CustomerRequestStatus extends AppCompatActivity {

    DatabaseReference requestsDatabase;
    TextView requestsResults;
    Button goBackMainBtn;
    FirebaseAuth auth;
    String myId;
    ArrayList requestsSent;
    ListView requestsSentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request_status);

        auth = FirebaseAuth.getInstance();
        myId = auth.getCurrentUser().getUid();
        requestsResults = (TextView) findViewById(R.id.textViewCustomerRequestStatus);
        requestsSentList = (ListView) findViewById(R.id.listViewSeeRequestsCustomer);
        requestsDatabase = FirebaseDatabase.getInstance().getReference("Requests");
        requestsSent = new ArrayList();

        //send the customer back to main page
        goBackMainBtn = (Button) findViewById(R.id.goBackMainStatus);
        goBackMainBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), CustomerActivity.class));
            }
        });
        ShowListRequest();



    }
    //to show branch profiles found by city
    protected void ShowListRequest() {
        requestsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    requestsSent.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if(postSnapshot.getValue(Request.class).getCustomerId()!=null) {
                            if (postSnapshot.getValue(Request.class).getCustomerId().equals(myId)) {
                                Request request = postSnapshot.getValue(Request.class);
                                requestsSent.add(request);
                            }
                        }
                    }
                    if (!requestsSent.isEmpty()) {
                        requestsResults.setText("All the requests that you have sent");
                    } else {
                        requestsResults.setText("You have sent no requests");
                    }
                    RequestList requestAdapter = new RequestList(CustomerRequestStatus.this, requestsSent);
                    requestsSentList.setAdapter(requestAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}