package com.example.workoutic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.workoutic.Exercises;
import com.example.workoutic.LoginActivity;
import com.example.workoutic.MainActivity;
import com.example.workoutic.R;
import com.example.workoutic.adapter.MessageAdapter;
import com.example.workoutic.adapter.UserAdapter;
import com.example.workoutic.model.Message;
import com.example.workoutic.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    String caller;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDB;
    private TextView username;
    private CircleImageView usericon;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ImageButton send;
    private ImageButton cam;
    private EditText message;

    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<Message> listMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        recyclerView = findViewById(R.id.rcv_message_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        listMessage = new ArrayList<Message>();




        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();

        username = findViewById(R.id.txt_message_username);
        usericon = findViewById(R.id.cimg_message_icon_user);

        send = findViewById(R.id.imgbtn_message_send);
        cam = findViewById(R.id.imgbtn_message_cam);
        message = findViewById(R.id.txt_message_message);

        Intent i = getIntent();
        String userid = i.getStringExtra("userid");
        caller = i.getStringExtra("caller");

        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDB.getReference("Users").child(userid);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = message.getText().toString();
                if (msg.length() == 0){
                    Toast.makeText(MessageActivity.this, "El texto está vacío", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(firebaseUser.getUid(), userid, msg);
                }
                message.setText("");
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                username.setText(u.getUsername());
                if (u.getUsericon().equals("default")){
                    usericon.setImageResource(R.drawable.ic_user_account_45);
                } else {
                    Glide.with(getApplicationContext()).load(u.getUsericon()).into(usericon);
                }

                getMessagesFromFirebase(firebaseUser.getUid(), userid, u.getUsericon());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void goBack(View view) {

        switch (caller){
            case "UserActivity":
                Intent intUserActivity = new Intent(getApplicationContext(),UserActivity.class);
                startActivity(intUserActivity);
                break;
            case "ChatActivity":
                Intent intChatActivity= new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intChatActivity);
        }
    }

    public void sendMessage(String senderID, String receiverID, String message){
        DatabaseReference databaseReference = firebaseDB.getReference();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("senderID", senderID);
        map.put("receiverID", receiverID);
        map.put("message", message);
        map.put("time", new SimpleDateFormat("dd/MM/yyyy - HH:mm a", Locale.getDefault()).format(new Date()));
        databaseReference.child("Chats").push().setValue(map);
    }

    public void getMessagesFromFirebase(String id1, String id2, String usericon){

        databaseReference = firebaseDB.getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMessage.clear();
                Iterable<DataSnapshot> children = snapshot.getChildren();
                Iterator<DataSnapshot> it = children.iterator();
                DataSnapshot dataSnapshot;
                Message m;
                while(it.hasNext()){
                    dataSnapshot = it.next();
                    m = dataSnapshot.getValue(Message.class);
                    if((m.getReceiverID().equals(id1) && m.getSenderID().equals(id2)) || (m.getReceiverID().equals(id2) && m.getSenderID().equals(id1))){
                        listMessage.add(m);
                    }
                }
                messageAdapter = new MessageAdapter(getApplicationContext(), listMessage, usericon);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}