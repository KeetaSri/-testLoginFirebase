package com.example.keetawatsrichompoo.loginfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText email, password;
    private Button signIn, signUp, skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.emailText);
        password = (EditText) findViewById(R.id.passwordText);

        signIn = (Button) findViewById(R.id.signInButton);
        signUp = (Button) findViewById(R.id.signUpButton);
        skip = (Button) findViewById(R.id.skipButton);

//  check if the user is logged in.
        if (mAuth.getCurrentUser() != null) {
            // User not login.
            finish();
            startActivity( new Intent( getApplicationContext(), SignInActivity.class ) );
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getEmail = email.getText().toString();
                String getPassword = password.getText().toString();

                callSignIn( getEmail, getPassword );
            }

        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getEmail = email.getText().toString();
                String getPassword = password.getText().toString();

                callSignUp(getEmail, getPassword);
            }

        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( getApplicationContext(), SignInActivity.class ) );
            }
        });
    }

    private void callSignUp( String email, String password ) {

        mAuth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            userProfile();
                            Toast.makeText(MainActivity.this, "Account Created.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Test", "Account Created.");
//                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("Test", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Sign Up failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);

                        }

                        // ...
                    }
                });
    }

    private void callSignIn( String email, String password ) {
        mAuth.signInWithEmailAndPassword( email, password ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Test", "Sign in is successful:" + task.isSuccessful());

                if( !task.isSuccessful() ) {
                    Log.d("Test", "Sign in with email FAILED:", task.getException());
                    Toast.makeText( MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, SignInActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        });
    }

    private void userProfile() {
        FirebaseUser user = mAuth.getCurrentUser();

        if( user != null ) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName( email.getText().toString()).build();

            user.updateProfile( profileUpdates ).addOnCompleteListener( new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if ( task.isSuccessful() ) {
                        Log.d("Test", "User profile update.");

                    }
                }
            });
        }

    }


}

