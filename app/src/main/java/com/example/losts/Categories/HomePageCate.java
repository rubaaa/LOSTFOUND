package com.example.losts.Categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.losts.Posts.MyPostList;
import com.example.losts.Posts.postsList;
import com.example.losts.R;
import com.example.losts.profile.ProfileActivity;

public class HomePageCate extends AppCompatActivity {

    private TextView mTextMessage;
// navigation control listener
private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                startActivity(new Intent(HomePageCate.this, HomePageCate.class));

                return true;
            case R.id.navigation_dashboard:
//messages
                return true;
            case R.id.navigation_notifications:
                  startActivity(new Intent(HomePageCate.this, ProfileActivity.class));
                return true;
            case R.id.list:
                startActivity(new Intent(HomePageCate.this, MyPostList.class));
                return true;
        }
        return false;
    }
};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_cate);
        loadFragment(new CategoryFreg());
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(1).setChecked(true);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
