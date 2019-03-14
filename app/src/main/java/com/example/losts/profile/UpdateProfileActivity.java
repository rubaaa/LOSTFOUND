package com.example.losts.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.losts.Categories.HomePageCate;
import com.example.losts.Posts.Form;
import com.example.losts.Posts.MyPostList;
import com.example.losts.R;
import com.example.losts.SignUp_SignIn.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {
    // navigation control listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(UpdateProfileActivity.this, HomePageCate.class));

                    return true;
                case R.id.navigation_dashboard:
//messages
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
                    return true;
                case R.id.list:
                    startActivity(new Intent(UpdateProfileActivity.this, MyPostList.class));
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


      //  BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
      //  navigation.getMenu().getItem(2).setChecked(true);

      //  navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String name = getIntent().getExtras().getString("name");
        String phone = getIntent().getExtras().getString("phone");
        String email = getIntent().getExtras().getString("email");
        Button mButton = findViewById(R.id.button4);
        final EditText mEditTextName = findViewById(R.id.editText2);
        final EditText mEditTextPhone = findViewById(R.id.editText4);
        final EditText mEditTextEmail = findViewById(R.id.editText3);
        final ImageView ivBack = findViewById(R.id.ivBack);
        mEditTextEmail.setText(email);
        mEditTextName.setText(name);
        mEditTextPhone.setText(phone);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfileActivity.this,ProfileActivity.class));
                finish();
            }
        });
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEditTextEmail.getText().toString();
                final String name = mEditTextName.getText().toString();
                final String phone = mEditTextPhone.getText().toString();
                if (email.equals("")){
                    Toast.makeText(UpdateProfileActivity.this,"Email Field Must Be Filled",Toast.LENGTH_LONG).show();
                }

                else if(name.equals("")){

                    Toast.makeText(UpdateProfileActivity.this,"Name Field Must Be Filled",Toast.LENGTH_LONG).show();

                }

                else if(phone.equals("")){

                    Toast.makeText(UpdateProfileActivity.this,"Phone Field Must Be Filled",Toast.LENGTH_LONG).show();
                }






                else if (! (isAllNumeric(phone.replaceAll(" ","")))) {
                    Toast.makeText(UpdateProfileActivity.this,"Phone Number Has to Be All Digits",Toast.LENGTH_LONG).show();

                }



                /*else if (! (phone.substring(0,4).equals("966"))) {
                    Toast.makeText(UpdateProfileActivity.this,"Phone Number Must start with 966",Toast.LENGTH_LONG).show();

                }*/


                else if ((!(phone.replaceAll(" ","").length() == 12)) && (!(phone.replaceAll(" ","").length() == 10))) {
                    Toast.makeText(UpdateProfileActivity.this,"Phone Number is Incorrect",Toast.LENGTH_LONG).show();

                }
                else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myref = database.getReference();
                    myref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dataSnapshot.getRef().child("name").setValue(name);
                            dataSnapshot.getRef().child("email").setValue(email);
                            dataSnapshot.getRef().child("phone").setValue(phone);
                            Toast.makeText(UpdateProfileActivity.this,"Successfully Updated ",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(UpdateProfileActivity.this,ProfileActivity.class));
                            finish();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("User", databaseError.getMessage());
                        }
                    });
                }
            }
        });
    }

    public boolean isAllNumeric(String input)
    {
        try
        {
            Double.parseDouble(input);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,ProfileActivity.class));
    }
}