package com.example.workoutic.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.workoutic.MainActivity;
import com.example.workoutic.Menu;
import com.example.workoutic.R;
import com.example.workoutic.adapters.UserAdapter;
import com.example.workoutic.models.Chatlist;
import com.example.workoutic.models.User;
import com.example.workoutic.notification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

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
    //private List<String> listUserID;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private List<Chatlist> listUserID;

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


        firebaseUser = firebaseAuth.getCurrentUser();

        listUserID = new ArrayList<>();

        databaseReference = firebaseDB.getReference("Chatlist").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUserID.clear();
                Iterable<DataSnapshot> children = snapshot.getChildren();
                Iterator<DataSnapshot> it = children.iterator();
                DataSnapshot dataSnapshot;
                Chatlist chatlist;
                while(it.hasNext()){
                    dataSnapshot = it.next();
                    chatlist = dataSnapshot.getValue(Chatlist.class);
                    listUserID.add(chatlist);
                }

                mostrarChatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.w("TOKEN", "Fetching FCM registration token failed", task.getException());
                }
                String token = task.getResult();
                updateToken(token);
            }
        });
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
       goMain(view);
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

    private void updateToken(String token){
        DatabaseReference reference = firebaseDB.getReference("Tokens");
        Token t = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(t);
    }

    public void mostrarChatList(){
        listUser = new ArrayList<User>();
        databaseReference = firebaseDB.getReference("Users");
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
                    for(Chatlist chatlist:listUserID){
                        if(u.getId().equals(chatlist.getId())){
                            listUser.add(u);
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

    public void addFrequentChat(){
        /*
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

                    assert m != null;
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
        })*/
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