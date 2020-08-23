package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private TextView btnregister;
    private TextView signbutton;
    private TextView userbutton;
    private TextView passbutton;
    private TextView forgetbtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;

    public String email, pass;
    public String fName;
    public static final String MY_PREFS_NAME = "MyPrefsFile";


    public static final Pattern EMAIL_Address = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + " ( " + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")");
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})\n");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AnotherActivity();// To go to registration Activity

        materialeditText();


        signbutton = findViewById(R.id.SignIn);
        userbutton = findViewById(R.id.name);
        userbutton.setHint("Email");
        passbutton = findViewById(R.id.password);
        passbutton.setHint("Password");
        forgetbtn = findViewById(R.id.Forget_text);
        progressBar = findViewById(R.id.login_progress);


        firebaseAuth = FirebaseAuth.getInstance();


        forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
            }
        });


        signbutton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                email = userbutton.getText().toString();
                pass = passbutton.getText().toString();

                if (email.isEmpty()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    userbutton.setError("Email is required");
                    userbutton.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    userbutton.setError("Enter a Valid Email Address");
                    userbutton.requestFocus();
                }
                if (pass.isEmpty()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    passbutton.setError("Password is required");


                }/*else if(!PASSWORD_PATTERN.matcher(pass).matches()){

                   progressDialog.dismiss();
                   passbutton.setError("Your Password must Conatin one Capital letter,special charadyer,");
                   passbutton.requestFocus();

               }*/
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {


                    validate(email, pass);

                }
               /*Intent intent = new Intent(LoginActivity.this,HomeActivity.class);



               intent.putExtra("email",email);

               startActivity(intent);*/

            }
        });

    }

    public void materialeditText() {
        TextInputLayout hintView = (TextInputLayout) findViewById(R.id.activity_login_inputlayout_password);
        hintView.setHintAnimationEnabled(false);
        hintView.setHint("");
        TextInputLayout hint = (TextInputLayout) findViewById(R.id.activity_inputlayout_password);
        hint.setHintAnimationEnabled(false);
        hint.setHint("");
    }

    public void AnotherActivity() {
        btnregister = (TextView) findViewById(R.id.signUp_text);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });


    }


    private void validate(String useremail, String userPassword) {
        firebaseAuth.signInWithEmailAndPassword(userbutton.getText().toString(), passbutton.getText().toString())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
                            //progressBar.setVisibility(View.INVISIBLE);

                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                    /*    else{
                            new AlertDialog.Builder(LoginActivity.this).setMessage("Please verify your email!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setCancelable(false).show();
                            firebaseAuth.signOut();
                        }
*/



                        } else {
                            // If sign in fails, display a message to the user.

                            progressBar.setVisibility(View.INVISIBLE);
                            //Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(LoginActivity.this).setTitle("Can't find the Username ").setMessage("The username you entered doesn't belong to an account. Please check your username and try again.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setCancelable(false).show();

                        }


                    }
                });


    }


}
