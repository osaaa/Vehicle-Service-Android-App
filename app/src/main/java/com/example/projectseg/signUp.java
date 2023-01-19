package com.example.projectseg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class signUp extends AppCompatActivity {
    RadioButton branch;
    RadioButton customer;
    EditText username;
    EditText password;
    EditText email;
    Button signUpBtn;
    TextView toLogin;
    FirebaseAuth fAuth;
    DatabaseReference databaseAccounts;
    List<Service> accounts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = (EditText) findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        email = (EditText)findViewById(R.id.email);
        branch = (RadioButton) findViewById(R.id.branchOp);
        customer = (RadioButton) findViewById(R.id.customerOp);

        //initialize firebase
        fAuth = FirebaseAuth.getInstance();

        signUpBtn = (Button) findViewById(R.id.signupbtn);
        toLogin = (TextView) findViewById(R.id.tologin);

        accounts = new ArrayList<>();
        databaseAccounts = FirebaseDatabase.getInstance().getReference("Users");

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                createUser();
            }

        });

        //send them to login page
        toLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signUp.this, Login.class));
            }
        });


    }

    //for when user already logged in
    @Override
    protected void onStart() {
        super.onStart();
        if(fAuth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),Welcome.class));
            finish();
        }
    }

    protected void createUser(){
        final String usernameC = username.getText().toString().trim();
        String passwordC = password.getText().toString().trim();
        final String emailC = email.getText().toString().trim();
        if (TextUtils.isEmpty(usernameC)){
            username.setError("username is required!");
            return;
        }
        if (passwordC.length()<6){
            password.setError("Password must be at least 6 characters!");
            return;
        }
        if(TextUtils.isEmpty(passwordC)){
            password.setError("Password is required!");
            return;
        }
        if(TextUtils.isEmpty(emailC)){
            email.setError("email is required!");
            return;
        }
        fAuth.createUserWithEmailAndPassword(emailC, passwordC).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User newUser=null;
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    if(branch.isChecked() && !customer.isChecked()){

                        if(emailC.equals("admin@admin.com")){
                            newUser = new User(id,usernameC, passwordC, emailC, User.Role.Admin);
                        }else{
                            newUser = new User(id,usernameC, passwordC, emailC, User.Role.Employee);
                        }
                        databaseAccounts.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>(){
                            @Override
                            public void onComplete(@NonNull Task<Void> task){
                                if(task.isSuccessful()){
                                    Toast.makeText(signUp.this, "You have been registered successfully",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),Welcome.class));
                                }else{
                                    Toast.makeText(signUp.this, "Error! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else if(!branch.isChecked() && customer.isChecked()){

                        newUser = new User(id,usernameC, passwordC, emailC, User.Role.Customer);
                        databaseAccounts.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>(){
                            @Override
                            public void onComplete(@NonNull Task<Void> task){
                                if(task.isSuccessful()){
                                    Toast.makeText(signUp.this, "You have been registered successfully",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),Welcome.class));
                                }else{
                                    Toast.makeText(signUp.this, "Error! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else if(!branch.isChecked() && !customer.isChecked()){
                        Toast.makeText(signUp.this, "Please choose account type",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(signUp.this, "Error! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
