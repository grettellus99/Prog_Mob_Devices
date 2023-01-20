package com.example.workoutic.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.example.workoutic.MainActivity;
import com.example.workoutic.Menu;
import com.example.workoutic.R;
import com.example.workoutic.adapters.UserAdapter;
import com.example.workoutic.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDB;
    private SearchView search;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> listUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();

        search = findViewById(R.id.sv_user_seach);

        recyclerView = findViewById(R.id.rcv_user_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        listUser = new ArrayList<User>();

        getUsersFromFirebase();

        search.setOnQueryTextListener(this);
    }


    public void goBack(View view) {
        Intent intMain = new Intent(getApplicationContext(), ChatActivity.class);
        startActivity(intMain);
    }

    public void goMain(View view) {
        Intent intMain = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intMain);
    }

    public void goMenu(View view) {
        Intent intMenu = new Intent(getApplicationContext(), Menu.class);
        intMenu.putExtra("caller","User");
        startActivity(intMenu);
    }

    public void getUsersFromFirebase(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference = firebaseDB.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                Iterable<DataSnapshot> children = snapshot.getChildren();
                Iterator<DataSnapshot> it = children.iterator();
                DataSnapshot dataSnapshot;
                User u;
                while(it.hasNext()){
                    dataSnapshot = it.next();
                    u = dataSnapshot.getValue(User.class);
                    assert  u != null;
                    assert firebaseUser != null;
                    if(!u.getId().equals(firebaseUser.getUid())){
                        listUser.add(u);
                    }
                }
                userAdapter = new UserAdapter(getApplicationContext(), listUser, 0);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void searchUser(String s){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        // "\uf8ff" is scape
        Query query = firebaseDB.getReference("Users").orderByChild("username").startAt(s.toUpperCase()).endAt(s.toLowerCase() +"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                Iterable<DataSnapshot> children = snapshot.getChildren();
                Iterator<DataSnapshot> it = children.iterator();
                DataSnapshot dataSnapshot;
                User u;
                while(it.hasNext()){
                    dataSnapshot = it.next();
                    u = dataSnapshot.getValue(User.class);
                    assert  u != null;
                    assert firebaseUser != null;
                    if(!u.getId().equals(firebaseUser.getUid())){
                        listUser.add(u);
                    }
                }
                userAdapter = new UserAdapter(getApplicationContext(), listUser, 0);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        userAdapter.filter(s);
        return false;
    }
}