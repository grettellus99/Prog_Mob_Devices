package com.example.workoutic.signin_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.workoutic.MainActivity;
import com.example.workoutic.Menu;
import com.example.workoutic.R;
import com.example.workoutic.chat.ChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText username, email, password, passwordValidation;
    Button btn_register;
    ProgressBar pb;

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.usernameRe);
        email = findViewById(R.id.emailRe);
        password = findViewById(R.id.passwordRe);
        passwordValidation = findViewById(R.id.passwordRe2);
        btn_register = findViewById(R.id.btn_registerRe);
        pb = findViewById(R.id.pb_create_acount);

        auth = FirebaseAuth.getInstance();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_valid_password = passwordValidation.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_valid_password) ){
                    Toast.makeText(RegisterActivity.this, R.string.msg_regist_void, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("caller",getIntent().getStringExtra("caller"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else if (txt_password.length() < 6 ){
                    Toast.makeText(RegisterActivity.this, R.string.msg_six_caracters, Toast.LENGTH_LONG).show();
                }else if(!txt_password.equals(txt_valid_password)){
                    Toast.makeText(RegisterActivity.this, R.string.msg_dif_password, Toast.LENGTH_LONG).show();
                }
                else {
                    pb.setVisibility(View.VISIBLE);
                    btn_register.setVisibility(View.GONE);
                    register(txt_username, txt_email, txt_password);
                }
            }
        });
    }

    private void register(String username, String email, String password){

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    currentUser = auth.getCurrentUser();
                    assert currentUser != null;
                    String userid = currentUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    Map<String, String> userObj = new HashMap<>();
                    userObj.put("id", userid);
                    userObj.put("username", username);
                    userObj.put("usericon", "default");
                    userObj.put("email", email);


                    reference.setValue(userObj)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pb.setVisibility(View.INVISIBLE);
                                    // Ir a chat
                                    Toast.makeText(RegisterActivity.this, R.string.user_added, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, ChatActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, R.string.msg_err_register, Toast.LENGTH_LONG).show();
                                    btn_register.setVisibility(View.VISIBLE);
                                    pb.setVisibility(View.GONE);
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, R.string.msg_err_register, Toast.LENGTH_LONG).show();
                    btn_register.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, R.string.msg_err_register, Toast.LENGTH_LONG).show();
                        btn_register.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                    }
                });
    }

    public void goMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void goMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        intent.putExtra("caller","Register");
        String caller = getIntent().getStringExtra("caller");
        intent.putExtra("caller2",caller);
        intent.putExtra("caller3",getIntent().getStringExtra("caller3"));
        intent.putExtra("userid",getIntent().getStringExtra("userid"));
        startActivity(intent);
    }

    public void goBack(View view) {
        String caller = getIntent().getStringExtra("caller");
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.putExtra("caller2",getIntent().getStringExtra("caller3"));
        intent.putExtra("caller",caller);
        intent.putExtra("userid",getIntent().getStringExtra("userid"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}