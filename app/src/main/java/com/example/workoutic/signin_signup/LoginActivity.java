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
                    Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
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
                                Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
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
        startActivity(intent);
    }

    public void noAccount(View view) {
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        intent.putExtra("caller","Login");
        startActivity(intent);
    }
}