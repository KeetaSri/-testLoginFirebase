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
    private Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        textDisplay = (TextView) findViewById(R.id.userDisplay) ;
        signOut = (Button) findViewById(R.id.signOutButton);

        String welcomeText = "Welcome " + mAuth.getCurrentUser().getEmail();

        textDisplay.setText(welcomeText);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity( new Intent( getApplicationContext(), MainActivity.class ) );
            }

        });

    }
}
