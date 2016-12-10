package com.example.zhihao.hwtwitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends Activity implements View.OnClickListener{
    private EditText tweetEditText;
    private Button postButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        tweetEditText = (EditText)findViewById(R.id.editTextTweet);
        postButton = (Button)findViewById(R.id.buttonPost);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Toast.makeText(PostActivity.this, user.getEmail() + " signed in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostActivity.this, "signed out", Toast.LENGTH_SHORT).show();
                }
            }
        };

        postButton.setOnClickListener(this);
    }

    public void postTweet() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("tweets");

        String content = tweetEditText.getText().toString();
        if(content == "") {
            Toast.makeText(PostActivity.this, "The content is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        Tweet tweet = new Tweet(content, mAuth.getCurrentUser().getEmail());
        DatabaseReference newTweet = ref.push();
        newTweet.setValue(tweet);
        Toast.makeText(PostActivity.this, "Tweet post successfully!", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_logout:
                signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_post:
                break; // do nothing
            case R.id.menu_read:
                Intent intent2 = new Intent(this, TweetsActivity.class);
                startActivity(intent2);
                break;
            default: // do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonPost:
                postTweet();
                break;
            default:
        }
    }
}
