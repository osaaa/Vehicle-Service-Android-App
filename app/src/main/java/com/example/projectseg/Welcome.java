package com.example.projectseg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Welcome extends AppCompatActivity {
    Button logOutBtn;
    DatabaseReference mDatabase;
    FirebaseAuth auth;
    String uid;
    User user;
    TextView myText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        auth = FirebaseAuth.getInstance();
        uid =  auth.getCurrentUser().getUid();
        myText = (TextView) findViewById(R. id. display);
        logOutBtn = (Button) findViewById(R.id.logoutbtn);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        if(!uid.isEmpty()){
            getUserData();
        }
        //logout the user
        logOutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                Toast.makeText(Welcome.this, "successfully logged out!!", Toast.LENGTH_LONG).show();
                finish();
            }

        });
    }

    private void getUserData() {
        mDatabase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if(user!=null) {
                    if (user.getEmail().equals("admin@admin.com")) {
                        startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                        finish();
                    }else if(user.getRole()== User.Role.Employee){
                        startActivity(new Intent(getApplicationContext(), EmployeeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), CustomerActivity.class));
                        finish();
                    }
                }
                if(user==null){
                    auth.signOut();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    Toast.makeText(Welcome.this, "Your account is deleted!!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Welcome.this, "something went wrong 222222!!", Toast.LENGTH_LONG).show();
            }
        });

    }

}