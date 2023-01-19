package com.example.projectseg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class ManageAccounts extends AppCompatActivity {
    ListView listViewUsers;
    List<User> users;
    DatabaseReference databaseUsers;
    Button goBackTo;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accounts);

        goBackTo = (Button) findViewById(R.id.goBack);
        //take back the Admin
        goBackTo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
            }

        });
        /////////////////////////////************************
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        users = new ArrayList<>();
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        
        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = users.get(i);
                showUpdateDeleteDialog(user.getId(), user.getUsername());
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    User user = postSnapshot.getValue(User.class);
                    users.add(user);
                }
                UserList userAdapter = new UserList(ManageAccounts.this, users);
                listViewUsers.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showUpdateDeleteDialog(final String userId, String usernameC) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);

        dialogBuilder.setTitle(usernameC);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTheUser(userId);
                b.dismiss();
                
            }
        });
        
    }
    private boolean deleteTheUser(String id) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Users").child(id);
        dr.removeValue();
        Toast.makeText(getApplicationContext(), "Account Deleted", Toast.LENGTH_LONG).show();
        return true;
    }
}