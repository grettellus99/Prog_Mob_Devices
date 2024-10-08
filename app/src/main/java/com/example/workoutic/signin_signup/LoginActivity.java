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
import android.widget.TextView;
import android.widget.Toast;

import com.example.workoutic.MainActivity;
import com.example.workoutic.Menu;
import com.example.workoutic.R;
import com.example.workoutic.chat.ChatActivity;
import com.example.workoutic.chat.MessageActivity;
import com.example.workoutic.chat.UserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button btn_login;
    Button btn_new_account;

    ProgressBar pb;

    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseAuth.AuthStateListener authStateListener;

    TextView forgot_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        forgot_password = findViewById(R.id.forgot_password);
        btn_new_account = findViewById(R.id.btn_create_new_account);
        pb = findViewById(R.id.pb_login);
        preparingForCheckingLogInUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null){
                    Intent intent;
                    String caller = getIntent().getStringExtra("caller");
                    if(caller != null && caller.equals("User")){
                        intent = new Intent(LoginActivity.this, UserActivity.class);
                    }else if(caller != null && caller.equals("Message")){
                        String actUser = currentUser.getUid();
                        String chatUser = getIntent().getStringExtra("userid");
                        if(actUser.equals(chatUser)){
                            intent = new Intent(LoginActivity.this,ChatActivity.class);
                        }else{
                            intent = new Intent(LoginActivity.this, MessageActivity.class);
                            intent.putExtra("userid",chatUser);
                            intent.putExtra("caller",getIntent().getStringExtra("caller2"));
                        }
                    }
                    else{
                        intent = new Intent(LoginActivity.this, ChatActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                  allowingLogInUser();

                }
            }
        };


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                pb.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, R.string.email_pass_void, Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, R.string.log_success, Toast.LENGTH_SHORT).show();
                                String caller = getIntent().getStringExtra("caller");
                                Intent intent;
                                if(caller != null && caller.equals("User")){
                                    intent = new Intent(LoginActivity.this, UserActivity.class);
                                }else if(caller != null && caller.equals("Message")){
                                    String actUser = currentUser.getUid();
                                    String chatUser = getIntent().getStringExtra("userid");
                                    if(actUser.equals(chatUser)){
                                        intent = new Intent(LoginActivity.this,ChatActivity.class);
                                    }else{
                                        intent = new Intent(LoginActivity.this, MessageActivity.class);
                                        intent.putExtra("userid",chatUser);
                                        intent.putExtra("caller",getIntent().getStringExtra("caller2"));
                                    }
                                }
                                else{
                                    intent = new Intent(LoginActivity.this, ChatActivity.class);
                                }
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                btn_login.setVisibility(View.VISIBLE);
                                pb.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        currentUser = auth.getCurrentUser();
        auth.addAuthStateListener(authStateListener);
    }

    private void preparingForCheckingLogInUser(){
        pb.setVisibility(View.VISIBLE);
        email.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        btn_login.setVisibility(View.INVISIBLE);
        forgot_password.setVisibility(View.INVISIBLE);
        btn_new_account.setVisibility(View.INVISIBLE);
    }

    private void allowingLogInUser(){
        pb.setVisibility(View.GONE);
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.VISIBLE);
        forgot_password.setVisibility(View.VISIBLE);
        btn_new_account.setVisibility(View.VISIBLE);
    }


    public void goMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        intent.putExtra("caller","Login");
        String caller = getIntent().getStringExtra("caller");
        intent.putExtra("caller2",caller);
        if(caller!=null && caller.equals("Message")){
            intent.putExtra("userid",getIntent().getStringExtra("userid"));
            intent.putExtra("caller3",getIntent().getStringExtra("caller2"));
        }
        startActivity(intent);
    }

    public void goBack(View view) {
        goMain(view);
    }

    public void forgotPassword(View view) {
        Toast.makeText(LoginActivity.this, "Funcionalidad no disponible :(", Toast.LENGTH_SHORT).show();
    }

    public void goMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void noAccount(View view) {
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        String caller = getIntent().getStringExtra("caller");
        if(caller != null && caller.equals("User")){
            intent.putExtra("caller","User");
        }else if(caller != null && caller.equals("Message")){
            intent.putExtra("caller","Message");
            intent.putExtra("userid",getIntent().getStringExtra("userid"));
            intent.putExtra("caller3",getIntent().getStringExtra("caller2"));
        }
        else{
            intent.putExtra("caller","Login");
        }
        startActivity(intent);
    }
}