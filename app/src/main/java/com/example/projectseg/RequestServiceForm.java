package com.example.projectseg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestServiceForm extends AppCompatActivity {
    DatabaseReference serviceDatabase;
    String serviceId;
    String branchId;
    Service service;
    ArrayList serviceInfo;

    FirebaseAuth auth;
    DatabaseReference requestDatabase;
    ArrayList requestInfo;

    Button goBackBtnZ;
    Button submitRequestBtn;
    boolean flagError=true;

    EditText firstName;
    EditText lastName;
    EditText address;
    EditText phone;
    EditText birthDate;
    EditText licence;
    EditText email;
    EditText date;
    EditText time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service_form);

        serviceId = getIntent().getStringExtra("keyService");
        branchId = getIntent().getStringExtra("myBranchId");

        serviceDatabase = FirebaseDatabase.getInstance().getReference("Services");
        requestDatabase = FirebaseDatabase.getInstance().getReference("Requests");
        auth = auth = FirebaseAuth.getInstance();

        firstName = (EditText)findViewById(R.id.firstNameZ);
        lastName = (EditText)findViewById(R.id.lastNameZ);
        address = (EditText)findViewById(R.id.addressZ);
        phone = (EditText)findViewById(R.id.phoneZ);
        birthDate = (EditText)findViewById(R.id.birthdateZ);
        licence = (EditText)findViewById(R.id.licenceZ);
        email = (EditText)findViewById(R.id.emailZ);
        date = (EditText)findViewById(R.id.dateZ);
        time = (EditText)findViewById(R.id.timeZ);

        getServiceData();


        //send service request
        submitRequestBtn = (Button) findViewById(R.id.submitRequestZ);
        submitRequestBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addRequest();
                if(flagError==false) {
                    startActivity(new Intent(getApplicationContext(), CustomerActivity.class));
                }
            }
        });


        //send back customer to search
        goBackBtnZ = (Button) findViewById(R.id.goBackZ);
        goBackBtnZ.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), CustomerActivity.class));
            }
        });

    }

    //to get manager name
    private void getServiceData() {
        serviceDatabase.child(serviceId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                service = dataSnapshot.getValue(Service.class);
                if(service.getInfo()!=null) {
                    serviceInfo = new ArrayList();
                    serviceInfo = service.getInfo();
                    for(int i=0;i<serviceInfo.size();i++){
                        if(serviceInfo.get(i).equals("first name")){ firstName.setVisibility(View.VISIBLE); }
                        if(serviceInfo.get(i).equals("last name")){ lastName.setVisibility(View.VISIBLE); }
                        if(serviceInfo.get(i).equals("Address")){ address.setVisibility(View.VISIBLE); }
                        if(serviceInfo.get(i).equals("Phone number")){ phone.setVisibility(View.VISIBLE); }
                        if(serviceInfo.get(i).equals("Birth date")){ birthDate.setVisibility(View.VISIBLE); }
                        if(serviceInfo.get(i).equals("Licence type")){ licence.setVisibility(View.VISIBLE); }
                        if(serviceInfo.get(i).equals("Email")){ email.setVisibility(View.VISIBLE); }
                        if(serviceInfo.get(i).equals("Date")){ date.setVisibility(View.VISIBLE); }
                        if(serviceInfo.get(i).equals("Time")){ time.setVisibility(View.VISIBLE); }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RequestServiceForm.this, "something went wrong!!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void addRequest() {
        requestInfo = new ArrayList();

        if (firstName.getVisibility() == View.VISIBLE){
            System.out.println("oneeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"+firstName.getVisibility());
            String firstNameY = firstName.getText().toString().trim();
            if(!TextUtils.isEmpty(firstNameY)){ requestInfo.add("First name: "+firstNameY); }
            else{ Toast.makeText(this, "Error! Enter first name!", Toast.LENGTH_LONG).show(); return; }
        }
        if (lastName.getVisibility() == View.VISIBLE){
            String lastNameY = lastName.getText().toString().trim();
            if(!TextUtils.isEmpty(lastNameY)){ requestInfo.add("last name: "+lastNameY); }
            else{Toast.makeText(this, "Error! Enter last name!", Toast.LENGTH_LONG).show(); return; }
        }
        if (address.getVisibility() == View.VISIBLE){
            String addressY = address.getText().toString().trim();
            if(!TextUtils.isEmpty(addressY)){ requestInfo.add("Address: "+addressY); }
            else{Toast.makeText(this, "Error! Enter address!", Toast.LENGTH_LONG).show(); return; }
        }
        if (phone.getVisibility() == View.VISIBLE){
            System.out.println("twoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"+phone.getVisibility());
            String phoneY = phone.getText().toString().trim();
            if(!TextUtils.isEmpty(phoneY)){
                if(TextUtils.isDigitsOnly(phoneY)){
                    if(phone.length()==10){
                        requestInfo.add("Phone number: "+phoneY);
                    }else{
                        Toast.makeText(this, "Error!wrong phone number!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }else{
                    Toast.makeText(this, "Error! number only digits!", Toast.LENGTH_LONG).show();
                    return;
                }
            }else{
                Toast.makeText(this, "Error! Enter phone number!", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (birthDate.getVisibility() == View.VISIBLE){
            String birthdateY = birthDate.getText().toString().trim();
            if(!TextUtils.isEmpty(birthdateY)){
                if(TextUtils.isDigitsOnly(birthdateY)){
                    if(birthdateY.length()==8){
                        requestInfo.add("Birth date: "+birthdateY);
                    }else{
                        Toast.makeText(this, "Error! birthdate wrong format!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }else{
                    Toast.makeText(this, "Error! Enter only digits for birthdate!", Toast.LENGTH_LONG).show();
                    return;
                }
            } else{
                Toast.makeText(this, "Error! Enter birthdate!", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (licence.getVisibility() == View.VISIBLE){
            String licenceY = licence.getText().toString().trim();
            if(!TextUtils.isEmpty(licenceY)){ requestInfo.add("Licence type: "+licenceY); }
            else{Toast.makeText(this, "Error! Enter licence type!", Toast.LENGTH_LONG).show(); return; }
        }
        if (email.getVisibility() == View.VISIBLE){
            String emailY = email.getText().toString().trim();
            if(!TextUtils.isEmpty(emailY)){ requestInfo.add("Email: "+emailY); }
            else{Toast.makeText(this, "Error! Enter email!", Toast.LENGTH_LONG).show(); return; }
        }
        if (date.getVisibility() == View.VISIBLE){
            String dateY = date.getText().toString().trim();
            if(!TextUtils.isEmpty(dateY)){
                if(TextUtils.isDigitsOnly(dateY)){
                    if(dateY.length()==8){
                        requestInfo.add("Service Date: "+dateY);
                    }else{
                        Toast.makeText(this, "Error! date wrong format!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }else{
                    Toast.makeText(this, "Error! Enter only digits for date!", Toast.LENGTH_LONG).show();
                    return;
                }
            } else{
                Toast.makeText(this, "Error! Enter date!", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (time.getVisibility() == View.VISIBLE){
            String timeY = time.getText().toString().trim();
            if(!TextUtils.isEmpty(timeY)){ requestInfo.add("Time: "+timeY); }
            else{Toast.makeText(this, "Error! Enter Time!", Toast.LENGTH_LONG).show();  return; }
        }
        flagError=false;
        String customerId = auth.getCurrentUser().getUid();
        String id = requestDatabase.push().getKey();
        Request request = new Request(id,customerId, branchId, service.getServiceName(), false, requestInfo);
        requestDatabase.child(id).setValue(request);

        firstName.setVisibility(View.INVISIBLE);
        lastName.setVisibility(View.INVISIBLE);
        address.setVisibility(View.INVISIBLE);
        phone.setVisibility(View.INVISIBLE);
        birthDate.setVisibility(View.INVISIBLE);
        licence.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        date.setVisibility(View.INVISIBLE);
        time.setVisibility(View.INVISIBLE);

        Toast.makeText(this, "Request sent", Toast.LENGTH_LONG).show();

    }

//    private void getInfoRequest(){
//        requestInfo = new ArrayList();
//        if (firstName.getVisibility() == View.VISIBLE){
//            String firstNameY = firstName.getText().toString().trim();
//            if(!TextUtils.isEmpty(firstNameY)){ requestInfo.add("First name: "+firstNameY); }
//            else{Toast.makeText(this, "Error! Enter first name!", Toast.LENGTH_LONG).show();}
//        }
//        if (lastName.getVisibility() == View.VISIBLE){
//            String lastNameY = lastName.getText().toString().trim();
//            if(!TextUtils.isEmpty(lastNameY)){ requestInfo.add("last name: "+lastNameY); }
//            else{Toast.makeText(this, "Error! Enter last name!", Toast.LENGTH_LONG).show();}
//        }
//        if (address.getVisibility() == View.VISIBLE){
//            String addressY = address.getText().toString().trim();
//            if(!TextUtils.isEmpty(addressY)){ requestInfo.add("Address: "+addressY); }
//            else{Toast.makeText(this, "Error! Enter address!", Toast.LENGTH_LONG).show();}
//        }
//        if (phone.getVisibility() == View.VISIBLE){
//            String phoneY = phone.getText().toString().trim();
//            if(!TextUtils.isEmpty(phoneY)){
//                if(TextUtils.isDigitsOnly(phoneY)){
//                    if(phone.length()==10){
//                        requestInfo.add("Phone number: "+phoneY);
//                    }else{
//                        Toast.makeText(this, "Error!wrong phone number!", Toast.LENGTH_LONG).show();
//                    }
//                }else{
//                    Toast.makeText(this, "Error! number only digits!", Toast.LENGTH_LONG).show();
//                }
//            }else{
//                Toast.makeText(this, "Error! Enter phone number!", Toast.LENGTH_LONG).show();
//            }
//        }
//        if (birthDate.getVisibility() == View.VISIBLE){
//            String birthdateY = birthDate.getText().toString().trim();
//            if(!TextUtils.isEmpty(birthdateY)){
//                if(TextUtils.isDigitsOnly(birthdateY)){
//                    if(birthdateY.length()==8){
//                        requestInfo.add("Birth date: "+birthdateY);
//                    }else{
//                        Toast.makeText(this, "Error! birthdate wrong format!", Toast.LENGTH_LONG).show();
//                    }
//                }else{
//                    Toast.makeText(this, "Error! Enter only digits for birthdate!", Toast.LENGTH_LONG).show();
//                }
//            } else{
//                Toast.makeText(this, "Error! Enter birthdate!", Toast.LENGTH_LONG).show();
//            }
//        }
//        if (licence.getVisibility() == View.VISIBLE){
//            String licenceY = licence.getText().toString().trim();
//            if(!TextUtils.isEmpty(licenceY)){ requestInfo.add("Licence type: "+licenceY); }
//            else{Toast.makeText(this, "Error! Enter licence type!", Toast.LENGTH_LONG).show();}
//        }
//        if (email.getVisibility() == View.VISIBLE){
//            String emailY = email.getText().toString().trim();
//            if(!TextUtils.isEmpty(emailY)){ requestInfo.add("Email: "+emailY); }
//            else{Toast.makeText(this, "Error! Enter email!", Toast.LENGTH_LONG).show();}
//        }
//        if (date.getVisibility() == View.VISIBLE){
//            String dateY = date.getText().toString().trim();
//            if(!TextUtils.isEmpty(dateY)){
//                if(TextUtils.isDigitsOnly(dateY)){
//                    if(dateY.length()==8){
//                        requestInfo.add("Service Date: "+dateY);
//                    }else{
//                        Toast.makeText(this, "Error! date wrong format!", Toast.LENGTH_LONG).show();
//                    }
//                }else{
//                    Toast.makeText(this, "Error! Enter only digits for date!", Toast.LENGTH_LONG).show();
//                }
//            } else{
//                Toast.makeText(this, "Error! Enter date!", Toast.LENGTH_LONG).show();
//            }
//        }
//        if (time.getVisibility() == View.VISIBLE){
//            String timeY = time.getText().toString().trim();
//            if(!TextUtils.isEmpty(timeY)){ requestInfo.add("Time: "+timeY); }
//            else{Toast.makeText(this, "Error! Enter Time!", Toast.LENGTH_LONG).show();}
//        }
//
//
//
//
//    }
}