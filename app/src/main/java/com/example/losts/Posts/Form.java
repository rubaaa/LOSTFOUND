package com.example.losts.Posts;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.losts.Categories.HomePageCate;
import com.example.losts.Categories.Location;
import com.example.losts.Firebase.FirebaseHelper;
import com.example.losts.R;
import com.example.losts.profile.ProfileActivity;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Form extends AppCompatActivity implements GalleryCameraUtils.GetImageData {
    Button saveBtn;
    ListView lv;
    Calendar currentDate;
    int mDay, mMonth, mShowMonth, mYear;
    Spinner locationDropdown;

    EditText date, des;
    FirebaseHelper helper;
    CustomAdapter adapter;
    private String mCategory = "";
    private String mLocation = "";
    private String mImage = "";
    private ImageView mImageView;
    TextView tv;
    // navigation control listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(Form.this, HomePageCate.class));

                    return true;
                case R.id.navigation_dashboard:
//messages
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(Form.this, ProfileActivity.class));
                    return true;
                case R.id.list:
                    startActivity(new Intent(Form.this, MyPostList.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(1).setChecked(true);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        saveBtn = (Button) findViewById(R.id.buttonSubmet);
        date = (EditText) findViewById(R.id.editTextName);
        mImageView = (ImageView) findViewById(R.id.imageView);
        des = (EditText) findViewById(R.id.editTextEmail);
        dropdown();
        dropdown2();


        tv = (TextView) findViewById(R.id.textView10);

        currentDate = Calendar.getInstance();

        mDay = currentDate.get(Calendar.DAY_OF_MONTH);
        mMonth = currentDate.get(Calendar.MONTH);
        mYear = currentDate.get(Calendar.YEAR);

        mShowMonth = mMonth + 1;

        tv.setText(mDay + "/" + mShowMonth + "/" + mYear);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(Form.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        tv.setText(i2 + "/" + i1 + "/" + i);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

                datePickerDialog.show();

            }
        });



        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils permissionUtils = new PermissionUtils(Form.this);
                if (permissionUtils.hasPermission(Form.this,Manifest.permission.CAMERA)&&permissionUtils.hasPermission(Form.this,Manifest.permission.READ_EXTERNAL_STORAGE)&&permissionUtils.hasPermission(Form.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    OpenImageDialog();
                }else {
                    String[] arr = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    permissionUtils.requestPermissions(Form.this,arr,1200);
                }
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mData = tv.getText().toString();
                String des = Form.this.des.getText().toString();
                if (mData.equals("") || des.equals("") || mCategory.equals("") || mLocation.equals("") || mImage.equals("")) {
                    Toast.makeText(Form.this, "continue all data", Toast.LENGTH_LONG).show();
                } else {
                    Post user = new Post(
                            mData, des, mCategory, FirebaseAuth.getInstance().getCurrentUser().getUid(), "LOST", mLocation, mImage);

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference memberRef = rootRef.child("Post");
                    String id = memberRef.push().getKey();
                    memberRef.child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("NTest", "Success Post");
                                Toast.makeText(Form.this, "Post Successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Form.this, HomePageCate.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
    private void OpenImageDialog() {
        GalleryCameraUtils galleryCameraUtils = new GalleryCameraUtils(Form.this);
        galleryCameraUtils.onGetPic(Form.this,null,200,300,"Choose Image From","Choose Image","Camera","Gallery",this);
    }






    public void dropdown() {
        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        final String[] items = new String[]{"Jewelry", "Clothes", "Devices", "Books", "Bags & Wallets", "Cards", "Keys", "Others"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategory = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void dropdown2() {
        locationDropdown = findViewById(R.id.spinner2);

        Query catQuery1 = FirebaseDatabase.getInstance().getReference().child("Location");
        // Attach a listener to read the data at our posts reference
        catQuery1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List catlist1 = new ArrayList<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                  String m=(child.getValue(Location.class)).LocationName;
                    if(!m.equals("Pick a building"))
catlist1.add((child.getValue(Location.class)).LocationName);
                }
                //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Form.this, android.R.layout.simple_spinner_dropdown_item, catlist1);
                locationDropdown.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        locationDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mLocation = (String)locationDropdown.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }


    public void dropdown5() {
        Spinner dropdown = findViewById(R.id.spinner2);
        String[] items = new String[]{"College of Computer Science & Information", "Elctronics", "Clothed", "Accessories", "Bags & Wallets", "Cards", "Keys", "Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GalleryCameraUtils galleryCameraUtils = new GalleryCameraUtils(Form.this);
        galleryCameraUtils.onGetPicOnActivityResult(this,resultCode,requestCode,data,100,100,200,300);
    }

    @Override
    public void onGetImageData(String Name, Bitmap bitmap, Uri filePath, String realPathFromURI) {
        GalleryCameraUtils galleryCameraUtils = new GalleryCameraUtils(Form.this);
        mImage = galleryCameraUtils.encodeImageBase64(this,bitmap);
        mImageView.setImageBitmap(bitmap);
        Log.i("NTest","Worked");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1200){
            PermissionUtils permissionUtils = new PermissionUtils(Form.this);
            if (permissionUtils.checkGrantResults(grantResults)){
                OpenImageDialog();
            }else {
                Toast.makeText(this,"You Shouid Granted Permissions",Toast.LENGTH_LONG).show();
            }
        }
    }
}
