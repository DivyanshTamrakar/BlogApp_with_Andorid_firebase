package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.regex.Pattern;


public class RegistrationActivity extends AppCompatActivity {

    private TextView emailbutton;
    private TextView userbutton;
    private TextView passbutton;
    private TextView registerbutton;
    private TextView userback;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private String current_user_id;
    public static final Pattern EMAIL_Address = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + " ( " + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")");
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})\n");
    public String user_email;
    public String user_username;
    public String user_pass;
    private DatabaseReference firebaseDatabase;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);


        Listener();
        materialeditTextinput();

        registerbutton = (TextView) findViewById(R.id.SignUp);

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_email = emailbutton.getText().toString();
                user_username = userbutton.getText().toString();
                user_pass = passbutton.getText().toString();


                progressDialog.setMessage("Registering....");
                progressDialog.setCancelable(false);
                progressDialog.show();




             /*else if(!PASSWORD_PATTERN.matcher(user_pass).matches()){

                  progressDialog.dismiss();
                  passbutton.setError("Your Password must Conatin one Capital letter,special charadyer,");
                  passbutton.requestFocus();

              }*/

                if (validate()) {

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_pass).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();

                                        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

                                        String uid = current_user.getUid();

                                        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                                        String device_token = FirebaseInstanceId.getInstance().getToken();

                                        HashMap<String, String> userMap = new HashMap<>();

                                        userMap.put("username", user_username);
                                        userMap.put("email", user_email);
                                        userMap.put("image", "default");
                                        userMap.put("device_token", device_token);

                                        firebaseDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){

                                                    startActivity(new Intent(RegistrationActivity.this, HomeActivity.class).
                                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                                    // we use flag in intent bcoz we accept a going back to home after  main page.
                                                    finish();
                                                }else{
                                                    Toast.makeText(RegistrationActivity.this, "NIKAL LAUDE", Toast.LENGTH_SHORT).show();

                                                }


                                            }
                                        });


                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }

            }
        });


    }


    public void materialeditTextinput() {
        TextInputLayout hintView = (TextInputLayout) findViewById(R.id.Activity_email);
        hintView.setHintAnimationEnabled(false);
        hintView.setHint("");


        TextInputLayout hint = (TextInputLayout) findViewById(R.id.activity_inputlayout_password);
        hint.setHintAnimationEnabled(false);
        hint.setHint("");


        TextInputLayout vu = (TextInputLayout) findViewById(R.id.activity_password);
        vu.setHintAnimationEnabled(false);
        vu.setHint("");

    }


    public void Listener() {
        emailbutton = (TextView) findViewById(R.id.reg_email);
        emailbutton.setHint("Email");
        userbutton = (TextView) findViewById(R.id.reg_username);
        userbutton.setHint("Username");
        passbutton = (TextView) findViewById(R.id.reg_password);
        passbutton.setHint("Password");
        userback = (TextView) findViewById(R.id.user_back);

        userback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });


        emailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        userbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        passbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }

    private boolean validate() {
        Boolean result = false;

        String name = userbutton.getText().toString();
        String email = emailbutton.getText().toString();
        String pass = passbutton.getText().toString();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {

            Toast.makeText(this, "Please enter all the Details", Toast.LENGTH_SHORT).show();


            if (user_email.isEmpty()) {
                progressDialog.dismiss();
                emailbutton.setError("Email is required");
                emailbutton.requestFocus();

            } else if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
                progressDialog.dismiss();
                emailbutton.setError("Enter a Valid Email Address");
                emailbutton.requestFocus();
            }
            if (user_username.isEmpty()) {
                userbutton.setError("username is required");
            }

            if (user_pass.isEmpty()) {
                progressDialog.dismiss();
                passbutton.setError("Password is required");
            }

        } else {
            result = true;
        }

        return result;
    }
}