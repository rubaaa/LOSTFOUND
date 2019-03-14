package com.example.losts.SignUp_SignIn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.losts.R;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button2:
                finish();
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;

            case R.id.textViewSignup:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
