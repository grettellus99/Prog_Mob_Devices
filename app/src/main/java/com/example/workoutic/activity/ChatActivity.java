package com.example.workoutic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;

import com.example.workoutic.LoginActivity;
import com.example.workoutic.MainActivity;
import com.example.workoutic.Menu;
import com.example.workoutic.R;
import com.example.workoutic.adapter.MessageAdapter;
import com.example.workoutic.adapter.UserAdapter;
import com.example.workoutic.model.Message;
import com.example.workoutic.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDB;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> listUser;
    private List<String> listUserID;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();

        recyclerView = findViewById(R.id.rcv_chat_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        listUserID = new ArrayList<String>();
        listUser = new ArrayList<User>();

        addFrequentChat();

        floatingActionButton = findViewById(R.id.fab_add_user);
        goUser();

    }

    public void goUser(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, UserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    public void goBack(View view) {
        Intent intMain = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intMain);
    }

    public void goMain(View view) {
        Intent intMain = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intMain);
    }

    public void goMenu(View view) {
        Intent intMenu = new Intent(getApplicationContext(), Menu.class);
        intMenu.putExtra("caller","Chat");
        startActivity(intMenu);
    }

    public void addFrequentChat(){
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDB.getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUserID.clear();
                Iterable<DataSnapshot> children = snapshot.getChildren();
                Iterator<DataSnapshot> it = children.iterator();
                DataSnapshot dataSnapshot;
                Message m;
                while(it.hasNext()){
                    dataSnapshot = it.next();
                    m = dataSnapshot.getValue(Message.class);

                    if(m.getSenderID().equals(firebaseUser.getUid())){
                        listUserID.add(m.getReceiverID());
                    }
                    if(m.getReceiverID().equals(firebaseUser.getUid())){
                        listUserID.add(m.getSenderID());
                    }
                }

                getMessagesFromFirebase();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getMessagesFromFirebase(){

        databaseReference = firebaseDB.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                Iterable<DataSnapshot> children = snapshot.getChildren();
                Iterator<DataSnapshot> it = children.iterator();
                DataSnapshot dataSnapshot;
                User u;
                int sizeListUserID = listUserID.size();
                int sizeListUser = listUser.size();
                while(it.hasNext()){
                    dataSnapshot = it.next();
                    u = dataSnapshot.getValue(User.class);

                    for(int i = 0; i < sizeListUserID; i++){
                        if(u.getId().equals(listUserID.get(i))){

                            if(sizeListUser != 0){
                                for(int j = 0; j < sizeListUser; j++){
                                    if(!u.getId().equals(listUser.get(j).getId())){
                                        listUser.add(u);
                                    }
                                }
                            }
                            else{
                                listUser.add(u);
                            }

                        }
                    }


                }
                userAdapter = new UserAdapter(getApplicationContext(), listUser, 1);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}