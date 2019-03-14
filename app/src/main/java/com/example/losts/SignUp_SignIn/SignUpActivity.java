package com.example.losts.SignUp_SignIn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.losts.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword, editTextName, editTextPhone,editTextPassword2;
    private FirebaseAuth mAuth;
    //ms2salim@gmail.com Passw0rd
    //rnashoor@gmail.com Pa55word

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword2 = findViewById(R.id.editTextPassword2);


        progressBar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
        findViewById(R.id.vback).setOnClickListener(this);


    }



    private void registerUser() {
        String mEmail = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();
        final String Name = editTextName.getText().toString().trim();
        final String Phone = editTextPhone.getText().toString().trim();

        if (mEmail.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (Name.isEmpty()) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }


        if (Phone.isEmpty()) {
            editTextPhone.setError("Phone Number is Required"); //meshael
            editTextPhone.requestFocus();
            return;
        }

        if (! (isAllNumeric(Phone))) {
            editTextPhone.setError("Phone Number Should Only Have Numbers"); //meshael
            editTextPhone.requestFocus();
            return;
        }




        if ((!(Phone.replaceAll(" ","").length() == 12)) && (!(Phone.replaceAll(" ","").length() == 10))) {
            editTextPhone.setError("Phone Number Should Start with 966"); //meshael
            editTextPhone.requestFocus();
            return;
        }


        if (!(Phone.replaceAll(" ","").length() == 12)) {
            editTextPhone.setError("Phone Number is Incorrect"); //meshael
            editTextPhone.requestFocus();
            return;
        }


        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6"); //meshael
            editTextPassword.requestFocus();
            return;
        }


        if (!password2.equalsIgnoreCase(password)){
            editTextPassword.setError("Passwords Don't Match");
            editTextPassword.requestFocus();
            return;//meshael
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(mEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    User user = new User(
                            Name, editTextEmail.getText().toString(),
                            Phone
                    );

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Log.i("NTest","Success");
                                Toast.makeText(SignUpActivity.this, "Register Successfully", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            } else {
                                //display a failure message
                            }
                        }
                    });
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.vback:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }


    //meshael
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

}