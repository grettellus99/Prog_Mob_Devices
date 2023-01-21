package com.example.workoutic.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.workoutic.MainActivity;
import com.example.workoutic.Menu;
import com.example.workoutic.R;
import com.example.workoutic.adapters.MessageAdapter;
import com.example.workoutic.models.Message;
import com.example.workoutic.models.User;
import com.example.workoutic.notification.APIService;
import com.example.workoutic.notification.Client;
import com.example.workoutic.notification.Data;
import com.example.workoutic.notification.Sender;
import com.example.workoutic.notification.Token;
import com.example.workoutic.notification.WKResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> listMessage;

    private String userid;

    private static final int GALERY_PHOTO_SEND = 1;
    private static final int CAM_PHOTO_SEND = 2;

    private final String ROOT = "ImagenesWorkoutic/";
    private final String IMAGE_ROUTE = ROOT + "CameraImages";

    private String currentPhotoPath;
    private Uri photoURI;

    private String pathImg;
    private File fileImg;
    private Bitmap bitmapImg;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private APIService apiService;
    private boolean notify = false;

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
        firebaseStorage = FirebaseStorage.getInstance();

        username = findViewById(R.id.txt_message_username);
        usericon = findViewById(R.id.cimg_message_icon_user);

        send = findViewById(R.id.imgbtn_message_send);
        cam = findViewById(R.id.imgbtn_message_cam);
        message = findViewById(R.id.txt_message_message);



        Intent i = getIntent();
        userid = i.getStringExtra("userid");
        caller = i.getStringExtra("caller");

        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDB.getReference("Users").child(userid);


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = message.getText().toString();
                if (msg.length() == 0){
                    Toast.makeText(MessageActivity.this, "El texto está vacío", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(firebaseUser.getUid(), userid, msg);
                }
                message.setText("");
            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectOption();


            }
        });

/*
        ActivityResultLauncher<Intent> camLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){

                }
            }
        });*/


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(requestCode == GALERY_PHOTO_SEND && resultCode == RESULT_OK){

            Uri uri = data.getData();
            storageReference = firebaseStorage.getReference("chat_images");
            final StorageReference imageID = storageReference.child(uri.getLastPathSegment());

            imageID.putFile(uri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    sendImage(firebaseUser.getUid(), userid, uri.toString());
                }
            });
        }
        else if(requestCode == CAM_PHOTO_SEND && resultCode == RESULT_OK){
            Toast.makeText(MessageActivity.this, "HALLO!", Toast.LENGTH_SHORT).show();
            storageReference = firebaseStorage.getReference("chat_images");
            final StorageReference imageID = storageReference.child(photoURI.getLastPathSegment());
            imageID.putFile(photoURI).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    sendImage(firebaseUser.getUid(), userid, photoURI.toString());
                }
            });
        }


    }

    public void selectOption(){
        final CharSequence[] options = {"Hacer foto", "Seleccionar imagen", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);
        builder.setTitle("Elija una opción");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(options[i].equals("Hacer foto")){
                    openCam();
                }
                else if(options[i].equals("Seleccionar imagen")){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"),GALERY_PHOTO_SEND);

                }
                else{
                    dialogInterface.dismiss();
                }

            }
        });
        builder.show();
    }

    private void openCam(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAM_PHOTO_SEND);
            }
        }

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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
    public void sendImage(String senderID, String receiverID, String imageURL){
        DatabaseReference databaseReference = firebaseDB.getReference();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("senderID", senderID);
        map.put("receiverID", receiverID);

        map.put("message", "");
        map.put("imageURL", imageURL);
        map.put("time", new SimpleDateFormat("dd/MM/yyyy - HH:mm a", Locale.getDefault()).format(new Date()));
        databaseReference.child("Chats").push().setValue(map);

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid()).child(userid);

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void sendMessage(String senderID, String receiverID, String message){
        DatabaseReference databaseReference = firebaseDB.getReference();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("senderID", senderID);
        map.put("receiverID", receiverID);
        map.put("imageURL", "");
        map.put("message", message);
        map.put("time", new SimpleDateFormat("dd/MM/yyyy - HH:mm a", Locale.getDefault()).format(new Date()));
        databaseReference.child("Chats").push().setValue(map);

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid()).child(userid);

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid)
                .child(firebaseUser.getUid());
        chatRefReceiver.child("id").setValue(firebaseUser.getUid());


        final String msg = message;

        databaseReference = firebaseDB.getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    assert user != null;
                    sendNotification(receiverID, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void sendNotification(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.drawable.ic_user_account_45, username.toUpperCase()+"\n\t"+message, "¡Has recibido un nuevo mensaje!",
                            userid);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<WKResponse>() {
                                @Override
                                public void onResponse(Call<WKResponse> call, Response<WKResponse> response) {
                                    if (response.code() == 200){
                                        assert response.body() != null;
                                        if (response.body().success != 1){
                                            Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<WKResponse> call, Throwable t) {
                                    Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    m.setID(dataSnapshot.getKey());
                    if((m.getReceiverID().equals(id1) && m.getSenderID().equals(id2)) || (m.getReceiverID().equals(id2) && m.getSenderID().equals(id1))){
                        listMessage.add(m);
                    }
                }
                messageAdapter = new MessageAdapter(MessageActivity.this, listMessage, usericon);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){

            }
        });

    }


    public void goMain(View view) {
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public void goMenu(View view) {
        Intent intMenu = new Intent(getApplicationContext(), Menu.class);
        intMenu.putExtra("caller","Message");
        intMenu.putExtra("userid",userid);
        intMenu.putExtra("caller2",caller);
        startActivity(intMenu);
    }
}