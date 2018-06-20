package com.example.keetawatsrichompoo.loginfirebase;

import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewPost extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    private FirebaseRecyclerAdapter<ViewSingleItem, ShowDataViewHolder> mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("User_Details");

        recyclerView = (RecyclerView) findViewById(R.id.viewPostData);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewPost.this));
        Toast.makeText(ViewPost.this,"Please wait, it is loading ... ", Toast.LENGTH_SHORT).show();


    }

    public void onStart() {
        super.onStart();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ViewSingleItem, ShowDataViewHolder>(
                ViewSingleItem.class, R.layout.viewsingleitem, ShowDataViewHolder.class, myRef) {

            @Override
            protected void populateViewHolder( final ShowDataViewHolder viewHolder, ViewSingleItem model, final int position ) {
                viewHolder.Image_URL(model.getImage_URL());
                viewHolder.Image_Title(model.getImage_Title());

                // Click at any row of the list and we will delete that row out of the database

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( final View view ) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewPost.this);
                        builder.setMessage("Delete?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int selectedItem = position;
                                        mFirebaseAdapter.getRef(selectedItem).removeValue();
                                        mFirebaseAdapter.notifyItemRemoved(selectedItem);
                                        recyclerView.invalidate();
                                        onStart();

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.setTitle("Are you sure?");
                        dialog.show();

                    }
                });

            }
        };

        recyclerView.setAdapter(mFirebaseAdapter);
    }

    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView img_title;
        private final ImageView img_url;

        public ShowDataViewHolder(final View itemView) {
            super(itemView);
            img_title = (TextView) itemView.findViewById(R.id.fetch_image_title);
            img_url = (ImageView) itemView.findViewById(R.id.fetch_image);

        }

        public void Image_Title(String title) {
            img_title.setText(title);
        }

        public void Image_URL(String title) {
            Glide.with(itemView.getContext())
                    .load(title)
                    .crossFade()
                    .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                    .thumbnail(01.f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_url);
        }


    }
}