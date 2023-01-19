package com.example.projectseg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;

//class for requests that a branch receive
public class BranchRequest extends AppCompatActivity {

    DatabaseReference requestsDatabase;
    FirebaseAuth auth;
    ArrayList requestsReceived;

    String requestId;
    Button GoBackBtn;
    TextView showingResults;
    ListView listViewRequestsReceived;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_request);

        requestsDatabase = FirebaseDatabase.getInstance().getReference("Requests");
        auth = FirebaseAuth.getInstance();

        listViewRequestsReceived = (ListView) findViewById(R.id.listviewRequestsH);
        requestsReceived = new ArrayList();
        showingResults = (TextView) findViewById(R.id.showingResultsH);


        //send service request
        GoBackBtn = (Button) findViewById(R.id.GoBackBtnH);
        GoBackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), EmployeeActivity.class));
            }
        });


        //choose a request by long click
        listViewRequestsReceived.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Request request = (Request) requestsReceived.get(i);
                requestId = request.getId();
                Boolean status;
                if(request.getApproved()==false){
                    status= true;
                    updateRequest(request.getId(), request.getCustomerId(), request.getBranchId(),request.getServiceName(),status,request.getInfo());
                } else {
                    status=false;
                    updateRequest(request.getId(), request.getCustomerId(), request.getBranchId(),request.getServiceName(),status,request.getInfo());
                }
                return true;
            }
        });

        ShowListService();

    }
    //to show branch profiles found by city
    protected void updateRequest(String id,String myCustomerId, String myBranchId,String serviceName,Boolean status,ArrayList info) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Requests").child(id);
        Request request = new Request(id,myCustomerId, myBranchId, serviceName, status, info);
        dr.setValue(request);
        Toast.makeText(getApplicationContext(), "Request Updated", Toast.LENGTH_LONG).show();
    }

    //to show branch profiles found by city
    protected void ShowListService() {
        requestsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    requestsReceived.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.getValue(Request.class).getBranchId().equals(auth.getCurrentUser().getUid())) {
                            Request request = postSnapshot.getValue(Request.class);
                            requestsReceived.add(request);
                        }
                    }
                    if (!requestsReceived.isEmpty()) {
                        showingResults.setText("Requests Received from customers");
                    } else {
                        showingResults.setText("No request has been received");
                    }
                    RequestList requestAdapter = new RequestList(BranchRequest.this, requestsReceived);
                    listViewRequestsReceived.setAdapter(requestAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}