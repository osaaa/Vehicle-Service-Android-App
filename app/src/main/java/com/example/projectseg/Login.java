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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {
    EditText email;
    EditText password;
    Button loginBtn;
    TextView toSignupGo;
    FirebaseAuth fAuth;

    public Login(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email =(EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginbtn);
        toSignupGo = (TextView) findViewById(R.id.toSignup);
        fAuth = FirebaseAuth.getInstance();

        //send them to signup
        toSignupGo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, signUp.class));
            }
        });
        //admin and admin
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                logInUser();
            }

        });


    }

    protected void logInUser(){
        //get the strings and trim them
        final String emailC = email.getText().toString().trim();
        String passwordC = password.getText().toString().trim();
        //check if inputs are valid
        if (TextUtils.isEmpty(emailC)){
            email.setError("username is required!");
            return;
        }
        if(TextUtils.isEmpty(passwordC)){
            password.setError("Password is required!");
            return;
        }
        if(password.length()<4){
            password.setError("Password Must be at least 4 char");
            return;
        }

        //Authenticate the user
        fAuth.signInWithEmailAndPassword(emailC,passwordC).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this,"You have been logged in successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Welcome.class));
                }else{
                    Toast.makeText(Login.this, "Error! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
