package com.example.zhihao.hwtwitter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "EmailPassword";
    private Button loginButton;
    private Button logoutButton;
    private Button registerButton;
    private EditText nameEditText;
    private EditText passwordEditText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.buttonLogin);
        logoutButton = (Button) findViewById(R.id.buttonLogout);
        registerButton = (Button) findViewById(R.id.buttonRegister);
        nameEditText = (EditText) findViewById(R.id.editTextName);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    //Toast.makeText(MainActivity.this, user.getEmail() + " signed in", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MainActivity.this, "signed out", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loginButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Account creation successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(String email, String password) {
        Task<AuthResult> result = mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Sign in successfully - moving to tweets page", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, TweetsActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                signIn(nameEditText.getText().toString(), passwordEditText.getText().toString());
                break;
            case R.id.buttonLogout:
                signOut();
                break;
            case R.id.buttonRegister:
                createAccount(nameEditText.getText().toString(), passwordEditText.getText().toString());
                break;
            default:
        }
    }
}
