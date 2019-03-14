package com.example.losts.Categories;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.losts.Posts.GalleryCameraUtils;
import com.example.losts.Posts.Post;
import com.example.losts.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private  int mLayout;
    private  ArrayList<Category> mArray;
    private  Activity mContext;
    private  LayoutInflater mInflater;
    Post post, currentPost, updatedPost;
    Query postQuery;

    public CustomAdapter(Activity context, int resource, ArrayList<Category> objects) {
        mLayout=resource;
        mArray=objects;
        mContext=context;
        mInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View view = mInflater.inflate(mLayout, parent, false);
        TextView mTextViewData = (TextView) view.findViewById(R.id.Text);

        ImageView mImageView = (ImageView) view.findViewById(R.id.Images00);

        GalleryCameraUtils galleryCameraUtils = new GalleryCameraUtils(mContext);
        Bitmap bitmap = galleryCameraUtils.decodedBase64(mArray.get(position).icon);
        mImageView.setImageBitmap(bitmap);
        mTextViewData.setText(mArray.get(position).CatName);

        return view;
    }

    public CustomAdapter( ) {

        super();

    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
