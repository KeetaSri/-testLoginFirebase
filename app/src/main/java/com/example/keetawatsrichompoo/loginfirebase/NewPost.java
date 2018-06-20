package com.example.keetawatsrichompoo.loginfirebase;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewPost extends AppCompatActivity {

    private ImageView img;
    private EditText title;
    private Button selectImg, postImg;

    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog msgDialog;
    private Firebase mRootRef;
    private Uri imgUri = null;
    private DatabaseReference databaseRef;
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Firebase.setAndroidContext(this);

        img = (ImageView) findViewById(R.id.imagePreview);
        title = (EditText) findViewById(R.id.inputTitle);
        selectImg = (Button)findViewById(R.id.selectButton);
        postImg = (Button)findViewById(R.id.postButton);

        msgDialog = new ProgressDialog( NewPost.this );

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(), "Call for permission", Toast.LENGTH_SHORT).show();

                    if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_EXTERNAL_STORAGE );
                    }

                } else {
                    callGallery();
                }
            }
        });

        databaseRef = FirebaseDatabase.getInstance().getReference();
        mRootRef = new Firebase("https://loginfirebase-bdb86.firebaseio.com/").child("User_Details").push();
        storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://loginfirebase-bdb86.appspot.com/");

        postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mName = title.getText().toString().trim();
                if( mName.isEmpty() ) {
                    Toast.makeText(getApplicationContext(),"Please enter title", Toast.LENGTH_SHORT).show();
                    return;

                }

                Firebase childRef_name = mRootRef.child("Image_Title");
                childRef_name.setValue(mName);
                Toast.makeText(getApplicationContext(),"Uploaded Info", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void onRequestPermissionResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode){
            case READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callGallery();
                return;
        }
        Toast.makeText(getApplicationContext(),"...",Toast.LENGTH_SHORT).show();
    }

    private void callGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            imgUri = data.getData();
            img.setImageURI(imgUri);
            StorageReference filePath = storage.child("User_Images").child(imgUri.getLastPathSegment());

            msgDialog.setMessage("Uploading...");
            msgDialog.show();

            filePath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    mRootRef.child("Image_URL").setValue(downloadUri.toString());
                    Glide.with(getApplicationContext())
                            .load(downloadUri)
                            .crossFade()
                            .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(img);

                    Toast.makeText(getApplicationContext(),"Updated...",Toast.LENGTH_SHORT).show();
                    msgDialog.dismiss();

                }
            });
        }
    }
}
