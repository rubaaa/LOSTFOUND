package com.example.losts.Posts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.losts.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class listAdapter extends ArrayAdapter {
    private final int mLayout;
    private final ArrayList<Post> mArray;
    private final Activity mContext;
    private final LayoutInflater mInflater;
    Post post, currentPost, updatedPost;
    Query postQuery;

    public listAdapter(Activity context, int resource, ArrayList<Post> objects) {
        super(context, resource, objects);
        mLayout=resource;
        mArray=objects;
        mContext=context;
        mInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View view = mInflater.inflate(mLayout, parent, false);
        TextView mTextViewData = (TextView) view.findViewById(R.id.Date);
        TextView mTextViewDes = (TextView) view.findViewById(R.id.descTxt);
        TextView mTextViewStatus = (TextView) view.findViewById(R.id.textStatus);
        ImageView mImageView = (ImageView) view.findViewById(R.id.ivImage);
        mTextViewDes.setText(mArray.get(position).Description);
        mTextViewData.setText(mArray.get(position).Date);
        mTextViewStatus.setText(mArray.get(position).Status);
        GalleryCameraUtils galleryCameraUtils = new GalleryCameraUtils(mContext);
        Bitmap bitmap = galleryCameraUtils.decodedBase64(mArray.get(position).Image);
        mImageView.setImageBitmap(bitmap);

        if ( ! FirebaseAuth.getInstance().getCurrentUser().getUid().equals(mArray.get(position).UserID)){
            ((Button) view.findViewById(R.id.found)).setVisibility(View.INVISIBLE);
            ((Button) view.findViewById(R.id.notfound)).setVisibility(View.INVISIBLE);
        }else{
            //reunited
            ((Button) view.findViewById(R.id.found)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPost = ((Post)mArray.get(position));
                    updatedPost = new Post(mArray.get(position).Date, mArray.get(position).Description, mArray.get(position).Category, FirebaseAuth.getInstance().getCurrentUser().getUid(), "Reunited", mArray.get(position).Location, mArray.get(position).Image);

                    postQuery = FirebaseDatabase.getInstance().getReference().child("Post");
                    // Attach a listener to read the data at our posts reference
                    postQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                            /*if(currentPost.equals(((Post)child.getValue(Post.class)))){
                                FirebaseDatabase.getInstance().getReference().child("Post").child(child.getKey()).setValue(updatedPost);
                                Toast.makeText(getContext(), "Post Updated Successfully", Toast.LENGTH_LONG).show();
                            }*/
                                post = ((Post)child.getValue(Post.class));
                                if (post.Status.equals(mArray.get(position).Status) &&
                                        post.Description.equals(mArray.get(position).Description) &&
                                        post.Location.equals(mArray.get(position).Location) &&
                                        post.Category.equals(mArray.get(position).Category) &&
                                        post.Date.equals(mArray.get(position).Date) &&
                                        post.Image.equals(mArray.get(position).Image) &&
                                        post.UserID.equals(mArray.get(position).UserID)){

                                    FirebaseDatabase.getInstance().getReference().child("Post").child(child.getKey()).setValue(updatedPost);
                                    Toast.makeText(getContext(), "Post Updated Successfully", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            });
            //lost
            ((Button) view.findViewById(R.id.notfound)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPost = ((Post)mArray.get(position));
                    updatedPost = new Post(mArray.get(position).Date, mArray.get(position).Description, mArray.get(position).Category, FirebaseAuth.getInstance().getCurrentUser().getUid(), "Lost", mArray.get(position).Location, mArray.get(position).Image);

                    postQuery = FirebaseDatabase.getInstance().getReference().child("Post");
                    // Attach a listener to read the data at our posts reference
                    postQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                            /*if(currentPost.equals(((Post)child.getValue(Post.class)))){
                                FirebaseDatabase.getInstance().getReference().child("Post").child(child.getKey()).setValue(updatedPost);
                                Toast.makeText(getContext(), "Post Updated Successfully", Toast.LENGTH_LONG).show();
                            }*/
                                post = ((Post)child.getValue(Post.class));
                                if (post.Status.equals(mArray.get(position).Status) &&
                                        post.Description.equals(mArray.get(position).Description) &&
                                        post.Location.equals(mArray.get(position).Location) &&
                                        post.Category.equals(mArray.get(position).Category) &&
                                        post.Date.equals(mArray.get(position).Date) &&
                                        post.Image.equals(mArray.get(position).Image) &&
                                        post.UserID.equals(mArray.get(position).UserID)){

                                    FirebaseDatabase.getInstance().getReference().child("Post").child(child.getKey()).setValue(updatedPost);
                                    Toast.makeText(getContext(), "Post Updated Successfully", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            });
        }
        return view;
    }
}