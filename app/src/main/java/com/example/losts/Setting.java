package com.example.losts;

import android.Manifest;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.losts.Categories.Category;
import com.example.losts.Categories.HomePageCate;
import com.example.losts.Categories.Location;
import com.example.losts.Posts.Form;
import com.example.losts.Posts.GalleryCameraUtils;
import com.example.losts.Posts.MyPostList;
import com.example.losts.Posts.PermissionUtils;
import com.example.losts.Posts.Post;
import com.example.losts.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.losts.Firebase.FirebaseHelper;
import com.example.losts.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Setting extends AppCompatActivity implements GalleryCameraUtils.GetImageData {

    ImageView mImageView;
    private Button addCatButton, deleteCatButton, addCatButton2, deleteCatButton2;
    private String mImage = "";

    private TextView mTextViewName, mTextViewPhone, mTextViewEmail, catNameEditText, catNameEditText2;
    private Spinner catSpinner, catSpinner2;
    Query catQuery1, catQuery2;
    String cat;
    List<String> catlist1, catlist2;
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
                    startActivity(new Intent(Setting.this, HomePageCate.class));

                    return true;
                case R.id.navigation_dashboard:
//messages
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(Setting.this, ProfileActivity.class));
                    return true;
                case R.id.list:
                    startActivity(new Intent(Setting.this, MyPostList.class));
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mImageView = (ImageView) findViewById(R.id.imageView);
        addCatButton = findViewById(R.id.addCatButton);
        catNameEditText = findViewById(R.id.catNameEditText);
        addCatButton2 = findViewById(R.id.addCatButton2);
        catSpinner = findViewById(R.id.catSpinner);
        catSpinner2 = findViewById(R.id.catSpinner2);
        deleteCatButton=findViewById(R.id.deleteCatButton);
        catNameEditText2 = findViewById(R.id.catNameEditText2);
        deleteCatButton2=findViewById(R.id.deleteCatButton2);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(2).setChecked(true);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        loadLocation();

        loadcat();

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils permissionUtils = new PermissionUtils(Setting.this);
                if (permissionUtils.hasPermission(Setting.this, Manifest.permission.CAMERA) && permissionUtils.hasPermission(Setting.this, Manifest.permission.READ_EXTERNAL_STORAGE) && permissionUtils.hasPermission(Setting.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    OpenImageDialog();
                } else {
                    String[] arr = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    permissionUtils.requestPermissions(Setting.this, arr, 1200);
                }
            }
        });



        deleteCatButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                catlist1 = new ArrayList<String>();
                catQuery1 = FirebaseDatabase.getInstance().getReference().child("Location");
                // Attach a listener to read the data at our posts reference
                catQuery1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (((Location) child.getValue(Location.class)).LocationName.equals((String) catSpinner.getSelectedItem())) {

                                FirebaseDatabase.getInstance().getReference().child("Location").child(child.getKey()).removeValue();
                                Toast.makeText(Setting.this, "Current Location was removed Successfully", Toast.LENGTH_LONG).show();

                            } else {
                               catlist1.add((child.getValue(Location.class)).LocationName);
                            }
                        }
                        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Setting.this, android.R.layout.simple_spinner_dropdown_item, catlist1);
                        catSpinner.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });



        addCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cat = catNameEditText.getText().toString();
                if (cat.equals("")) {
                    Toast.makeText(Setting.this, "fill location name", Toast.LENGTH_LONG).show();
                } else {
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference memberRef = rootRef.child("Location");
                    String id = memberRef.push().getKey();
                    memberRef.child(id).setValue(new Location(id, cat)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                catNameEditText.setText("");
                                Log.i("NTest", "Success Post");
                                Toast.makeText(Setting.this, "Location added Successfully", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }


            }
        });



        deleteCatButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                catlist1 = new ArrayList<String>();
                catQuery1 = FirebaseDatabase.getInstance().getReference().child("Cat");
                // Attach a listener to read the data at our posts reference
                catQuery1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if ((child.getValue(Category.class)).CatName.equals((String) catSpinner2.getSelectedItem())) {

                                FirebaseDatabase.getInstance().getReference().child("Cat").child(child.getKey()).removeValue();
                                Toast.makeText(Setting.this, "Current Category was removed Successfully", Toast.LENGTH_LONG).show();

                            } else {


                                catlist1.add((child.getValue(Category.class)).CatName);
                            }
                        }
                        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Setting.this, android.R.layout.simple_spinner_dropdown_item, catlist1);
                        catSpinner2.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });









        addCatButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cat = catNameEditText2.getText().toString();
                if (cat.equals("") || mImage.equals("")) {
                    Toast.makeText(Setting.this, "fill category name", Toast.LENGTH_LONG).show();
                } else {
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference memberRef = rootRef.child("Cat");
                    String id = memberRef.push().getKey();
                    Log.d("hello", "hello");
                    memberRef.child(id).setValue(new Category(id, cat, mImage)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                catNameEditText.setText("");
                                Log.i("NTest", "Success Post");
                                Toast.makeText(Setting.this, "Category added Successfully", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


    }



@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 1200){
        PermissionUtils permissionUtils = new PermissionUtils(Setting.this);
        if (permissionUtils.checkGrantResults(grantResults)){
            OpenImageDialog();
        }else {
            Toast.makeText(this,"You Shouid Granted Permissions",Toast.LENGTH_LONG).show();
        }
    }
}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GalleryCameraUtils galleryCameraUtils = new GalleryCameraUtils(Setting.this);
        galleryCameraUtils.onGetPicOnActivityResult(this,resultCode,requestCode,data,100,100,200,300);
    }

    private void OpenImageDialog() {
        GalleryCameraUtils galleryCameraUtils = new GalleryCameraUtils(Setting.this);
        galleryCameraUtils.onGetPic(Setting.this,null,200,300,"Choose Image From","Choose Image","Camera","Gallery",this);
    }

    public void onGetImageData(String Name, Bitmap bitmap, Uri filePath, String realPathFromURI) {
        GalleryCameraUtils galleryCameraUtils = new GalleryCameraUtils(Setting.this);
        mImage = galleryCameraUtils.encodeImageBase64(this,bitmap);
        mImageView.setImageBitmap(bitmap);
        Log.i("NTest","Worked");
    }


    private void loadcat() {
        //FirebaseDatabase.getInstance().getReference().child("Location").removeValue();
        //create a list of items for the spinner.
        catlist1 = new ArrayList<String>();
        catQuery2 = FirebaseDatabase.getInstance().getReference().child("Cat");
        // Attach a listener to read the data at our posts reference
        catQuery2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    catlist1.add((child.getValue(Category.class)).CatName);
                }
                //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Setting.this, android.R.layout.simple_spinner_dropdown_item, catlist1);
                catSpinner2.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    private void loadLocation() {
        //FirebaseDatabase.getInstance().getReference().child("Location").removeValue();
        //create a list of items for the spinner.
        catlist2 = new ArrayList<String>();
        catQuery2 = FirebaseDatabase.getInstance().getReference().child("Location");
        // Attach a listener to read the data at our posts reference
        catQuery2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String m=(child.getValue(Location.class)).LocationName;
                    if(!m.equals("Pick a building "))
                    catlist2.add(m);
                }
                //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Setting.this, android.R.layout.simple_spinner_dropdown_item, catlist2);
                catSpinner.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
