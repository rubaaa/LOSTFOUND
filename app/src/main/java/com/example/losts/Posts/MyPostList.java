package com.example.losts.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.losts.Categories.HomePageCate;
import com.example.losts.Categories.Location;
import com.example.losts.Firebase.FirebaseHelper;
import com.example.losts.R;
import com.example.losts.profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyPostList extends AppCompatActivity {


    DatabaseReference db;
    FirebaseHelper helper;
    CustomAdapter adapter;
    ListView lv;
    EditText nameEditTxt,propTxt,descTxt;
    Spinner locationDropdown;
    FloatingActionButton fab;
    ArrayList<Post> mArrayList;
    listAdapter mlistAdapter;
    Query postQuery;
    Post post;
    RadioButton rb;
    View parent;
    int m = -1;
    public static String mode = "LOSTS";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(MyPostList.this, HomePageCate.class));

                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(MyPostList.this, ProfileActivity.class));
                    return true;
                case R.id.list:
                    startActivity(new Intent(MyPostList.this, MyPostList.class));
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_list);
        LoadData4();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(3).setChecked(true);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        fillDropdown();
        lv = (ListView) findViewById(R.id.lv);
        ((Button)findViewById(R.id.buttonSearch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadData5();
            }
        });

    }








    //  final String[] items = new String[]{"Pick a building","College of  Computer and Information Science", "College of Nursing", "College of Sciences", "College of Medicine", "Research Centre and Complex Auditorium", "College of Applied Medical Science", "College of Dentistry", "Prince Naif Research Centre", "College of Pharmacy", "Sports Hall", "Central Library", "Science Colleges Entrance 2", "Science Colleges Entrance 3", "Female Faculty Housing", "Female Student Housing"};

    public void fillDropdown() {
        //get the spinner from the xml.
        locationDropdown = findViewById(R.id.spinner3);
        /*//create a list of items for the spinner.
        final String[] items = new String[]{"College of Computer Science & Information", "College of Elctronics"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        locationDropdown.setAdapter(adapter);*/
        Query catQuery1 = FirebaseDatabase.getInstance().getReference().child("Location");
        // Attach a listener to read the data at our posts reference
        catQuery1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List catlist1 = new ArrayList<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    catlist1.add((child.getValue(Location.class)).LocationName);
                }
                //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyPostList.this, android.R.layout.simple_spinner_dropdown_item, catlist1);
                locationDropdown.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




        //////////
        locationDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if ("Pick a building".equals((String) locationDropdown.getSelectedItem())){
                    LoadData4();

                } else
                    LoadData2();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




    private void LoadData() {
        //FirebaseDatabase.getInstance().getReference().child("Post").removeValue();
        postQuery = FirebaseDatabase.getInstance().getReference().child("Post");
        // Attach a listener to read the data at our posts reference

        if(getIntent().getStringExtra("mode").equals("FOUND")){
            postQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mArrayList = new ArrayList<Post>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        post = child.getValue(Post.class);
                        if (post != null) {

                            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(post.UserID)) {
                                mArrayList.add(post);
                            }



                        }
                    }
                    mlistAdapter = new listAdapter(MyPostList.this,R.layout.model2,mArrayList );
                    lv.setAdapter(mlistAdapter);
                    if (mArrayList.size() == 0){
                        Toast.makeText(MyPostList.this, "No posts found with the selected search criteria", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(MyPostList.this, mArrayList.size() + " posts were found with the selected search criteria", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else if(getIntent().getStringExtra("mode").equals("LOST")){
            postQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mArrayList = new ArrayList<Post>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        post = child.getValue(Post.class);
                        if (post != null) {
                            if (post.Category.equals(getIntent().getStringExtra("category"))) {
                                if(post.Status.equals("LOST")) {

                                    if ("Pick a building".equals((String) locationDropdown.getSelectedItem())) {
                                        mArrayList.add(post);

                                    } else if (post.Location.equals((String) locationDropdown.getSelectedItem())) {
                                        mArrayList.add(post);

                                    }
                                }


                            }
                        }
                    }
                    mlistAdapter = new listAdapter(MyPostList.this,R.layout.model2,mArrayList );
                    lv.setAdapter(mlistAdapter);
                    if (mArrayList.size() == 0){
                        Toast.makeText(MyPostList.this, "No posts found with the selected search criteria", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(MyPostList.this, mArrayList.size() + " posts were found with the selected search criteria", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }



    }




    private void LoadData2() {

        postQuery = FirebaseDatabase.getInstance().getReference().child("Post");


        postQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mArrayList = new ArrayList<Post>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    post = child.getValue(Post.class);
                    if (post != null) {

                       if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(post.UserID))  {

                                if ("Pick a building".equals((String) locationDropdown.getSelectedItem())) {
                                    mArrayList.add(post);

                                } else if (post.Location.equals((String) locationDropdown.getSelectedItem())) {
                                    mArrayList.add(post);

                                }
                            }



                    }
                }
                mlistAdapter = new listAdapter(MyPostList.this,R.layout.model2,mArrayList );
                lv.setAdapter(mlistAdapter);
                if (mArrayList.size() == 0){
                    Toast.makeText(MyPostList.this, "No posts found with the selected search criteria", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MyPostList.this, mArrayList.size() + " posts were found with the selected search criteria", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




    }





    private void LoadData3() {

        postQuery = FirebaseDatabase.getInstance().getReference().child("Post");


        postQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mArrayList = new ArrayList<Post>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    post = child.getValue(Post.class);
                    if (post != null) {

                        if((FirebaseAuth.getInstance().getCurrentUser().getUid().equals(post.UserID))){

                            mArrayList.add(post);}






                    }
                }
                mlistAdapter = new listAdapter(MyPostList.this,R.layout.model2,mArrayList );
                lv.setAdapter(mlistAdapter);
                if (mArrayList.size() == 0){
                    Toast.makeText(MyPostList.this, "No posts ", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MyPostList.this, mArrayList.size() + " posts were found with the selected search criteria", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




    }









    private void LoadData4() {
        postQuery = FirebaseDatabase.getInstance().getReference().child("Post");
        // Attach a listener to read the data at our posts reference
        postQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mArrayList = new ArrayList<Post>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    post = child.getValue(Post.class);
                    if (post != null) {


                        if ( (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(post.UserID))  ){
                            mArrayList.add(post);
                        }

                    }
                }
                mlistAdapter = new listAdapter(MyPostList.this,R.layout.model2,mArrayList );
                lv.setAdapter(mlistAdapter);
                if (mArrayList.size() == 0){
                    Toast.makeText(MyPostList.this, "", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });}



    private void LoadData5() {
        postQuery = FirebaseDatabase.getInstance().getReference().child("Post");
        // Attach a listener to read the data at our posts reference
        postQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mArrayList = new ArrayList<Post>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    post = child.getValue(Post.class);
                    if (post != null) {


                        if (post.Description.contains(((EditText)findViewById(R.id.editTextSearch)).getText())  && (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(post.UserID)) ) {
                            mArrayList.add(post);
                        }

                    }
                }
                mlistAdapter = new listAdapter(MyPostList.this,R.layout.model2,mArrayList );
                lv.setAdapter(mlistAdapter);
                if (mArrayList.size() == 0){
                    Toast.makeText(MyPostList.this, "123", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });}



}
