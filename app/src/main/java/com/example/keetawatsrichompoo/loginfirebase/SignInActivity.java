package com.example.keetawatsrichompoo.loginfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView textDisplay;
    private Button signOut,newPost,viewPost,locateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        mAuth = FirebaseAuth.getInstance();

        textDisplay = (TextView) findViewById(R.id.userDisplay) ;

        signOut = (Button) findViewById(R.id.signOutButton);
        newPost = (Button) findViewById(R.id.post);
        viewPost = (Button) findViewById(R.id.viewPost);
        locateButton =(Button) findViewById(R.id.locateButton);

//        String welcomeText = "Welcome " + mAuth.getCurrentUser().getEmail();
        String welcomeText = "Welcome test";

        textDisplay.setText(welcomeText);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity( new Intent( getApplicationContext(), MainActivity.class ) );
            }

        });

        newPost.setOnClickListener( new View.OnClickListener() {
            public void onClick( View v ) {
                startActivity( new Intent( getApplicationContext(), NewPost.class ) );
            }
        });

        viewPost.setOnClickListener( new View.OnClickListener() {
            public void onClick( View v ) {
                startActivity( new Intent( getApplicationContext(), ViewPost.class ) );
            }
        });

        locateButton.setOnClickListener( new View.OnClickListener() {
            public void onClick( View v ) {
                startActivity( new Intent( getApplicationContext(), view_location.class ) );
            }
        });

    }
}
