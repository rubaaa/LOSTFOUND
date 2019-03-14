package com.example.losts.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.losts.Categories.HomePageCate;
import com.example.losts.Posts.Form;
import com.example.losts.Posts.MyPostList;
import com.example.losts.Posts.Post;
import com.example.losts.R;
import com.example.losts.Setting;
import com.example.losts.SignUp_SignIn.MainActivity;
import com.example.losts.SignUp_SignIn.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView mTextViewName,mTextViewPhone,mTextViewEmail;
    private String mName = "";
    private String mEmail = "";
    private String mPhone = "";
    private SharedPreferences mPrefLosts;
    // navigation control listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ProfileActivity.this, HomePageCate.class));

                    return true;
                case R.id.navigation_dashboard:
//messages
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                    return true;
                case R.id.list:
                    startActivity(new Intent(ProfileActivity.this, MyPostList.class));
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
       navigation.getMenu().getItem(2).setChecked(true);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mTextViewName = findViewById(R.id.textView6);
        mTextViewPhone = findViewById(R.id.textView8);
        mTextViewEmail = findViewById(R.id.textView7);
        Button mButton = findViewById(R.id.button);
        Button mButtonSignOut = findViewById(R.id.button3);
        ImageView ivBack = findViewById(R.id.ivBack);
        Button SettingButton = findViewById(R.id.button4);






        mPrefLosts = getSharedPreferences("prefLosts", Context.MODE_PRIVATE);
        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrefLosts.edit().putBoolean("login",false).apply();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

      mPrefLosts = getSharedPreferences("prefLosts", Context.MODE_PRIVATE);
        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrefLosts.edit().putBoolean("login",false).apply();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });




        SettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, Setting.class);

                startActivity(intent);
                finish();
            }
        });





        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
                intent.putExtra("name",mName);
                intent.putExtra("email",mEmail);
                intent.putExtra("phone",mPhone);
                startActivity(intent);
                finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LoadUserData();

    }

    private void LoadUserData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mName = user.name;
                mEmail = user.email;
                mPhone = user.phone;
                mTextViewEmail.setText(user.email);
                mTextViewName.setText(user.name);
                mTextViewPhone.setText(user.phone);
                if ((mEmail.equals("ruba1111@gmail.com"))){
                    View b = findViewById(R.id.button4);
                    b.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(postListener);
    }

}
