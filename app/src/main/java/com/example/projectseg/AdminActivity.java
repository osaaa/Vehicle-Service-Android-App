package com.example.projectseg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class AdminActivity extends AppCompatActivity {
    Button logOutBtn;
    Button manageAccount;
    FirebaseAuth auth;
    EditText editTextName;
    EditText editTextPrice;
    Button buttonAddService;
    ListView listViewServices;
    private ArrayList<String> info;


    List<Service> services;

    DatabaseReference databaseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        logOutBtn = (Button) findViewById(R.id.logOutBtnAdmin);
        auth = FirebaseAuth.getInstance();



        //logout the Admin
        logOutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                Toast.makeText(AdminActivity.this, "successfully logged out!!", Toast.LENGTH_LONG).show();
                finish();
            }

        });
        manageAccount = (Button) findViewById(R.id.manageAccounts);
        //Go to managing accounts
        manageAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), ManageAccounts.class));
            }

        });
        /////////////////////////////************************
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        listViewServices = (ListView) findViewById(R.id.listViewServices);
        buttonAddService = (Button) findViewById(R.id.addButton);

        services = new ArrayList<>();
        databaseService = FirebaseDatabase.getInstance().getReference("Services");
        //adding an to button
        info = new ArrayList<String>();
        buttonAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox address = findViewById(R.id.address);
                if (address.isChecked()){info.add("Address");}
                CheckBox phone = findViewById(R.id.phone);
                if (phone.isChecked()){info.add("Phone number");}
                CheckBox firstName = findViewById(R.id.firstName);
                if (firstName.isChecked()){info.add("first name");}
                CheckBox lastName = findViewById(R.id.lastName);
                if (lastName.isChecked()){info.add("last name");}
                CheckBox birth = findViewById(R.id.birth);
                if (birth.isChecked()){info.add("Birth date");}
                CheckBox licence = findViewById(R.id.licence);
                if (licence.isChecked()){info.add("Licence type");}
                CheckBox emailR = findViewById(R.id.emailX);
                if (emailR.isChecked()){info.add("Email");}
                CheckBox date = findViewById(R.id.date);
                if (date.isChecked()){info.add("Date");}
                CheckBox timeR = findViewById(R.id.time);
                if (timeR.isChecked()){info.add("Time");}
                addService();
                info.clear();
                address.setChecked(false);
                phone.setChecked(false);
                firstName.setChecked(false);
                lastName.setChecked(false);
                birth.setChecked(false);
                licence.setChecked(false);
                emailR.setChecked(false);
                date.setChecked(false);
                timeR.setChecked(false);
            }
        });

        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Service service = services.get(i);
                showUpdateDeleteDialog(service.getId(), service.getServiceName(), service.getPrice(), service.getInfo());
                return true;
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
                ServiceList serviceAdapter = new ServiceList(AdminActivity.this, services);
                listViewServices.setAdapter(serviceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showUpdateDeleteDialog(final String productId, String theServiceName, double thePrice, ArrayList<String> infoU) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);


        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextPrice  = (EditText) dialogView.findViewById(R.id.editTextPrice);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProduct);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);

        //check the checked boxes to and load them on the update dialog************************************************************
        CheckBox addressU = (CheckBox) dialogView.findViewById(R.id.addressUpdate); //use dialogView
        CheckBox phoneU = (CheckBox) dialogView.findViewById(R.id.phoneUpdate);
        CheckBox firstNameU = (CheckBox) dialogView.findViewById(R.id.firstNameUpdate);
        CheckBox lastNameU = (CheckBox) dialogView.findViewById(R.id.lastNameUpdate);
        CheckBox birthU = (CheckBox) dialogView.findViewById(R.id.birthUpdate);
        CheckBox licenceU = (CheckBox) dialogView.findViewById(R.id.licenceUpdate);
        CheckBox emailU = (CheckBox) dialogView.findViewById(R.id.emailXUpdate);
        CheckBox dateU = (CheckBox) dialogView.findViewById(R.id.dateUpdate);
        CheckBox timeU = (CheckBox) dialogView.findViewById(R.id.timeUpdate);
        if(infoU!=null){ //check if the info array list is not empty
            for (int t=0; t<infoU.size();t++){
                if(infoU.get(t).equals("Address")){addressU.setChecked(true);}
                if(infoU.get(t).equals("Phone number")){phoneU.setChecked(true);}
                if(infoU.get(t).equals("first name")){firstNameU.setChecked(true);}
                if(infoU.get(t).equals("last name")){lastNameU.setChecked(true);}
                if(infoU.get(t).equals("Birth date")){birthU.setChecked(true);}
                if(infoU.get(t).equals("Licence type")){licenceU.setChecked(true);}
                if(infoU.get(t).equals("Email")){emailU.setChecked(true);}
                if(infoU.get(t).equals("Date")){dateU.setChecked(true);}
                if(infoU.get(t).equals("Time")){timeU.setChecked(true);}
            }
        }
        //*********************************************************************************************
        dialogBuilder.setTitle(theServiceName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String price = editTextPrice.getText().toString().trim();
                ArrayList<String> newInfoUpdate = new ArrayList<String>();

                CheckBox addressNew = (CheckBox) dialogView.findViewById(R.id.addressUpdate); //use dialogView
                if (addressNew.isChecked()){newInfoUpdate.add("Address");}
                CheckBox phoneNew = (CheckBox) dialogView.findViewById(R.id.phoneUpdate);
                if (phoneNew.isChecked()){newInfoUpdate.add("Phone number");}
                CheckBox firstNameNew = (CheckBox) dialogView.findViewById(R.id.firstNameUpdate);
                if (firstNameNew.isChecked()){newInfoUpdate.add("first name");}
                CheckBox lastNameNew = (CheckBox) dialogView.findViewById(R.id.lastNameUpdate);
                if (lastNameNew.isChecked()){newInfoUpdate.add("last name");}
                CheckBox birthNew = (CheckBox) dialogView.findViewById(R.id.birthUpdate);
                if (birthNew.isChecked()){newInfoUpdate.add("Birth date");}
                CheckBox licenceNew = (CheckBox) dialogView.findViewById(R.id.licenceUpdate);
                if (licenceNew.isChecked()){newInfoUpdate.add("Licence type");}
                CheckBox emailNew = (CheckBox) dialogView.findViewById(R.id.emailXUpdate);
                if (emailNew.isChecked()){newInfoUpdate.add("Email");}
                CheckBox dateNew = (CheckBox) dialogView.findViewById(R.id.dateUpdate);
                if (dateNew.isChecked()){newInfoUpdate.add("Date");}
                CheckBox timeNew = (CheckBox) dialogView.findViewById(R.id.timeUpdate);
                if (timeNew.isChecked()){newInfoUpdate.add("Time");}

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(String.valueOf(price))) {
                    updateService(productId, name, Double.valueOf(price), newInfoUpdate);
                    b.dismiss();
                }else if(TextUtils.isEmpty(name) && TextUtils.isEmpty(String.valueOf(price)))   {
                    if(!TextUtils.isDigitsOnly(price)){
                        Toast.makeText(AdminActivity.this, "Please enter digits only for price!!", Toast.LENGTH_LONG).show();
                    } else {
                        updateService(productId, theServiceName, thePrice, newInfoUpdate);
                        b.dismiss();
                    }
                }else if(!TextUtils.isEmpty(name) && TextUtils.isEmpty(String.valueOf(price))){
                    updateService(productId, name, thePrice, newInfoUpdate);
                    b.dismiss();
                }else if(TextUtils.isEmpty(name) && !TextUtils.isEmpty(String.valueOf(price))){
                    if(!TextUtils.isDigitsOnly(price)){
                        Toast.makeText(AdminActivity.this, "Please enter digits only for price!!", Toast.LENGTH_LONG).show();
                    } else {
                        updateService(productId, theServiceName, Double.valueOf(price), newInfoUpdate);
                        b.dismiss();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteService(productId);
                b.dismiss();
            }
        });
    }

    private void updateService(String id, String name, double price, ArrayList<String> info) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Services").child(id);
        Service service = new Service(id, name, price, info);
        dr.setValue(service);


        Toast.makeText(getApplicationContext(), "Service Updated", Toast.LENGTH_LONG).show();
    }

    private boolean deleteService(String id) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Services").child(id);
        dr.removeValue();
        Toast.makeText(getApplicationContext(), "Service Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    private void addService() {
        String name = editTextName.getText().toString().trim();
        String value = editTextPrice.getText().toString().trim();
        if (!TextUtils.isEmpty(value)) {
            if (!TextUtils.isEmpty(name)) {
                if(!TextUtils.isDigitsOnly(value)){
                    Toast.makeText(this, "Please enter digits only!", Toast.LENGTH_LONG).show();
                } else {
                    double price = Double.parseDouble(String.valueOf(editTextPrice.getText().toString()));
                    String id = databaseService.push().getKey();
                    Service service = new Service(id, name, price, info);
                    databaseService.child(id).setValue(service);
                    editTextName.setText("");
                    editTextPrice.setText("");
                    Toast.makeText(this, "Service added", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(this, "Something went wrong! Add name and price", Toast.LENGTH_LONG).show();
        }
    }
}